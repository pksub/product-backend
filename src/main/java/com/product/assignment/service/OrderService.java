package com.product.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.assignment.entity.*;
import com.product.assignment.exception.BusinessException;
import com.product.assignment.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private  OrderRepository orderRepository;
    private  CartService cartService;
    private  ProductService productService;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }   

    public List<Order> getCustomerOrders(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    @Transactional
    public Order createOrder(Customer customer) {
        List<Cart> cartItems = cartService.getCustomerCart(customer);
        if (cartItems.isEmpty()) {
            throw new BusinessException("장바구니가 비어있습니다.");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);

        long totalAmount = 0;
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getOrderItems().add(orderItem);

            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            productService.updateStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        cartService.clearCart(customer);
        return orderRepository.save(order);
    }

    @Transactional
    public Order processPayment(Long orderId, String transactionId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("이미 처리된 주문입니다.");
        }

        order.setTransactionId(transactionId);
        order.setStatus(OrderStatus.PAID);
        return order;
    }
} 