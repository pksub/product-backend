package com.product.assignment.controller;

import com.product.assignment.entity.Customer;
import com.product.assignment.entity.Order;
import com.product.assignment.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주문", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private  OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 목록 조회", description = "고객의 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return ResponseEntity.ok(orderService.getCustomerOrders(customer));
    }

    @Operation(summary = "주문 생성", description = "장바구니의 상품들로 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        return ResponseEntity.ok(orderService.createOrder(customer));
    }

    @Operation(summary = "결제 처리", description = "주문에 대한 결제를 처리합니다.")
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Order> processPayment(
            @PathVariable Long orderId,
            @RequestParam String transactionId) {
        return ResponseEntity.ok(orderService.processPayment(orderId, transactionId));
    }
} 