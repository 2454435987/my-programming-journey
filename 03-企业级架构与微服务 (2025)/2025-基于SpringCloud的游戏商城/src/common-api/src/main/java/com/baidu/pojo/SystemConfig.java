package com.baidu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("system_config")
public class SystemConfig {

    @TableId(value = "sid", type = IdType.AUTO)
    private Integer sid;

    private String mallName;

    private String carousel1;

    private String carousel2;

    private String carousel3;

    private String carousel4;

    private String carousel5;
}
