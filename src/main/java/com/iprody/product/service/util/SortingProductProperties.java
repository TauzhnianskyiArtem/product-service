package com.iprody.product.service.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum for column Product by which to sort.
 */
@Getter
@RequiredArgsConstructor
public enum SortingProductProperties {


    /**
     * Sort by {@link Product#name}.
     */
    NAME("name"),

    /**
     * Sort by {@link Product#active}.
     */
    ACTIVE("active");

    /**
     * Name column in Product.
     */
    private final String value;
}
