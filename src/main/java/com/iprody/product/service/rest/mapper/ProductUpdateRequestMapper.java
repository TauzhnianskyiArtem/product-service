package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.rest.dto.ProductUpdateRequestDto;
import com.iprody.product.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductUpdateRequestMapper {

    /**
     * Injection of ProductService.
     */
    private ProductService productService;

    /**
     * @param requestDto The ProductRequestDto to be converted to a Product object.
     * @return The Product object is converted from the provided ProductRequestDto object.
     */
    public Product map(ProductUpdateRequestDto requestDto) {
        final Product productFromDb = productService.findById(requestDto.getId());

        if (requestDto.getName() != null) {
            productFromDb.setName(requestDto.getName());
        }

        if (requestDto.getActive() != null) {
            productFromDb.setActive(requestDto.getActive());
        }

        if (requestDto.getCurrency() != null) {
            final Currency currency = productService
                    .getCurrencyByValue(requestDto.getCurrency());
            productFromDb.getPrice().setCurrency(currency);
        }

        if (requestDto.getPrice() != null) {
            productFromDb.getPrice().setValue(requestDto.getPrice());
        }

        return productFromDb;
    }
}
