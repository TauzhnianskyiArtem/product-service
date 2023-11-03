package com.iprody.product.service.util;

import com.iprody.product.service.domain.ProductFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortingProductHelper {

    /**
     * Method for validating and getting Sort by sorting properties.
     *
     * @param sortRequest has sorting properties and sorting direction.
     * @return The BigDecimal object is calculated price with discount.
     */
    public Sort validateAndGetSortForProduct(ProductFilter.SortRequest sortRequest) {
        if (sortRequest == null || sortRequest.sortBy() == null || sortRequest.sortBy().isEmpty()) {
            return Sort.unsorted();
        }

        final List<String> listProperties = sortRequest.sortBy().stream()
                .map(SortingProductProperties::getValue).toList();

        if (sortRequest.directionSort() != null) {
            return Sort.by(sortRequest.directionSort(), listProperties.toArray(new String[0]));
        }
        return Sort.by(listProperties.toArray(new String[0]));
    }
}
