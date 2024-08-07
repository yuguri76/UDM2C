package com.example.livealone.admin.service;

import com.example.livealone.admin.dto.AdminRequestDto;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.service.UserService;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  @Value("${admin.token}")
  private String adminToken;

  private final UserService userService;
  private final MessageSource messageSource;

  @Transactional
  public void registerAdmin(User user, AdminRequestDto requestDto) {
    if (!Objects.equals(adminToken, requestDto.getPassword())) {
      throw new CustomException(messageSource.getMessage(
          "wrong.token",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.BAD_REQUEST);
    }

    user.registerAdmin();

    userService.saveUser(user);
  }
}
