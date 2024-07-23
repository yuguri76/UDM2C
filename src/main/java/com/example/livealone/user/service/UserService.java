package com.example.livealone.user.service;

import com.example.livealone.global.exception.CustomException;
import com.example.livealone.user.dto.UserInfoDto;
import com.example.livealone.user.entity.Social;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.mapper.UserMapper;
import com.example.livealone.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository userRepository;
    public final MessageSource messageSource;

    @Transactional
    public UserInfoDto getUserInfo(long userId/*, User user*/) {

        //더미 유저
        User user = User.builder()
                .username("꾸미")
                .nickname("ggumi")
                .email("tndus@gmail.com")
                .social(Social.GOOGLE)
                .build();

        User curUser = userRepository.save(user);

        checkUser(userId, curUser);
        return UserMapper.toUserInfoDto(curUser);
    }

    public void checkUser(long userId, User user) {
        if (!(user.getId().equals(userId))) {
            throw new CustomException(messageSource.getMessage(
                    "user.not.match",
                    null,
                    CustomException.DEFAULT_ERROR_MESSAGE,
                    Locale.getDefault()
            ), HttpStatus.NOT_FOUND);
        }
    }

}
