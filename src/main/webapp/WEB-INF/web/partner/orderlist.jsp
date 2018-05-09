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
<link href="static/inspinia/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="static/inspinia/css/plugins/toastr/toastr.min.css"
	rel="stylesheet">
<link href="static/inspinia/js/plugins/gritter/jquery.gritter.css"
	rel="stylesheet">
<link href="static/inspinia/css/animate.css" rel="stylesheet">
<link href="static/inspinia/css/style.css" rel="stylesheet">

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../include/partner/nav-left.jsp" />
		<div id="page-wrapper" class="gray-bg">
			<jsp:include page="../include/partner/navbar-top.jsp" />
			<jsp:include page="../include/partner/nav-breadcrumb.jsp">
				<jsp:param name="curr" value="交易管理" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="row">
								<div class="tabs-container">
									<ul class="nav nav-tabs">
										
										<li <c:if test="${status==0 }">class="active"</c:if>><a
											href="partner/orderlist?status=0"> 待成交</a></li>
										<li <c:if test="${status==1 }">class="active"</c:if>><a
											href="partner/orderlist?status=1"> 已成交</a></li>
										<li <c:if test="${status==2 }">class="active"</c:if>><a
											href="partner/orderlist?status=2"> 平仓记录</a></li>
										<li <c:if test="${status==3 }">class="active"</c:if>><a
											href="partner/orderlist?status=3"> 已结算</a></li>
									</ul>
									<div class="tab-content">
										<div id="tab-1" class="tab-pane active">
											<div class="panel-body">
												<table
													class="table table-striped table-bordered table-hover">
													<thead>
														<tr>
															<c:if test="${status==0||status==2 }">
															<th>期权买卖方向</th>
														</c:if>	
															<th>购买者</th>
															<th>名称</th>
															<th>代码</th>
															<th>涨跌</th>
															<th>权利金（元）</th>
															<th>期限</th>
															<th>名义本金/手数</th>
															<th>期权类型</th>
															<th>
															<c:choose>
															<c:when test="${status==0 }">
																购买时间
															</c:when>
															<c:otherwise>
																合同时间
															</c:otherwise>
															</c:choose>
															</th>
															<c:if test="${status==3 }">
																<th>行权价格（元）</th>
																<th>结算金额（元）</th>
															</c:if>
															<th>状态</th>

															<th>购买人</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${page.list }" var="item">
															<tr>
																<c:choose>
																	<c:when test="${status==0 }">
																	<td>买入</td>
																	</c:when>
																	<c:when test="${status==2 }">
																	<td>卖出</td>
																	</c:when>
																</c:choose>
																<td>${item.buyerName }<br />(${item.buyerPartnerCompanyName })
																</td>
																<td>${item.productName }</td>
																<td>${item.productCode }</td>
																<td><c:choose>
																		<c:when test="${item.buyAndFall==1 }">
																			<span class="label label-success"> 看涨 </span>
																		</c:when>
																		<c:when test="${item.buyAndFall==2 }">
																			<span class="label label-danger"> 看跌 </span>
																		</c:when>
																	</c:choose></td>
																<td>${item.amountFormat }</td>
																<td>${item.period }天</td>
																<td>${item.notionalPrincipalFormat }</td>
																<td><c:choose>
																		<c:when test="${item.type==1 }">
																	美式
																	</c:when>
																		<c:when test="${item.type==2 }">
																	欧式
																	</c:when>
																	</c:choose></td>
																<td>
																	<c:choose>
															<c:when test="${status==0 }">
																${item.createTimeFormat }
															</c:when>
															<c:otherwise>
																${item.orderStartTime } - ${item.orderEndTime }
															</c:otherwise>
															</c:choose>
																</td>
																<c:if test="${status==3 }">
																	<td>${item.executivePrice }</td>
																	<td>${item.balanceAmountFormat }</td>
																</c:if>
																<td><c:choose>
																		<c:when test="${item.status==0 }">
																	待成交
																</c:when>
																		<c:when test="${item.status==1 }">
																	已成交
																</c:when>
																		<c:when test="${item.status==2 }">
																	平仓记录
																</c:when>
																		<c:when test="${item.status==3 }">
																	已结算
																</c:when>
																	</c:choose></td>
																<td>
																	${item.buyerName }
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
										<div id="tab-2" class="tab-pane">
											<div class="panel-body"></div>
										</div>
									</div>


								</div>


							</div>
						</div>
					</div>

				</div>
			</div>

			<jsp:include page="../include/partner/footer.jsp" />
		</div>
	</div>
	<!-- Mainly scripts -->
	<script src="static/inspinia/js/jquery-3.1.1.min.js"></script>
	<script src="static/inspinia/js/bootstrap.min.js"></script>
	<script src="static/inspinia/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script
		src="static/inspinia/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="static/inspinia/js/inspinia.js"></script>
	<script src="static/inspinia/js/plugins/pace/pace.min.js"></script>
	<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>


	<script>
		function reload_page() {
			window.location.href = window.location.href;
		}

		function orderView(id) {
			layer.open({
				type : 2,
				title : '交易记录',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/orderView/' + id
			});
		}
	</script>
</body>

</html>