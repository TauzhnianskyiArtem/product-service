package com.iprody.product.service.e2e.util;

import org.springframework.http.ResponseEntity;

public record TestContext(ResponseEntity<?> response, ProductOperationType operationType) {

    /**
     * Return http status of response stored in the same thread.
     *
     * @return - Http status value
     */
    public int status() {
        return response.getStatusCode().value();
    }
}
