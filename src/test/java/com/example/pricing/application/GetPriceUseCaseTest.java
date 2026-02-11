package com.example.pricing.application;

import com.example.pricing.domain.model.Price;
import com.example.pricing.domain.port.PriceRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GetPriceUseCaseTest {

    private final PriceRepository priceRepository = (brandId, productId, applicationDate) -> {
        // In-memory stub
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
        return List.of(p1, p2);
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
    void shouldFilterByDateRangeUsingStreams() {
        LocalDateTime testDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        List<Price> allPrices = priceRepository.findByBrandProductAndDate(1L, 35455L, testDate);

        long applicableCount = allPrices.stream()
                .filter(p -> p.isApplicableAt(testDate))
                .count();

        assertThat(applicableCount).isEqualTo(1);
    }

    @Test
    void shouldSelectMaxPriorityUsingStreams() {
        LocalDateTime testDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        List<Price> allPrices = priceRepository.findByBrandProductAndDate(1L, 35455L, testDate);

        Optional<Integer> maxPriority = allPrices.stream()
                .filter(p -> p.isApplicableAt(testDate))
                .map(Price::getPriority)
                .max(Integer::compareTo);

        assertThat(maxPriority).isPresent();
        assertThat(maxPriority.get()).isEqualTo(1);
    }
}
