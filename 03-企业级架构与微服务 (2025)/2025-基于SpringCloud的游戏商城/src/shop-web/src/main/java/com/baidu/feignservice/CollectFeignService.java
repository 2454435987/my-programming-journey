package com.baidu.feignservice;

import com.baidu.pojo.Collect;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "shop-service", path = "/collect")
public interface CollectFeignService {

    @PostMapping("/toggle")
    boolean toggleCollect(@RequestBody Collect collect);

    @GetMapping("/count/{pid}")
    long getCollectCount(@PathVariable("pid") Integer pid);
    
    @PostMapping("/isCollected")
    boolean isCollected(@RequestBody Collect collect);

    @PostMapping("/getCollectPageByUid")
    com.baidu.utils.PageResult<Collect> getCollectPageByUid(@RequestBody java.util.Map<String, Object> map);
}
