package com.example.livealone.broadcast.controller;

import com.example.livealone.broadcast.dto.BroadcastRequestDto;
import com.example.livealone.broadcast.dto.BroadcastResponseDto;
import com.example.livealone.broadcast.dto.CreateBroadcastResponseDto;
import com.example.livealone.broadcast.dto.ReservationStateResponseDto;
import com.example.livealone.broadcast.dto.ReservationRequestDto;
import com.example.livealone.broadcast.dto.ReservationResponseDto;
import com.example.livealone.broadcast.dto.UserBroadcastResponseDto;
import com.example.livealone.broadcast.service.BroadcastService;
import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.mail.MailService;
import com.example.livealone.global.security.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BroadcastController {

  private final BroadcastService broadcastService;
  private final MailService mailService;

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

  @GetMapping("/user/broadcast")
  public ResponseEntity<CommonResponseDto<List<UserBroadcastResponseDto>>> getBroadcast(
      @RequestParam(defaultValue = "1") int page
      , @AuthenticationPrincipal UserDetailsImpl userPrincipal) {

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "방송 내역이 성공적으로 조회되었습니다.",
            broadcastService.getBroadcast(page - 1, userPrincipal.getUser())
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

  @PostMapping("/broadcast/reservation")
  public ResponseEntity<CommonResponseDto<Void>> createReservation(
      @AuthenticationPrincipal UserDetailsImpl userPrincipal,
      @RequestBody ReservationRequestDto requestDto) {

    ReservationResponseDto responseDto = broadcastService.createReservation(requestDto, userPrincipal.getUser());

    mailService.sendEmail(responseDto.getEmail(),
        "[LiveAlone]방송 예약 완료 확인 메일입니다.",
        responseDto.getAirTime() + "타임 방송 예약\n streamKey: " + responseDto.getCode());

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "예약을 성공하였습니다.",
            null)
    );
  }

  @GetMapping("/broadcast/reservations")
  public ResponseEntity<CommonResponseDto<List<ReservationStateResponseDto>>> getReservations(@RequestParam LocalDate date) {
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "예약 목록 조회를 성공하였습니다.",
            broadcastService.getReservations(date))
    );
  }

}
