package com.product.assignment.controller;

import com.product.assignment.entity.Product;
import com.product.assignment.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "상품", description = "상품 관련 API")
@Controller
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private  ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 조회", description = "모든 상품의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            logger.info("상품 목록 조회 성공: {} 개의 상품", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("상품 목록 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProduct(id);
            logger.info("상품 상세 조회 성공: ID {}", id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("상품 상세 조회 중 오류 발생: ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 