package com.iprody.product.service.e2e.util.model;

import com.iprody.product.service.e2e.generated.model.DiscountResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListOfDiscountsDto {
    List<DiscountResponseDto> discountRequestDtos;
}
