<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
	<base href="<%=request.getContextPath()%>/" />
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>管理后台 | 登录</title>

    <link href="static/inspinia/css/bootstrap.min.css" rel="stylesheet">
    <link href="static/inspinia/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="static/inspinia/css/animate.css" rel="stylesheet">
    <link href="static/inspinia/css/style.css" rel="stylesheet">
    <style>
    	.logo-name{
    		font-size:60px !important;
    		letter-spacing:20px !important;
    		margin-bottom:50px;
    	}
    	
    	.loginscreen.middle-box{
    		width:70%;
    	}
    </style>
	
</head>

<body class="gray-bg">

    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div>
            <div>

                <h1 class="logo-name">代理商管理系统</h1>

            </div>
            <h3>代理商登录</h3>
           
            
            <form class="m-t" role="form" >
                <div class="form-group">
                    <input type="text" name="account" class="form-control" placeholder="请输入登录账号" required="">
                </div>
                <div class="form-group">
                    <input type="password" name="password" class="form-control" placeholder="请输入密码" required="">
                </div>
                <button type="button" class="btn btn-primary block full-width m-b" onclick="login(this)">登录</button>

               
                
            </form>
            <p class="m-t"> <small>管理后台 &copy; 2018</small> </p>
        </div>
    </div>

    <!-- Mainly scripts -->
    <script src="static/inspinia/js/jquery-3.1.1.min.js"></script>
    <script src="static/inspinia/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>
	
	<script>
	
		function login(obj){
			$.ajax({
				url : 'partner/json/doLogin',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					if (res.hasOwnProperty('result') && !res.result ){
						layer_alert(res.data);
					}else{
						gohref('partner/index');
					}
				}
			});
		}
	
	</script>
</body>

</html>