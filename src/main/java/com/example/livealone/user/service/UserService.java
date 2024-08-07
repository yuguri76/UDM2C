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
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final RedissonClient redissonClient;

    @Transactional
    public UserInfoResponseDto getUserInfo(Long userId) {

        User curUser = findUserById(userId);

        return UserMapper.toUserInfoResponseDto(curUser);
    }

    @Transactional
    public UserInfoResponseDto updateUserInfo(User user, UserInfoRequestDto userInfoRequestDto) {

        User curUser = findUserById(user.getId());

        curUser.updateUser(userInfoRequestDto.getNickname(), userInfoRequestDto.getBirthDay(), userInfoRequestDto.getAddress());
        updateCache(curUser);

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

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                "user.not.found",
                null,
                CustomException.DEFAULT_ERROR_MESSAGE,
                Locale.getDefault()
            ), HttpStatus.NOT_FOUND));
    }

    private void updateCache(User user) {
        RBucket<User> bucket = redissonClient.getBucket("User::" + user.getId());

        if (bucket.get() != null) {
            bucket.set(user, 30, TimeUnit.MINUTES);
        }
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
  
    public Page<AdminUserListResponseDto> getAllUserListPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<AdminUserListResponseDto> adminUserListResponseDtoList = userPage.stream()
            .map(user -> UserMapper.toAdminUserListResponseDto(user))
            .collect(Collectors.toList());

        return new PageImpl<>(adminUserListResponseDtoList, pageable, userPage.getTotalElements());
    }
}
