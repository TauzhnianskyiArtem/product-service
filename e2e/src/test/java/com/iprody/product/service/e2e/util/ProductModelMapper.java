package com.iprody.product.service.e2e.util;


import com.iprody.product.service.e2e.generated.model.ApiErrorResponse;
import com.iprody.product.service.e2e.generated.model.CurrencyResponseDto;
import com.iprody.product.service.e2e.generated.model.DiscountForProductsRequestDto;
import com.iprody.product.service.e2e.generated.model.DiscountRequestDto;
import com.iprody.product.service.e2e.generated.model.DiscountResponseDto;
import com.iprody.product.service.e2e.generated.model.PageProductResponseDto;
import com.iprody.product.service.e2e.generated.model.PageRequest;
import com.iprody.product.service.e2e.generated.model.PageableObject;
import com.iprody.product.service.e2e.generated.model.PriceResponseDto;
import com.iprody.product.service.e2e.generated.model.ProductCreateRequestDto;
import com.iprody.product.service.e2e.generated.model.ProductFilter;
import com.iprody.product.service.e2e.generated.model.ProductResponseDto;
import com.iprody.product.service.e2e.generated.model.ProductUpdateRequestDto;
import com.iprody.product.service.e2e.generated.model.SortObject;
import com.iprody.product.service.e2e.generated.model.SortRequest;
import io.cucumber.datatable.DataTable;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for mapping data set in feature file to model.
 */
@UtilityClass
public class ProductModelMapper {
    private static final String ERROR_DETAILS_SEPARATOR = ";";
    private static final String LIST_ELEMENTS_SEPARATOR = ",";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Convert Datatable from feature file to ProductCreateRequestDto.
     *
     * @param dataTable - productCreateRequestDto parameters in feature file
     * @return ProductCreateRequestDto with datatable parameters
     */
    public ProductCreateRequestDto toCreateRequestDto(DataTable dataTable) {
        final var entry = dataTable.asMap();
        final var builder = ProductCreateRequestDto.builder();

        if (entry.get("name") != null)
            builder.name(entry.get("name"));
        if (entry.get("active") != null)
            builder.active(Boolean.valueOf(entry.get("active")));
        if (entry.get("price") != null)
            builder.price(BigDecimal.valueOf(Double.parseDouble(entry.get("price"))));
        if (entry.get("currency") != null)
            builder.currency(ProductCreateRequestDto.CurrencyEnum.valueOf(entry.get("currency")));

        return builder.build();
    }

    /**
     * Convert Datatable from feature file to ProductCreateRequestDto.
     *
     * @param dataTable - productUpdateRequestDto parameters in feature file
     * @return ProductUpdateRequestDto with datatable parameters
     */

    public ProductUpdateRequestDto toUpdateRequestDto(DataTable dataTable) {
        final var entry = dataTable.asMap();
        final var builder = ProductUpdateRequestDto.builder();

        if (entry.get("id") != null)
            builder.name(entry.get("id"));
        if (entry.get("name") != null)
            builder.name(entry.get("name"));
        if (entry.get("active") != null)
            builder.active(Boolean.valueOf(entry.get("active")));
        if (entry.get("price") != null)
            builder.price(BigDecimal.valueOf(Double.parseDouble(entry.get("price"))));
        if (entry.get("currency") != null)
            builder.currency(ProductUpdateRequestDto.CurrencyEnum.valueOf(entry.get("currency")));

        return builder.build();
    }

    /**
     * Convert Datatable from feature file to ProductResponseDto.
     *
     * @param dataTable - productResponseDto parameters in feature file
     * @return productResponseDto with datatable parameters
     */
    public ProductResponseDto toResponseDto(DataTable dataTable) {
        final var entry = dataTable.asMap();
        return ProductResponseDto.builder()
                .name(entry.get("name"))
                .active(Boolean.valueOf(entry.get("active")))
                .price(PriceResponseDto.builder()
                        .value(BigDecimal.valueOf(Double.parseDouble(entry.get("price.value"))))
                        .currency(new CurrencyResponseDto(entry.get("price.currency.value")))
                        .build()
                )
                .build();
    }

    /**
     * Convert Datatable from feature file to ApiErrorResponse.
     *
     * @param dataTable - apiErrorResponse parameters in feature file
     * @return ApiErrorResponse with datatable parameters
     */
    public ApiErrorResponse toApiErrorResponse(DataTable dataTable) {
        final var entry = dataTable.asMap();
        return ApiErrorResponse.builder()
                .status(Integer.valueOf(entry.get("status")))
                .message(entry.get("message"))
                .details(toErrorDetailsList(entry))
                .build();

    }

