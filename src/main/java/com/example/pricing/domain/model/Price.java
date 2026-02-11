package com.example.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price {

    private final Long brandId;
    private final Long productId;
    private final Long priceList;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priority;
    private final BigDecimal price;
    private final String currency;

    public Price(Long brandId,
                 Long productId,
                 Long priceList,
                 LocalDateTime startDate,
                 LocalDateTime endDate,
                 Integer priority,
                 BigDecimal price,
                 String currency) {
        this.brandId = brandId;
        this.productId = productId;
        this.priceList = priceList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.price = price;
        this.currency = currency;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getPriceList() {
        return priceList;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isApplicableAt(LocalDateTime applicationDate) {
        return (applicationDate.isEqual(startDate) || applicationDate.isAfter(startDate))
                && (applicationDate.isBefore(endDate) || applicationDate.isEqual(endDate));
    }
}
