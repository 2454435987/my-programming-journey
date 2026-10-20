package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.HospitalDao;
import pojo.Hospital;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HospitalTypeServlet", value = "/HospitalTypeServlet")
public class HospitalTypeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String hospitalTypeName = request.getParameter("hospitalTypeName");
        String sql="SELECT * from hospital where hospitalType='"+hospitalTypeName+"'";
//        session
//        把sql语句存入Session里到
        request.getSession().setAttribute("sql",sql);
        System.out.println(hospitalTypeName);
        HospitalDao hd = new HospitalDao();
        PrintWriter pw = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Hospital> list = hd.pagination(sql);
        //分类后的总页数
        int sumPage= list.size()%5==0?list.size()/5:list.size()/5+1;

        List<String> sblist = new ArrayList<>();
        sblist.add(hd.ListHospitalFront(1,sql)+"");
        sblist.add(sumPage+"");
        System.out.println(sblist.get(1));
        String jsonString = objectMapper.writeValueAsString(sblist);
//        System.out.println(jsonString);

        //必须要加上objectMapper.writeValueAsString才能把集合传过去
        pw.println(objectMapper.writeValueAsString(jsonString));

        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request,response);
    }
}
