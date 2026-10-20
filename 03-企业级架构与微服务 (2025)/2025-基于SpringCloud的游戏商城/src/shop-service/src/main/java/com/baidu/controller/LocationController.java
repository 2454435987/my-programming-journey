package com.baidu.controller;


import com.baidu.pojo.Location;
import com.baidu.service.impl.LocationServiceImpl;
import com.baidu.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-09
 */
@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    LocationServiceImpl locationService;


    //实现地址管理分页查询和模糊分页查询
    @PostMapping("/getPage")
    public PageResult<Location> getPage(@RequestBody Map<String,Object> map){
        System.out.println("-----------getPage-----------map:"+map);

        String phone = (String) map.get("phone");
        Integer page = (Integer)map.get("page");
        Integer limit = (Integer) map.get("limit");
        Integer uid = (Integer) map.get("uid");

        PageResult<Location> locationByPage = locationService.getLocationByPage(phone, page, limit,uid);
        return locationByPage;
    }


    //根据id删除地址
    @RequestMapping("/doDel/{aid}")
    public boolean doDel(@PathVariable("aid") Integer aid){
        System.out.println("--------doDel-------");
        System.out.println("aid:"+aid);
        boolean flag = locationService.removeById(aid);
        return flag;
    }

    //批量删除地址
    @PostMapping("/batchDel")
    public boolean batchDel(@RequestBody Integer aids[]){
        System.out.println("----------batchDel---------");
        System.out.println("aids:"+aids);
        boolean flag = locationService.removeBatchByIds(java.util.List.of(aids));
        return flag;
    }

    //根据id获取地址
    @RequestMapping("/getLocationById/{aid}")
    public Location getLocationById(@PathVariable("aid") Integer aid){
        return locationService.getById(aid);
    }

    //执行修改操作
    @PostMapping("/doEdit")
    public boolean doEdit(@RequestBody Location location){
        System.out.println("------doEdit------location:"+location);
        boolean flag = locationService.updateById(location);
        return flag;
    }

    //执行添加操作
    @PostMapping("/doAdd")
    public boolean doAdd(@RequestBody Map<String,Object> map){
        System.out.println("------doAdd-----map:"+map);
        String realName = (String) map.get("realName");
        String phone = (String) map.get("phone");
        String address = (String) map.get("address");
        String identity = (String) map.get("identity");
        Integer uid = (Integer) map.get("uid");
        Integer oid = (Integer) map.get("oid");
        Location location = new Location();
        location.setRealName(realName);
        location.setPhone(phone);
        location.setIdentity(identity);
        location.setAddress(address);
        location.setUid(uid);
        location.setOid(oid);

        boolean flag = locationService.save(location);
        return flag;
    }

}
