package com.example.livealone.admin.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminBroadcastListResponseDto {
  private Long id;
  private String title;
  private String streamer;
  private LocalDate date;
}
