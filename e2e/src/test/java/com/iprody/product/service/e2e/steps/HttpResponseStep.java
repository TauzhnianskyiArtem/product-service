package com.iprody.product.service.e2e.steps;

import com.iprody.product.service.e2e.assertions.FactoryDtoAssertHandler;
import com.iprody.product.service.e2e.util.TestContext;
import com.iprody.product.service.e2e.util.TestContextStorage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * StepDefinition that compares data set in feature file and responseBody.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HttpResponseStep {

    FactoryDtoAssertHandler factoryDtoAssertHandler;

    /**
     * Check http status in feature file and response status.
     *
     * @param expectedCode - status code in feature file
     */
    @Then("response code is {int}")
    public void responseCodeIs(int expectedCode) {
        final int actualCode = TestContextStorage.getResponseContext().status();
        assertThat(actualCode).isEqualTo(expectedCode);
    }

    /**
     * Check dataTable in feature file and responseBody.
     *
     * @param dataTable - userDto parameters in feature file
     */
    @And("response body contains:")
    public void responseBodyContains(DataTable dataTable) {
        TestContext responseContext = TestContextStorage.getResponseContext();
        Object responseBody = responseContext.response().getBody();

        if (responseBody instanceof Collection<?>) {
            ((Collection<?>) responseBody).stream().forEach((element) -> {
                factoryDtoAssertHandler.getInstance(responseContext.operationType())
                        .assertResponseBody(dataTable, element);
            });
        } else {
            factoryDtoAssertHandler.getInstance(responseContext.operationType())
                    .assertResponseBody(dataTable, responseBody);
        }

        TestContextStorage.clearContext();
    }
}