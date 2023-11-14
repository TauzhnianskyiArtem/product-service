package com.iprody.product.service.rest.adapter;

import com.iprody.product.service.domain.ProductFilter;
import com.iprody.product.service.rest.dto.DiscountRequestDto;
import com.iprody.product.service.rest.dto.DiscountForProductsRequestDto;
import com.iprody.product.service.rest.dto.ProductCreateRequestDto;
import com.iprody.product.service.rest.dto.ProductResponseDto;
import com.iprody.product.service.rest.dto.ProductUpdateRequestDto;
import com.iprody.product.service.rest.mapper.DiscountRequestMapper;
import com.iprody.product.service.rest.mapper.ProductCreateRequestMapper;
import com.iprody.product.service.rest.mapper.ProductResponseMapper;
import com.iprody.product.service.rest.mapper.ProductUpdateRequestMapper;
import com.iprody.product.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * ProductServiceAdapter adapt data from ProductController to ProductService.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductServiceAdapter {

    /**
     * Injection of ProductService.
     */
    private ProductService productService;

    /**
     * Injection of ProductReadMapper.
     */
    private ProductResponseMapper productResponseMapper;

    /**
     * Injection of DiscountCreateMapper.
     */
    private DiscountRequestMapper discountRequestMapper;

    /**
     * Injection of ProductCreateEditMapper.
     */
    private ProductCreateRequestMapper productCreateRequestMapper;

    /**
     * Injection of ProductUpdateRequestMapper.
     */
    private ProductUpdateRequestMapper productUpdateRequestMapper;

    /**
     * Adapt creating product from ProductController to ProductService.
     *
     * @param productCreateRequestDto The DTO containing product creation data.
     * @return productReadDto The DTO representing the newly created product.
     */
    public ProductResponseDto createProduct(ProductCreateRequestDto productCreateRequestDto) {
        return Optional.of(productCreateRequestDto)
                .map(productCreateRequestMapper::map)
                .map(productService::saveProduct)
                .map(productResponseMapper::map)
                .orElseThrow();
    }

    /**
     * Adapt updating product from ProductController to ProductService.
     *
     * @param productUpdateRequestDto The DTO containing product edit data.
     * @return The DTO representing the updated product.
     * @throws ResourceNotFoundException if product not found by id.
     */
    public ProductResponseDto updateProduct(ProductUpdateRequestDto productUpdateRequestDto) {
        return Optional.of(productUpdateRequestDto)
                .map(requestDto -> productUpdateRequestMapper.map(productUpdateRequestDto))
                .map(productService::saveProduct)
                .map(productResponseMapper::map)
                .orElseThrow();
    }

    /**
     * Adapt finding product by id from ProductController to ProductService.
     *
     * @param id The ID of the product to retrieve.
     * @return The DTO representing the retrieved product.
     * @throws ResourceNotFoundException if product not found by id.
     */
    public ProductResponseDto findById(long id) {
        return Optional.of(productService.findById(id))
                .map(productResponseMapper::map)
                .orElseThrow();
    }

    /**
     * Adapt finding all products from ProductController to ProductService.
     *
     * @param productFilter The filter for product retrieval.
     * @return A page of product DTOs that match the filter criteria.
     */
    public Page<ProductResponseDto> findAll(ProductFilter productFilter) {
        return productService.findAll(productFilter)
                .map(productResponseMapper::map);
    }

    /**
     * Adapt apply discount for one product from ProductController to ProductService.
     *
     * @param productId         The ID of the product to apply the discount.
     * @param discountRequestDto The DTO containing discount creation data.
     * @return The DTO representing the product after applying the discount.
     * @throws ResourceNotFoundException if product not found by id.
     */
    public ProductResponseDto applyDiscount(long productId, DiscountRequestDto discountRequestDto) {
        return Optional.of(discountRequestDto)
                .map(discountRequestMapper::map)
                .map(discount -> productService.applyDiscount(productId, discount))
                .map(productResponseMapper::map)
                .orElseThrow();
    }

    /**
     * Adapt apply discount for group products from ProductController to ProductService.
     *
     * @param discountCreates The DTO containing discount creation data for a group of products.
     * @return A list of product DTOs representing the products after applying the discount.
     */
    public List<ProductResponseDto> applyDiscount(DiscountForProductsRequestDto discountCreates) {
        return productService.applyDiscount(discountCreates.ids(),
                        discountRequestMapper.map(discountCreates.discountRequestDto()))
                .stream()
                .map(productResponseMapper::map)
                .toList();
    }

    /**
     * Adapt deactivate discount for one product from ProductController to ProductService.
     *
     * @param productId The ID of the product for which to deactivate the discount.
     * @return The DTO representing the product after deactivating the discount.
     * @throws ResourceNotFoundException if product not found by id.
     */
    public ProductResponseDto deactivateDiscount(long productId) {
        return Optional.of(productService.deactivateDiscount(productId))
                .map(productResponseMapper::map)
                .orElseThrow();
    }

    /**
     * Adapt deactivate discount for group products from ProductController to ProductService.
     *
     * @param ids A list of product IDs for which to deactivate discounts.
     * @return A list of product DTOs representing the products after deactivating discounts.
     */
    public List<ProductResponseDto> deactivateDiscount(List<Long> ids) {
        return productService.deactivateDiscount(ids)
                .stream()
                .map(productResponseMapper::map)
                .toList();
    }
}
