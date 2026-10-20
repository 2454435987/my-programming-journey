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

@WebServlet("/Login2Servlet")
public class Login2Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //注册页面处理
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        UserOperator uo = new UserOperator();//实例化用户表操作对象

        // 获取用户输入的数据
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");
        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String identity = request.getParameter("identity");
        String mobile = request.getParameter("mobile");

        //判断用户是否存在/（这里没有约束其他数据，只在js保证其他input不为空）
        if(uo.checkExistence(user)){
            out.println("<script>");
            out.println("alert('账号已存在，请重新注册！');");
            out.println("location.href='login2.jsp';");
            out.println("</script>");
        }else{
            if (uo.saveUser(new User(user, pwd, name, gender, identity, mobile))) {
                // 数据库保存成功，先清空之前输出的响应
                response.reset();
                //向用户提示注册成功
                request.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=UTF-8");
                out.println("<script >");
                out.println("alert('注册成功！');");
                out.println("location.href='login1.jsp';");
                out.println("</script>");
            } else {
                // 数据库保存失败
                request.setAttribute("error", "数据保存失败！");
                request.getRequestDispatcher("login2.jsp").forward(request, response);
            }
        }
    }

}
