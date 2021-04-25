package com.mercadolibre.mercadopuntos.controllers;

import com.mercadolibre.mercadopuntos.dtos.IpInformationRequestDto;
import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.DependencyException;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.services.IpInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class IpInformationController {

    private Logger logger = LoggerFactory.getLogger(IpInformationController.class);


    private IpInformationService ipInformationService;

    public IpInformationController(IpInformationService ipInformationService){
        this.ipInformationService = ipInformationService;
    }


    @PostMapping("/trace")
    public IpInformationResponseDto getIPInformation(@RequestBody IpInformationRequestDto ipInformationRequest) throws ValidationException, DependencyException {
        logger.info("IpInformationController - getIPInformation ip: {} ",ipInformationRequest);
        try {
            return this.ipInformationService.getIpInformation(ipInformationRequest.getIp());
        }catch (ValidationException | DependencyException exception){
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

    }



}
