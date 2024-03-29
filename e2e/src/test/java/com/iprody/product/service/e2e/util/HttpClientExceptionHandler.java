package com.iprody.product.service.e2e.util;

import com.iprody.product.service.e2e.generated.model.ApiErrorResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Supplier;

/**
 * Service class for catching Http client exceptions and creation of error response.
 */
@UtilityClass
public class HttpClientExceptionHandler {

    /**
     * Send request to service and create response.
     * when response with status 40X or 50X then catch client Exception
     *
     * @param request - accepts lambda function for call Api.
     *                Accepts Supplier so that the service api call is executed in this method
     * @param operationType - product operation type
     * @return TestContext with ResponseEntity and ProductOperationType
     */
    public TestContext sendRequest(Supplier<ResponseEntity<?>> request, ProductOperationType operationType) {
        try {
            return new TestContext(request.get(), operationType);
        } catch (RestClientResponseException ex) {
            final var apiError = ex.getResponseBodyAs(ApiErrorResponse.class);
            return new TestContext(
                    ResponseEntity.status(ex.getStatusCode()).body(apiError),
                    ProductOperationType.ERROR
            );
        }
    }
}
