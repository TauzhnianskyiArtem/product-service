package com.iprody.product.service.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrencyResponseDto(
        @Schema(description = "The currency value. This value follows ISO 4217.", example = "USD")
        String value) {
}
