package com.iprody.product.service.e2e.steps;

import com.iprody.product.service.e2e.generated.api.ProductControllerApi;
import com.iprody.product.service.e2e.util.HttpClientExceptionHandler;
import com.iprody.product.service.e2e.util.ProductModelMapper;
import com.iprody.product.service.e2e.util.ProductOperationType;
import com.iprody.product.service.e2e.util.TestContextStorage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * StepDefinition that inits requests creation to service.
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeactivateDiscountGroupProductsStep {

    /**
     * Generated HTTP client for Product service endpoints.
     */
    ProductControllerApi productControllerApi;

    /**
     * Accept dataTable in feature file and update storage.
     *
     * @param dataTable - userDto parameters in feature file
     */
    @When("a client wants deactivate discount for group of products with ids:")
    public void aClientWantsDeactivateDiscountForGroupOfProducts(DataTable dataTable) {
        final var productIdList = ProductModelMapper.toProductIdList(dataTable);
        final var response = HttpClientExceptionHandler.sendRequest(
                () -> productControllerApi
                        .deactivateDiscountForGroupOfProductsWithHttpInfo(productIdList),
                ProductOperationType.DEACTIVATE_DISCOUNT);
        TestContextStorage.setResponseContext(response);
    }
}