package com.baidu.feignservice;

import com.baidu.pojo.User;
import com.baidu.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "shop-service",path = "/user")
public interface UserFeignService {

    @PostMapping("/getUserAndRoleByPage/{uid}")
    public  PageResult<User> getUserAndRoleByPage(@PathVariable("uid") Integer uid);

    @PostMapping("/editUser")
    public boolean editUser(@RequestBody User user);

    @GetMapping("/getUser/{uid}")
    public User getUser(@PathVariable("uid")Integer uid);

    @GetMapping("/deleteUser/{uid}")
    public boolean deleteUser(@PathVariable("uid") Integer uid);

    @PostMapping("/addUser")
    public boolean addUser(@RequestBody User user);

    @PostMapping("/batchDelUser")
    public boolean batchDelUser(@RequestBody Integer userIds[]);

    //获取user表的所有内容
    @PostMapping("/getUserByPage")
    public PageResult<User> getUserByPage(@RequestBody Map<String,Object> map);

    @PostMapping("/login")
    public User login(@RequestBody User user);

    @PostMapping("/updatePasswordByPhone")
    public boolean updatePasswordByPhone(@RequestParam("telephone") String telephone, @RequestParam("password") String password);

    @PostMapping("/getUserByPhone")
    public User getUserByPhone(@RequestParam("telephone") String telephone);

    @PostMapping("/getUserByName")
    public User getUserByName(@RequestParam("name") String name);
}
