package com.iprody.product.service.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Price Entity.
 * Columns:
 *   id (PK)
 *   value (numeric[5,2], not-null)
 *   createdAt (timestamp with timezone, UTC+0 only, not-null) - from AbstractBaseEntity
 *   updatedAt (timestamp with timezone, UTC+0 only, not-null) - from AbstractBaseEntity
 * Relations:
 *   Currency: Many-2-One
 */
@Getter
@Setter
@ToString(exclude = "currency")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Price extends AbstractBaseEntity {

    /**
     * Price value.
     */
    @Column(nullable = false)
    private BigDecimal value;

    /**
     * Price have currency_id FK that references currency.id.
     */
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;
}

