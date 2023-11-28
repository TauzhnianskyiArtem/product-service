package com.iprody.product.service.e2e.assertions;

import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;

/**
 * This is an abstract class that provides a handler for asserting response.
 *
 * @param <T> the type of the response body
 */
public abstract class DtoAssertHandler<T> {

    /**
     * Asserts the response body with the provided DataTable.
     *
     * @param dataTable the DataTable against which to assert the response body
     */
    public void assertResponseBody(DataTable dataTable, T actualResponse) {
        final var softly = new SoftAssertions();

        assertActualResponse(dataTable, actualResponse, softly);

        softly.assertAll();
    }

    /**
     * Abstract method to be implemented by subclasses for asserting the actual response.
     *
     * @param dataTable      the DataTable against which to assert the response body
     * @param actualResponse the actual response body to be asserted
     * @param softly         the SoftAssertions object used for the assertions
     */
    abstract void assertActualResponse(DataTable dataTable, T actualResponse, SoftAssertions softly);
}
