package com.example.livealone.admin.mapper;

import com.example.livealone.admin.dto.AdminBroadcastDetailResponseDto;
import com.example.livealone.admin.dto.AdminBroadcastListResponseDto;
import com.example.livealone.admin.dto.AdminConsumerResponseDto;
import com.example.livealone.admin.dto.AdminRoleResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.order.entity.Order;
import com.example.livealone.product.entity.Product;
import com.example.livealone.user.entity.UserRole;

public class AdminMapper {
  public static AdminRoleResponseDto toAdminRoleResponseDto(UserRole role) {
    return AdminRoleResponseDto.builder()
        .role(role)
        .build();
  }

  public static AdminBroadcastDetailResponseDto toAdminBroadcastDetailResponseDto(Broadcast broadcast, Product product, Long totalOrderCount, Long totalSalePrice) {
    return AdminBroadcastDetailResponseDto.builder()
        .broadcastTitle(broadcast.getTitle())
        .broadcastStreamer(broadcast.getStreamer().getUsername())
        .broadcastStartTime(broadcast.getCreatedAt())
        .broadcastEndTime(broadcast.getUpdatedAt())
        .productName(product.getName())
        .productPrice(product.getPrice())
        .productQuantity(product.getQuantity())
        .productIntroduction(product.getIntroduction())
        .totalOrderCount(totalOrderCount)
        .totalSalePrice(totalSalePrice)
        .build();
  }

  public static AdminConsumerResponseDto toAdminConsumerResponseDto(Order order) {
    return AdminConsumerResponseDto.builder()
        .userId(order.getUser().getId())
        .username(order.getUser().getUsername())
        .productQuantity(order.getQuantity())
        .paymentAmount(order.getQuantity() * order.getProduct().getPrice())
        .orderDate(order.getUpdatedAt())
        .build();
  }

  public static AdminBroadcastListResponseDto toAdminBroadcastListResponseDto(Broadcast broadcast) {
    return AdminBroadcastListResponseDto.builder()
        .id(broadcast.getId())
        .title(broadcast.getTitle())
        .streamer(broadcast.getStreamer().getUsername())
        .date(broadcast.getCreatedAt().toLocalDate())
        .build();
  }
}
