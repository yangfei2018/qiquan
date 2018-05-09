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

.text-success {
	color: red;
}

.error {
	color: red;
}

.warning {
	color: #f37b1d;
	font-size:14px;
}

div.form-label{
	text-align: left !important;
}

</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>
	<article class="page-container">
		<p class="f-20 text-success">投资有风险，入市须谨慎！</p>
		<p>以下为券商成交标准:</p>
		<form class="form form-horizontal size-L">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">名称：</label>
				<div class="form-label col-xs-8 col-sm-9">${item.productName }</div>
			</div>
			
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">代码：</label>
				<div class="form-label col-xs-8 col-sm-9">
					${item.productCode }</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">权利金：</label>
				<div class="form-label col-xs-8 col-sm-9">
					${item.amountFormat }元</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">手数：</label>
				<div class="form-label col-xs-8 col-sm-9">
					${item.shou }</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">期权期限：</label>
				<div class="form-label col-xs-8 col-sm-9">${item.period }天</div>
			</div>
			
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">方向：</label>
				<div class="form-label col-xs-8 col-sm-9">
				<c:choose>
				<c:when test="${item.buyAndFall==1 }">
					看涨
				</c:when>
				<c:when test="${item.buyAndFall==2 }">
					看跌
				</c:when>
			</c:choose>
			</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">期权类型：</label>
				<div class="formControls col-xs-8 col-sm-9">
					${item.type==1?"美式":"欧式" }</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">名义本金：</label>
				<div class="form-label col-xs-8 col-sm-9">
					${item.notionalPrincipalFormat }元</div>
			</div>
			<div class="row cl warning" >
				点击成交后，系统会直接从您的账户余额中扣除管理费，您可以在交易记录(待成交页)中查询结果！<br />
				如有疑问，您也可以联系我们的客服。
			</div>
			<div class="row cl">
			<c:if test="${item.categoryId!=1}">
				<div class="col-xs-6 col-sm-6">

					<button onClick="jiaoyi(this,'${item.tradeNo}','1');"
						class="btn btn-secondary size-XL radius" type="button">
						市价下成交</button>

				</div>
			</c:if>	
				<div class="col-xs-6 col-sm-6">

					<button onClick="jiaoyi(this,'${item.tradeNo}','2');"
						class="btn btn-secondary size-XL radius" type="button">
						收盘价成交</button>

				</div>
			</div>
		</form>
	</article>


	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>

	<script type="text/javascript">
		function parentRedirect(){
			parent.gohref('customer/jiaoyilist');
		}

		function jiaoyi(obj,tradeNo,buyType) {
			$(obj).attr('disabled', true);
			$.ajax({
				url : 'customer/json/doTrade',
				type : 'post',
				dataType : "json",
				data : {
					tradeNo:tradeNo,
					buyType:buyType
				},
				success : function(res) {
					$(obj).attr('disabled', false);
					if (res.result) {
						layer_success(res.data,function(){
							parentRedirect();
						});
					} else {
						layer_alert(res.data);
					}
				}
			});
		}

		
	</script>

</body>
</html>