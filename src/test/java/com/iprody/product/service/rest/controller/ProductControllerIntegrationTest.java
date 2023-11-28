package com.iprody.product.service.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.product.service.AbstractIntegrationTestBase;
import com.iprody.product.service.domain.CurrencyValue;
import com.iprody.product.service.domain.Discount;
import com.iprody.product.service.domain.ProductFilter;
import com.iprody.product.service.rest.dto.CurrencyResponseDto;
import com.iprody.product.service.rest.dto.DiscountForProductsRequestDto;
import com.iprody.product.service.rest.dto.DiscountRequestDto;
import com.iprody.product.service.rest.dto.PriceResponseDto;
import com.iprody.product.service.rest.dto.ProductCreateRequestDto;
import com.iprody.product.service.rest.dto.ProductResponseDto;
import com.iprody.product.service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.iprody.product.service.util.SortingProductProperties.NAME;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@RequiredArgsConstructor
class ProductControllerIntegrationTest extends AbstractIntegrationTestBase {

    public static final String URL_PRODUCT_CONTROLLER = "/api/v1/products";

    private static final String EXCEPTION_MESSAGE = "$.message";
    private static final String EXCEPTION_STATUS = "$.status";
    private static final String EXCEPTION_DETAILS = "$.details";

    private static final String VALIDATION_EXCEPTION_MESSAGE = "Request validation error occurred";
    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "Resource was not found";

    private static final ProductResponseDto FIRST_EXISTING_PRODUCT_DTO_FROM_DB = ProductResponseDto.builder()
            .id(1L)
            .name("Product for Update")
            .active(true)
            .price(PriceResponseDto.builder()
                    .value(BigDecimal.valueOf(100.10))
                    .currency(new CurrencyResponseDto(CurrencyValue.USD.name()))
                    .build())
            .build();

    private static final ProductResponseDto THIRD_EXISTING_PRODUCT_DTO_FROM_DB = ProductResponseDto.builder()
            .id(3L)
            .name("Test product 3")
            .active(true)
            .price(PriceResponseDto.builder()
                    .value(BigDecimal.valueOf(250.00))
                    .currency(new CurrencyResponseDto(CurrencyValue.USD.name()))
                    .build())
            .build();

    private static final ProductCreateRequestDto TEST_PRODUCT_CREATE_DTO = ProductCreateRequestDto.builder()
            .name("ProductName")
            .active(true)
            .price(BigDecimal.valueOf(10L))
            .currency(CurrencyValue.USD)
            .build();

    private static final DiscountRequestDto TEST_DISCOUNT_CREATE_DTO = DiscountRequestDto.builder()
            .value(10)
            .from(Instant.now())
            .until(Instant.now().plusSeconds(1000))
            .build();

    private static final Discount TEST_DISCOUNT = Discount.builder()
            .value(10)
            .validFrom(Instant.now())
            .validUntil(Instant.now().plusSeconds(1000))
            .build();

    private static final DiscountForProductsRequestDto TEST_DISCOUNT_CREATE_FOR_PRODUCTS_DTO =
            new DiscountForProductsRequestDto(List.of(2L, 3L), TEST_DISCOUNT_CREATE_DTO);

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ProductService productService;

    @SneakyThrows
    @Test
    void shouldCreateProductSuccess() {
        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(TEST_PRODUCT_CREATE_DTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.name").value(TEST_PRODUCT_CREATE_DTO.name()),
                        jsonPath("$.active").value(TEST_PRODUCT_CREATE_DTO.active()),
                        jsonPath("$.price.value").value(TEST_PRODUCT_CREATE_DTO.price()),
                        jsonPath("$.price.currency.value").value(TEST_PRODUCT_CREATE_DTO.currency().name()),
                        jsonPath("$.createdAt").isNotEmpty(),
                        jsonPath("$.updatedAt").isNotEmpty()
                );
    }

