package com.example.livealone.broadcast.mapper;

import com.example.livealone.broadcast.dto.BroadcastCodeRequestDto;
import com.example.livealone.broadcast.dto.BroadcastCodeResponseDto;
import com.example.livealone.broadcast.entity.BroadcastCode;
import java.util.UUID;

public class BroadcastCodeMapper {

  public static BroadcastCode toBroadcastCode(BroadcastCodeRequestDto requestDto) {
    return BroadcastCode.builder()
        .code(UUID.randomUUID().toString())
        .airTime(requestDto.getAirtime())
        .build();
  }

  public static BroadcastCodeResponseDto toBroadcastResponseCodeDto(BroadcastCode broadcastCode) {
    return BroadcastCodeResponseDto.builder().code(broadcastCode.getCode()).build();
  }

}
