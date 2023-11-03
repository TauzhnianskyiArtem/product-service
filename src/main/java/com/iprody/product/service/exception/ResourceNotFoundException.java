package com.iprody.product.service.exception;

/**
 * The class is used to create an exception object that is thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {


    /**
     * Template for the error message when a resource is not found.
     */
    public static final String NOT_FOUND_WITH_ID_MESSAGE = "%s not found with id: %d";

    /**
     * Constructor that calls super constructor of RuntimeException.
     * @param domainName The name resource.
     * @param id The id resource.
     */
    public ResourceNotFoundException(final String domainName, final long id) {

        super(NOT_FOUND_WITH_ID_MESSAGE.formatted(domainName, id));
    }

}
