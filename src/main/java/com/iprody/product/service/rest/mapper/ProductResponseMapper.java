package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Product;
import com.iprody.product.service.rest.dto.CurrencyResponseDto;
import com.iprody.product.service.rest.dto.DiscountResponseDto;
import com.iprody.product.service.rest.dto.PriceResponseDto;
import com.iprody.product.service.rest.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductResponseMapper implements Mapper<Product, ProductResponseDto> {

    /**
     * Injection of DiscountReadMapper.
     */
    private DiscountResponseMapper discountResponseMapper;

    /**
     * @param product The Product to be converted to a ProductReadDto object.
     * @return The ProductReadDto object converted from the provided Product object.
     */
    @Override
    public ProductResponseDto map(final Product product) {

        final DiscountResponseDto discount = discountResponseMapper.map(product.getDiscount());

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .active(product.isActive())
                .price(new PriceResponseDto(product.getPrice().getValue(),
                        new CurrencyResponseDto(product.getPrice().getCurrency().getValue().name())))
                .discount(discount)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
