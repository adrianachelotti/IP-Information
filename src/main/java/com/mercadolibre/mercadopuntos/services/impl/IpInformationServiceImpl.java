package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.dtos.*;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.models.StatsModel;
import com.mercadolibre.mercadopuntos.repository.StatsRepository;
import com.mercadolibre.mercadopuntos.services.CountryIsoService;
import com.mercadolibre.mercadopuntos.services.IpCountryService;
import com.mercadolibre.mercadopuntos.services.IpInformationService;
import com.mercadolibre.mercadopuntos.services.QuoteService;
import com.mercadolibre.mercadopuntos.utils.IpInformationResponseDtoBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.mercadolibre.mercadopuntos.utils.IpInformationResponseDtoBuilder.KMS;

@Service
public class IpInformationServiceImpl implements IpInformationService {

    public static final String EUR_CURRENCY = "EUR";

    private final ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(IpInformationServiceImpl.class);



    private IpCountryService ipCountryService;

    private CountryIsoService countryIsoService;

    private QuoteService quoteService;

    private StatsRepository statsRepository;

    public IpInformationServiceImpl(IpCountryService ipCountryService, CountryIsoService countryIsoService,
                                    QuoteService quoteService, StatsRepository statsRepository, ModelMapper modelMapper){
        this.countryIsoService = countryIsoService;
        this.ipCountryService = ipCountryService;
        this.quoteService = quoteService;
        this.statsRepository =  statsRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public IpInformationResponseDto getIpInformation(String ip) throws ValidationException, DependencyException {

        IpCountryDetailDto ipCountryDetailDto = ipCountryService.getCountryDetailForIp(ip);

        CountryInfoDto countryInfoDto = countryIsoService.getCountryIsoInfoDetail(ipCountryDetailDto.getCountryCode3());

        IpInformationResponseDto response = getIpInformation(ip,ipCountryDetailDto, countryInfoDto);

        //this part of the response is out of the cache
        addingCurrencyAndQuote(countryInfoDto.getCurrencies(),response);

        try {
            saveStats(response);
        }catch (Exception exception){
            logger.error(exception.getMessage(), exception);
        }

        return response;
    }

    private void saveStats(IpInformationResponseDto response) {
        Optional result =statsRepository.findById(response.getCodeIso());
        StatsModel statsModel= null;
        if (result.isPresent()){
            statsModel = (StatsModel) result.get();
            statsModel.setInvocations(statsModel.getInvocations()+1);
        }else{
            statsModel = new StatsModel();
            statsModel.setInvocations(1L);
            statsModel.setDistance(new BigDecimal(response.getEstimatedDistance().replace(KMS,"").trim()));
            statsModel.setCodeIso3(response.getCodeIso());
        }
        statsRepository.save(statsModel);
    }



    @Cacheable(value = "ipResults", key = "#ip")
    private IpInformationResponseDto getIpInformation(String ip, IpCountryDetailDto ipCountryDetailDto, CountryInfoDto countryInfoDto) {
        IpInformationResponseDtoBuilder builder = new IpInformationResponseDtoBuilder(ip,ipCountryDetailDto, countryInfoDto);
        IpInformationResponseDto response = builder.ip()
                                                    .country()
                                                    .distance()
                                                    .countryCode3()
                                                    .languages()
                                                    .times()
                                                    .build();

        return response;
    }


    private void addingCurrencyAndQuote(List<CorruncyDto> currencyList, IpInformationResponseDto responseDto) throws DependencyException {
        QuoteDto quoteDto = quoteService.getQuote();

        if (quoteDto.isSuccess() && quoteDto.getBase().equalsIgnoreCase(EUR_CURRENCY)){
            currencyList.stream()
                        .map(CorruncyDto::getCode)
                        .forEach(currency -> responseDto.getCurrencies()
                                                        .put(currency,String.format(" (1 EUR = %.4f %s)", quoteDto.getRates().get(currency), currency)));
        }
    }




}
