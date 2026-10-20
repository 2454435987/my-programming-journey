package com.water.delivery.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "系统内部异常，请稍后重试"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期，请重新登录"),
    FORBIDDEN(403, "没有该资源的操作权限"),
    NOT_FOUND(404, "请求资源未找到"),

    // 核心业务异常码
    USER_NOT_FOUND(1001, "用户不存在"),
    PASSWORD_ERROR(1002, "账号或密码错误"),
    USER_DISABLED(1003, "账号已被停用，请联系管理员"),
    TICKET_BALANCE_INSUFFICIENT(2001, "客户该规格水票余票不足，请先充值续费"),
    COURIER_DISABLED(3001, "送水工处于停用离职状态，无法执行此操作"),
    ORDER_ALREADY_VERIFIED(4001, "该订单已完成核销，请勿重复操作");

    private final int code;
    private final String message;
}
