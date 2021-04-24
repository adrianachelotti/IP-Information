package com.mercadolibre.mercadopuntos.exceptions;

public class ValidationException extends Exception{


    public ValidationException(String ip) {
        super(ip);
    }
}
