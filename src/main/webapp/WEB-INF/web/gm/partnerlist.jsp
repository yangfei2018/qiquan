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
				<jsp:param name="curr" value="代理商管理" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div class="row">
									<div class="col-sm-4 pull-left">
										<button type="button" class="btn btn-sm btn-danger" onclick="addPartner()">
													新增代理商</button>
									</div>
									<!-- <div class="col-sm-4 pull-right">
										<div class="input-group">
											<input type="text" placeholder="搜索"
												class="input-sm form-control"> <span
												class="input-group-btn">
												<button type="button" class="btn btn-sm btn-primary">
													搜索!</button>
											</span>
										</div>
									</div> -->
								</div>
								<div class="row">
									<table class="table table-striped table-bordered table-hover dataTables">
										<thead>
											<tr>
												<th>代理商编号</th>
												<th>代理商名称</th>
												<th>法人姓名</th>
												<th>联系电话</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${page.list }" var="item">
												<tr class="gradeX">
													<td>${item.partnerNo }</td>
													<td>${item.companyName }</td>
													<td>${item.realName }</td>
													<td>${item.mobile }</td>
													<td>
														<div class="btn-group">
															<button data-toggle="dropdown"
																class="btn btn-xs btn-primary dropdown-toggle">
																操作 <span class="caret"></span>
															</button>
															<ul class="dropdown-menu">
																<li><a href="javascript:;" onclick="viewPartnerDetail('${item.partnerNo }')">查看详情</a></li>
																<li><a href="javascript:;" onclick="viewPartnerBankInfo('${item.partnerNo }')">有限合伙管理</a></li>
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
		$(function(){
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
	
	
		function reload_page(){
			window.location.href = window.location.href;
		}
		
		function addPartner(){
			layer.open({
				  type: 2,
				  title: '新增代理商',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['80%', '80%'],
				  maxmin:true,
				  content: 'gm/createPartner/'
				}); 
		}
		
		function viewPartnerDetail(partnerNo){
			console.log('url:'+'gm/partnerView/'+partnerNo);
			layer.open({
				  type: 2,
				  title: '代理商详情',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['80%', '80%'],
				  maxmin:true,
				  content: 'gm/partnerView/'+partnerNo
				}); 
		}
		
		
		function viewPartnerBankInfo(partnerNo){
			layer.open({
				  type: 2,
				  title: '有限合伙管理',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['80%', '80%'],
				  maxmin:true,
				  content: 'gm/partnerView/'+partnerNo+'/bankinfo'
				}); 
		}
	</script>
</body>

</html>