package com.example.livealone.user.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.user.dto.UserAddressResponseDto;
import com.example.livealone.user.dto.UserInfoRequestDto;
import com.example.livealone.user.dto.UserInfoResponseDto;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.mapper.UserMapper;
import com.example.livealone.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final MessageSource messageSource;

    @Transactional
    public UserInfoResponseDto getUserInfo(User user) {

        User curUser = findUserById(user.getId());

        return UserMapper.toUserInfoResponseDto(curUser);
    }

    @Transactional
    public UserInfoResponseDto updateUserInfo(User user, UserInfoRequestDto userInfoRequestDto) {

        User curUser = findUserById(user.getId());

        curUser.updateUser(userInfoRequestDto.getNickname(), userInfoRequestDto.getBirthDay(), userInfoRequestDto.getAddress());

        return UserMapper.toUserInfoResponseDto(curUser);

    }

    public UserAddressResponseDto getAddress(User user) {
        User curUser = findUserById(user.getId());

        if (curUser.getAddress() == null) {
            throw new CustomException(messageSource.getMessage(
                "not.register.address",
                null,
                CustomException.DEFAULT_ERROR_MESSAGE,
                Locale.getDefault()
            ), HttpStatus.NOT_FOUND);
        }

        return UserMapper.toUserAddressResponseDto(curUser);
    }

    public User findUserById(long userId) {

        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(messageSource.getMessage(
                        "user.not.found",
                        null,
                        CustomException.DEFAULT_ERROR_MESSAGE,
                        Locale.getDefault()
                ), HttpStatus.NOT_FOUND));
    }
}
