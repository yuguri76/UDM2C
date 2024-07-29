package com.example.livealone.broadcast.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BroadcastResponseDto {

  private String broadcast_title;
  private Long product_id;
  private String product_name;
  private Integer product_price;
  private String product_introduction;

}
