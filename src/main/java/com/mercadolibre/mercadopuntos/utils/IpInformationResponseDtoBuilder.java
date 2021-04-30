package com.mercadolibre.mercadopuntos.utils;

import com.mercadolibre.mercadopuntos.dtos.CountryInfoDto;
import com.mercadolibre.mercadopuntos.dtos.IpCountryDetailDto;
import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import com.mercadolibre.mercadopuntos.dtos.QuoteDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class IpInformationResponseDtoBuilder {

    private static final String ES_TRANSLATION = "es";

    public static final String KMS = "kms";

    private static final int LATITUD_INDEX = 0;

    private static final int LONGITUD_INDEX = 1;

    private final static String TIME_ZONE_ID = "America/Argentina/Buenos_Aires";

    private final static String TIME_FORMAT = "HH:mm:ss";

    private final static String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    //Latitud of Buenos Aires
    private final BigDecimal LATITUDE_REFENTIAL =  new BigDecimal(-34.603722);

    //Longitude of Buenos Aires
    private final BigDecimal LONGITUDE_REFERENTIAL  = new BigDecimal(-58.381592);

    private String ip;

    private IpCountryDetailDto ipCountryDetailDto;

    private CountryInfoDto countryInfoDto;

    private IpInformationResponseDto response;

    private IpInformationResponseDtoBuilder(){
    }

    public IpInformationResponseDtoBuilder(String ip, IpCountryDetailDto ipCountryDetailDto, CountryInfoDto countryInfoDto) {
        this.response = new IpInformationResponseDto();
        this.ipCountryDetailDto = ipCountryDetailDto;
        this.countryInfoDto = countryInfoDto;
        this.ip = ip;
    }

    public IpInformationResponseDtoBuilder ip(){
        response.setIp(ip);
        return this;
    }

    public IpInformationResponseDtoBuilder distance(){
            BigDecimal latitude = countryInfoDto.getLatlng().get(LATITUD_INDEX);
            BigDecimal longitude = countryInfoDto.getLatlng().get(LONGITUD_INDEX);
            response.setEstimatedDistance( String.format("%.0f %s", DistanceCalculator.distanceBetweenCities(latitude, longitude, LATITUDE_REFENTIAL, LONGITUDE_REFERENTIAL)
                    .setScale(0, RoundingMode.HALF_EVEN), KMS));
        return this;
    }

    public IpInformationResponseDtoBuilder countryCode3(){
        response.setCodeIso(ipCountryDetailDto.getCountryCode3());
        return this;
    }

    public IpInformationResponseDtoBuilder country(){
        response.setCountry(String.format("%s (%s)", countryInfoDto.getTranslations().get(ES_TRANSLATION), countryInfoDto.getName()));
        return this;
    }

    public IpInformationResponseDtoBuilder languages(){
        response.setLanguages(countryInfoDto.getLanguages()
                .stream()
                .map(leng -> String.format("%s (%s)", leng.getName(), leng.getIso639_1()))
                .collect(Collectors.toList()));
        return this;
    }

    public IpInformationResponseDtoBuilder times(){
        ZonedDateTime now = Instant.now().atZone(ZoneId.of(TIME_ZONE_ID));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        response.setDate(now.format(dateTimeFormatter));

        List<String> timesList = UTCTimeHelper.getTimeZoneListUTC(countryInfoDto.getTimezones(), now)
                .stream()
                .map(time ->String.format("%s (UTC%s)", timeFormatter.format(time), time.getOffset().getId().replace("Z","")))
                .collect(Collectors.toList());
        response.setTimes(timesList);
        return this;
    }

    public IpInformationResponseDto build(){
        return response;
    }

}
