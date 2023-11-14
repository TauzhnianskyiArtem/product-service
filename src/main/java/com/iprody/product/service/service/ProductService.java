package com.iprody.product.service.service;

import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.CurrencyValue;
import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.domain.ProductFilter;
import com.iprody.product.service.repository.CurrencyRepository;
import com.iprody.product.service.repository.ProductRepository;
import com.iprody.product.service.exception.ResourceNotFoundException;
import com.iprody.product.service.util.SortingProductHelper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true)
public class ProductService {

    /**
     * Product domain name.
     */
    public static final String PRODUCT_DOMAIN_NAME = "Product";

    /**
     * Injection of ProductRepository.
     */
    private ProductRepository productRepository;

    /**
     * Injection of ProductRepository.
     */
    private CurrencyRepository currencyRepository;

    /**
     * Injection of SortingHelper.
     */
    private SortingProductHelper sortingProductHelper;

    /**
     * Save a Product entity to the database.
     *
     * @param product to be saved as a Product entity.
     * @return ProductReadDto The saved Product entity as a ProductReadDto object.
     */
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Find Product entity if the provided id is found.
     *
     * @param id The Product id to found Product.
     * @return ProductReadDto The found Product entity as a ProductReadDto object.
     * @throws ResourceNotFoundException if product not found by id.
     */
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_DOMAIN_NAME, id));
    }

    /**
     * Find all Products by filters.
     *
     * @param productFilter The ProductFilter provide properties for filtering and sorting.
     * @return Page<Product> The found Products entities by filters.
     */
    public Page<Product> findAll(ProductFilter productFilter) {
        final PageRequest pageRequest = getPageRequest(productFilter);
        final Specification<Product> spec = getProductSpecification(productFilter);
        return productRepository.findAll(spec, pageRequest);
    }

    /**
     * Apply discount for one product.
     *
     * @param productId The product id to found Product and applied discount.
     * @param discount  The Discount to be applied for Product.
     * @return The saved Product entity.
     * @throws ResourceNotFoundException if product not found by productId.
     */
    @Transactional
    public Product applyDiscount(long productId, Discount discount) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setDiscount(discount);
                    return product;
                })
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_DOMAIN_NAME, productId));
    }

    /**
     * Apply discount for group of products.
     *
     * @param ids      The list id to found Products and applied discount.
     * @param discount The Discount to be applied for group of Products.
     * @return Product The saved Product entity.
     */
    @Transactional
    public List<Product> applyDiscount(List<Long> ids, Discount discount) {

        final List<Product> products = productRepository.findByIdIn(ids)
                .stream()
                .map(product -> {
                    product.setDiscount(discount.copy());
                    return product;
                })
                .toList();
        return productRepository.saveAll(products);
    }


    /**
     * Deactivate discount for one product.
     *
     * @param productId The product id to found Product and deactivated discount.
     * @return Product The saved Product entity.
     * @throws ResourceNotFoundException if product not found by productId.
     */
    @Transactional
    public Product deactivateDiscount(long productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setDiscount(null);
                    return product;
                })
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_DOMAIN_NAME, productId));
    }

    /**
     * Deactivate discount for group of products.
     *
     * @param ids The list id to found Products and deactivated discount.
     * @return Product The saved Product entity.
     */
    @Transactional
    public List<Product> deactivateDiscount(List<Long> ids) {
        final List<Product> products = productRepository.findByIdIn(ids)
                .stream()
                .map(product -> {
                    product.setDiscount(null);
                    return product;
                })
                .toList();
        return productRepository.saveAll(products);
    }

    /**
     * Find Currency entity by value.
     *
     * @param value The Currency value id to found Currency.
     * @return Currency The found Currency entity.
     */
    public Currency getCurrencyByValue(CurrencyValue value) {
        return currencyRepository.getByValue(value);
    }

    /**
     * Get product specification for filtering products.
     *
     * @param productFilter The ProductFilter has fields for filtering.
     * @return Specification<Product> The created specification.
     */
    private Specification<Product> getProductSpecification(ProductFilter productFilter) {
        Specification<Product> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(criteriaBuilder.literal(true));

        if (productFilter.name() != null) {
            spec = spec.and(ProductFilterSpecification.nameLike(productFilter.name()));
        }
        if (productFilter.active() != null) {
            spec = spec.and(ProductFilterSpecification.active(productFilter.active()));
        }
        return spec;
    }

    /**
     * Get pageNumber request for sorting and pagination products.
     *
     * @param productFilter The ProductFilter has fields for pagination and sorting.
     * @return Specification<Product> The created specification.
     */
    private PageRequest getPageRequest(ProductFilter productFilter) {
        PageRequest pageRequest = PageRequest.of(productFilter.page().pageNumber(), productFilter.page().pageSize());
        final Sort sort = sortingProductHelper
                .validateAndGetSortForProduct(productFilter.sort());
        pageRequest = pageRequest.withSort(sort);
        return pageRequest;
    }
}
