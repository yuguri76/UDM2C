package com.example.livealone.broadcast.mapper;

import com.example.livealone.broadcast.dto.ReservationRequestDto;
import com.example.livealone.broadcast.dto.BroadcastCodeResponseDto;
import com.example.livealone.broadcast.entity.Reservations;
import java.util.UUID;

public class BroadcastCodeMapper {

  public static Reservations toBroadcastCode(ReservationRequestDto requestDto) {
    return Reservations.builder()
        .code(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16))
        .airTime(requestDto.getAirtime())
        .build();
  }

  public static BroadcastCodeResponseDto toBroadcastResponseCodeDto(Reservations broadcastCode) {
    return BroadcastCodeResponseDto.builder().code(broadcastCode.getCode()).build();
  }

}
