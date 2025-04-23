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
import com.product.assignment.entity.Order;
import com.product.assignment.entity.OrderStatus;
import com.product.assignment.entity.Product;
import com.product.assignment.exception.BusinessException;
import com.product.assignment.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product;
    private Cart cart;
    private Order order;

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

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(20000L);
    }

    @Test
    void getCustomerOrders_ShouldReturnOrders() {
        // given
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByCustomer(customer)).thenReturn(orders);

        // when
        List<Order> result = orderService.getCustomerOrders(customer);

        // then
        assertEquals(1, result.size());
        assertEquals(order.getTotalAmount(), result.get(0).getTotalAmount());
    }

    @Test
    void createOrder_WithValidCart_ShouldCreateOrder() {
        // given
        List<Cart> cartItems = Arrays.asList(cart);
        when(cartService.getCustomerCart(customer)).thenReturn(cartItems);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // when
        Order result = orderService.createOrder(customer);

        // then
        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(cartService).clearCart(customer);
    }

    @Test
    void createOrder_WithEmptyCart_ShouldThrowException() {
        // given
        when(cartService.getCustomerCart(customer)).thenReturn(Arrays.asList());

        // when & then
        assertThrows(BusinessException.class, () -> orderService.createOrder(customer));
    }

    @Test
    void processPayment_WithValidOrder_ShouldProcessPayment() {
        // given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when
        Order result = orderService.processPayment(1L, "txn_123");

        // then
        assertEquals(OrderStatus.PAID, result.getStatus());
        assertEquals("txn_123", result.getTransactionId());
    }

    @Test
    void processPayment_WithNonExistentOrder_ShouldThrowException() {
        // given
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> orderService.processPayment(999L, "txn_123"));
    }

    @Test
    void processPayment_WithAlreadyProcessedOrder_ShouldThrowException() {
        // given
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when & then
        assertThrows(BusinessException.class, () -> orderService.processPayment(1L, "txn_123"));
    }
} 