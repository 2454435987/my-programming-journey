package com.baidu.feignservice;

import com.baidu.pojo.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "shop-service",path = "/role")
public interface RoleFeignService {

    @RequestMapping("/getAll")
    public List<Role> getAll();
}
