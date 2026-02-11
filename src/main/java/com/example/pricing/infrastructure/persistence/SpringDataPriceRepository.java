package com.example.pricing.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query("""
           SELECT p FROM PriceEntity p
           WHERE p.brandId = :brandId
             AND p.productId = :productId
             AND :applicationDate BETWEEN p.startDate AND p.endDate
           """)
    List<PriceEntity> findApplicablePrices(@Param("brandId") Long brandId,
                                           @Param("productId") Long productId,
                                           @Param("applicationDate") LocalDateTime applicationDate);
}
