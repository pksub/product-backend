package com.product.assignment.controller;

import com.product.assignment.entity.Cart;
import com.product.assignment.entity.Customer;
import com.product.assignment.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "장바구니", description = "장바구니 관련 API")
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "장바구니 조회", description = "고객의 장바구니 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Cart>> getCart(@RequestParam Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        Long longId = customer.getId();
        return ResponseEntity.ok(cartService.getCustomerCart(customer));
    }

    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<Cart> addToCart(
            @RequestParam Long customerId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        Customer customer = new Customer();
        return ResponseEntity.ok(cartService.addToCart(customer, productId, quantity));
    }

    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니에 있는 상품의 수량을 수정합니다.")
    @PutMapping("/{cartId}")
    public ResponseEntity<Void> updateCartItem(
            @PathVariable Long cartId,
            @RequestParam Integer quantity) {
        cartService.updateCartItem(cartId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 상품을 삭제합니다.")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 비우기", description = "고객의 장바구니를 비웁니다.")
    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestParam Long customerId) {
        Customer customer = new Customer();
        cartService.clearCart(customer);
        return ResponseEntity.ok().build();
    }
} 