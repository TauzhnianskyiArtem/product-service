package com.iprody.product.service.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ProductResponseDto(
        @Schema(description = "Product id")
        long id,
        @Schema(description = "Product name")
        String name,
        @Schema(description = "Indicates if the product is active")
        boolean active,
        @Schema(description = "Price details")
        PriceResponseDto price,
        @Schema(description = "Discount details")
        DiscountResponseDto discount,
        @Schema(description = "Date and time when the product was created. This data follows ISO 86",
                example = "1970-01-01T00:00:00")
        Instant createdAt,
        @Schema(description = "Date and time when the product was last updated. This data follows ISO 86",
                example = "1970-01-01T00:00:00")
        Instant updatedAt) {
}
