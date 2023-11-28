package com.iprody.product.service.e2e.assertions;

import com.iprody.product.service.e2e.generated.model.ProductResponseDto;
import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

@Component
public class DiscountDeactivateResponseDtoAssertion extends DtoAssertHandler<ProductResponseDto> {

    @Override
    public void assertActualResponse(DataTable dataTable, ProductResponseDto productResponse, SoftAssertions softly) {
        final var actualResponse = productResponse.getDiscount();

        softly.assertThat(actualResponse.getValue()).isZero();
        softly.assertThat(actualResponse.getFrom()).isNull();
        softly.assertThat(actualResponse.getUntil()).isNull();
    }
}
