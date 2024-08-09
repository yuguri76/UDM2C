package com.example.livealone.product.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.product.dto.ProductRequestDto;
import com.example.livealone.product.dto.ProductResponseDto;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.mapper.ProductMapper;
import com.example.livealone.product.repository.ProductRepository;
import com.example.livealone.user.entity.User;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  private final MessageSource messageSource;
  private final RedissonClient redissonClient;

  public final static String REDIS_PRODUCT_KEY = "Product::";

  @Transactional
  public ProductResponseDto createProduct(User user, ProductRequestDto requestDto) {

    Product newProduct = ProductMapper.toProduct(requestDto, user);

    Product saveProduct = productRepository.save(newProduct);

    return ProductMapper.toProductResponseDto(saveProduct);
  }

  public Product findByProductId(Long productId) {
    RBucket<Product> bucket = redissonClient.getBucket(REDIS_PRODUCT_KEY + productId);
    if (bucket.isExists()) {
      return bucket.get();
    }

    Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                    "product.not.found",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND)
    );

    bucket.set(product, 1, TimeUnit.HOURS);

    return product;
  }

  public Product saveProduct(Product product) {

    return productRepository.save(product);
  }
}
