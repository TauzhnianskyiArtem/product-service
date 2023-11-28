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

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FindAllProductsStep {

    /**
     * Generated HTTP client for Product service endpoints.
     */
    ProductControllerApi productControllerApi;

    /**
     * Accept dataTable in feature file and update storage.
     *
     * @param dataTable - productFilter parameters in feature file
     */
    @When("a client wants find products by filter:")
    public void aClientWantsFindProductsByFilter(DataTable dataTable) {
        final var productFilter = ProductModelMapper.toProductFilter(dataTable);
        final var response = HttpClientExceptionHandler.sendRequest(
                () -> productControllerApi.findAllWithHttpInfo(productFilter),
                ProductOperationType.FIND_ALL);
        TestContextStorage.setResponseContext(response);
    }
}
