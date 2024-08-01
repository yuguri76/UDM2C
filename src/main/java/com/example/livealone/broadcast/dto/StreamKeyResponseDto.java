package com.example.livealone.broadcast.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class StreamKeyResponseDto {
  private Boolean is_live;
  private String stream_key;
}
