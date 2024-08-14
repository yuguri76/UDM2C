package com.example.livealone.broadcast.dto;

import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBroadcastResponseDto {
  private String title;

  private BroadcastStatus status;

  private String productName;

  private LocalDateTime airTime;
  private Integer totalSalePrice;

  @QueryProjection
  public UserBroadcastResponseDto(String title, BroadcastStatus status, String productName, LocalDateTime airTime, Integer totalSalePrice) {

    this.title = title;
    this.status = status;
    this.productName = productName;
    this.airTime = airTime;
    this.totalSalePrice = totalSalePrice;

  }
}
