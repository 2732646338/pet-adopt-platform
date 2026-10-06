package com.itheima.common;

import lombok.Getter;

@Getter
public enum ResultCdoeEnum {

    SUCCESS(200, "成功"),
    FAIL(500, "失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数错误");

    private final Integer code;
    private final String msg;

    ResultCdoeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
