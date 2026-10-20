package servlet;

import dao.InformationDao;
import pojo.Doctor;
import pojo.Information;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "InformationServlet", value = "/InformationServlet")
public class InformationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        Doctor doc = (Doctor)request.getSession().getAttribute("doctor");
        String user = request.getParameter("user");
        int doctorID =  doc.getDoctorID();
        String datetime = request.getParameter("datetime");
        int price =  doc.getOutpatientCharge();
        String state =  "正在进行";
        String patients = request.getParameter("patients");
        String patientsCard = request.getParameter("patientsCard");
        String patientsPhone = request.getParameter("patientsPhone");
        PrintWriter pw = response.getWriter();
//        System.out.println(user);
//        System.out.println(doctorID);
//        System.out.println(datetime);
//        System.out.println(price);
//        System.out.println(state);
//        System.out.println(patients);
//        System.out.println(patientsCard);
//        System.out.println(patientsPhone);

        if(1==InformationDao.insertOrdersInformationDao(user,doctorID,datetime,price,state,patients,patientsCard,patientsPhone)){
            pw.println("<script>alert('提交成功！');"
                    + "location.href='my.jsp';"
                    + "</script>");
        }else{
            pw.println("<script>alert('提交失败！')</script>");

        }



    }
}
