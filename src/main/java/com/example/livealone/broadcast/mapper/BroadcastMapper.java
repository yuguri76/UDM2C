package com.example.livealone.broadcast.mapper;

import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.BroadcastTitleResponseDto;
import com.example.livealone.broadcast.dto.CreateBroadcastResponseDto;
import com.example.livealone.broadcast.dto.StreamKeyResponseDto;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.entity.BroadcastStatus;
import com.example.livealone.reservation.entity.Reservations;
import com.example.livealone.product.entity.Product;
import com.example.livealone.user.entity.User;

public class BroadcastMapper {

    public static Broadcast toBroadcast(String title, User streamer, Product product, Reservations reservation) {
        return Broadcast.builder()
                .title(title)
                .broadcastStatus(BroadcastStatus.ONAIR)
                .reservation(reservation)
                .streamer(streamer)
                .product(product)
                .build();
    }

    public static BroadcastResponseDto toBroadcastResponseDto(Broadcast broadcast, Product product) {
        return BroadcastResponseDto.builder()
                .broadcastId(broadcast.getId())
                .broadcastTitle(broadcast.getTitle())
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .productQuantity(product.getQuantity())
                .productIntroduction(product.getIntroduction())
                .build();
    }

    public static StreamKeyResponseDto toStreamKeyResponseDto(Boolean isLive, String streamKey) {
        return StreamKeyResponseDto.builder()
                .is_live(isLive)
                .stream_key(streamKey)
                .build();
    }

    public static CreateBroadcastResponseDto toCreateBroadcastResponseDto(Broadcast broadcast) {
        return CreateBroadcastResponseDto.builder()
                .id(broadcast.getId())
                .build();
    }

    public static BroadcastTitleResponseDto toBroadcastTitleResponseDto(Broadcast broadcast) {
        return BroadcastTitleResponseDto.builder()
            .title(broadcast.getTitle())
            .build();
    }
}
