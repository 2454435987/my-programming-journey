package com.baidu.utils;

import com.baidu.pojo.User;
import jakarta.servlet.http.HttpSession;

//用户权限工具类
public class AuthUtils {

    private  AuthUtils(){

    }
    public static boolean isAdmin(HttpSession session){
        if(session==null){
            return false;
        }
        User user1 = (User) session.getAttribute("user1");
        return user1 != null&&user1.getRoleId()==1;//是管理员

    }
}
