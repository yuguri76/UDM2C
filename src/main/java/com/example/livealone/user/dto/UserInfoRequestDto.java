package com.example.livealone.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfoRequestDto {

    @NotNull(message = "해당 값은 필수로 입력 되어야합니다.")
    private String nickname;

    @NotNull(message = "해당 값은 필수로 입력 되어야합니다.")
    private LocalDate birthDay;

    @NotNull(message = "해당 값은 필수로 입력 되어야합니다.")
    private String address;

}
