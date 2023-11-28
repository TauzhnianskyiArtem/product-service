package com.iprody.product.service.e2e.assertions;

import com.iprody.product.service.e2e.generated.model.ProductResponseDto;
import com.iprody.product.service.e2e.util.ProductModelMapper;
import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

@Component
public class ProductResponseDtoAssertion extends DtoAssertHandler<ProductResponseDto> {


    @Override
    public void assertActualResponse(DataTable dataTable, ProductResponseDto actualResponse, SoftAssertions softly) {
        var expectedResponseDto = ProductModelMapper.toResponseDto(dataTable);

        softly.assertThat(actualResponse.getName()).isEqualTo(expectedResponseDto.getName());
        softly.assertThat(actualResponse.getActive()).isEqualTo(expectedResponseDto.getActive());
        softly.assertThat(actualResponse.getPrice().getValue()).isEqualByComparingTo(
                expectedResponseDto.getPrice().getValue());
        softly.assertThat(actualResponse.getPrice().getCurrency().getValue()).isEqualTo(
                expectedResponseDto.getPrice().getCurrency().getValue());
    }
}
