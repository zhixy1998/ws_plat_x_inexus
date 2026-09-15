package com.persee.workstation.demo.system.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "用户名不能为空")
        String userName,
        String password,
        Integer isOnlyData,
        Integer isFastLogin,
        Integer simpleMode,
        String language
) {}
