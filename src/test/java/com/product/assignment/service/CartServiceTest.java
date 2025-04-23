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
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.product.assignment.entity.Cart;
import com.product.assignment.entity.Customer;
import com.product.assignment.entity.Product;
import com.product.assignment.exception.BusinessException;
import com.product.assignment.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Customer customer;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("테스트 고객");

        product = new Product();
        product.setId(1L);
        product.setName("테스트 상품");
        product.setPrice(10000L);
        product.setStock(10);

        cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(2);
    }

    @Test
    void getCustomerCart_ShouldReturnCartItems() {
        // given
        List<Cart> cartItems = Arrays.asList(cart);
        when(cartRepository.findByCustomer(customer)).thenReturn(cartItems);

        // when
        List<Cart> result = cartService.getCustomerCart(customer);

        // then
        assertEquals(1, result.size());
        assertEquals(cart.getQuantity(), result.get(0).getQuantity());
    }

    @Test
    void addToCart_WithValidQuantity_ShouldAddToCart() {
        // given
        when(productService.getProduct(1L)).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // when
        Cart result = cartService.addToCart(customer, 1L, 2);

        // then
        assertNotNull(result);
        assertEquals(2, result.getQuantity());
    }

    @Test
    void addToCart_WithInvalidQuantity_ShouldThrowException() {
        // when & then
        assertThrows(BusinessException.class, () -> cartService.addToCart(customer, 1L, 0));
    }

    @Test
    void addToCart_WithInsufficientStock_ShouldThrowException() {
        // given
        when(productService.getProduct(1L)).thenReturn(product);

        // when & then
        assertThrows(BusinessException.class, () -> cartService.addToCart(customer, 1L, 15));
    }

    @Test
    void updateCartItem_WithValidQuantity_ShouldUpdateQuantity() {
        // given
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        // when
        cartService.updateCartItem(1L, 3);

        // then
        assertEquals(3, cart.getQuantity());
    }

    @Test
    void updateCartItem_WithInvalidQuantity_ShouldThrowException() {
        // when & then
        assertThrows(BusinessException.class, () -> cartService.updateCartItem(1L, 0));
    }

    @Test
    void removeFromCart_ShouldDeleteCartItem() {
        // given
        when(cartRepository.existsById(1L)).thenReturn(true);

        // when
        cartService.removeFromCart(1L);

        // then
        verify(cartRepository).deleteById(1L);
    }

    @Test
    void removeFromCart_WithNonExistentCartItem_ShouldThrowException() {
        // given
        when(cartRepository.existsById(999L)).thenReturn(false);

        // when & then
        assertThrows(BusinessException.class, () -> cartService.removeFromCart(999L));
    }

    @Test
    void clearCart_ShouldDeleteAllCartItems() {
        // when
        cartService.clearCart(customer);

        // then
        verify(cartRepository).deleteByCustomer(customer);
    }
} 