package com.iprody.product.service.e2e.assertions;

import com.iprody.product.service.e2e.generated.model.ProductResponseDto;
import com.iprody.product.service.e2e.util.ProductModelMapper;
import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

@Component
public class DiscountActivateResponseDtoAssertion extends DtoAssertHandler<ProductResponseDto> {

    @Override
    public void assertActualResponse(DataTable dataTable, ProductResponseDto productResponse, SoftAssertions softly) {
        final var expectedResponse = ProductModelMapper.toDiscountResponseDto(dataTable);
        final var actualResponse = productResponse.getDiscount();

        softly.assertThat(actualResponse.getValue()).isEqualTo(expectedResponse.getValue());
        softly.assertThat(actualResponse.getFrom()).isEqualTo(expectedResponse.getFrom());
        softly.assertThat(actualResponse.getUntil()).isEqualTo(expectedResponse.getUntil());
    }
}
