package com.example.livealone.broadcast.controller;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.service.BroadcastService;
import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BroadcastController {

  private final BroadcastService broadcastService;

  @PostMapping("/broadcast")
  public ResponseEntity<CommonResponseDto<BroadcastRequestDto>> addBoard(
      @Valid @RequestBody BroadcastRequestDto boardRequestDto/*, @AuthenticationPrincipal UserDetailsImpl userPrincipal*/) {

    broadcastService.createBroadcast(boardRequestDto/*, user*/);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        new CommonResponseDto<>(
        HttpStatus.CREATED.value(),
        "방송을 성공적으로 시작하였습니다.",
        null)
    );

  }

}
