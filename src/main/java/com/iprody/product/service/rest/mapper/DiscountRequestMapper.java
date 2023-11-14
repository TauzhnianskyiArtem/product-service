package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.rest.dto.DiscountRequestDto;
import org.springframework.stereotype.Component;

@Component
public class DiscountRequestMapper implements Mapper<DiscountRequestDto, Discount> {

    /**
     * @param requestDto The DiscountRequestDto to be converted to a Discount object.
     * @return The Discount object is converted from the provided DiscountRequestDto object.
     */
    @Override
    public Discount map(DiscountRequestDto requestDto) {
        return Discount.builder()
                .value(requestDto.value())
                .validFrom(requestDto.from())
                .validUntil(requestDto.until())
                .build();
    }
}
