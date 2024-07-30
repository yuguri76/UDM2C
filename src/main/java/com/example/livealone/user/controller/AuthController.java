package com.example.livealone.user.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.user.dto.ReissueRequestDto;
import com.example.livealone.user.dto.UserInfoResponseDto;
import com.example.livealone.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/reissue")
  public ResponseEntity<CommonResponseDto<Void>> reissueAccessToken(
      @RequestBody ReissueRequestDto requestDto, HttpServletResponse response) throws IOException {

    response.sendRedirect(authService.reissueAccessToken(requestDto));

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "Reissue Access Token successfully.",
            null)
    );

  }

}
