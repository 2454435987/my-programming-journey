package servlet;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.*;
import pojo.*;


@WebServlet("/UserOperationServlet")
public class UserOperationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//用户表后台操作处理(包含修改密码，注销账户)
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String user = request.getSession().getAttribute("user").toString();
        
        UserOperator uo = new UserOperator();
        User us = uo.getUserByName(user);
        
        List<Orders> allOrders = new OrdersOperator().allOrders(user);
        
        
        // 修改信息
        if (request.getParameter("name") != null && !request.getParameter("name").isEmpty()) {
            String name = request.getParameter("name");// 姓名
            String gender = request.getParameter("gender");// 性别
            String identity = request.getParameter("identity");// 身份证号
            String mobile = request.getParameter("mobile");// 手机号
            uo.updateText(us.getUser(), name, gender, identity, mobile);
            out.println("yes");
        }
        
        // 修改密码
        if (request.getParameter("y_pwd") != null && !request.getParameter("y_pwd").isEmpty()) {
            String y_pwd = request.getParameter("y_pwd");// 原密码
            String x_pwd = request.getParameter("x_pwd");// 新密码
            
            if (us.getPwd().equals(y_pwd)) {
                uo.updatePassword(us.getUser(), x_pwd);
                out.println("yes");
            } else {
                out.println("no");
            }
        }
        
        //注销账户
        if(request.getParameter("pwd1") !=null && !request.getParameter("pwd1").isEmpty()) {
        	String pwd1 = request.getParameter("pwd1");//输入密码
        	String pwd2 = request.getParameter("pwd2");//确认密码
        	boolean f = true;
        	
        	// 遍历订单列表，检查状态值
        	for (Orders order : allOrders) {
        	    if (order.getState().equals("正在进行")) {
        	        f = false;
        	        break; // 找到进行中的订单，跳出循环
        	    }
        	}
        	if(f) {
        		if (us.getPwd().equals(pwd1)) {
            		if(pwd1.equals(pwd2)) {
            			uo.cancelAccount(us.getUser());
            			request.getSession().setAttribute("isLogin", false);
            			out.println("yes");
            		}else {
            			out.println("no");
            		}
            	}else {
            		out.println("error");
            	}
        	}else {
        		out.println("state");
        	}
        }
        
	}
}
