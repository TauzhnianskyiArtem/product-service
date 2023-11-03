package com.iprody.product.service.service;

import com.iprody.product.service.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
class ProductFilterSpecificationTest {

    @Test
    void shouldCreatedSpecificationWithLikeByName() {

        final String testName = "test";

        final Specification<Product> actualProductSpecification = ProductFilterSpecification.nameLike(testName);

        assertThat(actualProductSpecification).isNotNull();
    }

    @Test
    void shouldCreatedSpecificationByActive() {

        final Specification<Product> actualProductSpecification = ProductFilterSpecification.active(true);

        assertThat(actualProductSpecification).isNotNull();
    }

}
