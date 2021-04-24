package com.mercadolibre.mercadopuntos.repository;

import com.mercadolibre.mercadopuntos.models.StatsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Component
@Repository
@Transactional
public interface StatsRepository extends MongoRepository<StatsModel, String> {


}
