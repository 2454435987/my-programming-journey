<%@page import="pojo.*"%>
<%@page import="dao.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>挂号通-用户后台</title>
<link rel="stylesheet" href="./css/my.css" />
</head>
<body>
<%
	String user = (String)session.getAttribute("user");
	User us = new UserOperator().getUserByName(user);
	String txSrc = new UserOperator().getTxSrcByName(user);

	OrdersOperator o = new OrdersOperator();
	List<Orders> ordersList = o.allOrders();
%>
<div class="wrap">
	<!-- 头部 -->
	<div class="top">
		<img src="./img/index/logo.png"/>
		<span>挂号通 用户后台管理</span>
		<a href="./index.jsp">返回首页</a>
		<a id="logout">退出登录</a>
	</div>

	<!-- 内容区 -->
	<div class="main">
		<!-- 左侧导航栏 -->
		<div class="left_nav">
			<img id="tx" src="<%= txSrc != null ? txSrc : "./img/booking/photo.png" %>" title="点击更换头像"/>
			<h1><%= us.getName() %></h1>
			<ul class="tab">
				<li class="active"><a href="#">订单中心</a></li>
				<li><a href="#">个人信息</a></li>
				<li><a href="#">修改密码</a></li>
				<li><a href="#">注销账户</a></li>
			</ul>
		</div>

		<!-- 隐藏的头像上传弹窗 -->
		<div class="box">
			<h2>更换头像</h2>
			<span class="xx">&times;</span><!-- 关闭按钮 -->
			<form action="UploadImg" enctype="multipart/form-data" accept-charset="UTF-8" method="post">
				<input type="hidden" name="tx" value="<%= user %>"/>
				<div class="file-upload">
					<input id="upfile" class="file-upload-input" type="file" name="upfile" accept="image/*" />
					<label for="upfile" class="file-upload-label">
						<div class="icon">+</div>
					</label>
					<input type="submit" value="确定">
				</div>
			</form>
		</div>

		<!-- 右侧内容 -->
		<div class="right_main">
			<!-- 订单中心 -->
			<div id="one" class="active">
				<p>订单中心</p>
				<span class="orderTitle">
<%--					<a class="active" href="#">全部</a>--%>
<%--					<a href="#">正在进行</a>--%>
<%--					<a href="#">已完成</a>--%>
<%--					<a href="#">已过期</a>--%>
<%--					<a href="#">已取消</a>--%>
				</span>
				<table class="orderTable">
					<tr class="tit">
						<td>就诊人</td>
						<td>医生</td>
						<td>预约时间</td>
						<td>金额</td>
						<td>订单状态</td>
						<td>操作</td>
					</tr>
					<form class="delete" id="orderForm">
						<%
						for (Orders or : ordersList) {
							String buttonValue = (or.getState().equals("已完成") ||or.getState().equals("已过期")|| or.getState().equals("已取消")) ? "删除" : "完成";
						%>
						<tr class="list">
							<input type="hidden" name="hiddenInputName" class="orderidValue" value="<%=or.getOrdersID()%>" />
							<td><%=or.getPatients() %></td>
							<td><%=or.getDoctorName() %></td>
							<td><%=or.getDate() %></td>
							<td><%=or.getPrice() %>.00元</td>
							<td><%=or.getState() %></td>
							<td>
								<button type="button" class="operation-button" data-ordersid="<%= or.getOrdersID() %>" data-operation="<%= buttonValue %>">
						           <%= buttonValue %>
						       </button>
							</td>
						</tr>
						<%} %>
					</form>
				</table>
			</div>

			<!-- 个人信息 -->
			<div id="two">
				<p>个人信息</p>
				<form>
					<table>
						<tr>
							<td>用户名：</td>
							<td><%= us.getUser() %></td>
						</tr>
						<tr>
							<td>姓名：</td>
							<td><input type="text" name="name" value="<%= us.getName() %>"></td>
						</tr>
						<tr>
							<td>性别：</td>
							<td><input type="text" name="gender" value="<%= us.getGender() %>"></td>
						</tr>
						<tr>
							<td>身份证：</td>
							<td><input type="text" name="identity" value="<%= us.getIdentity() %>"></td>
						</tr>
						<tr>
							<td>手机号：</td>
							<td><input type="text" name="mobile" value="<%= us.getMobile() %>"></td>
						</tr>
					</table>
					<span>
						<input type="submit" value="确认"/>
						<input type="reset" value="取消"/>
					</span>
				</form>
				<span id="bj">编辑</span>
			</div>

			<!-- 修改密码 -->
			<div id="three">
				<p>修改密码</p>
				<form>
					<table>
						<tr>
							<td>原密码：</td>
							<td><input type="text" name="y_pwd" required></td>
						</tr>
						<tr>
							<td>新密码：</td>
							<td><input type="text" name="x_pwd" required></td>
						</tr>
					</table>
					<span>
						<input type="submit" value="确认"/>
						<input type="reset" value="取消"/>
					</span>
				</form>
			</div>

			<!-- 注销账户 -->
			<div id="four">
				<p>注销账户</p>
				<form>
					<table>
						<tr>
							<td>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
							<td><input type="text" name="pwd1" required></td>
						</tr>
						<tr>
							<td>确认密码：</td>
							<td><input type="text" name="pwd2" required></td>
						</tr>
					</table>
					<span>
						<input type="submit" value="确认"/>
						<input type="reset" value="取消"/>
					</span>
				</form>
			</div>
		</div>



		<!-- 订单详情 -->
		<div class="particulars">
			<h2>订单详情</h2>
			<span class="xx">&times;</span><!-- 关闭按钮 -->
			<div>
				<%
