package com.persee.workstation.demo.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.persee.workstation.demo.common.exception.BizException;
import com.persee.workstation.demo.system.dto.LoginRequest;
import com.persee.workstation.demo.system.entity.SysUser;
import com.persee.workstation.demo.system.mapper.SysUserMapper;
import com.persee.workstation.demo.system.service.SysLoginService;
import com.persee.workstation.demo.system.vo.LoginUserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements SysLoginService {

    private final SysUserMapper sysUserMapper;   // 构造器注入

    @Override
    public LoginUserInfoVO login(LoginRequest req) {
        // 快捷登录：参考公司项目 isFastLogin 的做法
        String userName = req.userName();
        if (req.isFastLogin() != null && req.isFastLogin() == 1) {
            userName = "kh-admin";
        }

        // 查库校验用户是否存在
        SysUser user = sysUserMapper.selectOne(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserName, userName));

        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 校验用户状态（1=正常，其他视为禁用）
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BizException("用户已被禁用");
        }

        // 校验密码（生产环境应使用 PasswordEncoder 加密比对，此处仅演示明文比对）
        if (req.password() != null && !req.password().isEmpty()
                && !req.password().equals(user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }

        // TODO: 生成真正的 token（JWT），当前为演示占位
        String token = "demo-token-" + System.currentTimeMillis();
        return new LoginUserInfoVO(token, user.getUserName(), user.getNickName());
    }
}
