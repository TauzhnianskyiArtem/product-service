package com.iprody.product.service.rest.dto;

import com.iprody.product.service.domain.CurrencyValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "DTO for updating Product")
@Data
@Builder
public class ProductUpdateRequestDto {
    /**
     * Id of the product.
     */
    @Schema(description = "Id of the product")
    private long id;
    /**
     * Name of the product.
     */
    @Schema(description = "Name of the product")
    private String name;
    /**
     * Indicates if the product is active.
     */
    @Schema(description = "Indicates if the product is active")
    private Boolean active;
    /**
     * Price of the product.
     */
    @Min(0)
    @Schema(description = "Price of the product")
    private BigDecimal price;
    /**
     * The currency value. This value follows ISO 4217.
     */
    @Schema(description = "The currency value. This value follows ISO 4217.", example = "USD")
    private CurrencyValue currency;
}

