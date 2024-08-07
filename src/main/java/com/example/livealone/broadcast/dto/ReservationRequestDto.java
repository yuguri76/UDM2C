package com.example.livealone.broadcast.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationRequestDto {
  private LocalDateTime airtime;
}
