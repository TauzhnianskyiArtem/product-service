package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.CurrencyValue;
import com.iprody.product.service.domain.Price;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.rest.dto.ProductUpdateRequestDto;
import com.iprody.product.service.service.ProductService;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class ProductUpdateRequestMapperTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductUpdateRequestMapper productUpdateRequestMapper;

    @Test
    void shouldMapDomainToDto(SoftAssertions softly) {
        final var id = 1L;

        final var productRequestDto = ProductUpdateRequestDto.builder()
                .id(id)
                .name("Name")
                .active(true)
                .price(BigDecimal.ONE)
                .currency(CurrencyValue.USD)
                .build();

        final var product = Product.builder()
                .id(id)
                .name("Old name")
                .active(false)
                .price(Price.builder()
                        .value(BigDecimal.ZERO)
                        .currency(new Currency(CurrencyValue.EUR))
                        .build())
                .build();

        when(productService.getCurrencyByValue(CurrencyValue.USD)).thenReturn(new Currency(CurrencyValue.USD));
        when(productService.findById(id)).thenReturn(product);

        final Product actualProduct = productUpdateRequestMapper.map(productRequestDto);

        softly.assertThat(actualProduct.getName()).isEqualTo(productRequestDto.getName());
        softly.assertThat(actualProduct.isActive()).isEqualTo(productRequestDto.getActive());
        softly.assertThat(actualProduct.getPrice().getValue()).isEqualTo(productRequestDto.getPrice());
        softly.assertThat(actualProduct.getPrice().getCurrency().getValue()).isEqualTo(productRequestDto.getCurrency());
    }
}
