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
				<jsp:param name="curr" value="提现记录" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								
								<div class="row">
									<table
													class="table table-striped table-bordered table-hover">
													<thead>
														<tr>
															<th>申请提现时间</th>
															<th>提现金额（元）</th>
															<th>真实姓名</th>
															<th>提现到账户</th>
															<th>提现到卡号</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${page.list }" var="item">
															<tr>
																<td>${item.createTimeFormat }</td>
																<td>${item.amountFormat }</td>
																<td>${item.realName }</td>
																
																<td>${item.bankOfDeposit }</td>
																<td>${item.bankCardNo }</td>
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

		
	</script>
</body>

</html>