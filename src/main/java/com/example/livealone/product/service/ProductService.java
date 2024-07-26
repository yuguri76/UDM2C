package com.example.livealone.product.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.mapper.ProductMapper;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final MessageSource messageSource;

  @Transactional
  public ProductResponseDto createProduct(User user, ProductRequestDto requestDto) {

    Product newProduct = ProductMapper.toProduct(requestDto, user);

    Product saveProduct = productRepository.save(newProduct);
    ProductResponseDto responseDto = ProductMapper.toProductResponseDto(saveProduct);

    return responseDto;
  }

  @Transactional(readOnly = true)
  public ProductResponseDto inquiryProduct(Long productId) {

    Product product = productRepository.findById(productId).orElseThrow(() ->
        new CustomException(messageSource.getMessage(
        "product.not.found",
        null,
        CustomException.DEFAULT_ERROR_MESSAGE,
        Locale.getDefault()
    ), HttpStatus.NOT_FOUND)
    );

    ProductResponseDto responseDto = ProductMapper.toProductResponseDto(product);

    return responseDto;
  }
}
