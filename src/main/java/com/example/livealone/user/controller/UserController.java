package com.example.livealone.user.controller;

import com.example.livealone.global.dto.CommonResponseDto;
import com.example.livealone.global.security.UserDetailsImpl;
import com.example.livealone.user.dto.UserAddressResponseDto;
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

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponseDto<UserInfoResponseDto>> getUserInfo(@PathVariable Long userId) {
        UserInfoResponseDto userInfoDto = userService.getUserInfo(userId);
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

    @GetMapping("/address")
    public ResponseEntity<CommonResponseDto<UserAddressResponseDto>> getAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserAddressResponseDto userAddressResponseDto = userService.getAddress(userDetails.getUser());
        CommonResponseDto<UserAddressResponseDto> commonResponseDto = CommonResponseDto.<UserAddressResponseDto>builder()
            .status(HttpStatus.OK.value())
            .message("주소를 성공적으로 가져왔습니다.")
            .data(userAddressResponseDto)
            .build();


        return ResponseEntity.ok().body(commonResponseDto);
    }
}
