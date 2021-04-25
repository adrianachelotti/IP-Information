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
import com.mercadolibre.mercadopuntos.utils.DistanceCalculator;
import com.mercadolibre.mercadopuntos.utils.UTCTimeHelper;
import com.mercadolibre.mercadopuntos.validators.IpValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IpInformationServiceImpl implements IpInformationService {

    public static final String EUR_CURRENCY = "EUR";
    public static final String IP_NOT_VALID = "Ip not valid";
    public static final int LATITUD_INDEX = 0;
    public static final int LONGITUD_INDEX = 1;

    private final ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(IpInformationServiceImpl.class);

    @Value("${latitude.ref}")
    private BigDecimal LATITUDE_REFENTIAL;

    @Value("${longitude.ref}")
    private BigDecimal LONGITUDE_REFERENTIAL ;

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

        validate(ip);

        IpCountryDetailDto ipCountryDetailDto = ipCountryService.getCountryDetailForIp(ip);

        CountryInfoDto countryInfoDto = countryIsoService.getCountryIsoInfoDetail(ipCountryDetailDto.getCountryCode3());

        IpInformationResponseDto response = getIpInformationResponse(ip,ipCountryDetailDto, countryInfoDto);

        addingCurrencyAndQuote(countryInfoDto.getCurrencies(),response);

        try {
            saveStats(response);
        }catch (Exception exception){
            logger.error(exception.getMessage(), exception);
        }

        return response;
    }

    private void saveStats(IpInformationResponseDto response) {
        Optional result =statsRepository.findById(response.getCodeIso3());
        StatsModel statsModel= null;
        if (result.isPresent()){
            statsModel = (StatsModel) result.get();
            statsModel.setInvocations(statsModel.getInvocations()+1);
        }else{
            statsModel = new StatsModel();
            statsModel.setInvocations(1L);
            statsModel.setDistance(response.getDistance());
            statsModel.setCodeIso3(response.getCodeIso3());
        }
        statsRepository.save(statsModel);
    }



    @Cacheable(value = "ipResults", key = "#ip")
    private IpInformationResponseDto getIpInformationResponse(String ip, IpCountryDetailDto ipCountryDetailDto, CountryInfoDto countryInfoDto) {
        IpInformationResponseDto response = new IpInformationResponseDto();
        response.setIp(ip);
        //Name and ISO code
        response.setCountry(countryInfoDto.getName());
        response.setCodeIso3(ipCountryDetailDto.getCountryCode3());
        //languages
        response.setLanguages(countryInfoDto.getLanguages().stream().map(LanguageDto::getIso639_2).collect(Collectors.toList()));
        //Local date time list for UTC/
        populateTimeZones(countryInfoDto, response);
        //Distance
        populateDistance(countryInfoDto, response);

        return response;
    }

    private void populateDistance(CountryInfoDto countryInfoDto, IpInformationResponseDto response) {
        BigDecimal latitude = countryInfoDto.getLatlng().get(LATITUD_INDEX);
        BigDecimal longitude = countryInfoDto.getLatlng().get(LONGITUD_INDEX);
        response.setDistance(DistanceCalculator.distanceBetweenCities(latitude, longitude, LATITUDE_REFENTIAL, LONGITUDE_REFERENTIAL)
                                                              .setScale(0, RoundingMode.HALF_EVEN));
    }

    private void populateTimeZones(CountryInfoDto countryInfoDto, IpInformationResponseDto response) {
        response.setTimezones(UTCTimeHelper.getTimeZoneListUTC(countryInfoDto.getTimezones()));
    }


    private void validate(String ip) throws ValidationException {
        if (!IpValidator.isValid(ip)){
            throw  new ValidationException(IP_NOT_VALID);
        }
    }

    private void addingCurrencyAndQuote(List<CorruncyDto> currencyList, IpInformationResponseDto responseDto) throws DependencyException {
        QuoteDto quoteDto = quoteService.getQuote();
        if (quoteDto.isSuccess() && quoteDto.getBase().equalsIgnoreCase(EUR_CURRENCY)){
            currencyList.stream().map(CorruncyDto::getCode).forEach(currency -> responseDto.getCurrencies().put(currency, quoteDto.getRates().get(currency)));
        }
    }




}
