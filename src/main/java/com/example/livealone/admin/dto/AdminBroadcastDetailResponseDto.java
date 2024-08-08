package com.example.livealone.admin.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminBroadcastDetailResponseDto {
  private String broadcastTitle;
  private String broadcastStreamer;
  private LocalDateTime broadcastStartTime;
  private LocalDateTime broadcastEndTime;
  private String productName;
  private int productPrice;
  private Long productQuantity;
  private String productIntroduction;
  private Long totalOrderCount;
  private Long totalSalePrice;
}
