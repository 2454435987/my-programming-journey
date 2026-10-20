package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserOperator;
import pojo.User;

@WebServlet("/Login1Servlet")
public class Login1Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //登录页面处理
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 获取用户输入的用户名和密码
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");

        // 在数据库中检查用户名和密码是否匹配
        UserOperator uo = new UserOperator();
        boolean success = uo.authenticateUser(new User(user, pwd));

        if (success) {
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("isLogin", true);
            response.sendRedirect("index.jsp");
        } else {
            // 用户名和密码不匹配，向用户提示登录失败
            out.println("<script>alert('登录失败，账号或密码有误');"
                    + "location.href='login1.jsp';"
                    + "</script>");
        }
    }

}
