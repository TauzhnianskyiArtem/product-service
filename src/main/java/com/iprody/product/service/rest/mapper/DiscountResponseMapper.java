package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.rest.dto.DiscountResponseDto;
import org.springframework.stereotype.Component;

@Component
public class DiscountResponseMapper implements Mapper<Discount, DiscountResponseDto> {

    /**
     * @param discount The Discount to be converted to a DiscountResponseDto object.
     * @return The DiscountResponseDto object converted from the provided Discount object.
     */
    @Override
    public DiscountResponseDto map(Discount discount) {
        if (discount == null) {
            return null;
        }

        return DiscountResponseDto.builder()
                .value(discount.getValue())
                .from(discount.getValidFrom())
                .until(discount.getValidUntil())
                .build();
    }
}
