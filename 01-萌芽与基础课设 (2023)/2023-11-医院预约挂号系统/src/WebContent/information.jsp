<%@ page import="pojo.Doctor" %>
<%@ page import="pojo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="./css/general.css" />
    <link rel="stylesheet" href="./css/booking.css">
    <link rel="stylesheet" href="./css/information.css">
</head>
<body>
<div class="wrap">
    <%@include file="nav.jsp" %>
</div>
<%
    Doctor doc = (Doctor)session.getAttribute("doctor");
    String datetime = request.getParameter("datetime");
    //获取账号信息
    String user2 = (String)session.getAttribute("user");
    if(session.getAttribute("user")==null){
        response.sendRedirect("login1.jsp");
        return;
    }
    UserOperator uo2 = new UserOperator();
    User u = uo2.getUserByName(user2);
    String name = u.getName();
    String identity = u.getIdentity();
    String mobile = u.getMobile();

%>

<%--    页面内容--%>
<div class="maxbox">
    <div class="header">
        <div class="photo">
            <img src="img/booking/photo.png" >
        </div>
        <div class="info">
            <div><h3><%=doc.getDoctorName() %></h3><span><%=doc.getTitle() %></span></div>
            <div><span><%=doc.getHospitalName() %>&nbsp;<%=doc.getDepartmentType() %></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>就诊日期：<%=datetime%></span></div>
            <div><span id="sc">擅长：<%=doc.getGoodAt() %></span></div>
        </div>
    </div>

    <div class="msg">请添加预约信息（真实有效）（输入的预约信息，我们将为你保密）</div>
    <form action="InformationServlet?datetime=<%=datetime%>&user=<%=user2%>" method="post">
        <table cellspacing="15px" class="information">

            <tr>
                <td>就诊人：&nbsp;&nbsp;</td>
                <td><input type="text" name="patients" value="<%=name%>"></td>
            </tr>
            <tr>
                <td>身份证号：&nbsp;&nbsp;</td>
                <td><input type="text" name="patientsCard" value="<%=identity%>"></td>
            </tr>
            <tr>
                <td>联系手机：&nbsp;&nbsp;</td>
                <td><input type="text" name="patientsPhone" value="<%=mobile%>"></td>
            </tr>
            <tr></tr>
            <tr>
                <td></td>
                <td class="submit"><input type="submit" value="提交预约单"></td>
            </tr>
        </table>
    </form>


<!-- 底部版权 -->
<%--<%@include file="copyright.jsp"%>--%>
</div>
</body>
</html>
