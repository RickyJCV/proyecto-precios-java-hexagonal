package com.example.pricing.infrastructure.configuration;

import com.example.pricing.application.GetPriceUseCase;
import com.example.pricing.domain.port.PriceRepository;
import com.example.pricing.infrastructure.persistence.PriceRepositoryAdapter;
import com.example.pricing.infrastructure.persistence.SpringDataPriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PriceRepository priceRepository(SpringDataPriceRepository springDataPriceRepository) {
        return new PriceRepositoryAdapter(springDataPriceRepository);
    }

    @Bean
    public GetPriceUseCase getPriceUseCase(PriceRepository priceRepository) {
        return new GetPriceUseCase(priceRepository);
    }
}
