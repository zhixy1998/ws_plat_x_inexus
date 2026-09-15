package com.persee.workstation.demo.common.result;

import lombok.Data;

/**
 * 统一响应结构。
 * 参考业界通用约定：code 表示业务状态码，success 为兼容旧字段的布尔标记。
 */
@Data
public class Result<T> {
    /** 200 成功；400 业务错误；401 未登录；403 无权限；500 系统错误 */
    private int code;
    private boolean success;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.success = true;
        r.message = "ok";
        r.data = data;
        return r;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.success = false;
        r.message = message;
        return r;
    }

    public static <T> Result<T> fail(String message) {
        return fail(400, message);
    }
}
