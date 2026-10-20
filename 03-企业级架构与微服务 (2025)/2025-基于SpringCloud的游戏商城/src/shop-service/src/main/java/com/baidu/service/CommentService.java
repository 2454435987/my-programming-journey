package com.baidu.service;

import com.baidu.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface CommentService extends IService<Comment> {
    List<Comment> getCommentsByPid(Integer pid);
    Integer checkPurchasedOid(Integer uid);
}
