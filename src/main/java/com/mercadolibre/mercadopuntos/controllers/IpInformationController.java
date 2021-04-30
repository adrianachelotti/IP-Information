package com.mercadolibre.mercadopuntos.controllers;

import com.mercadolibre.mercadopuntos.dtos.IpInformationRequestDto;
import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.services.IpInformationService;
import com.mercadolibre.mercadopuntos.validators.IpValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class IpInformationController {

    private Logger logger = LoggerFactory.getLogger(IpInformationController.class);

    public static final String IP_NOT_VALID = "Ip not valid";


    private IpInformationService ipInformationService;

    public IpInformationController(IpInformationService ipInformationService){
        this.ipInformationService = ipInformationService;
    }


    @PostMapping("/trace")
    public IpInformationResponseDto getIPInformation(@RequestBody IpInformationRequestDto ipInformationRequest) throws ValidationException, DependencyException {
        logger.info("IpInformationController - getIPInformation ip: {} ",ipInformationRequest);
        try {
            validate(ipInformationRequest.getIp());
            return this.ipInformationService.getIpInformation(ipInformationRequest.getIp());
        }catch (ValidationException | DependencyException exception){
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

    }

    private void validate(String ip) throws ValidationException {
        if (!IpValidator.isValid(ip)){
            throw  new ValidationException(IP_NOT_VALID);
        }
    }




}
