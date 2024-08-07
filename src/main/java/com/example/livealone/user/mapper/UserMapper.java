package com.example.livealone.user.mapper;

import com.example.livealone.user.dto.UserAddressResponseDto;
import com.example.livealone.user.dto.UserInfoResponseDto;
import com.example.livealone.user.entity.User;
import lombok.Getter;

@Getter
public class UserMapper {

    public static UserInfoResponseDto toUserInfoResponseDto(User entity) {
        return UserInfoResponseDto.builder()
                .name(entity.getUsername())
                .nickName(entity.getNickname())
                .birthDay(entity.getBirthDay())
                .address(entity.getAddress())
                .role(entity.getRole())
                .build();
    }

    public static UserAddressResponseDto toUserAddressResponseDto(User entity) {
        return UserAddressResponseDto.builder()
            .address(entity.getAddress())
            .build();
    }
}