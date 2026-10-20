<%@ page import="dao.*" %>
<%@ page import="pojo.*" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>医院简介</title>
    <link rel="stylesheet" href="./css/general.css" />
    <link rel="stylesheet" href="./css/hospitalSynopsis.css" />
    <script src="js/jquery-3.6.0.js"></script>
</head>

<%
    //简介部分
    int hospitalID = Integer.parseInt(request.getParameter("hospitalID"));
    HospitalSynopsisDao hsd = new HospitalSynopsisDao();
    List<Hospital> list = hsd.getHospital(hospitalID);
//    将字节数组的图像数据转换为Base64编码的字符串
    String base64Image =  Base64.getEncoder().encodeToString(list.get(0).getHospitalImg());
    String hospitalName =  list.get(0).getHospitalName();
    String hospitalType =  list.get(0).getHospitalType();
    String hospitalGrade =  list.get(0).getHospitalGrade();
    String hospitalAddress =  list.get(0).getHospitalAddress();
    String hospitalIntro=list.get(0).getHospitalIntro();
    hospitalIntro=hospitalIntro.trim();//去除空格
    hospitalIntro=hospitalIntro.replaceAll(" ","");
//    System.out.println(hospitalIntro);
    //根据前面空的两个空格来把字符串进行分割
    int startIndex=0;
    List<String> ls = new ArrayList<>();
    int j=0;
    while (true){
    int index = hospitalIntro.indexOf("　　",startIndex);
        if(index==-1||j==4){
            break;
        }
        String s="";
    try {

         s = hospitalIntro.substring(startIndex-1,++index);
         ls.add(s);
    }catch (Exception e){
        System.out.println("有问题");
    }
        startIndex=index;
        j++;

    }
%>

<body>
<div class="wrap">
    <!-- 导航栏 -->
    <%@include file="nav.jsp"%>

<%--   医院信息展示部分 --%>
    <div class="banner">
        <div class="img_box">
            <img src='data:image/jpeg;base64,<%=base64Image%>'/>
        </div>
        <div class="information">
            <h2><%=hospitalName%></h2>
            <p><span><%=hospitalType%></span><span><%=hospitalGrade%></span></p>
            <p class="address">地址：<%=hospitalAddress%></p>
        </div>


    </div>

    <%--简介和门诊科室--%>
    <div class="title">

        <div class="synopsis act">简介</div>
        <div class="department">门诊科室</div>
    </div>
<%--    分割线--%>
    <div class="hr"></div>
    <%-- 简介部分--%>
<div class="contentBox synopsisBox">
    <h3>医院简介</h3>
    <%
        for (int i = 0; i < ls.size(); i++) {
    %>
    <p><%=ls.get(i)%></p>
    <%
        }%>

</div>
<%--    门诊科室--%>
<div class="departmentBox">
    <div class="departmentType">
        科室:
        <span class="act">全部</span>
        <span>内科</span>
        <span>外科</span>
        <span>妇产科</span>
        <span>其他</span>
    </div>
    <div class="left_right">

<%--    左边医生板块--%>
    <div class="left">
        <%
            //医生部分
            DoctorDao dd = new DoctorDao();
            List<Doctor> doctorList= dd.getDoctor("全部",hospitalID);
            for (int i = 0; i < doctorList.size(); i++) {
                if(i==4){
                    break;
                }
              int doctorID=  doctorList.get(i).getDoctorID();
              int hospitalID2=  doctorList.get(i).getHospitalID();
              String departmentType=  doctorList.get(i).getDepartmentType();
              String doctorName=  doctorList.get(i).getDoctorName();
              String title=  doctorList.get(i).getTitle();
              String goodAt=  doctorList.get(i).getGoodAt();
              String professional=  doctorList.get(i).getProfessional();
              int outpatientCharge=  doctorList.get(i).getOutpatientCharge();
                String doctorImg=null;
              if(doctorList.get(i).getDoctorImg()!=null){
                  doctorImg = Base64.getEncoder().encodeToString(doctorList.get(i).getDoctorImg());
              }

        %>
        <div class="leftBox">
            <div class="img_box">
                <% if(doctorImg!=null){
                %>
                <img src='data:image/jpeg;base64,<%=doctorImg%>'/>
                <%
                }else {

                %>
                <img src="img/booking/photo.png">
                <%
                    }
                %>
            </div>
<%--            医生信息--%>
            <div class="information">
                <p class="doctorName"><%=doctorName%> <span><%=title%></span></p>
                <p><%=hospitalName%> <%=departmentType%></p>
                <p class="goodAt">擅长:<%=goodAt%></p>
                <p class="professional">简介:<%=professional%></p>
            </div>
<%--                预约--%>
            <div class="book">
                <a href="booking.jsp?doctorID=<%=doctorID%>">点击预约</a>
            </div>
        </div>


        <%
            }
        %>
    </div>
