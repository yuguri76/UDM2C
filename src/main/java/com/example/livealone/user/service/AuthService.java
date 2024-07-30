package com.example.livealone.user.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.global.security.JwtService;
import com.example.livealone.user.dto.ReissueRequestDto;
import com.example.livealone.user.entity.RefreshToken;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.RefreshTokenRepository;
import com.example.livealone.user.repository.UserRepository;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  public final MessageSource messageSource;

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  public String reissueAccessToken(ReissueRequestDto requestDto) {

    RefreshToken refreshToken = refreshTokenRepository.findById(requestDto.getRefresh())
        .orElseThrow(
            () -> new CustomException(messageSource.getMessage(
            "user.not.found",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.NOT_FOUND));

    User user = userRepository.findById(refreshToken.getUserId())
        .orElseThrow(
            () -> new CustomException(messageSource.getMessage(
            "user.not.found",
            null,
            CustomException.DEFAULT_ERROR_MESSAGE,
            Locale.getDefault()
        ), HttpStatus.NOT_FOUND));

    refreshTokenRepository.delete(refreshToken);

    return UriComponentsBuilder.fromHttpUrl("http://localhost:3000/oauth2/redirect")
        .queryParam("access", jwtService.generateToken(user))
        .queryParam("refresh", reissueRefreshToken(user))
        .build()
        .toUriString();

  }

  public String reissueRefreshToken(User user) {
    return refreshTokenRepository.save(RefreshToken.builder()
        .token(UUID.randomUUID().toString())
        .userId(user.getId())
        .build()
    ).getToken();
  }

}
