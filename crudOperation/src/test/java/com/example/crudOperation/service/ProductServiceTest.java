package com.example.crudOperation.service;

import com.example.crudOperation.exception.ResourceNotFoundException;
import com.example.crudOperation.pojos.Category;
import com.example.crudOperation.pojos.Product;
import com.example.crudOperation.repository.ProductRepository;
import com.example.crudOperation.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        category = new Category("Electronics", "Electronic gadgets and devices");
        category.setId(1L);

        product = new Product("Smartphone", "Latest model smartphone", new BigDecimal("699.99"), category);
        product.setId(1L);
    }

    @Test
    void shouldSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.createProduct(product);

        assertEquals("Smartphone", savedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = Optional.ofNullable(productService.getProductById(1L));

        assertTrue(foundProduct.isPresent());
        assertEquals("Smartphone", foundProduct.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<Product> updatedProduct = Optional.ofNullable(productService.updateProduct(1L, product));

        assertTrue(updatedProduct.isPresent());
        assertEquals("Smartphone", updatedProduct.get().getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");

        // Mocking
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFound() {
        // Mocking
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Asserting the exception
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Product not found with id: 1", thrown.getMessage());
    }
}

