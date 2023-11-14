package com.iprody.product.service.rest.mapper;

/**
 * The Mapper interface to convert objects from type F to type T.
 *
 * @param <F> The source type from which objects are converted.
 * @param <T> The target type to which objects are converted.
 */
public interface Mapper<F, T> {

    /**
     * Converts an object of type F to an object of type T.
     *
     * @param object The object to be converted to a T object.
     * @return The object of type T converted from the provided object of type F.
     */
    T map(F object);
}
