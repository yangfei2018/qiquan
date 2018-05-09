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
<link href="static/inspinia/css/plugins/dataTables/datatables.min.css"
	rel="stylesheet">

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../include/gm/nav-left.jsp" />
		<div id="page-wrapper" class="gray-bg">
			<jsp:include page="../include/gm/navbar-top.jsp" />
			<jsp:include page="../include/gm/nav-breadcrumb.jsp">
				<jsp:param name="curr" value="会员管理" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="row">
								<div class="tabs-container">
									<ul class="nav nav-tabs">
										<li <c:if test="${status==0 }">class="active"</c:if>><a
											href="gm/userlist?status=0&partnerId=${partnerId }"> 全部</a></li>
										<li <c:if test="${status==1 }">class="active"</c:if>><a
											href="gm/userlist?status=1&partnerId=${partnerId }">
												未认证会员</a></li>
										<li <c:if test="${status==2 }">class="active"</c:if>><a
											href="gm/userlist?status=2&partnerId=${partnerId }">
												待认证会员</a></li>
										<li <c:if test="${status==3 }">class="active"</c:if>><a
											href="gm/userlist?status=3&partnerId=${partnerId }">
												已认证会员</a></li>
										<li <c:if test="${status==4 }">class="active"</c:if>><a
											href="gm/userlist?status=4&partnerId=${partnerId }"> 已激活</a></li>
									</ul>
									<div class="tab-content">
										<div id="tab-1" class="tab-pane active">
											<div class="panel-body">
												<div class="row">
													<div class="col-sm-4 pull-right" style="text-align: right">
														<select class="form-control m-b" name="partnerId"
															onchange="search(this)">
															<option value="0">全部</option>
															<c:forEach items="${partners.list }" var="partner">
																<option value="${partner.userId }"
																	<c:if test="${partnerId==partner.userId }">selected="selected" </c:if>>${partner.companyName }</option>
															</c:forEach>
														</select>
													</div>
												</div>

												<table
													class="dataTables table table-striped table-bordered table-hover">
													<thead>
														<tr>
															<th>会员id</th>
															<th>会员账号</th>
															<th>会员真实姓名</th>
															<th>归属代理商</th>
															<th>注册时间</th>
															<th>状态</th>
															<c:if test="${role_id== 1||role_id==0}">
																<th>账户余额</th>
															</c:if>
															<c:if test="${role_id!= 1}">
															<th>操作</th>
															</c:if>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${page.list }" var="item">
															<tr class="gradeX">
																<td align="center">${item.userId }</td>
																<td>${item.name }</td>
																<td>${item.realName }</td>
																<td>${item.partnerCompanyName }</td>
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
																<c:if test="${role_id== 1||role_id==0}">
																	<td>${item.amount }</td>
																</c:if>
																<c:if test="${role_id!= 1}">
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
																				onclick="chongzhi('${item.userId }')">充值</a></li>
																		</ul>
																	</div>
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
	<script src="static/inspinia/js/plugins/dataTables/datatables.min.js"></script>

	<script>
		$(function() {
			$('.dataTables').DataTable({
				pageLength : 25,
				responsive : true,
				dom : '<"html5buttons"B>lTfgitp',
				buttons : [ {
					extend : 'copy'
				},

				]

			});
		})

		function reload_page() {
			window.location.href = window.location.href;
		}

		function search(obj) {
			gohref('gm/userlist?status=${status}&partnerId=' + $(obj).val());
		}

		function addPartner() {
			layer.open({
				type : 2,
				title : '新增代理商',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/createPartner/'
			});
		}

		function chongzhi(id) {
			layer.open({
				type : 2,
				title : '会员充值',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/userView/' + id + '?chongzhi=1'
			});
		}

		function viewUserDetail(id) {
			layer.open({
				type : 2,
				title : '会员详情',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/userView/' + id
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