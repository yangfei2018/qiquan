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

			<div class="col-md-12">
				<div class="ibox-content">

					<div class="row">

						<div class="col-lg-12">
							<form class="m-t">
								<div class="form-group">
									<input type="password" name="oldpwd" class="form-control"
										placeholder="请输入旧密码" required="">
								</div>
								<div class="form-group">
									<input type="password" name="newpwd" class="form-control"
										placeholder="请输入新密码" required="">
								</div>
								<div class="form-group">
									<input type="password" name="newpwd1" class="form-control"
										placeholder="再次输入新密码" required="">
								</div>

								<button type="button" onClick="doChangePwd(this);"
									class="btn btn-primary block full-width m-b">确认</button>
							</form>
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
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>
	<!-- Sweet alert -->
	<script src="static/inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>


	<script>
		function doChangePwd(obj) {
			if ($('input[name="oldpwd"]').val() == '') {
				swal("请输入旧密码", "", "error");
				$('input[name="oldpwd"]').focus();

				return false;
			}

			if ($('input[name="newpwd"]').val() == '') {
				swal("请输入新密码", "", "error");
				$('input[name="newpwd"]').focus();

				return false;
			}

			if ($('input[name="newpwd1"]').val() == '') {
				swal("请输入新密码", "", "error");
				$('input[name="newpwd1"]').focus();

				return false;
			}

			if ($('input[name="newpwd1"]').val() != $('input[name="newpwd"]')
					.val()) {
				swal("2次新密码不相同", "", "error");

				return false;
			}

			$(obj).attr('disabled', true);
			$.ajax({
				url : 'gm/json/doChangePwd',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					$(obj).attr('disabled', false);
					if (res.result) {
						swal({
							title : "更新成功",
							type : "success"
						},
								function() {
									var index = parent.layer
											.getFrameIndex(window.name);
									parent.layer.close(index);
								});
					} else {
						swal(res.data, "", "error");

					}
				}
			});
		}
	</script>
</body>

</html>