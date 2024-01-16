package com.iprody.product.service.rest.controller;

import com.iprody.product.service.domain.ProductFilter;
import com.iprody.product.service.logging.aspect.BusinessOpsLogging;
import com.iprody.product.service.rest.adapter.ProductServiceAdapter;
import com.iprody.product.service.rest.dto.DiscountForProductsRequestDto;
import com.iprody.product.service.rest.dto.DiscountRequestDto;
import com.iprody.product.service.rest.dto.ProductCreateRequestDto;
import com.iprody.product.service.rest.dto.ProductResponseDto;
import com.iprody.product.service.rest.dto.ProductUpdateRequestDto;
import com.iprody.product.service.util.SortingProductProperties;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * ProductControllerV1 handles HTTP requests related to product operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductController {

    /**
     * Injection of ProductService.
     */
    private ProductServiceAdapter productServiceAdapter;

    /**
     * Create a new product.
     *
     * @param productCreateRequestDto The data for creating or editing a product.
     * @return The created or updated product details.
     */
    @BusinessOpsLogging
    @Operation(summary = "Create a new product")
    @PostMapping
    public ProductResponseDto create(@Validated @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        return productServiceAdapter.createProduct(productCreateRequestDto);
    }

    /**
     * Update a product by its ID.
     *
     * @param id                      The ID of the product to be updated.
     * @param productUpdateRequestDto The updated data for the product.
     * @return The updated product.
     */
    @BusinessOpsLogging
    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable long id,
                                     @Validated @RequestBody ProductUpdateRequestDto productUpdateRequestDto) {
        productUpdateRequestDto.setId(id);
        return productServiceAdapter.updateProduct(productUpdateRequestDto);
    }

    /**
     * Find a product by its ID.
     *
     * @param id The ID of the product to be found.
     * @return The product details identified by the ID.
     */
    @BusinessOpsLogging
    @Operation(summary = "Find a product by ID")
    @GetMapping("/{id}")
    public ProductResponseDto findById(@PathVariable long id) {
        return productServiceAdapter.findById(id);
    }

    /**
     * Find all products based on the provided filtering criteria.
     *
     * @param name          Optional name filter for products.
     * @param active        Optional active status filter for products.
     * @param sortBy        Optional set of properties to sort the products by.
     * @param directionSort Optional direction for sorting (ASC or DESC).
     * @param pageNumber    The page number to retrieve, with a default of 0 if not provided.
     * @param pageSize      The size of the page to retrieve, with a default of 10 if not provided.
     * @return A page containing products that meet the filtering criteria.
     */
    @BusinessOpsLogging
    @Operation(summary = "Find all products by the provided filtering criteria")
    @GetMapping
    public Page<ProductResponseDto> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Set<SortingProductProperties> sortBy,
            @RequestParam(required = false) Sort.Direction directionSort,
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize) {

        final ProductFilter.SortRequest sortRequest = new ProductFilter.SortRequest(sortBy, directionSort);
        final ProductFilter.PageRequest pageRequest = new ProductFilter.PageRequest(pageNumber, pageSize);
        final ProductFilter productFilter = ProductFilter.builder()
                .name(name)
                .active(active)
                .sort(sortRequest)
                .page(pageRequest)
                .build();

        return productServiceAdapter.findAll(productFilter);
    }


    /**
     * Apply a discount to a product identified by its ID.
     *
     * @param id                 The ID of the product to apply the discount.
     * @param discountRequestDto The discount details to be applied.
     * @return The product details after applying the discount.
     */
    @BusinessOpsLogging
    @Operation(summary = "Apply discount to a product")
    @PostMapping("/{id}/discounts")
    public ProductResponseDto applyDiscount(@PathVariable long id,
                                            @Validated @RequestBody DiscountRequestDto discountRequestDto) {
        return productServiceAdapter.applyDiscount(id, discountRequestDto);
    }

    /**
     * Deactivate a discount for a product identified by its ID.
     *
     * @param id The ID of the product to deactivate the discount.
     * @return The product details after deactivating the discount.
     */
    @BusinessOpsLogging
    @Operation(summary = "Deactivate discount for a product")
    @DeleteMapping("/{id}/discounts")
    public ProductResponseDto deactivateDiscount(@PathVariable long id) {
        return productServiceAdapter.deactivateDiscount(id);
    }

    /**
     * Apply a discount to a group of products.
     *
     * @param discountCreates The details for applying discounts to multiple products.
     * @return A list of product details after applying the discounts.
     */
    @BusinessOpsLogging
    @Operation(summary = "Apply discount to a group of products")
    @PostMapping("/discounts")
    public List<ProductResponseDto> applyDiscountForGroupOfProducts(
            @Validated @RequestBody DiscountForProductsRequestDto discountCreates) {
        return productServiceAdapter.applyDiscount(discountCreates);
    }

    /**
     * Deactivate discounts for a group of products.
     *
     * @param ids The IDs of products for which discounts will be deactivated.
     * @return A list of product details after deactivating discounts for the specified products.
     */
    @BusinessOpsLogging
    @Operation(summary = "Deactivate discount for a group of products")
    @DeleteMapping("/discounts")
    public List<ProductResponseDto> deactivateDiscountForGroupOfProducts(@RequestBody List<Long> ids) {
        return productServiceAdapter.deactivateDiscount(ids);
    }
}
