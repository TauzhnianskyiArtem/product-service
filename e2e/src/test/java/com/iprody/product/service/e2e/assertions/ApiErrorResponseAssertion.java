package com.iprody.product.service.e2e.assertions;

import com.iprody.product.service.e2e.generated.model.ApiErrorResponse;
import com.iprody.product.service.e2e.util.ProductModelMapper;
import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

@Component
public class ApiErrorResponseAssertion extends DtoAssertHandler<ApiErrorResponse> {
    @Override
    public void assertActualResponse(DataTable dataTable, ApiErrorResponse actualResponse, SoftAssertions softly) {
        var expectedResponse = ProductModelMapper.toApiErrorResponse(dataTable);

        softly.assertThat(actualResponse.getStatus()).isEqualTo(expectedResponse.getStatus());
        softly.assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
        softly.assertThatList(actualResponse.getDetails()).isEqualTo(expectedResponse.getDetails());
    }
}
