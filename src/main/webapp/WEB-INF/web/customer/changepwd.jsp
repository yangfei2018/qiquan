<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=request.getContextPath()%>/" />
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--<link rel="Bookmark" href="favicon.ico" >
 <link rel="Shortcut Icon" href="favicon.ico" /> -->
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<link rel="stylesheet" type="text/css"
	href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css"
	href="lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="lib/icheck/icheck.css" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui.admin/css/style.css" />
<style>
body, th, td, button, input, select, textarea, .radio-box label {
	font-size: 16px;
}

.pad {
	max-width: 450px;
	width: 80%;
	margin-left: auto;
	margin-right: auto;
	font-size: 16px;
}

.form-label {
	font-size: 16px;
	color: black;
}

.select {
	font-size: 16px;
	height: 25px;
}

.page-container {
	padding-left: 30px;
	padding-right: 30px;
	overflow: hidden;
}

form {
	width: 80%;
	margin-left: auto;
	margin-right: auto;
}

.rqleft {
	width: 49%;
	float: left;
	display: block;
	height: 60px;
	line-height: 60px;
	font-size: 16px;
	text-align: center;
	cursor: pointer;
}

.rqleft.selected {
	color: red;
}

.error {
	color: red;
}
</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>
	<article class="page-container">
		<form class="form">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>旧密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="password" class="input-text size-L" value=""
						placeholder="请输入旧密码" name="oldpwd">

				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>新密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="password" class="input-text size-L" value=""
						placeholder="请输入新密码" name="newpwd">

				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span
					class="c-red">*</span>再次输入新密码：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="password" class="input-text size-L" value=""
						placeholder="再次输入新密码" name="newpwd1">
				</div>
			</div>
			<div class="row cl">
				<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-2">

					<button onClick="doChangePwd(this);"
						class="btn btn-secondary size-XL radius" type="button">确认
					</button>

				</div>
			</div>
		</form>
	</article>


	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>

	<script type="text/javascript">
		function doChangePwd(obj) {
			if ($('input[name="oldpwd"]').val() == '') {
				layer_alert('请输入旧密码', function() {
					$('input[name="oldpwd"]').focus();
				});
				return false;
			}

			if ($('input[name="newpwd"]').val() == '') {
				layer_alert('请输入新密码', function() {
					$('input[name="newpwd"]').focus();
				});
				return false;
			}

			if ($('input[name="newpwd1"]').val() == '') {
				layer_alert('请再次输入新密码', function() {
					$('input[name="newpwd1"]').focus();
				});
				return false;
			}

			if ($('input[name="newpwd1"]').val() != $('input[name="newpwd"]')
					.val()) {
				layer_alert('2次新密码不相同', function() {
					$('input[name="newpwd1"]').focus();
				});
				return false;
			}

			$(obj).attr('disabled', true);
			$.ajax({
				url : 'customer/json/doChangePwd',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					$(obj).attr('disabled', false);
					if (res.result) {
						layer_success(res.data,
								function() {
									var index = parent.layer
											.getFrameIndex(window.name);
									parent.layer.close(index);
								});
					} else {
						layer_alert(res.data);
					}
				}
			});
		}

		function chaxun(obj) {
			if ($('input[name="validateCode"]').val() == '') {
				alert('验证码不能为空');
				$('input[name="validateCode"]').focus();
				return false;
			}
			$(obj).text('发送中..');

			$.ajax({
				url : 'customer/json/sendsms',
				type : 'post',
				dataType : "json",
				data : {
					'account' : $(obj).closest('form').find('[name="mobile"]')
							.val(),
					'verifyCode' : $(obj).closest('form').find(
							'[name="validateCode"]').val()
				},
				success : function(res) {
					console.log(res);
					// res = jQuery.parseJSON(res);
					$(obj).attr('disabled', false);
					// alert(res.result);
					if (res.result) {
						_time = 120;
						_verify_btn = $(obj);
						timer('获取验证码');
					} else {
						alert(res.data);
						$('input[name="validateCode"]').focus();
						$(obj).val('获取验证码')
					}
				}
			});

		}
	</script>

</body>
</html>