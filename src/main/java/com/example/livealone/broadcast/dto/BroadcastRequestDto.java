package com.example.livealone.broadcast.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BroadcastRequestDto {

  @NotBlank(message = "방송 제목을 입력해주세요.")
  @Size(max = 50, message = "제목은 최대 50글자까지 입력할 수 있습니다.")
  private String title;

  @NotNull
  private Long productId;

}
