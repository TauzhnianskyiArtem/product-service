package com.iprody.product.service.e2e.steps;

import com.iprody.product.service.e2e.generated.api.ProductControllerApi;
import com.iprody.product.service.e2e.util.HttpClientExceptionHandler;
import com.iprody.product.service.e2e.util.ProductOperationType;
import com.iprody.product.service.e2e.util.TestContextStorage;
import io.cucumber.java.en.When;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * StepDefinition that inits requests creation to service.
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FindProductStep {

    /**
     * Generated HTTP client for Product service endpoints.
     */
    ProductControllerApi productControllerApi;

    /**
     * Accept productId in feature file and update storage.
     *
     * @param productId of the product to be found
     */
    @When("a client wants find product with id {long}")
    public void aClientWantsFindProductWithId(long productId) {
        final var response = HttpClientExceptionHandler.sendRequest(
                () -> productControllerApi.findByIdWithHttpInfo(productId),
                ProductOperationType.FIND_BY_ID);
        TestContextStorage.setResponseContext(response);
    }
}