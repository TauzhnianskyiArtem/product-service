package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.Price;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.rest.dto.ProductCreateRequestDto;
import com.iprody.product.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductCreateRequestMapper implements Mapper<ProductCreateRequestDto, Product> {

    /**
     * Injection of ProductService.
     */
    private ProductService productService;

    /**
     * @param requestDto The ProductRequestDto to be converted to a Product object.
     * @return The Product object is converted from the provided ProductRequestDto object.
     */
    @Override
    public Product map(ProductCreateRequestDto requestDto) {

        final Currency currency = productService.getCurrencyByValue(requestDto.currency());

        return Product.builder()
                .name(requestDto.name())
                .active(requestDto.active())
                .price(Price.builder()
                        .value(requestDto.price())
                        .currency(currency)
                        .build())
                .build();
    }
}
