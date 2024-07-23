package com.example.livealone.user.mapper;

import com.example.livealone.user.dto.UserInfoResponseDto;
import com.example.livealone.user.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UserMapper {

    // Entity를 DTO로 변환할 때
    public static UserInfoResponseDto toUserInfoResponseDto(User entity) {
        return UserInfoResponseDto.builder()
                .name(entity.getUsername())
                .nickName(entity.getNickname())
                .birthDay(entity.getBirthDay())
                .address(entity.getAddress())
                .build();
    }

}