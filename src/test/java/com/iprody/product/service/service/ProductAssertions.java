package com.iprody.product.service.service;

import com.iprody.product.service.domain.Product;

/**
 * The Functional interface to call all assertions for the actual Product.
 * This interface using in parametrized tests.
 */
@FunctionalInterface
public interface ProductAssertions {

    /**
     * Calling all assertions for the actualProduct.
     *
     * @param actualProduct  The actual Product.
     */
    void assertActual(Product actualProduct);
}
