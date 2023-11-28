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
public class DeactivateDiscountOneProductStep {

    /**
     * Generated HTTP client for Product service endpoints.
     */
    ProductControllerApi productControllerApi;

    /**
     * Accept dataTable in feature file and update storage.
     *
     * @param productId of the product to be deactivated discount
     */
    @When("a client wants deactivate discount for product with id {long}")
    public void aClientWantsDeactivateDiscountForProduct(long productId) {
        final var response = HttpClientExceptionHandler.sendRequest(
                () -> productControllerApi.deactivateDiscountWithHttpInfo(productId),
                ProductOperationType.DEACTIVATE_DISCOUNT);
        TestContextStorage.setResponseContext(response);
    }
}