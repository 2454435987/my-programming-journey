package com.baidu.controller;

import com.baidu.feignservice.CollectFeignService;
import com.baidu.pojo.Collect;
import com.baidu.pojo.User;
import com.baidu.utils.R;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.baidu.utils.PageResult;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectFeignService collectFeignService;

    @RequestMapping("/toList")
    public String toList() {
        return "collect/list";
    }

    @PostMapping("/list")
    @ResponseBody
    public PageResult<Collect> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "5") Integer limit,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user1");
        if (user == null) {
            return new PageResult<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("limit", limit);
        map.put("uid", user.getUid());
        return collectFeignService.getCollectPageByUid(map);
    }

    @PostMapping("/toggle")
    @ResponseBody
    public R toggleCollect(@RequestParam("pid") Integer pid, HttpSession session) {
        User user1 = (User) session.getAttribute("user1");
        if (user1 == null) {
            return R.error().put("msg", "请先登录");
        }
        
        Collect collect = new Collect();
        collect.setUid(user1.getUid());
        collect.setPid(pid);
        
        boolean isCollected = collectFeignService.toggleCollect(collect);
        long count = collectFeignService.getCollectCount(pid);
        
        return R.right().put("isCollected", isCollected).put("count", count);
    }

    @RequestMapping("/toCollectFront")
    public String toCollectFront() {
        return "collect/list_front";
    }
}
