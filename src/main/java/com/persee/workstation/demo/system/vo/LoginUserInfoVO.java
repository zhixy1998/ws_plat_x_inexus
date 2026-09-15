package com.persee.workstation.demo.system.vo;

public record LoginUserInfoVO(
        String token,
        String userName,
        String nickName
) {}
