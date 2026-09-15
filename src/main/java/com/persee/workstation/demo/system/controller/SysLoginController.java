package com.persee.workstation.demo.system.controller;

import com.persee.workstation.demo.common.result.Result;
import com.persee.workstation.demo.system.dto.LoginRequest;
import com.persee.workstation.demo.system.service.SysLoginService;
import com.persee.workstation.demo.system.vo.LoginUserInfoVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SysLoginController {

    private final SysLoginService loginService;   // 构造器注入，比 @Autowired 字段注入更推荐

    @PostMapping("/login")
    public Result<LoginUserInfoVO> login(@RequestBody @Valid LoginRequest body) {
        return Result.ok(loginService.login(body));
    }
}