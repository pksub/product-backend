package com.product.assignment.service;

import com.product.assignment.entity.Cart;
import com.product.assignment.entity.Customer;
import com.product.assignment.entity.Product;
import com.product.assignment.exception.BusinessException;
import com.product.assignment.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public List<Cart> getCustomerCart(Customer customer) {
        return cartRepository.findByCustomer(customer);
    }

    @Transactional
    public Cart addToCart(Customer customer, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("수량은 1 이상이어야 합니다.");
        }

        Product product = productService.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new BusinessException("재고가 부족합니다.");
        }
        
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        
        return cartRepository.save(cart);
    }

    @Transactional
    public void updateCartItem(Long cartId, Integer quantity) {
        if (quantity <= 0) {
            throw new BusinessException("수량은 1 이상이어야 합니다.");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException("장바구니 상품을 찾을 수 없습니다."));
        
        if (cart.getProduct().getStock() < quantity) {
            throw new BusinessException("재고가 부족합니다.");
        }
        
        cart.setQuantity(quantity);
    }

    @Transactional
    public void removeFromCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new BusinessException("장바구니 상품을 찾을 수 없습니다.");
        }
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public void clearCart(Customer customer) {
        cartRepository.deleteByCustomer(customer);
    }
} 