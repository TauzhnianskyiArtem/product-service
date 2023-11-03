package com.iprody.product.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


/**
 * Discount Entity.
 * Columns:
 *   id (PK)
 *   value (smallint, not-null)
 *   validFrom (timestamp with timezone, UTC+0 only, not-null)
 *   validUntil (timestamp with timezone, UTC+0 only, not-null)
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
public class Discount extends AbstractBaseEntity {

    /**
     * Discount value, this value is percentage.
     */
    private short value;

    /**
     * Discount valid from.
     */
    @Column(nullable = false)
    private Instant validFrom;

    /**
     * Discount valid until.
     */
    @Column(nullable = false)
    private Instant validUntil;

    /**
     * Create a new Discount and copy all properties from current Discount.
     *
     * @return Discount The new Discount entity.
     */
    public Discount copy() {
        return Discount.builder()
                .value(this.value)
                .validFrom(this.validFrom)
                .validUntil(this.validUntil)
                .build();
    }
}

