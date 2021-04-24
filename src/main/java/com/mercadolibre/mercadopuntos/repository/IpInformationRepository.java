package com.mercadolibre.mercadopuntos.repository;

import com.mercadolibre.mercadopuntos.models.IpCountryInformationModel;
import com.mercadolibre.mercadopuntos.models.IpInformationId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface IpInformationRepository extends MongoRepository<IpCountryInformationModel, IpInformationId> {

}

