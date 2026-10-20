package com.baidu.feignservice;

import com.baidu.pojo.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(value = "shop-service", path = "/comment")
public interface CommentFeignService {

    @GetMapping("/list/{pid}")
    List<Comment> getComments(@PathVariable("pid") Integer pid);

    @PostMapping("/add")
    String addComment(@RequestBody Comment comment);
}
