package com.example.livealone.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livealone.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
