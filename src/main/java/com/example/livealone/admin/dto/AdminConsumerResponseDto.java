package com.example.livealone.admin.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminConsumerResponseDto {
  private Long userId;
  private String username;
  private int productQuantity;
  private int paymentAmount;
  private LocalDateTime orderDate;
}
