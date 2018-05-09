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

<body style="background: white">

	<div id="wrapper">

		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="row">
							<table class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>会员id</th>
										<th>会员账号</th>
										<th>会员真实姓名</th>
										<th>注册时间</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list }" var="item">
										<tr class="gradeX">
											<td align="center">${item.userId }</td>
											<td>${item.name }</td>
											<td>${item.realName }</td>
											<td>${item.regTime }</td>
											<td align="center"><c:choose>
													<c:when test="${item.status==1 }">
														<span class="label label-danger"> 未实名认证 </span>
													</c:when>
													<c:when test="${item.status==2 }">
														<span class="label label-Warning"> 待实名认证 </span>
													</c:when>
													<c:when test="${item.status==3 }">
														<span class="label label-primary"> 已实名认证 </span>
													</c:when>
													<c:when test="${item.status==4 }">
														<span class="label label-success"> 已激活 </span>
													</c:when>
												</c:choose></td>
											<td>
												<div class="btn-group">
													<button data-toggle="dropdown"
														class="btn btn-xs btn-primary dropdown-toggle">
														操作 <span class="caret"></span>
													</button>
													<ul class="dropdown-menu">
														<li><a href="javascript:;"
															onclick="viewUserDetail('${item.userId }')">查看详情</a></li>
														<li><a href="javascript:;"
															onclick="viewUserTradeListDetail('${item.userId }')">查看会员询价记录</a></li>
														<li><a href="javascript:;"
															onclick="viewUserOrderListDetail('${item.userId }')">查看会员交易记录</a></li>
													</ul>
												</div>
											</td>
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

		function viewUserDetail(id) {
			layer.open({
				type : 2,
				title : '会员详情',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'partner/userView/' + id
			});
		}

		function viewUserTradeListDetail(id) {
			layer.open({
				type : 2,
				title : '会员询价列表',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'partner/tradelist/' + id
			});
		}

		function viewUserOrderListDetail(id) {
			layer.open({
				type : 2,
				title : '会员询价列表',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'partner/orderlist/' + id
			});
		}
	</script>
</body>

</html>