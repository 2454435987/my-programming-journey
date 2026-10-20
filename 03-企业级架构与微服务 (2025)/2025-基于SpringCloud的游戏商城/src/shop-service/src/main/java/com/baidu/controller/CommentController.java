package com.baidu.controller;

import com.baidu.pojo.Comment;
import com.baidu.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 获取某个商品的所有评论
    @GetMapping("/list/{pid}")
    public List<Comment> getComments(@PathVariable("pid") Integer pid) {
        return commentService.getCommentsByPid(pid);
    }

    // 发表评论
    @PostMapping("/add")
    public String addComment(@RequestBody Comment comment) {
        // 不再校验具体商品，只要该用户有支付过的订单即可获得评论权限
        Integer oid = commentService.checkPurchasedOid(comment.getUid());
        if (oid == null) {
            return "NOT_PURCHASED";
        }
        
        comment.setOid(oid); // 绑定购买的那笔订单
        comment.setCreateTime(new Date());
        boolean success = commentService.save(comment);
        return success ? "SUCCESS" : "ERROR";
    }
}
