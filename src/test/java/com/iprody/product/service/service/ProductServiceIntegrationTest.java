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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.iprody.product.service.exception.ResourceNotFoundException.NOT_FOUND_WITH_ID_MESSAGE;
import static com.iprody.product.service.service.ProductService.PRODUCT_DOMAIN_NAME;
import static com.iprody.product.service.util.SortingProductProperties.ACTIVE;
import static com.iprody.product.service.util.SortingProductProperties.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.of;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
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
                            .value(CurrencyValue.USD)
                            .build())
                    .build())
            .build();

    private static final Discount TEST_DISCOUNT = Discount.builder()
            .value((short) 10)
            .validFrom(Instant.now())
            .validUntil(Instant.now().plusSeconds(1000))
            .build();


    private ProductService productService;

    @Test
    void shouldCreateProductSuccess() {

        final Product actualProduct = productService.createProduct(TEST_PRODUCT_CREATE);


        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProduct.getId()).isNotZero();
            softAssertions.assertThat(actualProduct.getName()).isEqualTo(TEST_PRODUCT_CREATE.getName());
            softAssertions.assertThat(actualProduct.isActive()).isTrue();
            softAssertions.assertThat(actualProduct.getPrice().getCurrency().getValue()).isEqualTo(CurrencyValue.USD);
            softAssertions.assertThat(actualProduct.getPrice().getValue())
                    .isEqualTo(TEST_PRODUCT_CREATE.getPrice().getValue());
            softAssertions.assertThat(actualProduct.getCreatedAt()).isNotNull();
            softAssertions.assertThat(actualProduct.getUpdatedAt()).isNotNull();
        });

    }

    @ParameterizedTest
    @MethodSource("provideForUpdateProduct")
    void shouldUpdateProductSuccess(Product updateProduct, ProductAssertions productAssertions) {

        final Product actualProduct = productService.updateProduct(FIRST_EXISTING_PRODUCT_ID_FROM_DB, updateProduct);

        productAssertions.assertActual(actualProduct);
    }

    private static Stream<Arguments> provideForUpdateProduct() {
        return Stream.of(
                of(TEST_PRODUCT_CREATE,
                        (ProductAssertions) actualProduct -> {
                            final var expectedProduct = TEST_PRODUCT_CREATE;

                            assertSoftly(softAssertions -> {
                                softAssertions.assertThat(actualProduct.getId())
                                        .isEqualTo(FIRST_EXISTING_PRODUCT_ID_FROM_DB);
                                softAssertions.assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
                                softAssertions.assertThat(actualProduct.isActive())
                                        .isEqualTo(expectedProduct.isActive());
                                softAssertions.assertThat(actualProduct.getPrice().getCurrency().getValue())
                                        .isEqualTo(expectedProduct.getPrice().getCurrency().getValue());
                                softAssertions.assertThat(actualProduct.getPrice().getValue())
                                        .isEqualTo(expectedProduct.getPrice().getValue());
                                softAssertions.assertThat(actualProduct.getCreatedAt()).isNotNull();
                                softAssertions.assertThat(actualProduct.getUpdatedAt()).isNotNull();
                            });
                        },
                        TEST_PRODUCT_CREATE), // Update full product

                of(Product.builder()
                                .active(false)
                                .price(Price.builder().value(BigDecimal.valueOf(100L))
                                        .currency(Currency.builder()
                                                .value(CurrencyValue.USD)
                                                .build())
                                        .build())
                                .build(),
                        (ProductAssertions) actualProduct -> {
                            final var expectedProduct = Product.builder()
                                    .active(false)
                                    .price(Price.builder().value(BigDecimal.valueOf(100L))
                                            .currency(Currency.builder()
                                                    .value(CurrencyValue.USD)
                                                    .build())
                                            .build())
                                    .build();

                            assertSoftly(softAssertions -> {
                                softAssertions.assertThat(actualProduct.getId())
                                        .isEqualTo(FIRST_EXISTING_PRODUCT_ID_FROM_DB);
                                softAssertions.assertThat(actualProduct.isActive())
                                        .isEqualTo(expectedProduct.isActive());
                                softAssertions.assertThat(actualProduct.getPrice().getCurrency().getValue())
                                        .isEqualTo(expectedProduct.getPrice().getCurrency().getValue());
                                softAssertions.assertThat(actualProduct.getPrice().getValue())
                                        .isEqualTo(expectedProduct.getPrice().getValue());
                                softAssertions.assertThat(actualProduct.getCreatedAt()).isNotNull();
                                softAssertions.assertThat(actualProduct.getUpdatedAt()).isNotNull();
                            });
                        }), // Update product without name
                of(Product.builder()
                                .name("Product without Price")
                                .active(true)
                                .build(),
                        (ProductAssertions) actualProduct -> {
                            final var expectedProduct = Product.builder()
                                    .name("Product without Price")
                                    .active(true)
                                    .build();

                            assertSoftly(softAssertions -> {
                                softAssertions.assertThat(actualProduct.getId())
                                        .isEqualTo(FIRST_EXISTING_PRODUCT_ID_FROM_DB);
                                softAssertions.assertThat(actualProduct.isActive())
                                        .isEqualTo(expectedProduct.isActive());
                                softAssertions.assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
                                softAssertions.assertThat(actualProduct.getCreatedAt()).isNotNull();
                                softAssertions.assertThat(actualProduct.getUpdatedAt()).isNotNull();
                            });
                        }), // Update product without price
                of(Product.builder()
                                .name("New Name")
                                .active(false)
                                .price(Price.builder().value(BigDecimal.valueOf(100L))
                                        .currency(Currency.builder().value(CurrencyValue.EUR).build()).build())
                                .build(),
                        (ProductAssertions) actualProduct -> {
                            final var expectedProduct = Product.builder()
                                    .name("New Name")
                                    .active(false)
                                    .price(Price.builder().value(BigDecimal.valueOf(100L))
                                            .currency(Currency.builder().value(CurrencyValue.EUR).build()).build())
                                    .build();

                            assertSoftly(softAssertions -> {
                                softAssertions.assertThat(actualProduct.getId())
                                        .isEqualTo(FIRST_EXISTING_PRODUCT_ID_FROM_DB);
                                softAssertions.assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
                                softAssertions.assertThat(actualProduct.isActive())
                                        .isEqualTo(expectedProduct.isActive());
                                softAssertions.assertThat(actualProduct.getPrice().getCurrency().getValue())
                                        .isEqualTo(expectedProduct.getPrice().getCurrency().getValue());
                                softAssertions.assertThat(actualProduct.getCreatedAt()).isNotNull();
                                softAssertions.assertThat(actualProduct.getUpdatedAt()).isNotNull();
                            });
                        })); // Update product with price, bur not have price value
    }


    @Test
    void shouldUpdateProductWithNonExistIdFail() {

        final long nonExistId = 100L;
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(nonExistId, TEST_PRODUCT_CREATE));

        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND_WITH_ID_MESSAGE
                .formatted(PRODUCT_DOMAIN_NAME, nonExistId));
    }


    @Test
    void shouldFindByIdProductSuccess() {

        final Product actualProduct = productService.findById(SECOND_EXISTING_PRODUCT_FROM_DB.getId());

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProduct.getId()).isEqualTo(SECOND_EXISTING_PRODUCT_FROM_DB.getId());
            softAssertions.assertThat(actualProduct.getName()).isEqualTo(SECOND_EXISTING_PRODUCT_FROM_DB.getName());
            softAssertions.assertThat(actualProduct.getPrice().getCurrency().getValue())
                    .isEqualTo(SECOND_EXISTING_PRODUCT_FROM_DB.getPrice().getCurrency().getValue());
            softAssertions.assertThat(actualProduct.getPrice().getValue())
                    .isEqualByComparingTo(SECOND_EXISTING_PRODUCT_FROM_DB.getPrice().getValue());
            softAssertions.assertThat(actualProduct.getCreatedAt()).isNotNull();
            softAssertions.assertThat(actualProduct.getUpdatedAt()).isNotNull();
        });
    }

    @Test
    void shouldFindProductByIdWithNonExistIdFail() {

        final long nonExistId = 100L;
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.findById(nonExistId));

        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND_WITH_ID_MESSAGE
                .formatted(PRODUCT_DOMAIN_NAME, nonExistId));
    }


    @Test
    void shouldFindAllProductsWithDefinePageableSuccess() {

        final String byName = "Test";
        final ProductFilter productFilter = ProductFilter.builder()
                .name(byName)
                .active(true)
                .sort(new ProductFilter.SortRequest(Set.of(NAME), Sort.Direction.DESC))
                .page(new ProductFilter.PageRequest(0, 1))
                .build();

        final Page<Product> actualProducts = productService.findAll(productFilter);


        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProducts).allSatisfy(product -> {
                assertThat(product.isActive()).isTrue();
                assertThat(product.getName().indexOf(byName)).isNotEqualTo(-1);
            });
            softAssertions.assertThat(actualProducts.getContent()).hasSize(1);
            softAssertions.assertThat(actualProducts.getPageable().getPageSize()).isEqualTo(1);
            softAssertions.assertThat(actualProducts.getPageable().getPageNumber()).isZero();
            softAssertions.assertThat(actualProducts.getPageable().getSort().getOrderFor(NAME_COLUMN))
                    .isEqualTo(Sort.Order.desc(NAME_COLUMN));
        });
    }

    @Test
    void shouldFindAllProductsByActiveSuccess() {

        final ProductFilter productFilter = ProductFilter.builder()
                .active(false)
                .page(new ProductFilter.PageRequest(0, 1))
                .build();

        final Page<Product> actualProducts = productService.findAll(productFilter);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProducts.getContent()).isEmpty();
            softAssertions.assertThat(actualProducts.getPageable().getSort()).isEqualTo(Sort.unsorted());
        });
    }

    @Test
    void shouldFindAllProductsWithOnlySortingSuccess() {

        final ProductFilter productFilter = ProductFilter.builder()
                .sort(new ProductFilter.SortRequest(Set.of(NAME, ACTIVE), Sort.Direction.ASC))
                .page(new ProductFilter.PageRequest(0, 2))
                .build();

        final Page<Product> actualProducts = productService.findAll(productFilter);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProducts.getContent()).hasSize(2);
            softAssertions.assertThat(actualProducts.getPageable().getPageSize()).isEqualTo(2);
            softAssertions.assertThat(actualProducts.getPageable().getPageNumber()).isZero();
            softAssertions.assertThat(actualProducts.getPageable().getSort().getOrderFor(NAME_COLUMN))
                    .isEqualTo(Sort.Order.asc(NAME_COLUMN));
            softAssertions.assertThat(actualProducts.getPageable().getSort().getOrderFor(ACTIVE_COLUMN))
                    .isEqualTo(Sort.Order.asc(ACTIVE_COLUMN));
        });
    }

    @Test
    void shouldApplyDiscountForOneProductSuccess() {

        final Product actualProduct = productService
                .applyDiscount(FIRST_EXISTING_PRODUCT_ID_FROM_DB, TEST_DISCOUNT);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProduct.getDiscount().getValue()).isEqualTo(TEST_DISCOUNT.getValue());
            softAssertions.assertThat(actualProduct.getDiscount().getValidFrom())
                    .isEqualTo(TEST_DISCOUNT.getValidFrom());
            softAssertions.assertThat(actualProduct.getDiscount()
                    .getValidUntil()).isEqualTo(TEST_DISCOUNT.getValidUntil());
            softAssertions.assertThat(actualProduct.getDiscount().getCreatedAt()).isNotNull();
            softAssertions.assertThat(actualProduct.getDiscount().getUpdatedAt()).isNotNull();
        });

    }


    @Test
    void shouldApplyDiscountForOneProductWithNonExistIdFail() {

        final long nonExistId = 100L;
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.applyDiscount(nonExistId, TEST_DISCOUNT));

        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND_WITH_ID_MESSAGE
                .formatted(PRODUCT_DOMAIN_NAME, nonExistId));
    }


    @Test
    void shouldApplyDiscountForGroupOfProductsSuccess() {

        final List<Product> actualProducts = productService
                .applyDiscount(List.of(2L, 3L), TEST_DISCOUNT);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualProducts)
                    .hasSize(2)
                    .extracting(Product::getDiscount)
                    .allSatisfy(discount -> {
                        softAssertions.assertThat(discount).isNotNull();
                        softAssertions.assertThat(discount.getValue()).isEqualTo(TEST_DISCOUNT.getValue());
                        softAssertions.assertThat(discount.getValidFrom()).isEqualTo(TEST_DISCOUNT.getValidFrom());
                        softAssertions.assertThat(discount.getValidUntil()).isEqualTo(TEST_DISCOUNT.getValidUntil());
                        softAssertions.assertThat(discount.getCreatedAt()).isNotNull();
                        softAssertions.assertThat(discount.getUpdatedAt()).isNotNull();
                    });
        });
    }


    @Test
    void shouldApplyDiscountForGroupOfProductsWithEmptyIdsSuccess() {

        final List<Product> actualProducts = productService
                .applyDiscount(Collections.emptyList(), TEST_DISCOUNT);

        assertThat(actualProducts).isEmpty();
    }

    @Test
    void shouldDeactivateDiscountForOneProductSuccess() {

        final Discount testDiscount = Discount.builder()
                .value((short) 10)
                .validFrom(Instant.now())
                .validUntil(Instant.now().plusSeconds(1000))
                .build();

        productService.applyDiscount(FIRST_EXISTING_PRODUCT_ID_FROM_DB, testDiscount);
        final Product actualProduct = productService.deactivateDiscount(2L);

        assertThat(actualProduct.getDiscount()).isNull();
    }

    @Test
    void shouldDeactivateDiscountForOneProductFail() {

        final long nonExistId = 100L;
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                productService.deactivateDiscount(nonExistId));

        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND_WITH_ID_MESSAGE
                .formatted(PRODUCT_DOMAIN_NAME, nonExistId));
    }

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

    @Test
    void shouldDeactivateDiscountForGroupOfProductsWithEmptyIdsSuccess() {

        productService.applyDiscount(Collections.emptyList(), TEST_DISCOUNT);

        final List<Product> actualProducts = productService.
                deactivateDiscount(Collections.emptyList());

        assertThat(actualProducts).isEmpty();
    }

}
