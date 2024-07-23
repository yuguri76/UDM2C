package com.example.livealone.user.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.dto.UserInfoDto;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.service.UserService;
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

    @GetMapping("")
    public ResponseEntity<CommonResponseDto<UserInfoDto>> getUserInfo(
                                                                     /* @AuthenticationPrincipal UserDetailsImpl userDetails*/) {
//        User user = userDetails.getUser();
        UserInfoDto userInfoDto = userService.getUserInfo(/*,user*/);
        CommonResponseDto<UserInfoDto> commonResponseDto = CommonResponseDto.<UserInfoDto>builder()
                .status(HttpStatus.OK.value())
                .message("User data inquiry successfully")
                .data(userInfoDto)
                .build();


        return ResponseEntity.ok().body(commonResponseDto);
    }
}
