package com.iprody.product.service.e2e.assertions;

import com.iprody.product.service.e2e.util.ProductOperationType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class FactoryDtoAssertHandler {
    Map<ProductOperationType, DtoAssertHandler> dtoAssertHandlers;

    public FactoryDtoAssertHandler(ApiErrorResponseAssertion apiErrorResponseAssertion,
                                   ProductResponseDtoAssertion productResponseDtoAssertion,
                                   DiscountActivateResponseDtoAssertion discountActivateResponseDtoAssertion,
                                   DiscountDeactivateResponseDtoAssertion discountDeactivateResponseDtoAssertion,
                                   PageProductResponseDtoAssertion pageProductResponseDtoAssertion) {
        this.dtoAssertHandlers = Map.of(
            ProductOperationType.CREATE, productResponseDtoAssertion,
            ProductOperationType.UPDATE, productResponseDtoAssertion,
            ProductOperationType.FIND_BY_ID, productResponseDtoAssertion,
            ProductOperationType.FIND_ALL, pageProductResponseDtoAssertion,
            ProductOperationType.ACTIVATE_DISCOUNT, discountActivateResponseDtoAssertion,
            ProductOperationType.DEACTIVATE_DISCOUNT, discountDeactivateResponseDtoAssertion,
            ProductOperationType.ERROR, apiErrorResponseAssertion
        );
    }

    public DtoAssertHandler getInstance(ProductOperationType operationType) {

        if (dtoAssertHandlers.containsKey(operationType)) {
            return dtoAssertHandlers.get(operationType);
        }

        throw new IllegalArgumentException("An unprocessed operation type was obtained: " + operationType);
    }
}