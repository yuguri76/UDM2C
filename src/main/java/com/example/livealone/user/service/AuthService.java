package com.example.livealone.user.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.global.security.JwtService;
import com.example.livealone.user.dto.ReissueRequestDto;
import com.example.livealone.user.dto.TokenResponseDto;
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

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  public final MessageSource messageSource;

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  public TokenResponseDto reissueAccessToken(ReissueRequestDto requestDto) {
    RefreshToken refreshToken = refreshTokenRepository.findByToken(requestDto.getRefresh())
        .orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                "refresh.not.found",
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

    return TokenResponseDto.builder()
        .access(jwtService.generateToken(user))
        .refresh(reissueRefreshToken(user))
        .build();
  }

  public String reissueRefreshToken(User user) {
    return refreshTokenRepository.save(RefreshToken.builder()
        .token(UUID.randomUUID().toString())
        .userId(user.getId())
        .build()
    ).getToken();
  }

  public void logout(User user) {

    RefreshToken refreshToken = refreshTokenRepository.findById(user.getId())
        .orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                "user.not.found",
                null,
                CustomException.DEFAULT_ERROR_MESSAGE,
                Locale.getDefault()
            ), HttpStatus.NOT_FOUND));

    refreshTokenRepository.delete(refreshToken);

  }

}
