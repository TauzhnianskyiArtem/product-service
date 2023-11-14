package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.rest.dto.DiscountResponseDto;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

@ExtendWith(SoftAssertionsExtension.class)
class DiscountResponseMapperTest {

    private final DiscountResponseMapper discountResponseMapper = new DiscountResponseMapper();

    @Test
    void shouldMapDomainToDto(SoftAssertions softly) {

        final var discount = Discount.builder()
                .value(10)
                .validFrom(Instant.now())
                .validUntil(Instant.now().plusSeconds(1000))
                .build();

        final DiscountResponseDto actualResponseDto = discountResponseMapper.map(discount);

        softly.assertThat(actualResponseDto.value()).isEqualTo(discount.getValue());
        softly.assertThat(actualResponseDto.from()).isEqualTo(discount.getValidFrom());
        softly.assertThat(actualResponseDto.until()).isEqualTo(discount.getValidUntil());

    }
}
