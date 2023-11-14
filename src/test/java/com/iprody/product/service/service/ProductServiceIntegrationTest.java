package com.iprody.product.service.service;

import com.iprody.product.service.AbstractIntegrationTestBase;
import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.CurrencyValue;
import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.domain.Price;
import com.iprody.product.service.domain.Product;
import com.iprody.product.service.domain.ProductFilter;
import com.iprody.product.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.iprody.product.service.exception.ResourceNotFoundException.NOT_FOUND_WITH_ID_MESSAGE;
import static com.iprody.product.service.service.ProductService.PRODUCT_DOMAIN_NAME;
import static com.iprody.product.service.util.SortingProductProperties.ACTIVE;
import static com.iprody.product.service.util.SortingProductProperties.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ExtendWith(SoftAssertionsExtension.class)
class ProductServiceIntegrationTest extends AbstractIntegrationTestBase {

    private static final Long FIRST_EXISTING_PRODUCT_ID_FROM_DB = 1L;
    private static final String NAME_COLUMN = "name";
    private static final String ACTIVE_COLUMN = "active";

    private static final Product SECOND_EXISTING_PRODUCT_FROM_DB = Product.builder()
            .id(2L)
            .name("Test product 2")
            .active(true)
            .price(Price.builder()
                    .value(BigDecimal.valueOf(200.00))
                    .currency(Currency.builder()
                            .value(CurrencyValue.USD)
                            .build())
                    .build())
            .build();

    private static final Product TEST_PRODUCT_CREATE = Product.builder()
            .name("ProductName")
            .active(true)
            .price(Price.builder().value(BigDecimal.valueOf(10L))
                    .currency(Currency.builder()
                            .id(1L)
                            .value(CurrencyValue.USD)
                            .build())
                    .build())
            .build();

    private static final Discount TEST_DISCOUNT = Discount.builder()
            .value(10)
            .validFrom(Instant.now())
            .validUntil(Instant.now().plusSeconds(1000))
            .build();


    private ProductService productService;

    @Order(1)
    @Test
    void shouldCreateProductSuccess(SoftAssertions softly) {

        final Product actualProduct = productService.saveProduct(TEST_PRODUCT_CREATE);

        softly.assertThat(actualProduct.getId()).isNotZero();
        softly.assertThat(actualProduct.getName()).isEqualTo(TEST_PRODUCT_CREATE.getName());
        softly.assertThat(actualProduct.isActive()).isTrue();
        softly.assertThat(actualProduct.getPrice())
                .extracting("currency")
                .extracting("value")
                .isEqualTo(CurrencyValue.USD);
        softly.assertThat(actualProduct.getPrice())
                .extracting("value")
                .isEqualTo(TEST_PRODUCT_CREATE.getPrice().getValue());
        softly.assertThat(actualProduct.getCreatedAt()).isNotNull();
        softly.assertThat(actualProduct.getUpdatedAt()).isNotNull();
    }

    @Order(2)
    @Test
    void shouldFindByIdProductSuccess(SoftAssertions softly) {

        final Product actualProduct = productService.findById(SECOND_EXISTING_PRODUCT_FROM_DB.getId());

        softly.assertThat(actualProduct.getId()).isEqualTo(SECOND_EXISTING_PRODUCT_FROM_DB.getId());
        softly.assertThat(actualProduct.getName()).isEqualTo(SECOND_EXISTING_PRODUCT_FROM_DB.getName());
        softly.assertThat(actualProduct.getPrice())
                .extracting("currency")
                .extracting("value")
                .isEqualTo(SECOND_EXISTING_PRODUCT_FROM_DB.getPrice().getCurrency().getValue());
        softly.assertThat(actualProduct.getPrice().getValue())
                .isEqualByComparingTo(SECOND_EXISTING_PRODUCT_FROM_DB.getPrice().getValue());
        softly.assertThat(actualProduct.getCreatedAt()).isNotNull();
        softly.assertThat(actualProduct.getUpdatedAt()).isNotNull();
    }

