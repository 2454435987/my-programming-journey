<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="pojo.*"%>
<%@page import="dao.*"%>
<%@page import="java.util.*" %>
<html>
<head>
    <title>挂号通-首页</title>
    <link rel="stylesheet" href="./css/general.css" />
    <link rel="stylesheet" href="./css/index.css" />
</head>
<body>
<%
	List<Hospital> hospitalList = new HospitalOperator().allHospital();
	List<Doctor> doctorList = new DoctorOperator().allDoctor();
	
	Random random = new Random();
	Set<Integer> nums = new HashSet<>();
%>
    <div class="wrap">
        <!-- 导航栏 -->
        <%@include file="nav.jsp"%>

        <!-- banner -->
        <a href="https://www.ght120.com/article/">
        	<div class="banner">
            <img id="ban1" src="./img/index/banner.jpg"/>
            <img id="ban2" src="./img/index/ban.png"/>
        </div>
        </a>
        

        <!-- 小标题 -->
        <p class="tit"><span>预约挂号</span>快速预约，便携就医！（费用由医院自行设定，平台不收取任何额外费用）</p>

        <!-- 优选医院 -->
        <div class="recommend">
            <p><span>优选医院</span><a href="./hospital.jsp">更多>>></a></p>
            <div class="hospital">
                <table>
                    <tr>
			             <% 
							while (nums.size() < 4)
								nums.add(random.nextInt(28) + 1);
			             	for (int num : nums) {
		                    	Hospital hos = hospitalList.get(num);
		               	 %>
		                    <td>
			                    <a href="hospitalSynopsis.jsp?hospitalID=<%= hos.getHospitalID() %>">
			                        <img src="<%= hos.getHosSrc() != null ? hos.getHosSrc() : "./img/booking/photo.png" %>"/>
			                        <div>
			                        	<p><%= hos.getHospitalName() %></p>
			                       		<p><span><%=hos.getDoctorCount() %></span>名医生</p>
			                       		<p>简介：<%=hos.getHospitalIntro() %></p>
			                        </div>
		                        </a>
		                    </td>
		                <% } %>
                    </tr>
                </table>
            </div>
        </div>

        <!-- 严选医生 -->
        <div class="recommend">
            <p><span>严选医生</span><!-- <a href="./doctor.jsp">更多>>></a> --></p>
            <div class="doctor">
                <table>
                    <tr>
                    <%
                    	while (nums.size() < 4)
							nums.add(random.nextInt(80) + 1);
                    	for(int num : nums){
                    		Doctor doc = doctorList.get(num);
                    %>
                   		 <td>
                            <img src="./img/booking/photo.png"/>
                            <h4><span><%=doc.getDoctorName() %></span>&nbsp;&nbsp;<%=doc.getTitle() %></h4>
                            <p class="p1"><%=doc.getHospitalName() %></p>
                            <p><%=doc.getGoodAt() %></p>
                            <a href="./booking.jsp?doctorID=<%= doc.getDoctorID() %>">预约挂号</a>
                        </td>
                    <%} %>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <!-- 底部版权 -->
    <%@include file="copyright.jsp"%>
</body>
</html>
