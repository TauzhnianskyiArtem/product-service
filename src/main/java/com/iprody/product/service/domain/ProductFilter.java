package com.iprody.product.service.domain;

import com.iprody.product.service.util.SortingProductProperties;
import lombok.Builder;
import org.springframework.data.domain.Sort;

import java.util.Set;


/**
 * This record needed for filtering, sorting and paging products.
 * Include field:
 *
 * @param name   - filtering by name product,
 * @param active - filtering by active status,
 * @param sort   - sorting products: sortBy - set columns to be sorted products,
 *               directionSort - sort direction (ASC, DESC),
 * @param page   - paging products: pageNumber - number pageNumber, pageSize - size pageNumber;
 */
@Builder
public record ProductFilter(String name,
                            Boolean active,
                            SortRequest sort,
                            PageRequest page
) {

    public record SortRequest(Set<SortingProductProperties> sortBy,
                              Sort.Direction directionSort) {
    }

    public record PageRequest(int pageNumber,
                              int pageSize) {
    }
}
