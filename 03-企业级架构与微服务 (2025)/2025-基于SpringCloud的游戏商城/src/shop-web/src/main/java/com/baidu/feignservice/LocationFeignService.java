package com.baidu.feignservice;

import com.baidu.pojo.Location;
import com.baidu.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "shop-service",path = "/location")
public interface LocationFeignService {

    //执行添加操作
    @PostMapping("/doAdd")
    public boolean doAdd(@RequestBody Map<String,Object> map);

    //根据id删除地址
    @RequestMapping("/doDel/{aid}")
    public boolean doDel(@PathVariable("aid") Integer aid);

    //批量删除地址
    @PostMapping("/batchDel")
    public boolean batchDel(@RequestBody Integer aids[]);

    //根据id获取地址
    @RequestMapping("/getLocationById/{aid}")
    public Location getLocationById(@PathVariable("aid") Integer aid);

    //执行修改操作
    @PostMapping("/doEdit")
    public boolean doEdit(@RequestBody Location location);

    //实现地址管理分页查询和模糊分页查询
    @PostMapping("/getPage")
    public PageResult<Location> getPage(@RequestBody Map<String,Object> map);
}