    @SneakyThrows
    @Test
    void shouldCreateProductWithInvalidDataFail() {

        final ProductCreateRequestDto notValidProductDto = ProductCreateRequestDto.builder()
                .name(null)
                .active(false)
                .price(null)
                .currency(CurrencyValue.USD)
                .build();

        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(notValidProductDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(VALIDATION_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.BAD_REQUEST.value())
                );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("provideForUpdateProduct")
    void shouldUpdateProductSuccess(ProductCreateRequestDto createEditDto,
                                    Supplier<List<ResultMatcher>> resultMatchersSupplier) {
        mockMvc.perform(
                        put(URL_PRODUCT_CONTROLLER + "/{id}", FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createEditDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(resultMatchersSupplier.get().toArray(new ResultMatcher[0]));
    }

    @SneakyThrows
    private static Stream<Arguments> provideForUpdateProduct() {
        return Stream.of(
                of(TEST_PRODUCT_CREATE_DTO,
                        (Supplier<List<ResultMatcher>>) () -> {
                            final var expectedProduct = TEST_PRODUCT_CREATE_DTO;
                            return List.of(
                                    jsonPath("$.id").value(FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id()),
                                    jsonPath("$.name").value(expectedProduct.name()),
                                    jsonPath("$.active").value(expectedProduct.active()),
                                    jsonPath("$.price.value").value(expectedProduct.price()),
                                    jsonPath("$.price.currency.value")
                                            .value(expectedProduct.currency().name()),
                                    jsonPath("$.createdAt").isNotEmpty(),
                                    jsonPath("$.updatedAt").isNotEmpty());
                        }), // Update full product
                of(ProductCreateRequestDto.builder()
                                .active(false)
                                .price(BigDecimal.valueOf(100L))
                                .currency(CurrencyValue.USD)
                                .build(),
                        (Supplier<List<ResultMatcher>>) () -> {
                            final var expectedProduct = ProductCreateRequestDto.builder()
                                    .active(false)
                                    .price(BigDecimal.valueOf(100L))
                                    .currency(CurrencyValue.USD)
                                    .build();
                            return List.of(
                                    jsonPath("$.id").value(FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id()),
                                    jsonPath("$.active").value(expectedProduct.active()),
                                    jsonPath("$.price.value").value(expectedProduct.price()),
                                    jsonPath("$.price.currency.value").value(expectedProduct.currency().name()),
                                    jsonPath("$.createdAt").isNotEmpty(),
                                    jsonPath("$.updatedAt").isNotEmpty()
                            );
                        }), // Update product without name
                of(ProductCreateRequestDto.builder()
                                .name("Test name")
                                .active(true)
                                .build(),
                        (Supplier<List<ResultMatcher>>) () -> {
                            final var expectedProduct = ProductCreateRequestDto.builder()
                                    .name("Test name")
                                    .active(true)
                                    .build();
                            return List.of(
                                    jsonPath("$.id").value(FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id()),
                                    jsonPath("$.name").value(expectedProduct.name()),
                                    jsonPath("$.active").value(expectedProduct.active()),
                                    jsonPath("$.createdAt").isNotEmpty(),
                                    jsonPath("$.updatedAt").isNotEmpty()
                            );
                        }) // Update product without price
        );
    }

    @SneakyThrows
    @Test
    void shouldUpdateProductWithNotExistIdFail() {
        final long notExistId = 100L;
        mockMvc.perform(
                        put(URL_PRODUCT_CONTROLLER + "/{id}", notExistId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(TEST_PRODUCT_CREATE_DTO))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(NOT_FOUND_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.NOT_FOUND.value()),
                        jsonPath(EXCEPTION_DETAILS).value("Product not found with id: " + notExistId)
                );
    }


    @SneakyThrows
    @Test
    void shouldFindByIdProductSuccess() {
        mockMvc.perform(
                        get(URL_PRODUCT_CONTROLLER + "/{id}", THIRD_EXISTING_PRODUCT_DTO_FROM_DB.id())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.id").value(THIRD_EXISTING_PRODUCT_DTO_FROM_DB.id()),
                        jsonPath("$.name").value(THIRD_EXISTING_PRODUCT_DTO_FROM_DB.name()),
                        jsonPath("$.active").value(THIRD_EXISTING_PRODUCT_DTO_FROM_DB.active()),
                        jsonPath("$.price.value").value(THIRD_EXISTING_PRODUCT_DTO_FROM_DB.price().value()),
                        jsonPath("$.price.currency.value")
                                .value(THIRD_EXISTING_PRODUCT_DTO_FROM_DB.price().currency().value()),
                        jsonPath("$.createdAt").isNotEmpty(),
                        jsonPath("$.updatedAt").isNotEmpty()
                );
    }

    @SneakyThrows
    @Test
    void shouldFindByIdWithNotExistIdFail() {
        final long notExistId = 100L;
        mockMvc.perform(
                        get(URL_PRODUCT_CONTROLLER + "/{id}", notExistId)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(NOT_FOUND_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.NOT_FOUND.value()),
                        jsonPath(EXCEPTION_DETAILS).value("Product not found with id: " + notExistId)
                );
    }

    @SneakyThrows
    @Test
    void shouldFindAllProductsSuccess() {
        final ProductFilter productFilter = ProductFilter.builder()
                .name("Test")
                .active(true)
                .sort(new ProductFilter.SortRequest(Set.of(NAME), Sort.Direction.DESC))
                .page(new ProductFilter.PageRequest(0, 1))
                .build();

        mockMvc.perform(
                        get(URL_PRODUCT_CONTROLLER)
                                .content(objectMapper.writeValueAsString(productFilter))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.content", hasSize(1)),
                        jsonPath("$.pageable.pageSize").value(1),
                        jsonPath("$.pageable.pageNumber").value(0)
                );
    }

    @SneakyThrows
    @Test
    void shouldApplyDiscountForOneProductSuccess() {

        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER + "/{id}/discounts",
                                FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(TEST_DISCOUNT_CREATE_DTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.id").value(FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id()),
                        jsonPath("$.discount.value").value(TEST_DISCOUNT_CREATE_DTO.value()),
                        jsonPath("$.discount.from")
                                .value(TEST_DISCOUNT_CREATE_DTO.from().atOffset(ZoneOffset.UTC).format(ISO_INSTANT)),
                        jsonPath("$.discount.until")
                                .value(TEST_DISCOUNT_CREATE_DTO.until().atOffset(ZoneOffset.UTC).format(ISO_INSTANT))
                );
    }

    @SneakyThrows
    @Test
    void shouldApplyDiscountForOneProductWithInvalidDataFail() {

        final var invalidDiscount = DiscountRequestDto.builder()
                .value(10)
                .until(Instant.now())
                .from(Instant.now().plusSeconds(1000))
                .build();


        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER + "/{id}/discounts",
                                FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDiscount))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(VALIDATION_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.BAD_REQUEST.value())
                );
    }

