package com.product.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.assignment.entity.Cart;
import com.product.assignment.entity.Customer;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomer(Customer customer);
    void deleteByCustomer(Customer customer);
} 