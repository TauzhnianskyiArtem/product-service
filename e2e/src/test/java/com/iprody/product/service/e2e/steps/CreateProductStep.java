package com.iprody.product.service.e2e.steps;

import com.iprody.product.service.e2e.generated.api.ProductControllerApi;
import com.iprody.product.service.e2e.generated.model.ProductCreateRequestDto;
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
public class CreateProductStep {

    /**
     * Generated HTTP client for Product service endpoints.
     */
    ProductControllerApi productControllerApi;

    /**
     * Accept dataTable in feature file and update storage.
     *
     * @param dataTable - productCreateRequestDto parameters in feature file
     */
    @When("a client wants create product with parameters:")
    public void aClientWantsCreateProductWithMandatoryParameters(DataTable dataTable) {
        final ProductCreateRequestDto request = ProductModelMapper.toCreateRequestDto(dataTable);
        final var response = HttpClientExceptionHandler.sendRequest(
                () -> productControllerApi.createWithHttpInfo(request),
                ProductOperationType.CREATE);
        TestContextStorage.setResponseContext(response);
    }
}