<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<link href="static/inspinia/css/plugins/sweetalert/sweetalert.css"
	rel="stylesheet">
<link href="static/inspinia/js/plugins/gritter/jquery.gritter.css"
	rel="stylesheet">
<link href="static/inspinia/css/animate.css" rel="stylesheet">
<link href="static/inspinia/css/style.css" rel="stylesheet">
<style>
.pd {
	overflow: hidden;
	padding: 10px;
}
</style>
</head>

<body style="background: white;">
	<div id="wrapper">
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div class="row">
								<table class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>订单号</th>
											<th>客户</th>
											<th>产品名称</th>
											<th>产品代码</th>
											<th>成交价格</th>
											<th>期权金</th>
											<th>名义本金</th>
											<th>合同开始日期</th>
											<th>合同终止日期</th>
											<c:choose>
											<c:when test="${balance }">
												<th>行权价格（元）</th>
												<th>结算金额（元）</th>
											</c:when>
											<c:otherwise>
												<th>退回客户金额</th>
											</c:otherwise>
											</c:choose>
											
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${orderList }" var="item">
											<tr class="gradeX">
												<td align="center">${item.orderNo }</td>
												<td align="center">${item.buyerName }</td>
												<td align="center">${item.productName }</td>
												<td align="center">${item.productCode }</td>
												<td align="center">${item.tradeAmountFormat }</td>
												<td align="center">${item.amountFormat }</td>
												<td align="center">${item.notionalPrincipalFormat }</td>
												<td align="center">${item.orderStartTime }</td>
												<td align="center">${item.orderEndTime }</td>
												<c:choose>
												<c:when test="${balance }">
												<td align="center">
												<fmt:formatNumber value="${item.executivePrice }" minFractionDigits="2"/>
												</td>
												<td align="center">
												
												${item.balanceAmountFormat }</td>
												</c:when>
												<c:otherwise>
												<td align="center">
												<fmt:formatNumber value="${item.difAmount }" minFractionDigits="2"/></td>
												</c:otherwise>
												</c:choose>
												
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6" style="text-align: center">
					<button class="btn btn-default btn-large" type="button"
						onclick="cancelUpdate()">取消</button>
				</div>
				<div class="col-xs-6" style="text-align: center">
					<button class="btn btn-primary btn-large" type="button"
						onclick="updateData('${tmpKey}')">确认更新</button>
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
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>
	<!-- Sweet alert -->
	<script src="static/inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>


	<script>
		function showbaojia() {
			window.location.href = window.location.href + "?baojia=1";
		}

		function cancelUpdate() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}

		function updateData(tmpKey) {
			swal({
				title : "你确定要执行此操作吗？",
				text : "",
				type : "warning",
				showCancelButton : true,
				cancelButtonText : '取消',
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : 'gm/json/dealOrderBatch',
					type : 'post',
					dataType : "json",
					data : {
						tmpKey : tmpKey
					},
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "更新成功",
								type : "success"
							}, function() {
								parent.reload_page();
								parent.layer.close(index);
							});
						}
					}
				});
			});

		}
	</script>
</body>

</html>