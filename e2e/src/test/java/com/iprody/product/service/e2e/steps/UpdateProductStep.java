package com.iprody.product.service.e2e.steps;

import com.iprody.product.service.e2e.generated.api.ProductControllerApi;
import com.iprody.product.service.e2e.generated.model.ProductUpdateRequestDto;
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
public class UpdateProductStep {

    /**
     * Generated HTTP client for Product service endpoints.
     */
    ProductControllerApi productControllerApi;

    /**
     * Accept dataTable in feature file and productUpdateRequestDto storage.
     *
     * @param dataTable - productUpdateRequestDto parameters in feature file
     */
    @When("a client wants update product with id {long}:")
    public void aClientWantsUpdateProductWithId(long productId, DataTable dataTable) {
        final ProductUpdateRequestDto request = ProductModelMapper.toUpdateRequestDto(dataTable);
        final var response = HttpClientExceptionHandler.sendRequest(
                () -> productControllerApi.updateWithHttpInfo(productId, request),
                ProductOperationType.UPDATE);
        TestContextStorage.setResponseContext(response);
    }
}