package com.iprody.product.service.e2e.assertions;

import com.iprody.product.service.e2e.generated.model.PageProductResponseDto;
import com.iprody.product.service.e2e.util.ProductModelMapper;
import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

@Component
public class PageProductResponseDtoAssertion extends DtoAssertHandler<PageProductResponseDto> {
    @Override
    public void assertActualResponse(DataTable dataTable, PageProductResponseDto actualResponse, SoftAssertions softly) {
        final var expectedResponse = ProductModelMapper.toPageResponse(dataTable);

        softly.assertThat(actualResponse.getTotalElements()).isEqualTo(expectedResponse.getTotalElements());
        softly.assertThat(actualResponse.getTotalPages()).isEqualTo(expectedResponse.getTotalPages());
        softly.assertThat(actualResponse)
                .extracting("sort")
                .extracting("sorted")
                .isEqualTo(expectedResponse.getSort().getSorted());
        softly.assertThat(actualResponse)
                .extracting("pageable")
                .extracting("pageNumber")
                .isEqualTo(expectedResponse.getPageable().getPageNumber());
        softly.assertThat(actualResponse)
                .extracting("pageable")
                .extracting("pageSize")
                .isEqualTo(expectedResponse.getPageable().getPageSize());
    }
}
