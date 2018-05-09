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
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
						<form class="form-horizontal">
							<input type="hidden" name="userId" value="${gmUser.userId }" />
							<c:if test="${gmUser.userId==0}">
								<div class="form-group">
									<label class="col-lg-2 control-label">登录账号</label>
									<div class="col-lg-10">
										<input type="text" class="form-control" name="name"
											placeholder="请设置登录账号" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-lg-2 control-label">登录密码</label>
									<div class="col-lg-10">
										<input type="text" class="form-control" name="password"
											placeholder="请设置登录密码，建议大于4个字符。" />
									</div>
								</div>
								<div class="hr-line-dashed"></div>
							</c:if>
							<c:if test="${gmUser.userId>0}">
								<div class="form-group">
									<label class="col-lg-2 control-label">登录账号</label>
									<div class="col-lg-10">
										<p class="form-control-static">${gmUser.name }</p>
									</div>
								</div>
						
								<div class="hr-line-dashed"></div>
							</c:if>

							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">姓名</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${gmUser.realName }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="realName"
												value="${gmUser.realName }" />
										</c:when>
									</c:choose>

								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">权限</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${gmUser.positionName }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<select class="form-control m-b" name="positionId">
												<c:forEach items="${gmPositions }" var="position">
													<option value="${position.id }" <c:if test="${gmUser.positionId==position.id }"> selected="selected"</c:if> >${position.value }</option>
												</c:forEach>
											</select>
										</c:when>
									</c:choose>

								</div>
							</div>
							
							<div class="form-group">
								<label class="col-lg-2 control-label">状态</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${gmUser.status==1?'正常':'禁用' }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<select class="form-control m-b" name="status">
												<option value="1" <c:if test="${gmUser.status==1 }"> selected="selected"</c:if>  >正常</option>
												<option value="0" <c:if test="${gmUser.status==0 }"> selected="selected"</c:if>  >禁用</option>
											</select>
										</c:when>
									</c:choose>

								</div>
							</div>
							
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-2">
									<c:choose>
										<c:when test="${edit==0 }">
											<button class="btn btn-primary" type="button"
												onclick="edit()">修改信息</button>
											<button class="btn btn-danger" type="button"
												onclick="resetPassword()">重置密码</button>	
										</c:when>
										<c:when test="${edit==1 }">
											<button class="btn btn-white" type="button"
												onclick="cancelEdit()">取消</button>
											<button class="btn btn-primary" type="button"
												onclick="save(this)">保存</button>
										</c:when>
									</c:choose>

								</div>
							</div>
							
						</form>
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
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>
	<!-- Sweet alert -->
	<script src="static/inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>

	<script>


		function edit() {
			window.location.href = window.location.href + "?edit=1";
		}

		function cancelEdit() {
			gohref('gm/gmUserView/${gmUser.userId}');
		}

		function save(obj) {
			
			$.ajax({
				url : 'gm/json/updategmUser',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					console.log(res);
					if (res.hasOwnProperty('result') && !res.result) {
						swal(res.data, "", "error")
					} else {
						swal({
							title : "更新成功",
							type : "success"
						},
								function() {
									var index = parent.layer
											.getFrameIndex(window.name);
									parent.reload_page();
									parent.layer.close(index);
								});
					}
				}
			});
		}
		
		
		function resetPassword() {
			layer.prompt({
				title : '请输入新的密码',
				formType : 1
			}, function(pass, index) {
				layer.close(index);
				$.ajax({
					url : 'gm/json/resetGmUserPassword',
					type : 'post',
					dataType : "json",
					data : {
						userId:$("input[name='userId']").val(),
						newPass:pass
					},
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "操作成功",
								type : "success"
							}, function() {
								layer.close(index);
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