package com.baidu.controller;


import com.baidu.utils.SysMenu;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class MainController {



//    跳转到管理员登录成功的后台首页
    @RequestMapping("/toIndex")
    public String toContent(HttpServletRequest request) {
        //保存左侧功能栏
        request.setAttribute("userMenuList",getSysMenu());
        return "content";
    }

    //跳转到普通用户登录成功的后台首页
    @RequestMapping("/toUserIndex")
    public String toUserContent(HttpServletRequest request) {
        //保存左侧功能栏
        request.setAttribute("userMenuList",getUserMenu());
        return "userMenu/userContent";
    }


    //返回欢迎页面
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    //管理员的菜单
    private List<SysMenu> getSysMenu() {
        List<SysMenu> ml = new ArrayList<SysMenu>();
        SysMenu m1 = new SysMenu("system", "系统管理", "layui-icon layui-icon-util", false, null);
        SysMenu m1_1 = new SysMenu("system", "用户管理", "layui-icon layui-icon-user", true, "/user/toUserList");
        SysMenu m1_3 = new SysMenu("system", "商品管理", "layui-icon layui-icon-component", true, "/prod/toList");
        SysMenu m1_5 = new SysMenu("system", "订单管理", "layui-icon layui-icon-list", true, "/order/toOrderList");
        SysMenu m1_6 = new SysMenu("system", "客户地址管理", "layui-icon layui-icon-location", true, "/location/toList");

        SysMenu m1_9 = new SysMenu("system", "系统配置", "layui-icon layui-icon-set", true, "/sysConfig/toSetting");
        m1.getChildMenuList().add(m1_1);
        m1.getChildMenuList().add(m1_3);
        m1.getChildMenuList().add(m1_5);
        m1.getChildMenuList().add(m1_6);
        m1.getChildMenuList().add(m1_9);
        ml.add(m1);
        return ml;
    }

    //用户的菜单
    private List<SysMenu> getUserMenu() {
        List<SysMenu> ml = new ArrayList<SysMenu>();
        SysMenu m1 = new SysMenu("system", "个人管理", "layui-icon layui-icon-user", false, null);
        SysMenu m1_3 = new SysMenu("system", "订单管理", "layui-icon layui-icon-list", true, "/order/toOrderList");
        SysMenu m1_4 = new SysMenu("system", "地址管理", "layui-icon layui-icon-location", true, "/location/toList");

        m1.getChildMenuList().add(m1_3);
        m1.getChildMenuList().add(m1_4);
        ml.add(m1);

        return ml;
    }
}
