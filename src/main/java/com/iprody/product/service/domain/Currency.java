package com.iprody.product.service.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Currency
 * Columns:
 *   id (PK)
 *   value (varchar, min-max length 3, not-null) - ISO 4217
 *   createdAt (timestamp with timezone, UTC+0 only, not-null) - from AbstractBaseEntity
 *   updatedAt (timestamp with timezone, UTC+0 only, not-null) - from AbstractBaseEntity
 * The entity hasn`t relations.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Currency extends AbstractBaseEntity {

    /**
     * Currency value. This value follow ISO 4217.
     */
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CurrencyValue value;
}

