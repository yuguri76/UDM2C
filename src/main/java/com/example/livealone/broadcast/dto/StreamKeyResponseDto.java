package com.example.livealone.broadcast.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class StreamKeyResponseDto {
  private String stream_key;
}
