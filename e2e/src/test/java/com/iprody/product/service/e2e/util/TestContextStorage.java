package com.iprody.product.service.e2e.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

/**
 * Service class for saving step results and transmitting data.
 */
@UtilityClass
public class TestContextStorage {

    /**
     * Thread safe storage for response.
     */
    private final ThreadLocal<ResponseEntity<Object>> responseContext = new ThreadLocal<>();

    /**
     * Return Response stored in the same thread.
     *
     * @return - ResponseEntity with body and Http status
     */
    public ThreadLocal<ResponseEntity<Object>> getResponseContext() {
        return responseContext;
    }

    /**
     * Save Response in thread safe storage.
     *
     * @param context - ResponseEntity with body
     */
    public void setResponseContext(ResponseEntity<?> context) {
        TestContextStorage.responseContext.set((ResponseEntity<Object>) context);
    }

    /**
     * Clear response context.
     *
     */
    public void clearResponseContext() {
        TestContextStorage.responseContext.remove();
    }

    /**
     * Return http status of response stored in the same thread.
     *
     * @return - Http status value
     */
    public int getStatus() {
        return getResponse().getStatusCode().value();
    }

    /**
     * Return responseEntity stored in the same thread.
     *
     * @return - response
     */
    public ResponseEntity<Object> getResponse() {
        return getResponseContext().get();
    }

    /**
     * Return generic responseBody stored in the same thread.
     *
     * @param <T> - type to cast responseBody
     * @return - T responseBody
     */
    public <T> T getResponseBody() {
        return (T) getResponse().getBody();
    }

    /**
     * @return Class of ResponseBody
     */
    public Class<?> getResponseBodyType() {
        return getResponseBody().getClass();
    }
}