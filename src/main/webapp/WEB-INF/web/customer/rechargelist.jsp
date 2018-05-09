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

thead {
	background: #d9d9d9;
}

.emptyRow{
	text-align: center;
	background: #f0f3f8;
	height:40px;
	line-height: 40px;
}
</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>

	<jsp:include page="../include/nav-top.jsp" />

	<jsp:include page="../include/nav-left.jsp" />

	<section class="Hui-article-box">
		<jsp:include page="../include/nav-breadcrumb.jsp">
			<jsp:param name="curr" value="充值记录" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20">
				<div class="panel panel-default">
					<div class="panel-body">
						<div id="tab_demo" class="HuiTab">
							<div class="tabBar clearfix">
								<span <c:if test="${status==0 }">class="current"</c:if> onclick="gohref('customer/rechargelist?status=0')" >待处理</span> <span <c:if test="${status==1 }">class="current"</c:if> onclick="gohref('customer/rechargelist?status=1')" >已到账</span>
							</div>
							<div style="padding-top: 10px; overflow: hidden;">
								<table class="table table-border table-bordered table-hover">
									<thead>
										<tr class="active">
											<th class="active">充值编号</th>
											<th class="active">金额（元）</th>
											<%--<th class="active">公司名称</th>--%>
											<%--<th class="active">开户银行</th>--%>
											<%--<th class="active">银行账户</th>--%>
											<th class="active">创建时间</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${page.list }" var="item">
											<tr>
												<td>${item.rechargeNo }</td>
												<td>${item.amountFormat }</td>
												<%--<td>${item.companyName }</td>--%>
												<%--<td>${item.bankName }</td>--%>
												<%--<td>${item.bankNo }</td>--%>
												<td>${item.createTimeFormat }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<c:if test="${page.total==0 }">
									<p class="emptyRow">暂无记录</p>
								</c:if>
							</div>

						</div>

					</div>
				</div>


			</article>
			<jsp:include page="../include/footer.jsp" />
		</div>
	</section>


	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>

	<script type="text/javascript">
	
		function goumai(id){
			layer.open({
				type: 2,
				maxmin: false,
				shadeClose: true,
				title: "报价详情",
				area: ['500px', '600px'],
				content: ['customer/trade/'+id, 'no']
			})
		}
	</script>

</body>
</html>