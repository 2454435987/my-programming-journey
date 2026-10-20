package com.water.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "用户名或工号不能为空")
    private String username;

    @NotBlank(message = "登录密码不能为空")
    private String password;
}
