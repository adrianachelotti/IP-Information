package com.mercadolibre.mercadopuntos.services.impl;

import com.mercadolibre.mercadopuntos.dtos.StatsDto;
import com.mercadolibre.mercadopuntos.dtos.StatsResponseDto;
import com.mercadolibre.mercadopuntos.exceptions.ValidationException;
import com.mercadolibre.mercadopuntos.models.StatsModel;
import com.mercadolibre.mercadopuntos.repository.StatsRepository;
import com.mercadolibre.mercadopuntos.services.StatsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    public static final String DISTANCE_ORDER = "distance";

    private Logger logger = LoggerFactory.getLogger(StatsServiceImpl.class);

    private StatsRepository statsRepository;

    private final ModelMapper modelMapper;


    public StatsServiceImpl(StatsRepository repository, ModelMapper modelMapper){
        this.statsRepository = repository;
        this.modelMapper = modelMapper;
    }


    @Override
    public StatsResponseDto getStats() throws ValidationException {
        StatsResponseDto responseDto = null;
        List<StatsModel> statsModelsList = null;
        try {
            statsModelsList = statsRepository.findAll(Sort.by(DISTANCE_ORDER).descending());
        }catch (Exception exception){
            logger.error(exception.getMessage(),exception);
        }
        if (!CollectionUtils.isEmpty(statsModelsList)) {
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
        modelMapper.map(minStatsModel, minStatsDto);
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
                .divide(BigDecimal.valueOf(averageDenominator), 0, RoundingMode.HALF_EVEN );
        return average;
    }

}
