package com.example.livealone.product.service;

import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.mapper.ProductMapper;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public ProductResponseDto createProduct(User user, ProductRequestDto requestDto) {

    Product newProduct = ProductMapper.toProduct(requestDto, user);

    Product saveProduct = productRepository.save(newProduct);

    return ProductMapper.toProductResponseDto(saveProduct);
  }
}
