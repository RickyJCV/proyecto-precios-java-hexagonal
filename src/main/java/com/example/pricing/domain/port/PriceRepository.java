package com.example.pricing.domain.port;

import com.example.pricing.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    List<Price> findByBrandProductAndDate(Long brandId,
                                          Long productId,
                                          LocalDateTime applicationDate);
}
