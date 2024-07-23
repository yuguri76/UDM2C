package com.example.livealone.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfoDto {

    private final String name;
    private final String nickName;
    private final LocalDate birthDay;
    private final String address;


    @Builder
    public UserInfoDto(String name, String nickName, LocalDate birthDay, String address) {
        this.name = name;
        this.nickName = nickName;
        this.birthDay = birthDay;
        this.address = address;
    }

}
