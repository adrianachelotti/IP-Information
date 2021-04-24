package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.models.IpCountryInformationModel;
import com.mercadolibre.mercadopuntos.models.IpInformationId;
import com.mercadolibre.mercadopuntos.models.StatsModel;
import com.mercadolibre.mercadopuntos.repository.IpInformationRepository;
import com.mercadolibre.mercadopuntos.repository.StatsRepository;
import com.mercadolibre.mercadopuntos.validators.IpValidator;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.dtos.*;
import com.mercadolibre.mercadopuntos.services.CountryIsoService;
import com.mercadolibre.mercadopuntos.services.IpCountryService;
import com.mercadolibre.mercadopuntos.services.IpInformationService;
import com.mercadolibre.mercadopuntos.services.QuoteService;
import com.mercadolibre.mercadopuntos.utils.DistanceCalculator;
import com.mercadolibre.mercadopuntos.utils.UTCTimeHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IpInformationServiceImpl implements IpInformationService {

    public static final String EUR_CURRENCY = "EUR";
    public static final String DISTANCE_ORDER = "distance";
    public static final String COUNTRY_NOT_FOUND = "Country not found";
    public static final String IP_NOT_VALID = "Ip not valid";
    public static final int LATITUD_INDEX = 0;
    public static final int LONGITUD_INDEX = 1;
    public static final int DISTANCE_SCALE = 2;

    @Value("${latitude.ref}")
    private BigDecimal LATITUDE_REFENTIAL;

    @Value("${longitude.ref}")
    private BigDecimal LONGITUDE_REFERENTIAL ;

    private IpCountryService ipCountryService;

    private CountryIsoService countryIsoService;

    private QuoteService quoteService;

    private IpInformationRepository ipInformationRepository;

    private StatsRepository statsRepository;


    public IpInformationServiceImpl(IpCountryService ipCountryService, CountryIsoService countryIsoService,
                                    QuoteService quoteService, IpInformationRepository ipInformationRepository, StatsRepository statsRepository){
        this.countryIsoService = countryIsoService;
        this.ipCountryService = ipCountryService;
        this.quoteService = quoteService;
        this.ipInformationRepository = ipInformationRepository;
        this.statsRepository =  statsRepository;
    }


    @Override
    public IpInformationResponseDto getIpInformationResponse(String ip) throws ValidationException {

        validate(ip);

        IpCountryDetailDto ipCountryDetailDto = ipCountryService.getCountryDetailForIp(ip);

        validateCodeISO3(ipCountryDetailDto);

        CountryInfoDto countryInfoDto = countryIsoService.getCountryIsoInfoDetail(ipCountryDetailDto.getCountryCode3());

        System.out.println(countryInfoDto.toString());

        IpInformationResponseDto response = getIpInformationResponse(ip,ipCountryDetailDto, countryInfoDto);

        addingCurrencyAndQuote(countryInfoDto.getCurrencies(),response);

        saveIpInformation(response);

        saveStats(response);

        return response;
    }

    private void saveStats(IpInformationResponseDto response) {
        Optional result =statsRepository.findById(response.getCodeIso3());
        StatsModel statsModel= null;
        if (result!=null && result.isPresent()){
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

    @Override
    public StatsResponseDto getStats() throws ValidationException {
        StatsResponseDto responseDto = null;
        List<StatsModel> statsModelsList = statsRepository.findAll(Sort.by(DISTANCE_ORDER).descending());

        if (!statsModelsList.isEmpty()) {
            BigDecimal averageDistance = calculateAverageDistance(statsModelsList);
            StatsDto minStatsDto = getMinDistance(statsModelsList);
            StatsDto maxStatsDto = getMaxDistance(statsModelsList);

             responseDto = StatsResponseDto.builder()
                                           .minStatsDto(minStatsDto)
                                           .maxStatsDto(maxStatsDto)
                                           .averageDistance(averageDistance)
                                           .build();
        }
        return responseDto;
    }

    private StatsDto getMinDistance(List<StatsModel> statsModelsList) {
        StatsDto minStatsDto = new StatsDto();
        StatsModel minStatsModel = statsModelsList.get(statsModelsList.size()-1);
        new ModelMapper().map(minStatsModel, minStatsDto);
        return minStatsDto;
    }

    private StatsDto getMaxDistance(List<StatsModel> statsModelsList) {
        StatsDto maxStatsDto = new StatsDto();
        StatsModel maxStatsModel = statsModelsList.get(0);
        new ModelMapper().map(maxStatsModel, maxStatsDto);
        return maxStatsDto;
    }

    private BigDecimal calculateAverageDistance(List<StatsModel> statsModelsList) {

        Long averageDenominator = statsModelsList.stream().mapToLong(StatsModel::getInvocations).sum();
        Double averageNumerator = statsModelsList.stream().mapToDouble(p -> p.getDistance().multiply(BigDecimal.valueOf(p.getInvocations())).doubleValue()).sum();
        BigDecimal average = BigDecimal.valueOf(averageNumerator)
                                       .divide(BigDecimal.valueOf(averageDenominator), DISTANCE_SCALE, RoundingMode.HALF_EVEN );
        return average;
    }


    @Cacheable(value = "ipResults", key = "#ip")
    private IpInformationResponseDto getIpInformationResponse(String ip, IpCountryDetailDto ipCountryDetailDto, CountryInfoDto countryInfoDto) {
        IpInformationResponseDto response = new IpInformationResponseDto();
        response.setIp(ip);
        //Name and ISO code
        response.setName(countryInfoDto.getName());
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
                                                              .setScale(DISTANCE_SCALE, RoundingMode.HALF_EVEN));
    }

    private void populateTimeZones(CountryInfoDto countryInfoDto, IpInformationResponseDto response) {
        response.setTimezones(UTCTimeHelper.getTimeZoneListUTC(countryInfoDto.getTimezones()));
    }


    private void validateCodeISO3(IpCountryDetailDto ipCountryDetailDto) throws ValidationException {
        if(Objects.isNull(ipCountryDetailDto) || Objects.isNull(ipCountryDetailDto.getCountryCode3())){
            throw new ValidationException(COUNTRY_NOT_FOUND);
        }
    }

    private void validate(String ip) throws ValidationException {

        if (!IpValidator.isValid(ip)){
            throw  new ValidationException(IP_NOT_VALID);
        }
    }

    private void addingCurrencyAndQuote(List<CorruncyDto> currencyList, IpInformationResponseDto responseDto){
        QuoteDto quoteDto = quoteService.getQuote();
        if (quoteDto.isSuccess() && quoteDto.getBase().equalsIgnoreCase(EUR_CURRENCY)){
            currencyList.stream().map(CorruncyDto::getCode).forEach(currency -> responseDto.getCurrencies().put(currency, quoteDto.getRates().get(currency)));
        }
    }

    private void saveIpInformation(IpInformationResponseDto responseDto){
        IpCountryInformationModel model = new IpCountryInformationModel();
        new ModelMapper().map(responseDto, model);
        model.setId( new IpInformationId(responseDto.getIp(),ZonedDateTime.now()));
        ipInformationRepository.save(model);

    }



}
