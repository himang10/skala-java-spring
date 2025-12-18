package com.skala.training.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skala.training.dto.ProductRequest;
import com.skala.training.dto.ProductResponse;
import com.skala.training.exception.ResourceNotFoundException;
import com.skala.training.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController
 * Demonstrates @WebMvcTest for testing REST controllers
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        // Given
        ProductResponse product1 = new ProductResponse(1L, "Product 1", "Description 1",
                new BigDecimal("10.00"), 5, LocalDateTime.now(), LocalDateTime.now());
        ProductResponse product2 = new ProductResponse(2L, "Product 2", "Description 2",
                new BigDecimal("20.00"), 10, LocalDateTime.now(), LocalDateTime.now());
        List<ProductResponse> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_WhenExists_ShouldReturnProduct() throws Exception {
        // Given
        ProductResponse product = new ProductResponse(1L, "Product 1", "Description 1",
                new BigDecimal("10.00"), 5, LocalDateTime.now(), LocalDateTime.now());
        when(productService.getProductById(1L)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.price").value(10.00));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void getProductById_WhenNotExists_ShouldReturn404() throws Exception {
        // Given
        when(productService.getProductById(999L))
                .thenThrow(new ResourceNotFoundException("Product", 999L));

        // When & Then
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    void createProduct_WithValidData_ShouldReturnCreated() throws Exception {
        // Given
        ProductRequest request = new ProductRequest("New Product", "New Description",
                new BigDecimal("50.00"), 20);
        ProductResponse response = new ProductResponse(1L, "New Product", "New Description",
                new BigDecimal("50.00"), 20, LocalDateTime.now(), LocalDateTime.now());
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(50.00));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturn400() throws Exception {
        // Given - Empty name and negative price
        ProductRequest request = new ProductRequest("", "Description",
                new BigDecimal("-10.00"), 5);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(ProductRequest.class));
    }

    @Test
    void updateProduct_WithValidData_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        ProductRequest request = new ProductRequest("Updated Product", "Updated Description",
                new BigDecimal("75.00"), 30);
        ProductResponse response = new ProductResponse(1L, "Updated Product", "Updated Description",
                new BigDecimal("75.00"), 30, LocalDateTime.now(), LocalDateTime.now());
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(75.00));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    void deleteProduct_WhenExists_ShouldReturn204() throws Exception {
        // Given
        doNothing().when(productService).deleteProduct(1L);

        // When & Then
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
