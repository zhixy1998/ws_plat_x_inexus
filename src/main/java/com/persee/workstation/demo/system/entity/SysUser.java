package com.persee.workstation.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("sys_user")
@Data
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;      // 驼峰自动映射到下划线列 user_name
    private String password;
    private String nickName;
    private Integer status;
    private LocalDateTime createTime;
}