<%--    右边名医推荐板块--%>
    <div class="right">
        <h3>名医推荐</h3>
        <%
            int k=0;
        for (int i = doctorList.size()-1; i >=0 ; i--) {
//        if(i==5){
//        break;
//        }
            k++;
            if(k>6){
                break;
            }
        int doctorID=  doctorList.get(i).getDoctorID();
        int hospitalID2=  doctorList.get(i).getHospitalID();
        String departmentType=  doctorList.get(i).getDepartmentType();
        String doctorName=  doctorList.get(i).getDoctorName();
        String title=  doctorList.get(i).getTitle();
        String goodAt=  doctorList.get(i).getGoodAt();
        String professional=  doctorList.get(i).getProfessional();
        int outpatientCharge=  doctorList.get(i).getOutpatientCharge();
        String doctorImg=null;
        if(doctorList.get(i).getDoctorImg()!=null){
        doctorImg = Base64.getEncoder().encodeToString(doctorList.get(i).getDoctorImg());
        }

        %>
        <div class="rightBox">
            <div class="img_box">
                <% if(doctorImg!=null){
                %>
                <img src='data:image/jpeg;base64,<%=doctorImg%>'/>
                <%
                }else {

                %>
                <img src="img/booking/photo.png">
                <%
                    }
                %>
            </div>
            <div class="information">
                <p class="doctorName"><%=doctorName%> <span><%=title%></span></p>
                <p><%=hospitalName%></p>
                <p><%=departmentType%></p>
                <a href="booking.jsp?doctorID=<%=doctorID%>" class="book2">预约挂号</a>
            </div>
      </div>
        <%
            }
        %>
    </div>
</div>
</div>
<!-- 底部版权 -->
<%@include file="copyright.jsp"%>
</body>
</html>
<script>
    $(function (){
        titleChange();
        departmentType();

    });
//     标签样式切换
   function titleChange(){
           $(".synopsis").click(function (){
               //先删除所有的样式
               $(".title div").removeClass("act");
               //给简介添加样式
               $(this).addClass("act");
               //门诊内容隐藏
               $(".departmentBox").hide();
                //简介显示
               $(".synopsisBox").show();
           });
       $(".department").click(function (){
           //先删除所有的样式
           $(".title div").removeClass("act");
           //给门诊添加样式
           $(this).addClass("act");
           //简介隐藏
           $(".synopsisBox").hide();
            //门诊内容显示
           $(".departmentBox").show();
       })
   }
 //科室分类

function departmentType(){
    $(".departmentType span").each(function (elem,index){
       $(this).click(function (elem2,index2){

           //先删除所有的样式
           $(".departmentType span").removeClass("act");
           //给科室添加样式
           $(this).addClass("act");
           //获取科室类别的名称
            var departmentTypeName = $(this).text();
           updateType(departmentTypeName);
       });
    });
}

    //更新分类，同时也要更新页面
    function updateType(departmentTypeName) {
        $.ajax({
            url: "HospitalSynopsisServlet?hospitalID=<%=hospitalID%>&departmentType=" + departmentTypeName + "",
            type: "POST",
            dataType: "json",
            success: function (res) {
                // 只有请求成功（状态码为200）才会执行这个函数
                // 请求成功后将字符串数组转换为JavaScript数组
                var jsonData = JSON.parse(res); // 解析从后端接收到的 JSON 字符串
                // 现在你可以使用 jsonData 对象来访问后端发送的数据
                // console.log(jsonData.toString());
                // 遍历 Doctor 对象数组
                $(".left").empty();


                for (var i = 0; i < jsonData.length; i++) {
                    if (i > 3) {
                        break;
                    }
                    var doctor = jsonData[i];
                    // 访问每个 Doctor 对象的属性
                    var doctorID = doctor.doctorID;
                    var departmentType = doctor.departmentType;
                    var doctorName = doctor.doctorName;
                    var title = doctor.title;
                    var goodAt = doctor.goodAt;
                    var professional = doctor.professional;
                    var outpatientCharge = doctor.outpatientCharge;

                    //移除所有子元素

                    $(".left").append(
                        "<div class='leftBox'>"+
                        "<div class='img_box'>" +
                        (doctor.doctorImg != null ? "<img src='data:image/jpeg;base64," + doctor.doctorImg + "'/>" : "<img src='img/booking/photo.png'>") +
                        "</div>" +
                        "<div class='information'>" +
                        "<p class='doctorName'>" + doctorName + " <span>" + title + "</span></p>" +
                        "<p><%=hospitalName%>" + departmentType + "</p>" +
                        "<p class='goodAt'>擅长:" + goodAt + "</p>" +
                        "<p class='professional'>简介:" + professional + "</p>" +
                        "</div>" +
                        "<div class='book'>" +
                        "<a href='booking.jsp?doctorID=" + doctorID + "'>点击预约</a>" +
                        "</div>"+
                        "</div>"
                    );
                }
                // error: functions(xhr) {
                //     // 当请求失败时执行的回调函数

                //     console.error(xhr); // 在控制台中打印错误信息
                // },
                // complete: function (xhr) {
                //     // 不管是成功还是失败都是完成，都会执行这个 complete 函数(一般成功与失败都需要执行的操作写在这里)
                //     console.log('complete', xhr.toString());
                // }
            }
        });
    }

</script>