    public List<String> toErrorDetailsList(Map<String, String> map) {
        return Arrays.stream(map.get("details").split("\\s*" + ERROR_DETAILS_SEPARATOR + "\\s*")).toList();
    }

    /**
     * Convert Datatable from feature file to ProductFilter.
     *
     * @param dataTable - productFilter parameters in feature file
     * @return ProductFilter with datatable parameters
     */
    public static ProductFilter toProductFilter(DataTable dataTable) {
        final var entry = dataTable.asMap();
        final var builder = ProductFilter.builder();

        if (entry.get("name") != null)
            builder.name(entry.get("name"));
        if (entry.get("active") != null)
            builder.active(Boolean.valueOf(entry.get("active")));

        if (entry.get("sortBy") != null) {
            Set<SortRequest.SortByEnum> sortBy = Arrays.stream(entry.get("sortBy").split(","))
                    .map(SortRequest.SortByEnum::fromValue)
                    .collect(Collectors.toSet());
            builder.sort(new SortRequest(sortBy,
                    SortRequest.DirectionSortEnum.valueOf(entry.get("directionSort"))));
        }

        return builder
                .page(new PageRequest(
                        Integer.valueOf(entry.get("pageNumber")),
                        Integer.valueOf(entry.get("pageSize"))
                ))
                .build();
    }

    /**
     * Convert Datatable from feature file to PageProductResponseDto.
     *
     * @param dataTable - pageProductResponseDto parameters in feature file
     * @return PageProductResponseDto with datatable parameters
     */
    public PageProductResponseDto toPageResponse(DataTable dataTable) {
        final var entry = dataTable.asMap();
        return PageProductResponseDto.builder()
                .totalPages(Integer.valueOf(entry.get("totalPages")))
                .totalElements(Long.valueOf(entry.get("totalElements")))
                .sort(SortObject.builder()
                        .sorted(Boolean.valueOf(entry.get("sorted")))
                        .build()
                )
                .pageable(PageableObject.builder()
                        .pageSize(Integer.valueOf(entry.get("pageSize")))
                        .pageNumber(Integer.valueOf(entry.get("pageNumber")))
                        .build()
                )
                .build();
    }

    /**
     * Convert Datatable from feature file to DiscountRequestDto.
     *
     * @param dataTable - discountRequestDto parameters in feature file
     * @return DiscountRequestDto with datatable parameters
     */
    @SneakyThrows
    public DiscountRequestDto toDiscountRequestDto(DataTable dataTable) {
        final var entry = dataTable.asMap();

        final var builder = DiscountRequestDto.builder();

        if (entry.get("value") != null)
            builder.value(Integer.valueOf(entry.get("value")));
        if (entry.get("from") != null)
            builder.from(DATE_FORMAT.parse(entry.get("from")).toInstant());
        if (entry.get("until") != null)
            builder.until(DATE_FORMAT.parse(entry.get("until")).toInstant());

        return builder.build();
    }

    /**
     * Convert Datatable from feature file to DiscountResponseDto.
     *
     * @param dataTable - discountResponseDto parameters in feature file
     * @return DiscountResponseDto with datatable parameters
     */
    @SneakyThrows
    public DiscountResponseDto toDiscountResponseDto(DataTable dataTable) {
        final var entry = dataTable.asMap();

        if (entry.get("discountValue") == null)
            return DiscountResponseDto.builder().build();

        return DiscountResponseDto.builder()
                .value(Integer.valueOf(entry.get("discountValue")))
                .from(DATE_FORMAT.parse(entry.get("from")).toInstant())
                .until(DATE_FORMAT.parse(entry.get("until")).toInstant())
                .build();
    }

    public DiscountForProductsRequestDto toDiscountForProductsRequestDto(DataTable dataTable) {
        return DiscountForProductsRequestDto.builder()
                .ids(toProductIdList(dataTable))
                .discountRequestDto(toDiscountRequestDto(dataTable))
                .build();
    }

    public List<Long> toProductIdList(DataTable dataTable) {
        final var entry = dataTable.asMap();
        return Arrays.stream(entry.get("productIds").split("\\s*" + LIST_ELEMENTS_SEPARATOR + "\\s*"))
                .map(Long::parseLong)
                .toList();
    }
}