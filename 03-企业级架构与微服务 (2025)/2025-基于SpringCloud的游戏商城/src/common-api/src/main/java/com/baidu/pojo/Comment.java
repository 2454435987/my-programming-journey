package com.baidu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@TableName("t_comment")
public class Comment implements Serializable {
    @TableId(value = "cid", type = IdType.AUTO)
    private Integer cid;
    private Integer uid;
    private Integer pid;
    private Integer oid;
    private String content;
    private Integer score;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @TableField(exist = false)
    private String userName; // 用于前端显示用户名
}
