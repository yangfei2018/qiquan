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

.emptyRow {
	text-align: center;
	background: #f0f3f8;
	height: 40px;
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
			<jsp:param name="curr" value="询价记录" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20">
				<div class="panel panel-default">
					<div class="panel-body">
						<div id="tab_demo" class="HuiTab">
							<div class="tabBar clearfix">
								<span <c:if test="${status==0 }">class="current"</c:if>
									onclick="gohref('customer/xunjialist?status=0')">未报价</span> <span
									<c:if test="${status==1 }">class="current"</c:if>
									onclick="gohref('customer/xunjialist?status=1')">已报价</span>
							</div>
							<div style="padding-top: 10px; overflow: hidden;">
								<table class="table table-border table-bordered table-hover">
									<thead>
										<tr class="active">
											<th class="active">名称</th>
											<th class="active">代码</th>
											<th class="active">权利金（元）</th>
											<th class="active">期限</th>
											<th class="active">名义本金（元）</th>
											<th class="active">询价时间</th>
											<c:if test="${status==1 }">
												<th class="active">报价时间</th>
												<th class="active">操作</th>
											</c:if>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${page.list }" var="item">
											<tr>
												<td>${item.productName }</td>
												<td>${item.productCode }</td>
												<td>${item.amountFormat }</td>
												<td>${item.period }天</td>
												<td>${item.notionalPrincipalFormat }</td>
												<td>${item.createTimeFormat }</td>
												<c:if test="${status==1 }">
													<td>${item.updateTimeFormat }</td>
													<td>
														<button onClick="goumai('${item.tradeNo}');"
															class="btn btn-secondary radius" type="button">
															购买</button>
													</td>
												</c:if>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<c:if test="${page.total==0 }">
									<p class="emptyRow">暂无记录</p>
								</c:if>
							</div>
							<c:if test="${status==1 }">
							<div style="margin-top: 10px; overflow: hidden;">
								<p style="font-weight: 800">交易规则及交易时间</p>
								<p  style="font-weight: 800">商品类：</p>
								<p>开盘前下单：开盘后进场交易，接受市价、收盘价下单方式</p>
								<p>开盘期间下单：实时进场交易，接受市价、收盘价下单方式</p>
								<p>收盘前30分钟下单：默认为下一个交易日开盘前下单</p>
								<p  style="font-weight: 800">指数类：</p>
								<p>仅接受当前收盘价成交</p>
								<p>收盘前30分钟下单，默认为下一收盘价成交</p>

								<p style="font-weight: 800">个股类：</p>
								<p>开盘前下单，开盘后进场交易，接受挂价、市价、30分钟均价、收盘价下单方式</p>
								<p>开盘期间实时进场交易，接受挂价、市价、30分钟均价、收盘价下单方式</p>
								<p>收盘前15分钟下单，默认为下一个交易日开盘前下单</p>
								
								
								<table class="table table-border table-bordered" style="width:60%;text-align:center">
									<thead>
										<tr class="active">
											<th class="active">交易所</th>
											<th class="active">品种</th>
											<th class="active">交易时间</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>上期所</td>
											<td>黄金、白银、螺纹钢、铜、铝等</td>
											<td>09：00-11：30<br/>
											13：30-15：00 <br/>
											21：00-02：30
											</td>
										</tr>
										<tr>
											<td>大商所</td>
											<td>焦炭、豆粕、焦煤、铁矿石等</td>
											<td>09：00-11：30<br/>
											13：30-15：00 <br/>
											21：00-23：30
											</td>
										</tr>
										<tr>
											<td>郑商所</td>
											<td>白糖、棉花、菜粕、甲醇、玻璃等</td>
											<td>09：00-11：30<br/>
											13：30-15：00 <br/>
											21：00-02：30
											</td>
										</tr>
										<tr>
											<td>上证、深证</td>
											<td>个股、沪深300指、中小板指</td>
											<td>09：00-11：30<br/>
											13：00-15：00
											</td>
										</tr>
									</tbody>
								</table>
								<p style="color:red">
								注：市价下单需交易员与券商柜台交易，需要一定时效，一般为在您下单后15-30分钟左右下单，具体以实际成交价格为准。
								</p>
							</div>
						</c:if>	
						
						<c:if test="${status==0 }">
						<div style="margin-top: 10px; overflow: hidden;">
							<p>商品类和指数类期权一般1-5分钟反馈报价，个股类期权预计在下一个开盘区间报价。</p>
						</div>
						</c:if>
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
		function goumai(id) {
			layer.open({
				type : 2,
				maxmin : false,
				shadeClose : true,
				title : "报价详情",
				area : [ '500px', '660px' ],
				content : [ 'customer/trade/' + id, 'no' ]
			})
		}
	</script>

</body>
</html>