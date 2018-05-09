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
		<div class="row">
			<div class="col-sm-6 b-r">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
						<form class="form-horizontal">
							<input type="hidden" name="userId" value="${customer.userId }" />

							<div class="form-group">
								<label class="col-lg-2 control-label">会员编号</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.userId }</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">会员登录账号</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.name }</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">会员联系电话</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.mobile }</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">会员真实姓名</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.realName }</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">会员身份证ID</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.idCard }</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">会员激活码</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.partnerCode }
										（${customer.partnerCompanyName }）</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">会员地址</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.address }</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">会员账户余额</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.amount }元</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">会员注册时间</label>
								<div class="col-lg-10">
									<p class="form-control-static">${customer.regTime }</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">会员状态</label>
								<div class="col-lg-10">
									<p class="form-control-static">
										<c:choose>
											<c:when test="${customer.status==1 }">
												<span class="label label-danger"> 未实名认证 </span>
											</c:when>
											<c:when test="${customer.status==2 }">
												<span class="label label-Warning"> 待实名认证 </span>
											</c:when>
											<c:when test="${customer.status==3 }">
												<span class="label label-primary"> 已实名认证 </span>
											</c:when>
											<c:when test="${customer.status==4 }">
												<span class="label label-success"> 已激活 </span>
											</c:when>
										</c:choose>

									</p>
								</div>
							</div>
							<%-- <c:if test="${customer.status==3 }">
								<div class="hr-line-dashed"></div>

								<div class="form-group">
									<label class="col-lg-2 control-label"></label>
									<div class="col-lg-10">
										<button class="btn btn-danger" type="button"
											onclick="audit(this)">加价激活该用户</button>
									</div>
								</div>
							</c:if> --%>
						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">

				<div class="pd">
					<h4>身份证正面</h4>
					<p class="text-center">
						<a href="javascript:;"> <c:choose>
								<c:when test="${empty customer.idCardFrontPic }">
									<img class="thumbnail" src="static/inspinia/img/empty.png"
										alt="身份证正面">

								</c:when>
								<c:otherwise>
									<img class="thumbnail" src="${customer.idCardFrontPic }"
										alt="身份证正面">
								</c:otherwise>
							</c:choose>
						</a>
					</p>
					<h4>身份证反面</h4>
					<p class="text-center">
						<a href="javascript:;"> <c:choose>
								<c:when test="${empty customer.idCardBackgroundPic }">
									<img class="thumbnail" src="static/inspinia/img/empty.png"
										alt="身份证反面">
								</c:when>
								<c:otherwise>
									<img class="thumbnail" src="${customer.idCardBackgroundPic }"
										alt="身份证反面">
								</c:otherwise>
							</c:choose>

						</a>
					</p>
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
		
	
		function audit(obj) {
			swal({
				title : "你确定要激活该用户吗？",
				text : "",
				type : "warning",
				showCancelButton : true,
				cancelButtonText :'取消',
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : 'partner/json/auditUser',
					type : 'post',
					dataType : "json",
					data : $(obj).closest("form").serialize(),
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "客户已激活",
								type : "success"
							}, function() {
								var index = parent.layer
										.getFrameIndex(window.name);
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