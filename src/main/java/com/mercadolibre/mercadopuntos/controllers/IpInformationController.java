package com.mercadolibre.mercadopuntos.controllers;

import com.mercadolibre.mercadopuntos.dtos.IpInformationResponseDto;
import com.mercadolibre.mercadopuntos.dtos.StatsDto;
import com.mercadolibre.mercadopuntos.dtos.StatsResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.services.IpInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class IpInformationController {

    private Logger logger = LoggerFactory.getLogger(IpInformationController.class);


    private IpInformationService ipInformationService;

    public IpInformationController(IpInformationService ipInformationService){
        this.ipInformationService = ipInformationService;
    }


    @PostMapping("/trace/")
    public IpInformationResponseDto getIPInformation(@RequestBody String ip) throws ValidationException {
        logger.info("IpInformationController - getIPInformation ip: {} ",ip);
        try {
            return this.ipInformationService.getIpInformationResponse(ip);
        }catch (ValidationException exception){
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

    }


    @GetMapping("/stats/")
    public StatsResponseDto getStats() throws ValidationException {
        logger.info("IpInformationController - stats  ");
        try {
            return this.ipInformationService.getStats();
        }catch (ValidationException exception){
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

    }

}
