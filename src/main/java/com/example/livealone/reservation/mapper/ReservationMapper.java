package com.example.livealone.reservation.mapper;

import com.example.livealone.reservation.dto.ReservationRequestDto;
import com.example.livealone.reservation.dto.ReservationResponseDto;
import com.example.livealone.reservation.entity.Reservations;
import com.example.livealone.user.entity.User;
import java.util.UUID;

public class ReservationMapper {

  public static Reservations toReservation(ReservationRequestDto requestDto, User user) {
    return Reservations.builder()
        .code(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16))
        .streamer(user)
        .airTime(requestDto.getAirtime())
        .build();
  }

  public static ReservationResponseDto toReservationResponseCodeDto(Reservations broadcastCode) {
    return ReservationResponseDto.builder()
        .email(broadcastCode.getStreamer().getEmail())
        .code(broadcastCode.getCode())
        .airTime(broadcastCode.getAirTime())
        .build();
  }

}
