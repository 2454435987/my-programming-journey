package com.baidu.service.impl;

import com.baidu.dao.CommentMapper;
import com.baidu.pojo.Comment;
import com.baidu.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByPid(Integer pid) {
        return commentMapper.selectCommentsWithUserName(pid);
    }

    @Override
    public Integer checkPurchasedOid(Integer uid) {
        return commentMapper.checkUserPurchased(uid);
    }
}