    @SneakyThrows
    @Test
    void shouldApplyDiscountForOneProductWithNotExistProductIdFail() {
        final long notExistProductId = 100L;
        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER + "/{id}/discounts",
                                notExistProductId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(TEST_DISCOUNT_CREATE_DTO))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(NOT_FOUND_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.NOT_FOUND.value()),
                        jsonPath(EXCEPTION_DETAILS).value("Product not found with id: " + notExistProductId)
                );
    }

    @SneakyThrows
    @Test
    void shouldApplyDiscountForGroupOfProductsSuccess() {
        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER + "/discounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(TEST_DISCOUNT_CREATE_FOR_PRODUCTS_DTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].discount.value").value(TEST_DISCOUNT_CREATE_DTO.value()),
                        jsonPath("$[0].discount.from")
                                .value(TEST_DISCOUNT_CREATE_DTO.from().atOffset(ZoneOffset.UTC).format(ISO_INSTANT)),
                        jsonPath("$[0].discount.until")
                                .value(TEST_DISCOUNT_CREATE_DTO.until().atOffset(ZoneOffset.UTC).format(ISO_INSTANT)),
                        jsonPath("$[1].discount.value").value(TEST_DISCOUNT_CREATE_DTO.value()),
                        jsonPath("$[1].discount.from")
                                .value(TEST_DISCOUNT_CREATE_DTO.from().atOffset(ZoneOffset.UTC).format(ISO_INSTANT)),
                        jsonPath("$[1].discount.until")
                                .value(TEST_DISCOUNT_CREATE_DTO.until().atOffset(ZoneOffset.UTC).format(ISO_INSTANT))
                );
    }

    @SneakyThrows
    @Test
    void shouldApplyDiscountForGroupOfProductsWithInvalidDataFail() {
        final var invalidDiscount = DiscountRequestDto.builder()
                .value(10)
                .until(null)
                .from(Instant.now().plusSeconds(1000))
                .build();

        final var testDiscountForProducts =
                new DiscountForProductsRequestDto(List.of(2L, 3L), invalidDiscount);


        mockMvc.perform(
                        post(URL_PRODUCT_CONTROLLER + "/discounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testDiscountForProducts))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(VALIDATION_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.BAD_REQUEST.value())
                );
    }

    @SneakyThrows
    @Test
    void shouldDeactivateDiscountForOneProductSuccess() {

        productService.applyDiscount(FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id(), TEST_DISCOUNT);

        mockMvc.perform(
                        delete(URL_PRODUCT_CONTROLLER + "/{id}/discounts",
                                FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.id").value(FIRST_EXISTING_PRODUCT_DTO_FROM_DB.id()),
                        jsonPath("$.discount.value").value(0),
                        jsonPath("$.discount.from").isEmpty(),
                        jsonPath("$.discount.until").isEmpty()
                );
    }

    @SneakyThrows
    @Test
    void shouldDeactivateDiscountForOneProductWithNotExistProductId() {
        final long notExistProductId = 100L;
        mockMvc.perform(
                        delete(URL_PRODUCT_CONTROLLER + "/{id}/discounts", notExistProductId)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath(EXCEPTION_MESSAGE).value(NOT_FOUND_EXCEPTION_MESSAGE),
                        jsonPath(EXCEPTION_STATUS).value(HttpStatus.NOT_FOUND.value()),
                        jsonPath(EXCEPTION_DETAILS).value("Product not found with id: " + notExistProductId)
                );
    }

    @SneakyThrows
    @Test
    void shouldDeactivateDiscountForGroupOfProductsSuccess() {

        final List<Long> listIdsProducts = List.of(2L, 3L);

        productService.applyDiscount(listIdsProducts, TEST_DISCOUNT);

        mockMvc.perform(
                        delete(URL_PRODUCT_CONTROLLER + "/discounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(listIdsProducts))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].discount.value").value(0),
                        jsonPath("$[0].discount.from").isEmpty(),
                        jsonPath("$[0].discount.until").isEmpty(),
                        jsonPath("$[1].discount.value").value(0),
                        jsonPath("$[1].discount.from").isEmpty(),
                        jsonPath("$[1].discount.until").isEmpty()
                );
    }
}
