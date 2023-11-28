package com.iprody.product.service.e2e.util;

import lombok.experimental.UtilityClass;

/**
 * Service class for saving step results and transmitting data.
 */
@UtilityClass
public class TestContextStorage {

    /**
     * Thread safe storage for response.
     */
    private final ThreadLocal<TestContext> context = new ThreadLocal<>();

    /**
     * Return Response stored in the same thread.
     *
     * @return - ResponseEntity with body and Http status
     */
    public TestContext getResponseContext() {
        return context.get();
    }

    /**
     * Save Response in thread safe storage.
     *
     * @param context - ResponseEntity with body
     */
    public void setResponseContext(TestContext context) {
        TestContextStorage.context.set(context);
    }

    /**
     * Clear response context.
     *
     */
    public void clearContext() {
        TestContextStorage.context.remove();
    }

}