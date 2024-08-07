package com.example.livealone.broadcast.dto;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationStateResponseDto {

  private LocalTime time;
  private boolean isReserved;

}
