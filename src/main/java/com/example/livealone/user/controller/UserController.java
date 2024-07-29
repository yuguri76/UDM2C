package com.example.livealone.user.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.dto.UserInfoRequestDto;
import com.example.livealone.user.dto.UserInfoResponseDto;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<CommonResponseDto<UserInfoResponseDto>> getUserInfo(
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        UserInfoResponseDto userInfoDto = userService.getUserInfo(user);
        CommonResponseDto<UserInfoResponseDto> commonResponseDto = CommonResponseDto.<UserInfoResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("User data inquiry successfully")
                .data(userInfoDto)
                .build();

        return ResponseEntity.ok().body(commonResponseDto);
    }

    @PutMapping()
    public ResponseEntity<CommonResponseDto<UserInfoResponseDto>> updateUserInfo(@RequestBody @Valid UserInfoRequestDto userInfoRequestDto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        UserInfoResponseDto userInfoDto = userService.updateUserInfo(user, userInfoRequestDto);
        CommonResponseDto<UserInfoResponseDto> commonResponseDto = CommonResponseDto.<UserInfoResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("User data update successfully")
                .data(userInfoDto)
                .build();


        return ResponseEntity.ok().body(commonResponseDto);
    }
}
