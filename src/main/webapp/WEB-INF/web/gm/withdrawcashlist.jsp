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
				<jsp:param name="curr" value="提现记录" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="row">
								<div class="tabs-container">
									<ul class="nav nav-tabs">

										<li <c:if test="${status==0 }">class="active"</c:if>><a
											href="gm/withdrawcashlist?status=0"> 未结算</a></li>
										<li <c:if test="${status==1 }">class="active"</c:if>><a
											href="gm/withdrawcashlist?status=1"> 已结算</a></li>
									</ul>
									<div class="tab-content">
										<div id="tab-1" class="tab-pane active">

											<div class="panel-body">
												<c:if test="${status==0 }">
											
												<div class="row">
													<div class="col-sm-4 pull-right" style="text-align: right">
														<button type="button" class="btn btn-sm btn-danger"
															onclick="excel()">导出excel</button>
													</div>
												</div>
												</c:if>
												<table
													class="table table-striped table-bordered table-hover">
													<thead>
														<tr>
															<th>申请提现时间</th>
															<th>代理商</th>
															<th>有限合伙公司</th>
															<th>提现金额（元）</th>
															<th>会员帐号</th>
															<th>真实姓名</th>
															<th>提现到账户</th>
															<th>提现到卡号</th>
															<c:if test="${status==0 }">
															<th>操作</th>
															</c:if>
															<c:if test="${status==1 }">
																<th>结算时间</th>
															</c:if>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${page.list }" var="item">
															<tr>
																<td>${item.createTimeFormat }</td>
																<td>${item.partnerCompanyName }</td>
																<td>${item.pcompanyName }</td>
																<td>${item.amountFormat }</td>
																<td>${item.name }</td>
																<td>${item.realName }</td>

																<td>${item.bankOfDeposit }</td>
																<td>${item.bankCardNo }</td>
																<c:if test="${status==0 }">
																<td>
																	<div class="btn-group">
																		<button onClick="confirmWithdraw('${item.id}')"
																				class="btn btn-xs btn-primary" type="button">
																			确认
																		</button>
																	</div>
																</td>
																</c:if>
																<c:if test="${status==1 }">
																	<th>${item.clearingTimeFormat }</th>
																</c:if>
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
			gohref("gm/withdrawcashlist?excel=1")
		}

        function confirmWithdraw(id) {
		    console.log(id);
            //询问框
            layer.confirm('确认已结算？', {
                btn: ['确认','取消'] //按钮
            }, function(index){
                $.ajax({
                    url : 'gm/json/updateWithdrawStatus',
                    type : 'post',
                    dataType : "json",
                    data : {
                        id : id
                    },
                    success : function(res) {
                        if (res.hasOwnProperty('result') && !res.result) {
                            layer.msg(res.data);
                        } else {
                            layer.close(index);
                            layer.msg('操作成功');
                            window.location.href = window.location.href;
                        }
                    }
                });
            });
        }
	</script>
</body>

</html>