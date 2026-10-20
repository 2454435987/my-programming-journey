<%@ page import="dao.UserOperator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="./css/general.css" />
    <script src="js/jquery-3.6.0.js"></script>

</head>
<body>
<!-- 导航栏 -->
<div class="nav">
  <img src="./img/index/logo.png"/>
  <ul>
    <li><a href="./index.jsp">首页</a></li>
    <li><a href="./hospital.jsp">优选医院</a></li>
	<!-- <li><a href="./doctor.jsp">严选医生</a></li> -->
  </ul>
  	<input type="text" class="sousuo" placeholder="请输入医院名"/>
    <div id="ss" onclick="sousuo()">搜索</div>

    <%  //用户未登录时显示的【登录/注册】按钮
        if (session.getAttribute("isLogin") == null || !(Boolean) session.getAttribute("isLogin")) {
    %>
        <a id="login" href="./login1.jsp">登录/注册</a>
    <%
        }
    	//用户登录成功之后显示的【头像】
        else{
            String user = (String)session.getAttribute("user");
            UserOperator uo = new UserOperator();
        	String txSrc = uo.getTxSrcByName(user);
    %>
         <a id="tx" href="./my.jsp"><img src="<%= txSrc != null ? txSrc : "./img/booking/photo.png" %>"></a>
    <%
        }
    %>
</div>
<script>
    function sousuo(){
        var hospitalName = $('.sousuo').val();
        console.log(hospitalName);
        if(hospitalName!=""){
            // 页面到医院页面
            window.location.href = "hospital.jsp?hospitalName="+hospitalName+"";
        }
    }
</script>
</body>

</html>
