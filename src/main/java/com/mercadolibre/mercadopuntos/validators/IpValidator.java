package com.mercadolibre.mercadopuntos.validators;

import org.apache.commons.validator.routines.InetAddressValidator;

public class IpValidator {


    public static boolean isValid(final String ip) {
        return InetAddressValidator.getInstance().isValid(ip);
    }

   }
