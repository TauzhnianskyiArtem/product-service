package com.iprody.product.service.repository;

import com.iprody.product.service.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Find Product by id.
     *
     * @param id The  id to found product.
     * @return Optional<Product> The found product.
     */
    @EntityGraph("ProductWithAllProperties")
    @Override
    Optional<Product> findById(Long id);


    /**
     * Find List products by ids.
     *
     * @param id The list id to found list products.
     * @return List<Product>The found list products.
     */
    @EntityGraph("ProductWithAllProperties")
    List<Product> findByIdIn(List<Long> id);

    /**
     * Find List products by specification and pageable properties.
     *
     * @param spec The Specification for filtering products.
     * @param pageable The Pageable for sorting and pagination.
     * @return Page<Product> The found products.
     */
    @EntityGraph("ProductWithAllProperties")
    @Override
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

}
