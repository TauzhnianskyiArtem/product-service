package com.iprody.product.service.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


/**
 * Product
 * Columns:
 *   id (PK)
 *   name (varchar, max. length 100 chars, not-null)
 *   active (boolean, 0 - disactivated 1 - active, not-null)
 *   createdAt (timestamp with timezone, UTC+0 only, not-null) - from AbstractBaseEntity
 *   updatedAt (timestamp with timezone, UTC+0 only, not-null) - from AbstractBaseEntity
 * Relations:
 *   Price: One-2-One
 *   Discount: One-2-One
 */
@NamedEntityGraph(
        name = "ProductWithAllProperties",
        attributeNodes = {@NamedAttributeNode(value = "price", subgraph = "subgraph.price"),
                @NamedAttributeNode("discount")},
        subgraphs = @NamedSubgraph(name = "subgraph.price",
                attributeNodes = @NamedAttributeNode("currency")))
@Getter
@Setter
@ToString(exclude = {"discount", "price"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Product extends AbstractBaseEntity {

    /**
     * Product name.
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * Product active (boolean, false - disactivated, true - active).
     */
    private boolean active;

    /**
     * Product have price_id FK that references price.id.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private Price price;

    /**
     * Product have discount_id FK that references discount.id.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;
}

