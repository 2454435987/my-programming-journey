package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrdersOperator;

@WebServlet("/OrdersServlet")
public class OrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //订单删除和取消
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");

        OrdersOperator oo = new OrdersOperator();

        // 获取订单ID和操作信息
        int ordersID = Integer.parseInt(request.getParameter("ordersID"));
        String operation = request.getParameter("operation");

        // 根据操作信息执行相应的逻辑
        if ("删除".equals(operation)) {
            if(oo.deleteOrders(ordersID))
                response.getWriter().println("del_yes");
        } else if ("取消".equals(operation)) {
            if(oo.updateOrders(ordersID))
                response.getWriter().println("can_yes");
        }else if("完成".equals(operation)){
            if(oo.updateOrders2(ordersID))
                response.getWriter().println("can_yes2");
        }
    }

}
