package com.example.livealone.user.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.dto.ReissueRequestDto;
import com.example.livealone.user.dto.TokenResponseDto;
import com.example.livealone.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PatchMapping("/logout")
  public ResponseEntity<CommonResponseDto<Void>> logout(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    authService.logout(userDetails.getUser());

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "Logout successfully.",
            null)
    );

  }

  @PostMapping("/reissue")
  public ResponseEntity<CommonResponseDto<TokenResponseDto>> reissueAccessToken(
      @RequestBody ReissueRequestDto requestDto) {
    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "Reissue Access Token successfully.",
            authService.reissueAccessToken(requestDto))
    );
  }

}
