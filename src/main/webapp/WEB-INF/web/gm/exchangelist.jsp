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

		<jsp:include page="../include/gm/nav-left.jsp" />
		<div id="page-wrapper" class="gray-bg">
			<jsp:include page="../include/gm/navbar-top.jsp" />
			<jsp:include page="../include/gm/nav-breadcrumb.jsp">
				<jsp:param name="curr" value="充值管理" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="row">
								<div class="tabs-container">
									<ul class="nav nav-tabs">
										<li <c:if test="${status==0 }">class="active"</c:if>><a
											href="gm/exchangelist?status=0">待处理</a></li>
										<li <c:if test="${status==1 }">class="active"</c:if>><a
											href="gm/exchangelist?status=1">已入账</a></li>

									</ul>
									<div class="tab-content">
										<div id="tab-1" class="tab-pane active">
											<div class="panel-body">
												<div class="row">
													<div class="col-sm-4 pull-right" style="text-align: right">
														<button type="button" class="btn btn-sm btn-danger"
															onclick="excel()">导出excel</button>
													</div>
												</div>

												<table
													class="table table-striped table-bordered table-hover">
													<thead>
														<tr>
															<th>购买者</th>
															<th>代理商</th>
															<th>会员帐号</th>
															<th>有限合伙公司</th>
															<th>充值编号</th>
															<th>金额（元）</th>

															<th>创建时间</th>
															<th>状态</th>
															<c:if test="${status==0 }">
																<th>操作</th>
															</c:if>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${page.list }" var="item">
															<tr>
																<td>${item.buyerName }</td>
																<td>${item.companyName }</td>
																<td>${item.name }</td>
																<td>${item.pcompanyName }</td>
																<td>${item.rechargeNo }</td>
																<td>${item.amountFormat }</td>

																<td>${item.createTimeFormat }</td>
																<td><c:choose>
																		<c:when test="${item.status==0 }">
																	待处理
																</c:when>
																		<c:when test="${item.status==1 }">
																	已入账
																</c:when>
																	</c:choose></td>
																<c:if test="${status==0 }">
																	<td>

																		<button onClick="rz(${item.id});"
																			class="btn btn-xs btn-success" type="button">
																			确定已到款</button>
																		
																		<button onClick="bh(${item.id});"
																			class="btn btn-xs btn-danger" type="button">
																			不符驳回/删除</button>	

																	</td>
																</c:if>
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

			<jsp:include page="../include/gm/footer.jsp" />
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

		function excel() {
			gohref("gm/exchangelist?status=${status}&excel=1")
		}
		
		
		function bh(id){
			swal({
				title : "你确定要驳回该充值记录吗？",
				text : "",
				type : "warning",
				showCancelButton : true,
				cancelButtonText :'取消',
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : 'gm/json/bohuichongzhi',
					type : 'post',
					dataType : "json",
					data : {id:id},
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "操作成功",
								type : "success"
							}, function() {
								reload_page();
							});
						}
					}
				});
			});
		}
		
		function rz(id){
			swal({
				title : "你确定已经收到这笔款项了吗？",
				text : "",
				type : "warning",
				showCancelButton : true,
				cancelButtonText :'取消',
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : 'gm/json/confirmchongzhi',
					type : 'post',
					dataType : "json",
					data : {id:id},
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "操作成功",
								type : "success"
							}, function() {
								reload_page();
							});
						}
					}
				});
			});
		}
		
	</script>
</body>

</html>