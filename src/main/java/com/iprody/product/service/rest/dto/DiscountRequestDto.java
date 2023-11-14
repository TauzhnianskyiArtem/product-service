package com.iprody.product.service.rest.dto;

import com.iprody.product.service.rest.validation.annotation.DiscountValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
@DiscountValidate
@Schema(description = "DTO for applying Discount to Product")
public record DiscountRequestDto(
        @NotNull
        @Min(0)
        @Max(100)
        @Schema(description = "Discount value in percentage (between 0 and 100)")
        int value,
        @NotNull
        @Schema(description = "Start date of the discount validity. This data follows ISO 8601",
                example = "1970-01-01T00:00:00")
        Instant from,
        @NotNull
        @Schema(description = "End date of the discount validity. This data follows ISO 8601",
                example = "1970-01-01T00:00:00")
        Instant until) {
}
