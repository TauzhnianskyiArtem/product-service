package com.iprody.product.service.rest.adapter;

import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.domain.ProductFilter;
import com.iprody.product.service.rest.dto.DiscountForProductsRequestDto;
import com.iprody.product.service.rest.dto.DiscountRequestDto;
import com.iprody.product.service.rest.dto.ProductCreateRequestDto;
import com.iprody.product.service.rest.dto.ProductResponseDto;
import com.iprody.product.service.rest.dto.ProductUpdateRequestDto;
import com.iprody.product.service.rest.mapper.DiscountRequestMapper;
import com.iprody.product.service.rest.mapper.ProductCreateRequestMapper;
import com.iprody.product.service.rest.mapper.ProductResponseMapper;
import com.iprody.product.service.rest.mapper.ProductUpdateRequestMapper;
import com.iprody.product.service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceAdapterTest {

    @Mock
    private ProductResponseMapper productResponseMapper;

    @Mock
    private DiscountRequestMapper discountRequestMapper;

    @Mock
    private ProductCreateRequestMapper productCreateRequestMapper;

    @Mock
    private ProductUpdateRequestMapper productUpdateRequestMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductServiceAdapter productServiceAdapter;

    @Test
    void shouldCreateProduct() {
        final ProductCreateRequestDto productCreateRequestDtoMock = mock(ProductCreateRequestDto.class);
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Product productMock = mock(Product.class);

        when(productCreateRequestMapper.map(productCreateRequestDtoMock)).thenReturn(productMock);
        when(productService.saveProduct(productMock)).thenReturn(productMock);
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final ProductResponseDto actualResponseDto = productServiceAdapter.createProduct(productCreateRequestDtoMock);

        assertThat(actualResponseDto).isEqualTo(productResponseDtoMock);

        verify(productCreateRequestMapper, times(1)).map(productCreateRequestDtoMock);
        verify(productService, times(1)).saveProduct(productMock);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldUpdateProduct() {
        final ProductUpdateRequestDto productUpdateRequestDto = mock(ProductUpdateRequestDto.class);
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Product productMock = mock(Product.class);
        final long id = 1L;
        productUpdateRequestDto.setId(id);

        when(productUpdateRequestMapper.map(productUpdateRequestDto)).thenReturn(productMock);
        when(productService.saveProduct(productMock)).thenReturn(productMock);
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final ProductResponseDto actualResponseDto = productServiceAdapter.updateProduct(productUpdateRequestDto);

        assertThat(actualResponseDto).isEqualTo(productResponseDtoMock);

        verify(productUpdateRequestMapper, times(1)).map(productUpdateRequestDto);
        verify(productService, times(1)).saveProduct(productMock);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldFindByIdProduct() {
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Product productMock = mock(Product.class);
        final long id = 1L;

        when(productService.findById(id)).thenReturn(productMock);
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final ProductResponseDto actualResponseDto = productServiceAdapter.findById(id);

        assertThat(actualResponseDto).isEqualTo(productResponseDtoMock);

        verify(productService, times(1)).findById(id);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldFindAllProduct() {
        final ProductFilter productFilterMock = mock(ProductFilter.class);
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Product productMock = mock(Product.class);
        final Page<Product> productPage = new PageImpl<>(List.of(productMock));

        when(productService.findAll(productFilterMock)).thenReturn(productPage);
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final Page<ProductResponseDto> actualPageResponse = productServiceAdapter.findAll(productFilterMock);

        assertThat(actualPageResponse.getContent()).asList().first().isEqualTo(productResponseDtoMock);

        verify(productService, times(1)).findAll(productFilterMock);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldApplyDiscount() {
        final DiscountRequestDto discountRequestDtoMock = mock(DiscountRequestDto.class);
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Discount discountMock = mock(Discount.class);
        final Product productMock = mock(Product.class);
        final long id = 1L;

        when(discountRequestMapper.map(discountRequestDtoMock)).thenReturn(discountMock);
        when(productService.applyDiscount(id, discountMock)).thenReturn(productMock);
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final ProductResponseDto actualResponseDto = productServiceAdapter.applyDiscount(id, discountRequestDtoMock);

        assertThat(actualResponseDto).isEqualTo(productResponseDtoMock);

        verify(discountRequestMapper, times(1)).map(discountRequestDtoMock);
        verify(productService, times(1)).applyDiscount(id, discountMock);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldApplyDiscountForGroupProducts() {
        final DiscountRequestDto discountRequestDtoMock = mock(DiscountRequestDto.class);
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Discount discountMock = mock(Discount.class);
        final Product productMock = mock(Product.class);
        final long id = 1L;
        final List<Long> ids = List.of(id);
        final DiscountForProductsRequestDto discountProductsDto =
                new DiscountForProductsRequestDto(ids, discountRequestDtoMock);

        when(discountRequestMapper.map(discountRequestDtoMock)).thenReturn(discountMock);
        when(productService.applyDiscount(ids, discountMock)).thenReturn(List.of(productMock));
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final List<ProductResponseDto> actualProducts = productServiceAdapter.applyDiscount(discountProductsDto);

        assertThat(actualProducts).asList().first().isEqualTo(productResponseDtoMock);

        verify(discountRequestMapper, times(1)).map(discountRequestDtoMock);
        verify(productService, times(1)).applyDiscount(ids, discountMock);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldDeactivateDiscount() {
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Product productMock = mock(Product.class);
        final long id = 1L;

        when(productService.deactivateDiscount(id)).thenReturn(productMock);
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final ProductResponseDto actualResponseDto = productServiceAdapter.deactivateDiscount(id);

        assertThat(actualResponseDto).isEqualTo(productResponseDtoMock);

        verify(productService, times(1)).deactivateDiscount(id);
        verify(productResponseMapper, times(1)).map(productMock);
    }

    @Test
    void shouldDeactivateDiscountForGroupProducts() {
        final ProductResponseDto productResponseDtoMock = mock(ProductResponseDto.class);
        final Product productMock = mock(Product.class);
        final long id = 1L;
        final List<Long> ids = List.of(id);

        when(productService.deactivateDiscount(ids)).thenReturn(List.of(productMock));
        when(productResponseMapper.map(productMock)).thenReturn(productResponseDtoMock);

        final List<ProductResponseDto> actualProducts = productServiceAdapter.deactivateDiscount(ids);

        assertThat(actualProducts).asList().first().isEqualTo(productResponseDtoMock);

        verify(productService, times(1)).deactivateDiscount(ids);
        verify(productResponseMapper, times(1)).map(productMock);
    }
}
