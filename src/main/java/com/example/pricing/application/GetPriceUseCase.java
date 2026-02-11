package com.example.pricing.application;

import com.example.pricing.domain.model.Price;
import com.example.pricing.domain.port.PriceRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

public class GetPriceUseCase {

    private final PriceRepository priceRepository;

    public GetPriceUseCase(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Returns the applicable price for the given brand, product and application date.
     * If several prices apply, the one with the highest priority is returned.
     */
    public Optional<Price> getApplicablePrice(Long brandId,
                                              Long productId,
                                              LocalDateTime applicationDate) {

        return priceRepository.findByBrandProductAndDate(brandId, productId, applicationDate)
                .stream()
                .filter(p -> p.isApplicableAt(applicationDate))
                .max(Comparator.comparingInt(Price::getPriority));
    }
}
