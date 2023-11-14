package com.iprody.product.service.rest.dto;

import com.iprody.product.service.domain.CurrencyValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Schema(description = "DTO for creating Product")
@Builder
public record ProductCreateRequestDto(
        @Schema(description = "Name of the product")
        @NotNull
        String name,
        @Schema(description = "Indicates if the product is active")
        @NotNull
        boolean active,
        @NotNull
        @Min(0)
        @Schema(description = "Price of the product")
        BigDecimal price,
        @NotNull
        @Schema(description = "The currency value. This value follows ISO 4217.", example = "USD")
        CurrencyValue currency) {
}

