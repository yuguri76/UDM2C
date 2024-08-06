package com.example.livealone.admin.controller;

import com.example.livealone.admin.dto.AdminRequestDto;
import com.example.livealone.admin.service.AdminService;
import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @PutMapping("/admin")
  public ResponseEntity<CommonResponseDto<Void>> registerAdmin(@RequestBody AdminRequestDto requestDto, @AuthenticationPrincipal
      UserDetailsImpl userDetails) {

    adminService.registerAdmin(userDetails.getUser(), requestDto);

    return ResponseEntity.status(HttpStatus.OK).body(
        new CommonResponseDto<>(
            HttpStatus.OK.value(),
            "관리자로 등록되었습니다.",
            null)
    );
  }
}