//					String id = request.getParameter("hiddenInputName");
					String id = request.getParameter("orderID");
//					out.println("ordersIDParam 值: " + id); // 输出到控制台
					if (id != null) {
					    int ordersID = Integer.parseInt(id);
					    Orders orders = o.getIDByOrders(ordersID);
				%>
				<!-- <input type="hidden" name="hiddenInputName" value=""/> -->
				<p><span>订单编号:</span><%=orders.getOrdersID() %></p>
				<p><span>订单状态:</span><%=orders.getState() %></p>
				<p><span>就诊人:</span><%=orders.getPatients() %></p>
				<p><span>身份证:</span><%=orders.getPatientsCard() %></p>
				<p><span>手机号:</span><%=orders.getPatientsPhone() %></p>
				<p><span>就诊医院:</span><%=orders.getHospitalName() %></p>
				<p><span>所在科室:</span><%=orders.getDepartmentType() %></p>
				<p><span>就诊医生:</span><%=orders.getDoctorName() %></p>
				<p><span>挂号金额:</span><%=orders.getPrice() %></p>
				<p><span>预约日期:</span><%=orders.getDate() %></p>
				<%
					}
				%>
			</div>
		</div>
	</div>
</div>
<script src="js/jquery-3.6.0.js"></script>
<script src="js/adminMy.js"></script>
<script>
	var flag=false;
	$(function (){
		// 解析URL，获取查询参数的值
		var urlParams = new URLSearchParams(window.location.search);
		if(urlParams.get('particulars')!=null){
			flag= urlParams.get('particulars');
		}
		if(flag){
			$('.particulars').addClass('show');
			// 根据获取到的参数值来显示相应内容
			$('.particulars').addClass('show').text("Order ID: " + orderID);
		}

	})
	//获取id
	function addClass(){
		$('#one .list').each(function (){
			$(this).click(function() {
				var orderID = $(this).find('.orderidValue').val();;
				window.location="adminMy.jsp?orderID="+orderID+"&particulars=true";
			});
		})

		$('.xx').click(function() {
			$('.particulars').removeClass('show');
			$('.particulars').empty(); // 清空.particulars的文本内容
			
			// 清空flag的值并从URL中删除查询参数
			  flag = false;
			  var url = window.location.href;
			  var urlParams = new URLSearchParams(window.location.search);
			  urlParams.delete('particulars');
			  var newUrl = url.split('?')[0] + '?' + urlParams.toString();
			  window.history.replaceState({}, '', newUrl);
		});
	}
			$(function (){
						// addClass();
						$('.operation-button').click(function() {
							event.stopPropagation(); // 阻止事件冒泡到父元素
					        var ordersID = $(this).data('ordersid');
					        var operation = $(this).data('operation');
					        $.ajax({
					            url: "OrdersServlet2",
					            type: "POST",
					            data: {
					                ordersID: ordersID,
					                operation: operation
					            },
					            success:function(response){
									console.log(response.trim());
					                if (response.trim() === 'del_yes') {
					                    alert('删除成功！');
					                    location.reload();
					                } else if (response.trim() === 'can_yes') {
					                    alert('取消成功！');
					                    location.reload();
					                } else if(response.trim() === 'can_yes2'){
										alert('更改为已完成！');
										location.reload();
					                }else{
										alert('操作失败！');
										location.reload();
									}
					            },
					            error:function(){
					                alert('请求失败，请重试！');
					            }
					        });
					    });
		// 			}
		// 		})
		// 	})
		// })
	})

</script>
</body>
</html>