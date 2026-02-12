package com.example.pricing.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.example.pricing.domain.model.Price;
import com.example.pricing.domain.port.PriceRepository;

class GetPriceUseCaseTest {

    private final PriceRepository priceRepository = (brandId, productId, applicationDate) -> {
        // In-memory stub that respects date filtering as per the query contract
        Price p1 = new Price(
                1L, 35455L, 1L,
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                0,
                new BigDecimal("35.50"),
                "EUR"
        );
        Price p2 = new Price(
                1L, 35455L, 2L,
                LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                1,
                new BigDecimal("25.45"),
                "EUR"
        );
        
        // Filter prices that are applicable at the given date (matching query behavior)
        return List.of(p1, p2).stream()
                .filter(p -> (applicationDate.isEqual(p.getStartDate()) || applicationDate.isAfter(p.getStartDate()))
                        && (applicationDate.isBefore(p.getEndDate()) || applicationDate.isEqual(p.getEndDate())))
                .toList();
    };

    private final GetPriceUseCase useCase = new GetPriceUseCase(priceRepository);

    @Test
    void shouldReturnHighestPriorityPriceWhenSeveralApply() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        Optional<Price> result = useCase.getApplicablePrice(1L, 35455L, applicationDate);

        assertThat(result).isPresent();
        assertThat(result.get().getPriceList()).isEqualTo(2L);
        assertThat(result.get().getPrice()).isEqualByComparingTo("25.45");
    }

    @Test
    void shouldReturnEmptyWhenNoPriceApplies() {
        LocalDateTime applicationDate = LocalDateTime.of(2019, 1, 1, 0, 0, 0);

        Optional<Price> result = useCase.getApplicablePrice(1L, 35455L, applicationDate);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSelectMaxPriorityBasedOnApplicablePrices() {
        LocalDateTime testDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        Optional<Price> result = useCase.getApplicablePrice(1L, 35455L, testDate);

        assertThat(result).isPresent();
        assertThat(result.get().getPriority()).isEqualTo(1);
    }
}
