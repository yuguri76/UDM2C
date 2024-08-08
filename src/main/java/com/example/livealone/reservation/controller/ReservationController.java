package com.example.livealone.reservation.controller;

import com.example.livealone.reservation.dto.ReservationRequestDto;
import com.example.livealone.reservation.dto.ReservationResponseDto;
import com.example.livealone.broadcast.dto.ReservationStateResponseDto;
import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.mail.MailService;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.reservation.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;
  private final MailService mailService;

  @PostMapping("/broadcast/reservation")
  public ResponseEntity<CommonResponseDto<Void>> createReservation(
      @AuthenticationPrincipal UserDetailsImpl userPrincipal,
      @RequestBody ReservationRequestDto requestDto) {

    ReservationResponseDto responseDto = reservationService.createReservation(requestDto, userPrincipal.getUser());

    mailService.sendEmail(responseDto.getEmail(),
        "[LiveAlone]방송 예약 완료 확인 메일입니다.",
        responseDto.getAirTime() + "타임 방송 예약\n streamKey: " + responseDto.getCode());

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "예약을 성공하였습니다. 이메일은 순차적으로 발송됩니다.",
            null)
    );
  }

  @GetMapping("/broadcast/reservations")
  public ResponseEntity<CommonResponseDto<List<ReservationStateResponseDto>>> getReservations(@RequestParam LocalDate date) {
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "예약 목록 조회를 성공하였습니다.",
            reservationService.getReservations(date))
    );
  }

}
