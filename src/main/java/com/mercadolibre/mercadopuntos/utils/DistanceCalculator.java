package com.mercadolibre.mercadopuntos.utils;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicLine;
import net.sf.geographiclib.GeodesicMask;

import java.math.BigDecimal;

public class DistanceCalculator {


    private static Geodesic geod = Geodesic.WGS84;

    public static BigDecimal distanceBetweenCities(BigDecimal latitudeRef, BigDecimal longitudeRef, BigDecimal latitude, BigDecimal Longitude){
        GeodesicLine line = geod.InverseLine(latitude.doubleValue(), Longitude.doubleValue(), latitude.doubleValue(), longitudeRef.doubleValue(), GeodesicMask.DISTANCE_IN | GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE);
        return new BigDecimal(line.Distance());

    }

}
