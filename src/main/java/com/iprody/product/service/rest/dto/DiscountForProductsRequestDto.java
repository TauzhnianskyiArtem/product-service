package com.iprody.product.service.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DTO for applying Discount to Products")
public record DiscountForProductsRequestDto(
        @NotNull
        @Schema(description = "List of product IDs on which the discount is applied")
        List<Long> ids,
        @NotNull
        @Valid
        @Schema(description = "Discount request details")
        DiscountRequestDto discountRequestDto) {
}
