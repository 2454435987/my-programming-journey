package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.OrdersOperator;
import pojo.Orders;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MyServlet", value = "/MyServlet")
public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String user = request.getParameter("user");
        String orderTitle = request.getParameter("orderTitle");
        OrdersOperator oo = new OrdersOperator();
        List<Orders> list = oo.getOrdersByStatus(user,orderTitle);

        PrintWriter pw = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);
        // 将 JSON 字符串写入响应
        response.setContentType("application/json");
        pw.println(objectMapper.writeValueAsString(json));
    }
}
