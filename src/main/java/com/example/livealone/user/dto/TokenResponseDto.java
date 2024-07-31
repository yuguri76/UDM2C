package com.example.livealone.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
  private String access;
  private String refresh;
}
