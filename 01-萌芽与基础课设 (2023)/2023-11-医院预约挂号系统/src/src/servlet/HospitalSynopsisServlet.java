package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DoctorDao;
import pojo.Doctor;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "HospitalSynopsisServlet", value = "/HospitalSynopsisServlet")
public class HospitalSynopsisServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String departmentType = request.getParameter("departmentType");
        int hospitalID = Integer.parseInt(request.getParameter("hospitalID"));
        System.out.println(departmentType+" "+hospitalID );
        DoctorDao dd = new DoctorDao();
        List<Doctor> list = dd.getDoctor( departmentType, hospitalID);
        PrintWriter pw = response.getWriter();

         ObjectMapper objectMapper = new ObjectMapper();
         String json = objectMapper.writeValueAsString(list);
        // 将 JSON 字符串写入响应
        response.setContentType("application/json");
        pw.println(objectMapper.writeValueAsString(json));
    }
}
