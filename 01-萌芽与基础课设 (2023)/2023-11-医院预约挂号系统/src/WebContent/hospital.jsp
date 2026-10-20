<%@ page import="java.util.List" %>
<%@ page import="pojo.Hospital" %>
<%@ page import="dao.HospitalDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>挂号通-优选医院</title>
    <link rel="stylesheet" href="./css/general.css" />
    <link rel="stylesheet" href="./css/recommend.css" />
    <script src="js/jquery-3.6.0.js"></script>

</head>
<%
    String hosName=null;
    HospitalDao hd  = new HospitalDao();
    int pageSum=1;
    System.out.println(request.getParameter("hospitalName")!=null);
    if(request.getParameter("hospitalName")!=null){
        hosName = request.getParameter("hospitalName");
        pageSum = hd.getHospital(hosName).size()%5==0?hd.getHospital(hosName).size()/5:hd.getHospital(hosName).size()/5+1;
        System.out.println("pageSum页数"+pageSum);
        if(pageSum==0){
            out.print("<script>alert(\"没有此医院！\")</script>");
            // 返回上一个页面
            out.print("<script> window.history.back();</script>");
            return;
        }
    }else{
        pageSum =  hd.getHospital().size()%5==0?hd.getHospital().size()/5:hd.getHospital().size()/5+1;
    }
    System.out.println(pageSum);
    //总页数
    //当前页数
//    int nowPage=1;

//    Object obj = request.getSession().getAttribute("sumPage");
//    if(obj!=null){
//    int sum = Integer.parseInt(obj+"");
//    pageSum=sum;
//    }

%>
<body>
    <div class="wrap">
        <!-- 导航栏 -->
        <%@include file="nav.jsp"%>

        <!-- 分类 -->
        <div class="classify">
            <dl>
                <dt>医院类型：</dt>
                <dd>
                    <div>
                        <a>综合医院</a>
                        <a>眼科医院</a>
                        <a>儿童医院</a>
                        <a>妇产医院</a>
                        <a>耳鼻喉医院</a>
                        <a>皮肤科医院</a>
                        <a>肿瘤医院</a>
                        <a>整形医院</a>
                        <a>传染病医院</a>
                    </div>
                </dd>
            </dl>
        </div> 

        <!-- 筛选 -->
        <div class="screen">
            <ul>
                <%
                    if(hosName==null){
                    out.print( hd.ListHospitalFront());

                    }else {
                    out.print( hd.ListHospitalFront(hosName));

                    }
                %>
            </ul>
        </div>

	<%--分页--%>
    <div class="pagination">
    </div>
    
    </div>
    <!-- 底部版权 -->
    <%@include file="copyright.jsp"%>
</body>
<%--分类--%>
<script>
$(function (){
    pageStyle(<%= pageSum %>); //页码
    //给分类的每个标签定义一个点击事件
    $('.classify dd div a').each(function(index, pageElement) {
        $(pageElement).on('click', function() {
            //删除所有的样式
            $(".classify dd div a").removeClass("act");
            //选中对应的页数添加类名样式
            $(this).addClass("act");
            //获取点击的医院类型
            var  hospitalTypeName = $(this).text();
            console.log(hospitalTypeName);
            updateType(hospitalTypeName);
        });
    });
});
//更新分类，同时也要更新页面
function updateType(hospitalTypeName){
    $.ajax({
        url:"HospitalTypeServlet?hospitalTypeName="+hospitalTypeName+"",
        type:"GET",
        dataType:"json",
        success: function (res) {
            // 只有请求成功（状态码为200）才会执行这个函数
            // 请求成功后将字符串数组转换为JavaScript数组
            var jsonArray = $.parseJSON(res);
            console.log("jsonArray"+jsonArray[1]);

            //移除ul的所有子元素
            $(".screen ul").empty();
            $(".screen ul").append(jsonArray[0]);

            pageStyle(jsonArray[1]);
        }
    });
}




<%--分页--%>

    var currentPageNumber=1; // 全局变量
//页码样式以及事件绑定
    function pageStyle(page){
        //先移除子元素再添加
        $('.pagination').empty();
        $('.pagination').append('<span class="backpage" onclick="backPage()"><</span>');
        var pageSum = page;
        for (var i = 0; i < pageSum; i++) {
            var pageNumber = i + 1;
            $('.pagination').append('<span class="page" >' + pageNumber + '</span>');
        }

        $('.pagination').append('<span class="nextpage" onclick="nextPage()">></span>');
        everPage();
        $(".pagination .page").eq(0).addClass("active");
    }

    //更新页面
    function updatePage(pageNumber){
        //删除所有的样式
        $(".pagination .page").removeClass("active");
        //选中对应的页数添加类名样式
        $(".pagination .page").eq(pageNumber-1).addClass("active");
        // 移除鼠标进入事件处理程序

        console.log('Page clicked:', pageNumber);
        $.ajax({
            url:"HospitalServlet?pageNumber="+pageNumber+"",
            type:"GET",
            dataType:"json",
            success: function (res) {
                // 只有请求成功（状态码为200）才会执行这个函数

                console.log(res);
                // var result = JSON.stringify(res);
                // var dataArr=jQuery.parseJSON(result);
                //移除ul的所有子元素
                    $(".screen ul").empty();
                $(".screen ul").append(res);
            },
            // error: function(xhr) {
            //     // 当请求失败时执行的回调函数

            //     console.error(xhr); // 在控制台中打印错误信息
            // },
            // complete: function (xhr) {
            //     // 不管是成功还是失败都是完成，都会执行这个 complete 函数(一般成功与失败都需要执行的操作写在这里)
            //     console.log('complete', xhr.toString());
            // }
        });
    }
    //下一页
    function nextPage(){

        //等于总页数就提示,否则页数加一
        if(currentPageNumber==<%=pageSum%>){
            alert("已经是最后一页了！");
        }else {
            currentPageNumber++;
            updatePage(currentPageNumber);
        }

    }
    //每页
    function everPage(){
        $('.page').each(function(index, pageElement) {
            $(pageElement).on('click', function() {
                currentPageNumber = $(this).text();
                updatePage(currentPageNumber);
            });
        });
    }
    //上一页
    function backPage(){
        //等于第一页就提示,否则页数减一
        if(currentPageNumber==1){
            alert("已经是第一页了！");
        }else {
            currentPageNumber--;
            updatePage(currentPageNumber);
        }

    }
</script>
</html>

