package com.baidu.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoye
 * @since 2026-06-03
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    //进出产品管理页面
    @RequestMapping("/toRoleList")
    public String toList(){
        System.out.println("--------toList--------");
        return "role/list";
    }

}
