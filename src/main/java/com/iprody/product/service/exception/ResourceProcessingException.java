package com.iprody.product.service.exception;

/**
 * The class is used to create an exception object that is thrown if there are problems in processing the resource.
 */
public class ResourceProcessingException extends RuntimeException {
    /**
     * Constructor that calls super constructor of RuntimeException.
     * @param message message about what exactly went wrong
     */
    public ResourceProcessingException(final String message) {
        super(message);
    }
}
