package com.iprody.product.service.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
public record DiscountResponseDto(
        @Schema(description = "Discount value in percentage")
        int value,
        @Schema(description = "Start date of the discount validity. This data follows ISO 86",
                example = "1970-01-01T00:00:00")
        Instant from,
        @Schema(description = "End date of the discount validity. This data follows ISO 86",
                example = "1970-01-01T00:00:00")
        Instant until) {
}

