package com.baidu.controller;


import com.baidu.pojo.Role;
import com.baidu.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleServiceImpl roleService;

    @RequestMapping("/getAll")
    public List<Role> getAll(){
        System.out.println("----------getAll----------");
        List<Role> list = roleService.list();
        return list;
    }
}
