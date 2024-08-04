package com.example.livealone.broadcast.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BroadcastResponseDto {

  private Long broadcastId;
  private String broadcastTitle;
  private Long productId;
  private String productName;
  private Integer productPrice;
  private Long productQuantity;
  private String productIntroduction;

}
