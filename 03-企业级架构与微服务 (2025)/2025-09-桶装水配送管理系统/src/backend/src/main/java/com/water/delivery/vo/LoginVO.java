package com.water.delivery.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private String tokenPrefix;
    private Long userId;
    private String username;
    private String realName;
    private String roleCode;
    private String roleName;
    private Long courierId;   // 若为送水工则返回其配送员档案ID
    private Long regionId;    // 主责片区ID
    private String regionName; // 主责片区名称
}
