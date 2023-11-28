package com.iprody.product.service.rest.validation.validator;

import com.iprody.product.service.rest.dto.DiscountRequestDto;
import com.iprody.product.service.rest.validation.annotation.DiscountValidate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * This validator needed for validation fields from and until discount. UUntil must be greater than from.
 */
public class DiscountValidateValidator implements ConstraintValidator<DiscountValidate, DiscountRequestDto>  {
    @Override
    public final boolean isValid(DiscountRequestDto value, ConstraintValidatorContext context) {
        return value.until() != null && value.from() != null && value.until().isAfter(value.from());
    }
}
