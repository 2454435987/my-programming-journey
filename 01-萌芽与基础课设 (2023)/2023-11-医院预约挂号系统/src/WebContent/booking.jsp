<%@page import="pojo.Doctor"%>
<%@page import="dao.DoctorOperator"%>
<%@page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="./css/general.css" />
    <link rel="stylesheet" href="./css/booking.css">
    <title>预约</title>
</head>
<body>
<%
	Doctor doc = new DoctorOperator().getIdByDoctor(request.getParameter("doctorID"));
    //把医生的信息存起来
    session.setAttribute("doctor",doc);
%>
<%--    导航栏--%>
<div class="wrap">
    <%@include file="nav.jsp" %>
</div>
<%--    页面内容--%>
<div class="maxbox">
    <div class="header">
        <div class="photo">
            <img src="img/booking/photo.png" >
        </div>
        <div class="info">
            <div><h3><%=doc.getDoctorName() %></h3><span><%=doc.getTitle() %></span></div>
            <div><span><%=doc.getHospitalName() %>&nbsp;<%=doc.getDepartmentType() %></span></div>
            <div><span id="sc">擅长：<%=doc.getGoodAt() %></span></div>
        </div>
    </div>
    <div class="content">
        <div class="left">
            <h3>预约挂号</h3>
            <div class="box">
		    <%
		        // 获取当前系统日期
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(new Date());
		        
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");// 获取星期几的格式
		        
		        for (int i = 0; i < 6; i++) {
		            // 获取日期和星期几
		            String date = dateFormat.format(calendar.getTime());
		            String week = dayFormat.format(calendar.getTime());
                    //String day =  i % 2 == 0 ? "上午" : "下午";
                    //String datetime=date+"("+week+")"+day;
            %>
		        <p>
		        	<span class="date"><%= date %></span> 
			        <span class="week"><%= week %></span>
			        <%-- <span class="ap"><%= i % 2 == 0 ? "上午" : "下午" %></span> --%>
			        <span class="price"><%=doc.getOutpatientCharge() %>.00</span>元
                    <a href="information.jsp?datetime=<%=date%>"> <input class="btn" type="submit" value="预约"></a>

		        </p>
		    <%
	       		 //if(i%2==1)
	       			calendar.add(Calendar.DAY_OF_MONTH, 1);// 日期增加一天
		    	}
		    %>
            </div>
        </div>
        <div class="right">
            <div class="box">
                <h4>挂号规则</h4>
                <p>1.每日8：00左右更新后一天号源</p>
                <p>2.可预约当天和本周号源</p>
                <p>3.请参考短信提示，到医院就诊</p>
                <p>4.就诊前一天16：00前，到"个人主页">"我的预约"，可取消预约</p>
                <p>5.如需帮助，可拨打客服4000-xxx-xxx</p>
            </div>
        </div>
    </div>
    <div class="text">
	    <h3>执业经历</h3>
	    <p><%= doc.getProfessional() %></p>
    </div>
</div>

<!-- 底部版权 -->
<%@include file="copyright.jsp"%>
</body>
</html>
