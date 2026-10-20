package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.UserOperator;

@WebServlet("/UploadImg")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
public class UploadImg extends HttpServlet {
    private static final long serialVersionUID = 1L;
    UserOperator uo = new UserOperator();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //用户上传头像
        String user = request.getParameter("tx");//上传头像的用户名

        if(user!=null) {
            Part filePart = request.getPart("upfile");// 获取上传的图片
            InputStream fileContent = filePart.getInputStream();
            out.println(uo.saveImage(fileContent,user) ? "yes" : "no");
        }
    }
}
