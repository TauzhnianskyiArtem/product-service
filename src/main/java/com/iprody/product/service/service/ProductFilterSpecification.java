package com.iprody.product.service.service;


import com.iprody.product.service.domain.Product;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;


@UtilityClass
public class ProductFilterSpecification {

    /**
     * Specification for  {@link Product#name}.
     *
     * @param name The name to added and filtered in specification.
     * @return Specification<Product> The specification product.
     */
    public Specification<Product> nameLike(String name) {
        final String percentage = "%";
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), percentage + name + percentage);
    }

    /**
     * Specification for {@link Product#active}.
     *
     * @param active The active status to added in specification.
     * @return Specification<Product> The specification product.
     */
    public Specification<Product> active(boolean active) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), active);
    }
}
