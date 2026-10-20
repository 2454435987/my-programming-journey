package com.baidu.controller;

import com.baidu.feignservice.CommentFeignService;
import com.baidu.pojo.Comment;
import com.baidu.pojo.User;
import com.baidu.utils.R;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentFeignService commentFeignService;

    @PostMapping("/doAdd")
    @ResponseBody
    public R addComment(@RequestParam("pid") Integer pid, 
                        @RequestParam("content") String content, 
                        @RequestParam("score") Integer score, 
                        HttpSession session) {
        User user1 = (User) session.getAttribute("user1");
        if (user1 == null) {
            return R.error().put("msg", "请先登录才能评价！");
        }
        
        Comment comment = new Comment();
        comment.setUid(user1.getUid());
        comment.setPid(pid);
        comment.setContent(content);
        comment.setScore(score);
        
        String result = commentFeignService.addComment(comment);
        if ("NOT_PURCHASED".equals(result)) {
            return R.error().put("msg", "您尚未购买过此商品或订单未支付，无法评价！");
        } else if ("SUCCESS".equals(result)) {
            return R.right().put("msg", "评价成功！");
        } else {
            return R.error().put("msg", "评价失败，请稍后再试！");
        }
    }
}
