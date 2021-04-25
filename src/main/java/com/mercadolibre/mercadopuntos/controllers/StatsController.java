package com.mercadolibre.mercadopuntos.controllers;

import com.mercadolibre.mercadopuntos.dtos.StatsResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.services.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class StatsController {

    private Logger logger = LoggerFactory.getLogger(StatsController.class);

    public static final String DISTANCE_ORDER = "distance";

    private StatsService statsService;

    public StatsController(StatsService statsService){
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public StatsResponseDto getStats() throws ValidationException {
        logger.info("StatsController - stats  ");
        try {
            return this.statsService.getStats();
        }catch (ValidationException exception){
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

    }


}
