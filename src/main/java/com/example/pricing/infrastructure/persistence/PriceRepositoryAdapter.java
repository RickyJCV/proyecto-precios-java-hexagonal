package com.example.pricing.infrastructure.persistence;

import com.example.pricing.domain.model.Price;
import com.example.pricing.domain.port.PriceRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PriceRepositoryAdapter implements PriceRepository {

    private final SpringDataPriceRepository springDataPriceRepository;

    public PriceRepositoryAdapter(SpringDataPriceRepository springDataPriceRepository) {
        this.springDataPriceRepository = springDataPriceRepository;
    }

    @Override
    public List<Price> findByBrandProductAndDate(Long brandId,
                                                 Long productId,
                                                 LocalDateTime applicationDate) {
        return springDataPriceRepository.findApplicablePrices(brandId, productId, applicationDate)
                .stream()
                .map(this::toDomain)
                .collect(toList());
    }

    private Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getProductId(),
                entity.getPriceList(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}
