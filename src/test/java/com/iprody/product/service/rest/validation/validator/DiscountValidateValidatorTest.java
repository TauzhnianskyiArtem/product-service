package com.iprody.product.service.rest.validation.validator;

import com.iprody.product.service.rest.dto.DiscountRequestDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

@ExtendWith(MockitoExtension.class)
class DiscountValidateValidatorTest {

    private final DiscountValidateValidator validator = new DiscountValidateValidator();

    @ParameterizedTest
    @MethodSource("provideArgumentForValidation")
    void validate(DiscountRequestDto createDto,
                  boolean expectedOutput) {

        final boolean actualOutput = validator.isValid(createDto, null);

        assertThat(actualOutput).isEqualTo(expectedOutput);

    }

    private static Stream<Arguments> provideArgumentForValidation() {
        return Stream.of(
                of(DiscountRequestDto.
                        builder()
                        .from(Instant.now())
                        .until(Instant.now().plusSeconds(10))
                        .build(), true),
                of(DiscountRequestDto.
                        builder()
                        .from(null)
                        .until(null)
                        .build(), false),
                of(DiscountRequestDto.
                        builder()
                        .from(Instant.now().plusSeconds(10))
                        .until(Instant.now())
                        .build(), false)
        );
    }
}
