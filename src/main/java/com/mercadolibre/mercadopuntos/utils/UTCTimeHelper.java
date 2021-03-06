package com.mercadolibre.mercadopuntos.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UTCTimeHelper {

    public static final String EMPTY = "";
    public static final String UTC = "UTC";

    public static List<ZonedDateTime> getTimeZoneListUTC(List<String> utcTimeZoneList, ZonedDateTime now){

        return utcTimeZoneList.stream()
                              .map(utc -> utc.replace(UTC, EMPTY))
                              .map( p -> getTimeFromUTC(p,now))
                              .collect(Collectors.toList());

    }

    private static ZonedDateTime getTimeFromUTC(String utc, ZonedDateTime now) {
        String offset = utc.isEmpty()?"+00:00":utc;
        ZoneOffset zoneOffSet= ZoneOffset.of(offset);
        return now.withZoneSameInstant(zoneOffSet);

    }


}
