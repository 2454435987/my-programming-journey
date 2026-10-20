package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.HospitalDao;
import net.sf.json.JSONArray;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HospitalServlet", value = "/HospitalServlet")
public class HospitalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置文本格式
        response.setContentType("text/html;charset=utf-8");
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        HospitalDao hd = new HospitalDao();
        PrintWriter pw = response.getWriter();
        // 创建 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();
        String typeSql;
        Object sqlObject = request.getSession().getAttribute("sql");
        if (sqlObject != null) {
            typeSql = sqlObject.toString();
            // 删除会话中的属性 用完后必须删除不然刷新后分页有问题
            request.getSession().removeAttribute("sql");

        } else {
            // 如果 sql 为 null
            typeSql = null;
        }
// 现在可以继续执行下面的逻辑，使用 typeSql 变量

        System.out.println("user:"+ typeSql);
// 将 StringBuffer 对象转换为 JSON 字符串
        String sql="SELECT * from hospital";
        if(typeSql!=null){
          sql=typeSql;
        }

        String jsonString = objectMapper.writeValueAsString(hd.ListHospitalFront(pageNumber,sql));
        pw.println(jsonString);
//        System.out.println(json.toString());
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
