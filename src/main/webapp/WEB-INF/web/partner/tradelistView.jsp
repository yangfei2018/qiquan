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

<body style="background: white;">

	<div id="wrapper">
<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="row">
								<div class="tabs-container">
									<ul class="nav nav-tabs">
										<li <c:if test="${status==-1 }">class="active"</c:if>><a
											href="partner/tradelist/${id }?status=-1"> 全部</a></li>
										<li <c:if test="${status==0 }">class="active"</c:if>><a
											href="partner/tradelist/${id }?status=0"> 未报价</a></li>
										<li <c:if test="${status==1 }">class="active"</c:if>><a
											href="partner/tradelist/${id }?status=1"> 已报价</a></li>
									</ul>
									<div class="tab-content">
										<div id="tab-1" class="tab-pane active">
											<div class="panel-body">
												<table
													class="table table-striped table-bordered table-hover">
													<thead>
														<tr>
															<th>名称</th>
															<th>代码</th>
															<th>涨跌</th>
															<th>权利金（元）</th>
															<th>期限</th>
															<th>名义本金（元）</th>
															<th>期权类型</th>
															<th>询价时间</th>
															
															<c:if test="${status==1 }">
																<th class="active">报价时间</th>
															</c:if>
															<th>状态</th>
															
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${page.list }" var="item">
															<tr>
																<td>${item.productName }</td>
																<td>${item.productCode }</td>
																<td>
																	<c:choose>
																	<c:when test="${item.buyAndFall==1 }">
																	<span class="label label-success"> 涨 </span>
																	</c:when>
																	<c:when test="${item.buyAndFall==2 }">
																	<span class="label label-danger"> 跌 </span>
																	</c:when>
																	</c:choose></td>
																<td>${item.amountFormat }</td>
																<td>${item.period }天</td>
																<td>${item.notionalPrincipalFormat }</td>
																<td>
																	<c:choose>
																	<c:when test="${item.type==1 }">
																	美式
																	</c:when>
																	<c:when test="${item.type==2 }">
																	欧式
																	</c:when>
																	</c:choose>
																</td>
																<td>${item.createTimeFormat }</td>
																
																<c:if test="${status==1 }">
																	<td>${item.updateTimeFormat }</td>
																</c:if>
																<td>
																<c:choose>
																<c:when test="${item.status==0 }">
																	未报价
																</c:when>
																<c:when test="${item.status==1 }">
																	已报价
																</c:when>
																</c:choose></td>
																
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

		function baojia(id) {
			layer.open({
				type : 2,
				title : '报价',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/tradeView/' + id
			}); 
		}

		function viewPartnerBankInfo(partnerNo) {
			layer.open({
				type : 2,
				title : '代理商银行卡',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/partnerView/' + partnerNo + '/bankinfo'
			});
		}
	</script>
</body>

</html>