package com.example.livealone.broadcast.controller;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.BroadcastTitleResponseDto;
import com.example.livealone.broadcast.dto.CreateBroadcastResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import com.example.livealone.broadcast.service.BroadcastService;
import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.dto.SocketMessageDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BroadcastController {

  private final BroadcastService broadcastService;

  @PostMapping("/broadcast")
  public ResponseEntity<CommonResponseDto<CreateBroadcastResponseDto>> createBroadcast(
      @Valid @RequestBody BroadcastRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userPrincipal)
      throws JsonProcessingException {

    CreateBroadcastResponseDto responseDto = broadcastService.createBroadcast(boardRequestDto, userPrincipal.getUser());

    return ResponseEntity.status(HttpStatus.CREATED).body(
        new CommonResponseDto<>(
        HttpStatus.CREATED.value(),
        "방송을 성공적으로 시작하였습니다.",
        responseDto)
    );

  }

  @GetMapping("/user/{userId}/broadcast")
  public ResponseEntity<CommonResponseDto<List<UserBroadcastResponseDto>>> getBroadcast(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "1") int page) {

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "방송 내역이 성공적으로 조회되었습니다.",
            broadcastService.getBroadcast(page - 1, userId)
        )
    );

  }

  @GetMapping("/broadcast")
  public ResponseEntity<CommonResponseDto<BroadcastResponseDto>> getBoard() {

    BroadcastResponseDto responseDto = broadcastService.inquiryCurrentBroadcast();

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "현재 진행중인 방송을 성공적으로 불러왔습니다.",
            responseDto)
    );
  }

  @PatchMapping("/broadcast")
  public ResponseEntity<CommonResponseDto<Void>> closeBroadcast(@AuthenticationPrincipal UserDetailsImpl userPrincipal)
      throws JsonProcessingException {

    broadcastService.closeBroadcast(userPrincipal.getUser());

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "방송을 성공적으로 중단하였습니다.",
            null)
    );

  }

  @GetMapping("/broadcast/{broadcastId}")
  public ResponseEntity<CommonResponseDto<BroadcastTitleResponseDto>> getBroadcastTitle(@PathVariable Long broadcastId) {
    BroadcastTitleResponseDto broadcastTitleResponseDto = broadcastService.getBroadcastTitle(broadcastId);

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "방송 제목을 조회하였습니다.",
            broadcastTitleResponseDto)
    );
  }

  @MessageMapping("/session/broadcast")
  @SendToUser("/queue/broadcast")
  public String getRequestBroadcastMessage(SocketMessageDto socketMessageDto) throws JsonProcessingException {

    return broadcastService.requestStreamKey();
  }
}


