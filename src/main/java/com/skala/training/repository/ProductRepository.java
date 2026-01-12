package com.skala.training.repository;

import com.skala.training.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Product entity
 * Demonstrates Spring Data JPA repository pattern and query methods
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by name containing the given string (case-insensitive)
     * Demonstrates derived query methods
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find products with price less than or equal to the given value
     * Demonstrates derived query methods with comparison operators
     */
    List<Product> findByPriceLessThanEqual(BigDecimal price);

    /**
     * Find products with stock greater than the given value
     * Demonstrates custom JPQL query
     */
    @Query("SELECT p FROM Product p WHERE p.stock > :stock")
    List<Product> findProductsWithStockGreaterThan(@Param("stock") Integer stock);

    /**
     * Check if a product with the given name exists
     * Demonstrates existence queries
     */
    boolean existsByName(String name);
}
