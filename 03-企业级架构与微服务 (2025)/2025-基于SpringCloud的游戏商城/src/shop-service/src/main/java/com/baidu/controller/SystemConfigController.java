package com.baidu.controller;

import com.baidu.pojo.SystemConfig;
import com.baidu.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/systemConfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    // 获取系统配置（默认取第一条，通常系统配置只有一条记录）
    @GetMapping("/get")
    public SystemConfig getConfig() {
        System.out.println("-------getConfig-------");
        // 如果没有记录则新建一条
        if (systemConfigService.count() == 0) {
            SystemConfig config = new SystemConfig();
            config.setMallName("游戏商城");
            systemConfigService.save(config);
            return config;
        }
        return systemConfigService.list().get(0);
    }

    // 更新系统配置
    @PostMapping("/update")
    public boolean updateConfig(@RequestBody SystemConfig config) {
        System.out.println("-------updateConfig-------");
        if (config.getSid() == null) {
            return systemConfigService.save(config);
        } else {
            return systemConfigService.updateById(config);
        }
    }
}
