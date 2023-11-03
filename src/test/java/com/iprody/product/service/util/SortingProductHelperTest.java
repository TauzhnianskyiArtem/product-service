package com.iprody.product.service.util;

import com.iprody.product.service.domain.ProductFilter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.iprody.product.service.util.SortingProductProperties.ACTIVE;
import static com.iprody.product.service.util.SortingProductProperties.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@ExtendWith(MockitoExtension.class)
class SortingProductHelperTest {

    private final SortingProductHelper sortingProductHelper = new SortingProductHelper();

    @ParameterizedTest
    @MethodSource("provideSetForSort")
    void validateAndGetSortForProduct(Set<SortingProductProperties> input,
                                      Sort.Direction direction,
                                      Sort expectedSort) {
        final Sort actualSort = sortingProductHelper
                .validateAndGetSortForProduct(new ProductFilter.SortRequest(input, direction));

        final List<Sort.Order> actualOrdersSort = actualSort.stream()
                .sorted(Comparator.comparing(Sort.Order::getProperty)).toList();

        final List<Sort.Order> expectedOrdersSort = expectedSort.stream()
                .sorted(Comparator.comparing(Sort.Order::getProperty)).toList();

        assertThat(actualOrdersSort)
                .isEqualTo(expectedOrdersSort);
    }

    private static Stream<Arguments> provideSetForSort() {
        final String name = "name";
        final String active = "active";

        return Stream.of(
                of(Set.of(NAME, ACTIVE), Sort.Direction.ASC, Sort.by(Sort.Direction.ASC, name, active)),
                of(Set.of(NAME), Sort.Direction.DESC, Sort.by(Sort.Direction.DESC, name)),
                of(Set.of(ACTIVE), Sort.Direction.ASC, Sort.by(Sort.Direction.ASC, active)),
                of(Set.of(), Sort.Direction.ASC, Sort.unsorted()),
                of(null, Sort.Direction.ASC, Sort.unsorted()));
    }

}
