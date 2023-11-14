package com.iprody.product.service.rest.validation.annotation;

import com.iprody.product.service.rest.validation.validator.DiscountValidateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DiscountValidateValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscountValidate {

    /**
     * Message error for DiscountValidate.
     *
     * @return message
     */
    String message() default "Discount.until must be greater than Discount.from";

    /**
     * Groups for DiscountValidate.
     *
     * @return groups
     */
    Class<?>[] groups() default { };


    /**
     * Payload for DiscountValidate.
     * @return payload
     */
    Class<? extends Payload>[] payload() default { };
}
