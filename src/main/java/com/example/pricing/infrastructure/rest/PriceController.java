package com.example.pricing.infrastructure.rest;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pricing.application.GetPriceUseCase;
import com.example.pricing.domain.model.Price;
import com.example.pricing.infrastructure.exception.PriceNotFoundException;
import com.example.pricing.infrastructure.rest.dto.PriceResponseDto;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;

    public PriceController(GetPriceUseCase getPriceUseCase) {
        this.getPriceUseCase = getPriceUseCase;
    }

    @GetMapping
    public ResponseEntity<PriceResponseDto> getPrice(
            @RequestParam("applicationDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") Long brandId) {

        Optional<Price> priceOpt = getPriceUseCase.getApplicablePrice(brandId, productId, applicationDate);

        return ResponseEntity.ok(
            toDto(
                priceOpt.orElseThrow(() -> new PriceNotFoundException(
                    String.format("No applicable price found for brandId: %d, productId: %d, applicationDate: %s",
                            brandId, productId, applicationDate)
                ))
            )
        );
    }

    private PriceResponseDto toDto(Price price) {
        return new PriceResponseDto(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice(),
                price.getCurrency()
        );
    }
}
