<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>挂号通-注册</title>
<link rel="stylesheet" href="./css/login.css" />
	<style type="text/css">
		/* 悬浮窗口样式 */
		.popup {
			position: absolute; /* 设置悬浮窗口的定位方式为绝对定位 */
			background-color: #fff; /* 设置悬浮窗口的背景颜色为白色 */
			border: 1px solid #ccc; /* 设置悬浮窗口的边框 */
			padding: 5px 10px; /* 设置悬浮窗口的内边距 */
			border-radius: 5px; /* 设置悬浮窗口的圆角 */
			box-shadow: 0 2px 5px rgba(0,0,0,0.2); /* 设置悬浮窗口的阴影效果 */
			/*display: none; !* 默认隐藏悬浮窗口 *!*/
		}
	</style>
	<script src="js/jquery-3.6.0.js"></script>

</head>
<body>
	<form action="Login2Servlet" name="zhuce">
		<table cellspacing="15px"  >
			<th colspan="2"><a href="./index.jsp"><h1>账号注册</h1></a></th>
			<tr>
				<td class="word">用户名</td>
				<td><input type="text" name="user"></td>
			</tr>
			<tr>
				<td class="word">密码</td>
				<td><input type="password" name="pwd"></td>
			</tr>
			<tr>
				<td class="word">姓名</td>
				<td><input type="text" name="name"></td>
			</tr>
			<tr>
				<td class="word"><input type="radio" name="gender" checked="checked" value="男">&nbsp;男</td>
				<td><input type="radio" name="gender"  value="女">&nbsp;女</td>
			</tr>
			<tr>
				<td class="word">身份证号</td>
				<td><input type="text" name="identity"></td>
			</tr>
			<tr>
				<td class="word">手机号</td>
				<td><input type="text" name="mobile"></td>
			</tr>
			<tr >
				<td colspan="2"><input type="submit" value="注册"  id="submitButton"  style="width: 100%"></td>
			</tr>
			<tr>
				<td colspan="2"><a href="login1.jsp" class="tt"><< 返回登录 </a></td>
			</tr>
		</table>
	</form>
	<!-- 悬浮窗口 -->
	<div id="errorPopup" class="popup"></div> <!-- 错误提示悬浮窗口，默认隐藏 -->
	<script>
		$(document).ready(function() {
			$('#submitButton').click(function(event) { // 当提交按钮被点击时
				console.log(validateForm());
				if (!validateForm()) { // 如果表单验证不通过
					event.preventDefault(); // 阻止默认的表单提交行为
					// alert('提交按钮已禁用！'); // 弹出提示信息，可根据需求修改

				}
			});
		});
		var popup = $("#errorPopup"); // 获取悬浮窗口元素
		// 表单验证函数
		function validateForm() {
			var username = $("input[name='user']").val().trim(); // 获取用户名输入框的值
			var password = $("input[name='pwd']").val().trim(); // 获取密码输入框的值
			var name = $("input[name='name']").val().trim(); // 获取姓名输入框的值
			var identity = $("input[name='identity']").val().trim(); // 获取身份证输入框的值
			var mobile = $("input[name='mobile']").val().trim(); // 获取手机号输入框的值
			if (username === "") { // 如果用户名为空
				popup.text("用户名不能为空"); // 设置悬浮窗口的内容为错误信息
				popup.css({ // 设置悬浮窗口的位置为提交按钮下方
					top: $("input[name='user']").offset().top + $("input[name='user']").outerHeight(),
					left: $("input[name='user']").offset().left
				});
				showErrorPopup(); // 显示错误提示悬浮窗口

				return false; // 阻止表单提交
			}
			if(password === ""){
				popup.text("密码不能为空"); // 设置悬浮窗口的内容为错误信息
				popup.css({ // 设置悬浮窗口的位置为提交按钮下方
					top: $("input[name='pwd']").offset().top + $("input[name='pwd']").outerHeight(),
					left: $("input[name='pwd']").offset().left
				});
				showErrorPopup(); // 显示错误提示悬浮窗口

				return false; // 阻止表单提交
			}
			if(name === ""){
				popup.text("姓名不能为空"); // 设置悬浮窗口的内容为错误信息
				popup.css({ // 设置悬浮窗口的位置为提交按钮下方
					top: $("input[name='name']").offset().top + $("input[name='name']").outerHeight(),
					left: $("input[name='name']").offset().left
				});
				showErrorPopup(); // 显示错误提示悬浮窗口

				return false; // 阻止表单提交
			}
			var res = /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/
			if(!res.test(identity)){
				popup.text("身份证号格式有误"); // 设置悬浮窗口的内容为错误信息
				popup.css({ // 设置悬浮窗口的位置为提交按钮下方
					top: $("input[name='identity']").offset().top + $("input[name='identity']").outerHeight(),
					left: $("input[name='identity']").offset().left
				});
				showErrorPopup(); // 显示错误提示悬浮窗口

				return false; // 阻止表单提交
			}
			res = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/
			// (?:\+|00) 匹配以加号或者双零开头的区号，
			// 86 匹配中国的国家代码。
			// 整个表达式用 ? 标记表示是可选的，即手机号可以有或者没有国际区号。
			if(!res.test(mobile)){
				popup.text("请输入正确的手机号"); // 设置悬浮窗口的内容为错误信息
				popup.css({ // 设置悬浮窗口的位置为提交按钮下方
					top: $("input[name='mobile']").offset().top + $("input[name='mobile']").outerHeight(),
					left: $("input[name='mobile']").offset().left
				});
				showErrorPopup(); // 显示错误提示悬浮窗口

				return false; // 阻止表单提交
			}
			return true; // 表单验证通过，允许提交
		}

		// 显示悬浮窗口并设置内容
		function showErrorPopup() {

			popup.fadeIn(); // 淡入显示悬浮窗口
			// 2秒后自动隐藏
			setTimeout(function() {
				popup.fadeOut(); // 淡出隐藏悬浮窗口
			}, 3000);
		}


	</script>
</body>
</html>