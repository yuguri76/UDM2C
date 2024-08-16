package com.example.livealone.user.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserInfoRequestDto {

    @Size(message = "닉네임은 최대 15글자까지 입력할 수 있습니다.")
    private String nickname;

    private LocalDate birthDay;

    @Size(message = "주소는 127글자까지 입력할 수 있습니다.")
    private String address;

}
