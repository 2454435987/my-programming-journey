<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>挂号通-登录</title>
<link rel="stylesheet" href="./css/login.css" />
</head>
<body>
	<form action="Login1Servlet" name="denglu">
		<table cellspacing="15px" >
			<th colspan="2"><a href="./index.jsp"><h1>用户登录</h1></a></th>
			<tr>
				<td>用户名</td>
				<td><input type="text" name="user"></td>
			</tr>
			<tr>
				<td>密&nbsp;&nbsp;&nbsp;码</td>
				<td><input type="password" name="pwd"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="登录" style="width: 100%"></td>
			</tr>
			<tr>
				<td colspan="2"><a href="login2.jsp" class="tt">前往注册 >></a></td>
			</tr>
		</table>
	</form>
</body>
</html>