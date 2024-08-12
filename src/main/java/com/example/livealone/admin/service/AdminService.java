package com.example.livealone.admin.service;

import com.example.livealone.admin.dto.AdminBroadcastDetailResponseDto;
import com.example.livealone.admin.dto.AdminBroadcastListResponseDto;
import com.example.livealone.admin.dto.AdminConsumerResponseDto;
import com.example.livealone.admin.dto.AdminRequestDto;
import com.example.livealone.admin.dto.AdminRoleResponseDto;
import com.example.livealone.admin.dto.AdminUserListResponseDto;
import com.example.livealone.admin.mapper.AdminMapper;
import com.example.livealone.broadcast.entity.Broadcast;
import com.example.livealone.broadcast.service.BroadcastService;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.order.service.OrderService;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.service.UserService;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private static final int PAGEABLE_SIZE = 10;

  @Value("${admin.token}")
  private String adminToken;

  private final UserService userService;
  private final BroadcastService broadcastService;
  private final OrderService orderService;

  private final MessageSource messageSource;

  @Transactional
  public void registerAdmin(User user, AdminRequestDto requestDto) {
    if (!Objects.equals(adminToken, requestDto.getPassword())) {
      throw new CustomException(messageSource.getMessage(
          "wrong.token",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.BAD_REQUEST);
    }

    user.registerAdmin();

    userService.saveUser(user);
  }

  public AdminRoleResponseDto getUserRole(User user) {
    return AdminMapper.toAdminRoleResponseDto(user.getRole());
  }

  public Page<AdminBroadcastListResponseDto> getBroadcasts(int page) {
    return broadcastService.getAllBroadcastListPageable(page - 1, PAGEABLE_SIZE);
  }

  public Page<AdminUserListResponseDto> getUsers(int page) {
    return userService.getAllUserListPageable(page - 1, PAGEABLE_SIZE);
  }

  public AdminBroadcastDetailResponseDto getBroadcastDetails(Long broadcastId) {
    Broadcast broadcast = broadcastService.findByBroadcastId(broadcastId);

    Long totalOrderCount = orderService.sumOrderQuantity(broadcastId);
    Long totalSalePrice = totalOrderCount * broadcast.getProduct().getPrice();

    return AdminMapper.toAdminBroadcastDetailResponseDto(broadcast, broadcast.getProduct(), totalOrderCount, totalSalePrice);
  }

  public Page<AdminConsumerResponseDto> getConsumers(Long broadcastId, int page) {
    return orderService.getAllOrderByBroadcastId(broadcastId, page - 1, PAGEABLE_SIZE);
  }
}
