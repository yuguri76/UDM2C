package com.example.livealone.admin.mapper;

import com.example.livealone.admin.dto.AdminRoleResponseDto;
import com.example.livealone.user.entity.UserRole;

public class AdminMapper {
  public static AdminRoleResponseDto toAdminRoleResponseDto(UserRole role) {
    return AdminRoleResponseDto.builder()
        .role(role)
        .build();
  }
}
