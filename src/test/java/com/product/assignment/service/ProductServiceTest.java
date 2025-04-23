package com.product.assignment.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.product.assignment.entity.Product;
import com.product.assignment.exception.BusinessException;
import com.product.assignment.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("테스트 상품");
        product.setDescription("테스트 상품 설명");
        product.setPrice(10000L);
        product.setStock(10);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // given
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> result = productService.getAllProducts();

        // then
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
    }

    @Test
    void getProduct_WithValidId_ShouldReturnProduct() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        Product result = productService.getProduct(1L);

        // then
        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
    }

    @Test
    void getProduct_WithInvalidId_ShouldThrowException() {
        // given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> productService.getProduct(999L));
    }

    @Test
    void updateStock_WithValidQuantity_ShouldUpdateStock() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        productService.updateStock(1L, 5);

        // then
        assertEquals(5, product.getStock());
    }

    @Test
    void updateStock_WithInvalidQuantity_ShouldThrowException() {
        // given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when & then
        assertThrows(BusinessException.class, () -> productService.updateStock(1L, 15));
    }
} 