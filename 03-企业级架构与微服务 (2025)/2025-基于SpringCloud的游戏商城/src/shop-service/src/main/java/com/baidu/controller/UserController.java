package com.baidu.controller;

import com.baidu.pojo.Role;
import com.baidu.pojo.User;
import com.baidu.service.impl.RoleServiceImpl;
import com.baidu.service.impl.UserServiceImpl;
import com.baidu.utils.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @PostMapping("/getUserAndRoleByPage/{uid}")
    public  PageResult<User> getUserAndRoleByPage(@PathVariable("uid") Integer uid){
        System.out.println("----------getUserAndRoleByPage-----uid:"+uid);

        //1.查User表
        User user = userService.getById(uid);
        List<User> list = new ArrayList();
        //2.根据user查role id
        Role role = roleService.getById(user.getRoleId());
        //3.把role的内容添加到user中
        user.setRole(role);
        PageResult<User> pageResult = new PageResult<>();
        list.add(user);
        pageResult.setCode(0);
        pageResult.setCount(1);
        pageResult.setMsg("查询成功");
        pageResult.setData(list);
        return pageResult;
    }
    //修改用户信息
    @PostMapping("/editUser")
    public boolean editUser(@RequestBody User user){
        System.out.println("-------editUser--------");
        boolean flag = userService.updateById(user);
        return flag;
    }

    //根据id查询用户
    @GetMapping("/getUser/{uid}")
    public User getUser(@PathVariable("uid")Integer uid){
        System.out.println("---------getUser---------");
        User user = userService.getById(uid);
        return user;
    }

    //删除单个用户
    @GetMapping("/deleteUser/{uid}")
    public boolean deleteUser(@PathVariable("uid") Integer uid){
        System.out.println("----------deleteUser---------");
        boolean flag = userService.removeById(uid);
        return flag;
    }

    //添加用户
    @PostMapping("/addUser")
    public boolean addUser(@RequestBody User user){
        System.out.println("--------addUser-----------");
        user.setCreateTime(new java.util.Date());
        boolean flag = userService.save(user);
        return flag;
    }

    //根据多个uid进行批量删除
    @PostMapping("/batchDelUser")
    public boolean batchDelUser(@RequestBody Integer userIds[]){
        System.out.println("------------batchDelUser-----------");
        List<Integer> list = new ArrayList<>(List.of(userIds));
        boolean flag = userService.removeBatchByIds(list);
        return flag;
    }


    //获取user表的所有内容
    @PostMapping("/getUserByPage")
    public PageResult<User> getUserByPage(@RequestBody Map<String,Object> map){
        System.out.println("--------getUserPageByUidAndName--------");
        String name = (String) map.get("name");
        Integer currentPage = (Integer) map.get("currentPage");
        Integer pageSize = (Integer) map.get("pageSize");
        System.out.println("name:"+name+" currentPage"+currentPage+" pageSize"+pageSize);
        PageResult<User> pageResult = userService.getUserByPage(name,currentPage,pageSize);

        return pageResult;
    }

    @PostMapping("/login")
    public User login(@RequestBody User user){
        System.out.println("----------login---------");
        User user1 = userService.login(user);
        return user1;
    }

    @PostMapping("/updatePasswordByPhone")
    public boolean updatePasswordByPhone(@RequestParam("telephone") String telephone, @RequestParam("password") String password) {
        System.out.println("---------updatePasswordByPhone---------");
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("telephone", telephone);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            user.setPassword(password);
            return userService.updateById(user);
        }
        return false;
    }

    @PostMapping("/getUserByPhone")
    public User getUserByPhone(@RequestParam("telephone") String telephone) {
        System.out.println("---------getUserByPhone---------");
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("telephone", telephone);
        return userService.getOne(queryWrapper);
    }
    @PostMapping("/getUserByName")
    public User getUserByName(@RequestParam("name") String name) {
        System.out.println("---------getUserByName---------");
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("name", name);
        return userService.getOne(queryWrapper);
    }
}