    @Order(3)
    @Test
    void shouldFindProductByIdWithNonExistIdFail() {

        final long nonExistId = 100L;
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.findById(nonExistId));

        assertThatThrownBy(() -> productService.findById(nonExistId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(NOT_FOUND_WITH_ID_MESSAGE.formatted(PRODUCT_DOMAIN_NAME, nonExistId));
    }


    @Order(4)
    @Test
    void shouldFindAllProductsWithDefinePageableSuccess(SoftAssertions softly) {

        final String byName = "Test";
        final ProductFilter productFilter = ProductFilter.builder()
                .name(byName)
                .active(true)
                .sort(new ProductFilter.SortRequest(Set.of(NAME), Sort.Direction.DESC))
                .page(new ProductFilter.PageRequest(0, 1))
                .build();

        final Page<Product> actualProducts = productService.findAll(productFilter);

        softly.assertThat(actualProducts).allSatisfy(product -> {
            assertThat(product.isActive()).isTrue();
            assertThat(product.getName().indexOf(byName)).isNotEqualTo(-1);
        });
        softly.assertThat(actualProducts.getContent()).hasSize(1);
        softly.assertThat(actualProducts.getPageable()).extracting("pageSize").isEqualTo(1);
        softly.assertThat(actualProducts.getPageable()).extracting("pageNumber").isEqualTo(0);
        softly.assertThat(actualProducts.getPageable().getSort().getOrderFor(NAME_COLUMN))
                .isEqualTo(Sort.Order.desc(NAME_COLUMN));
    }

    @Order(5)
    @Test
    void shouldFindAllProductsByActiveSuccess(SoftAssertions softly) {

        final ProductFilter productFilter = ProductFilter.builder()
                .active(false)
                .page(new ProductFilter.PageRequest(0, 1))
                .build();

        final Page<Product> actualProducts = productService.findAll(productFilter);

        softly.assertThat(actualProducts.getContent()).isEmpty();
        softly.assertThat(actualProducts.getPageable()).extracting("sort").isEqualTo(Sort.unsorted());
    }

    @Order(6)
    @Test
    void shouldFindAllProductsWithOnlySortingSuccess(SoftAssertions softly) {

        final ProductFilter productFilter = ProductFilter.builder()
                .sort(new ProductFilter.SortRequest(Set.of(NAME, ACTIVE), Sort.Direction.ASC))
                .page(new ProductFilter.PageRequest(0, 2))
                .build();

        final Page<Product> actualProducts = productService.findAll(productFilter);

        softly.assertThat(actualProducts.getContent()).hasSize(2);
        softly.assertThat(actualProducts.getPageable())
                .extracting("pageSize")
                .isEqualTo(2);
        softly.assertThat(actualProducts.getPageable())
                .extracting("pageNumber")
                .isEqualTo(0);
        softly.assertThat(actualProducts.getPageable().getSort().getOrderFor(NAME_COLUMN))
                .isEqualTo(Sort.Order.asc(NAME_COLUMN));
        softly.assertThat(actualProducts.getPageable().getSort().getOrderFor(ACTIVE_COLUMN))
                .isEqualTo(Sort.Order.asc(ACTIVE_COLUMN));
    }

    @Order(7)
    @Test
    void shouldApplyDiscountForOneProductSuccess(SoftAssertions softly) {

        final Product actualProduct = productService
                .applyDiscount(FIRST_EXISTING_PRODUCT_ID_FROM_DB, TEST_DISCOUNT);

        softly.assertThat(actualProduct.getDiscount())
                .extracting("value")
                .isEqualTo(TEST_DISCOUNT.getValue());
        softly.assertThat(actualProduct.getDiscount())
                .extracting("validFrom")
                .isEqualTo(TEST_DISCOUNT.getValidFrom());
        softly.assertThat(actualProduct.getDiscount())
                .extracting("validUntil")
                .isEqualTo(TEST_DISCOUNT.getValidUntil());
        softly.assertThat(actualProduct.getDiscount())
                .extracting("createdAt")
                .isNotNull();
        softly.assertThat(actualProduct.getDiscount())
                .extracting("updatedAt")
                .isNotNull();
    }

    @Order(8)
    @Test
    void shouldApplyDiscountForOneProductWithNonExistIdFail() {

        final long nonExistId = 100L;
        assertThatThrownBy(() -> productService.applyDiscount(nonExistId, TEST_DISCOUNT))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(NOT_FOUND_WITH_ID_MESSAGE.formatted(PRODUCT_DOMAIN_NAME, nonExistId));

    }


    @Order(9)
    @Test
    void shouldApplyDiscountForGroupOfProductsSuccess(SoftAssertions softly) {

        final List<Product> actualProducts = productService
                .applyDiscount(List.of(2L, 3L), TEST_DISCOUNT);

        softly.assertThat(actualProducts)
                .hasSize(2)
                .extracting(Product::getDiscount)
                .allSatisfy(discount -> {
                    softly.assertThat(discount).isNotNull();
                    softly.assertThat(discount.getValue()).isEqualTo(TEST_DISCOUNT.getValue());
                    softly.assertThat(discount.getValidFrom()).isEqualTo(TEST_DISCOUNT.getValidFrom());
                    softly.assertThat(discount.getValidUntil()).isEqualTo(TEST_DISCOUNT.getValidUntil());
                    softly.assertThat(discount.getCreatedAt()).isNotNull();
                    softly.assertThat(discount.getUpdatedAt()).isNotNull();
                });
    }


    @Order(10)
    @Test
    void shouldApplyDiscountForGroupOfProductsWithEmptyIdsSuccess() {

        final List<Product> actualProducts = productService
                .applyDiscount(Collections.emptyList(), TEST_DISCOUNT);

        assertThat(actualProducts).isEmpty();
    }

    @Order(11)
    @Test
    void shouldDeactivateDiscountForOneProductSuccess() {

        final Discount testDiscount = Discount.builder()
                .value(10)
                .validFrom(Instant.now())
                .validUntil(Instant.now().plusSeconds(1000))
                .build();

        productService.applyDiscount(FIRST_EXISTING_PRODUCT_ID_FROM_DB, testDiscount);
        final Product actualProduct = productService.deactivateDiscount(2L);

        assertThat(actualProduct.getDiscount()).isNull();
    }

    @Order(12)
    @Test
    void shouldDeactivateDiscountForOneProductFail() {

        final long nonExistId = 100L;
        assertThatThrownBy(() -> productService.deactivateDiscount(nonExistId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(NOT_FOUND_WITH_ID_MESSAGE.formatted(PRODUCT_DOMAIN_NAME, nonExistId));
    }

    @Order(13)
    @Test
    void shouldDeactivateDiscountForGroupOfProductsSuccess() {

        productService.applyDiscount(List.of(2L, 3L), TEST_DISCOUNT);

        final List<Product> actualProducts = productService.deactivateDiscount(List.of(2L, 3L));

        assertThat(actualProducts)
                .hasSize(2)
                .extracting(Product::getDiscount)
                .allSatisfy(discount -> {
                    assertThat(discount).isNull();
                });
    }


    @Order(14)
    @Test
    void shouldGetCurrencyByValueSuccess(SoftAssertions softly) {

        final Currency actualCurrency = productService.getCurrencyByValue(CurrencyValue.USD);

        softly.assertThat(actualCurrency.getId()).isNotNull();
        softly.assertThat(actualCurrency.getValue()).isEqualTo(CurrencyValue.USD);
        softly.assertThat(actualCurrency.getCreatedAt()).isNotNull();
        softly.assertThat(actualCurrency.getValue()).isNotNull();
    }

}
