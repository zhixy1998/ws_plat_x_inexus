package com.persee.workstation.demo.system.service;

import com.persee.workstation.demo.system.dto.LoginRequest;
import com.persee.workstation.demo.system.vo.LoginUserInfoVO;

public interface SysLoginService {
    LoginUserInfoVO login(LoginRequest request);
}