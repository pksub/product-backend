package com.product.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.assignment.entity.Product;
import com.product.assignment.exception.BusinessException;
import com.product.assignment.repository.ProductRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private  ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
   

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("상품을 찾을 수 없습니다."));
    }

    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        if (product.getStock() < quantity) {
            throw new BusinessException("재고가 부족합니다.");
        }
        product.setStock(product.getStock() - quantity);
    }
} 