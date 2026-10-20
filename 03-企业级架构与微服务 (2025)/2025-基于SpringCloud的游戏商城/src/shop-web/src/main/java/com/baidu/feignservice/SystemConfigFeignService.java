package com.baidu.feignservice;

import com.baidu.pojo.SystemConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "shop-service", path = "/systemConfig")
public interface SystemConfigFeignService {

    @GetMapping("/get")
    public SystemConfig getConfig();

    @PostMapping("/update")
    public boolean updateConfig(@RequestBody SystemConfig config);
}
