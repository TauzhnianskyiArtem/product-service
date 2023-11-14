package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.rest.dto.DiscountRequestDto;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

@ExtendWith(SoftAssertionsExtension.class)
class DiscountRequestMapperTest {

    private final DiscountRequestMapper discountRequestMapper = new DiscountRequestMapper();

    @Test
    void shouldMapDtoToDomain(SoftAssertions softly) {

        final var discountRequestDto = DiscountRequestDto.builder()
                .value(10)
                .from(Instant.now())
                .until(Instant.now().plusSeconds(1000))
                .build();

        final Discount actualDiscount = discountRequestMapper.map(discountRequestDto);

        softly.assertThat(actualDiscount.getValue()).isEqualTo(discountRequestDto.value());
        softly.assertThat(actualDiscount.getValidFrom()).isEqualTo(discountRequestDto.from());
        softly.assertThat(actualDiscount.getValidUntil()).isEqualTo(discountRequestDto.until());
    }
}
