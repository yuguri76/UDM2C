package com.example.livealone.reservation.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
  private LocalDateTime airtime;
}
