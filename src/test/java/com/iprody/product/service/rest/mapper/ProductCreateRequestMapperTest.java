package com.iprody.product.service.rest.mapper;


import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.CurrencyValue;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.rest.dto.ProductCreateRequestDto;
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
class ProductCreateRequestMapperTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductCreateRequestMapper productCreateRequestMapper;

    @Test
    void shouldMapDomainToDto(SoftAssertions softly) {

        final var productRequestDto = ProductCreateRequestDto.builder()
                .name("Name")
                .active(true)
                .price(BigDecimal.ONE)
                .currency(CurrencyValue.USD)
                .build();

        when(productService.getCurrencyByValue(CurrencyValue.USD)).thenReturn(new Currency(CurrencyValue.USD));

        final Product actualProduct = productCreateRequestMapper.map(productRequestDto);

        softly.assertThat(actualProduct.getName()).isEqualTo(productRequestDto.name());
        softly.assertThat(actualProduct.isActive()).isEqualTo(productRequestDto.active());
        softly.assertThat(actualProduct.getPrice().getValue()).isEqualTo(productRequestDto.price());
        softly.assertThat(actualProduct.getPrice().getCurrency().getValue()).isEqualTo(productRequestDto.currency());
    }
}
