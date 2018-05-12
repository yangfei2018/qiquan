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
				<jsp:param name="curr" value="财务记录" />
			</jsp:include>

			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div class="row">
									<div class="col-sm-8 pull-left">
										<input type="text" placeholder="请输入帐号、客户名或公司" value="" id="searchText">
										<input type="text" placeholder="开始日期" value="" id="startTimePicker">-
										<input type="text" placeholder="结束日期" value="" id="endTimePicker">
										<button type="button" class="btn btn-sm btn-search"
												onclick="search()">查询</button>
									</div>
									<div class="col-sm-4 pull-right" style="text-align: right">
										<button type="button" class="btn btn-sm btn-danger"
											onclick="excel()">导出excel</button>
									</div>
								</div>

								<div class="row">
									<table class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>账号</th>
												<th>客户名称</th>
												<th>代理商</th>
												<th>订单号</th>
												<th>权利金（元）</th>
												<th>结算金额（元）</th>
												<th>应返有限合伙（元）</th>
												<th>应返客户（元）</th>
												<th>结算时间</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${page.list }" var="item">
												<tr>
													<td>${item.buyerMobile}</td>
													<td>${item.buyerName}</td>
													<td>${item.buyerPartnerCompanyName }</td>
													<td>${item.orderNo }</td>
													<td align="right">${item.amountFormat }</td>
													<td align="right">${item.balanceAmountFormat }</td>
													<td align="right">${item.hehuoAmountFormat }</td>
													<td align="right">${item.discountFormat }</td>
													<td align="right">${item.balanceTimeFormat }</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
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
	<script src="static/inspinia/js/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<link href="static/inspinia/js/plugins/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css"
		  rel="stylesheet">


	<script>
		function reload_page() {
			window.location.href = window.location.href;
		}
		
		
		function excel() {
			gohref("gm/caiwureport?excel=1")
		}
        $('#startTimePicker').datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language:  'zh-CN',
            format: 'yyyy-mm-dd HH:mm:ss',
            todayBtn:  1,
            autoclose: 1,
        });
        $('#endTimePicker').datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language:  'zh-CN',
            format: 'yyyy-mm-dd HH:mm:ss',
            todayBtn:  1,
            autoclose: 1,
        });
        function search(){
            var startTime = $('#startTimePicker').val();
            var endTime = $('#endTimePicker').val();
            var searchText = $('#searchText').val();
            var encodeparam = encodeURI(searchText) ;
            window.location.href = "gm/caiwureport?startTime="+startTime + "&endTime=" + endTime + "&searchText=" + encodeparam;
            // console.log("status:" + status + "\nstartTime:" + startTime + "\nendTime:" + endTime);
        }
	</script>
</body>

</html>