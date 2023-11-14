package com.iprody.product.service.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PriceResponseDto(
        @Schema(description = "Value of the price")
        BigDecimal value,
        @Schema(description = "Currency details")
        CurrencyResponseDto currency) {
}
