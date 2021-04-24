package com.mercadolibre.mercadopuntos;

import com.mercadolibre.mercadopuntos.repository.StatsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableCaching
@EnableMongoRepositories(basePackageClasses = StatsRepository.class)
public class IpInformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpInformationApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


}
