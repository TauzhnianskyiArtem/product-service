package com.iprody.product.service.repository;

import com.iprody.product.service.domain.Currency;
import com.iprody.product.service.domain.CurrencyValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Find Currency entity by value.
     *
     * @param value The Currency value id to found Currency.
     * @return Currency The found Currency entity.
     */
    Currency getByValue(CurrencyValue value);
}
