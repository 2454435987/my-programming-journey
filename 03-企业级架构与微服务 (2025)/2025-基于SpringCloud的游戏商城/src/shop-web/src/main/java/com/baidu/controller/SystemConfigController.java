package com.baidu.controller;

import com.baidu.feignservice.SystemConfigFeignService;
import com.baidu.pojo.SystemConfig;
import com.baidu.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sysConfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigFeignService systemConfigFeignService;

    // 跳转到系统设置页面并展示数据
    @RequestMapping("/toSetting")
    public String toSetting(HttpServletRequest request) {
        System.out.println("--------toSetting--------");
        SystemConfig config = systemConfigFeignService.getConfig();
        request.setAttribute("sysConfig", config);
        return "setting/info"; // 假设前端对应的页面在 setting/info.html
    }

    // 更新系统配置
    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(SystemConfig config) {
        System.out.println("--------doEdit config--------");
        System.out.println("config:" + config);
        boolean flag = systemConfigFeignService.updateConfig(config);
        if (flag) {
            return R.right();
        }
        return R.error();
    }
}
