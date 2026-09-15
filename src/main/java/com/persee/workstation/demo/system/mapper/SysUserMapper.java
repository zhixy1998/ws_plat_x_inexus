package com.persee.workstation.demo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.persee.workstation.demo.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper：继承 BaseMapper 后自动获得单表 CRUD 能力，无需手写 SQL。
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
