<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=request.getContextPath()%>/" />
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--<link rel="Bookmark" href="favicon.ico" >
 <link rel="Shortcut Icon" href="favicon.ico" /> -->
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<link rel="stylesheet" type="text/css"
	href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css"
	href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="lib/icheck/icheck.css" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui.admin/css/style.css" />
<style>
body, th, td, button, input, select, textarea, .radio-box label {
	font-size: 16px;
}

.pad {
	max-width: 450px;
	width: 80%;
	margin-left: auto;
	margin-right: auto;
	font-size: 16px;
}

.form-label {
	font-size: 16px;
	color: black;
}

.select {
	font-size: 16px;
	height: 25px;
}

.page-container {
	padding-left: 30px;
	padding-right: 30px;
	overflow: hidden;
}

form {
	width: 80%;
	margin-left: auto;
	margin-right: auto;
}

.rqleft {
	width: 49%;
	float: left;
	display: block;
	height: 60px;
	line-height: 60px;
	font-size: 16px;
	text-align: center;
	cursor: pointer;
}

.rqleft.selected {
	color: red;
}

.error {
	color: red;
}


.HuiTab{
	padding:20px;
	

}

.HuiTab{
	padding:20px;
	

}
</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>

<div class="HuiTab">
							<div class="tabBar clearfix">
								<span 
									onclick="gohref('customer/calcu')">看涨</span> <span class="current" >看跌</span>
							</div>
	<article class="page-container">
		<form class="form">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">权利金：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text size-L" value="" id="b1"
						placeholder="请输入权利金" name="amount">

				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">名义本金：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text size-L" value="" id="b2"
						placeholder="请输入名义本金" name="nybj">

				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">下单点位：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text size-L" value="" id="b5"
						placeholder="请输入下单点位" name="newpwd1">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">当前点位：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text size-L" value="" id="b7"
						placeholder="请输入当前点位">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">持平点：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<span id="cpd"></span>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">预计收益：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<span id="sy"></span>
				</div>
			</div>
			<p style="color:red;margin-top:40px">
			预计收益里不包含您购买该产品的权利金（实际收益=预计收益-权利金）		
			</p>
		</form>
	</article>
</div>

	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>

	<script type="text/javascript">
	
	
	var regu = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
	
	function isMoney(s) { 
	    if (regu.test(s)) {   
	        return true;   
	    } else {   
	        return false;   
	    }   
	}  
	
	$(function(){
		$("input").keyup(function(){
				var b1 = $("#b1").val();
				var b2 = $("#b2").val();
				var b5 = $("#b5").val();
				var b7 = $("#b7").val();
				
				
				console.log('b1:'+b1+",isMoney:"+isMoney(b1));
				console.log('b2:'+b2)
				console.log('b5:'+b5)
				console.log('b7:'+b7)
				
				if (isMoney(b1) && isMoney(b2) && isMoney(b5) && isMoney(b7)){
					console.log('是金额');
					var b3 = b2/b1;
					var b4 = 100 / b3 / 100;
					var b5_val = b5-b5*b4;
					var b7_val = (b5-b7)/b5*b2;
					
					console.log('b3:'+b3);
					console.log('b4:'+b4)
					console.log('b5_val:'+b5_val)
					
					$("#cpd").html(b5_val);
					$("#sy").html(b7_val);
				}else{
					$("#cpd").html('N/A');
					$("#sy").html('N/A');
				}
				
		})
		
	})
		

	</script>

</body>
</html>