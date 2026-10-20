package com.baidu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_collect")
public class Collect implements Serializable {
    @TableId(value = "cid", type = IdType.AUTO)
    private Integer cid;
    private Integer uid;
    private Integer pid;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Product product;
}
