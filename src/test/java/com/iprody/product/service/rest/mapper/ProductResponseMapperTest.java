package com.iprody.product.service.rest.mapper;

import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.CurrencyValue;
import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.domain.Price;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.rest.dto.DiscountResponseDto;
import com.iprody.product.service.rest.dto.ProductResponseDto;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class ProductResponseMapperTest {

    @Mock
    private DiscountResponseMapper discountResponseMapper;
    @InjectMocks
    private ProductResponseMapper productResponseMapper;

    @Test
    void shouldMapDomainToDto(SoftAssertions softly) {
        final var discountMock = mock(Discount.class);
        final var discountResponseMock = mock(DiscountResponseDto.class);

        final var product = Product.builder()
                .id(1L)
                .name("Name")
                .active(true)
                .discount(discountMock)
                .price(new Price(BigDecimal.ONE, new Currency(CurrencyValue.USD)))
                .build();

        when(discountResponseMapper.map(discountMock)).thenReturn(discountResponseMock);

        final ProductResponseDto actualResponseDto = productResponseMapper.map(product);

        softly.assertThat(actualResponseDto.id()).isEqualTo(product.getId());
        softly.assertThat(actualResponseDto.name()).isEqualTo(product.getName());
        softly.assertThat(actualResponseDto.active()).isEqualTo(product.isActive());
        softly.assertThat(actualResponseDto.discount()).isEqualTo(discountResponseMock);
        softly.assertThat(actualResponseDto.price().value()).isEqualTo(product.getPrice().getValue());
        softly.assertThat(actualResponseDto.price().currency().value())
                .isEqualTo(product.getPrice().getCurrency().getValue().name());

        verify(discountResponseMapper, times(1)).map(discountMock);
    }
}
