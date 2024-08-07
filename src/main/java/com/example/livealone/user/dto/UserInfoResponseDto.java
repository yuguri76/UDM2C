package com.example.livealone.user.dto;

import com.example.livealone.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfoResponseDto {

    private final String name;
    private final String nickName;
    private final LocalDate birthDay;
    private final String address;
    private final UserRole role;

    @Builder
    public UserInfoResponseDto(String name, String nickName, LocalDate birthDay, String address, UserRole role) {
        this.name = name;
        this.nickName = nickName;
        this.birthDay = birthDay;
        this.address = address;
        this.role = role;
    }

}
