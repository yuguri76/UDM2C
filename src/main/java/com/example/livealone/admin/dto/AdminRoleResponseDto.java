package com.example.livealone.admin.dto;

import com.example.livealone.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminRoleResponseDto {
  private UserRole role;
}
