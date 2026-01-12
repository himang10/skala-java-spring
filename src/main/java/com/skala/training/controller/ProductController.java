package com.skala.training.controller;

import com.skala.training.dto.ProductRequest;
import com.skala.training.dto.ProductResponse;
import com.skala.training.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Product API
 * Demonstrates REST API design, HTTP methods, and Spring MVC annotations
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Constructor injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /api/products - Get all products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id} - Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * POST /api/products - Create a new product
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * PUT /api/products/{id} - Update an existing product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * DELETE /api/products/{id} - Delete a product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/products/search?name={name} - Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            List<ProductResponse> products = productService.searchProductsByName(name);
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * GET /api/products/filter/price?max={maxPrice} - Filter products by maximum price
     */
    @GetMapping("/filter/price")
    public ResponseEntity<List<ProductResponse>> filterByPrice(
            @RequestParam BigDecimal max) {
        List<ProductResponse> products = productService.findProductsByMaxPrice(max);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/filter/stock?min={minStock} - Filter products by minimum stock
     */
    @GetMapping("/filter/stock")
    public ResponseEntity<List<ProductResponse>> filterByStock(
            @RequestParam Integer min) {
        List<ProductResponse> products = productService.findProductsWithStockGreaterThan(min);
        return ResponseEntity.ok(products);
    }
}
