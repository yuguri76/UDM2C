package com.example.livealone.broadcast.mapper;

import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastCode;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.product.entity.Product;
import com.example.livealone.user.entity.User;

public class BroadcastMapper {

  public static Broadcast toBroadcast(String title, User streamer, Product product, BroadcastCode broadcastCode) {
    return Broadcast.builder()
        .title(title)
        .broadcastStatus(BroadcastStatus.ONAIR)
        .broadcastCode(broadcastCode)
        .streamer(streamer)
        .product(product)
        .broadcastCode(broadcastCode)
        .build();
  }

  public static BroadcastResponseDto toBroadcastResponseDto(Broadcast broadcast) {
    return BroadcastResponseDto.builder()
        .title(broadcast.getTitle())
        .build();
  }
}
