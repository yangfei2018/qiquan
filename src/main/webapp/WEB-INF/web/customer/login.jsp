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
	width: 270px;
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

.tb{
	clear: both;
	height:60px;
	overflow: hidden;
		width: 270px;
	margin-left: auto;
	margin-right: auto;
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
		<div class="tb" >
			<div class="rqleft selected" >短信登录</div>
			<div class="rqleft c">密码登录</div>
		</div>
		<form class="form" >
			<input type="hidden" name="loginMethod" value="1" />
			<p>
				<input name="account" type="text" placeholder="请输入手机号..."
					class="input-text size-L" tabIndex="1" autofocus="autofocus">
				<span class="error"></span>
			</p>
			<p>
				<input class="input-text size-L" type="text" placeholder="验证码:"
					name="validateCode" value="" style="width: 150px;" tabIndex="3">
				<a id="kanbuq" href="javascript:;" style="color: #fff"
					onclick="changeVerfiyCode('v_img');"><img
					src="customer/verify_pic" id="v_img"></a>
			</p>
			<p>
				<input class="input-text size-L" type="text" placeholder="手机短信验证码"
					name="smsCode" value="" style="width: 150px;" tabIndex="3">
				<button id="yzm" onClick="chaxun(this);"
					class="btn btn-secondary radius" type="button">获取验证码</button>
			</p>
			<p>
			<div class="col-xs-5">
				<button onClick="doLogin(this);" class="btn btn-secondary radius"
					type="button">登录</button>
			</div>
			<div class="col-xs-7">
				<button onClick="closePop();" class="btn btn-secondary radius"
					type="button">取消</button>
			</div>
			</p>
		</form>
		
		<form class="form" style="display: none" >
			<input type="hidden" name="loginMethod" value="2" />
			<p>
				<input name="account" type="text" placeholder="请输入手机号..."
					class="input-text size-L" tabIndex="1" autofocus="autofocus">
				<span class="error"></span>
			</p>
			<p>
				<input name="password" type="password" placeholder="请输入登录密码"
					class="input-text size-L" tabIndex="5" >
			</p>
			<p>
			<div class="col-xs-5">
				<button onClick="doLogin(this);" class="btn btn-secondary radius"
					type="button">登录</button>
			</div>
			<div class="col-xs-7">
				<button onClick="closePop();" class="btn btn-secondary radius"
					type="button">取消</button>
			</div>
			</p>
		</form>
	</article>


	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>

	<script type="text/javascript">
		var _time = 121;
		var scrollTimer;
		var _verify_btn;
		
		function parentLogin(){
			parent.goIndex();
		}
		
		$(function(){
			var f = $("form");
			console.log(f);
			$(".tb>div").each(function(i,item){
				$(this).click(function(){
					
					$(".tb>div").removeClass('selected');
					$(this).addClass('selected');
					
					$('form').each(function(j){
						if (i==j){
							$(this).show();
						}else{
							$(this).hide();
						}
					})
				})
			})
		})
		
		function timer(old_text) {
			console.log("time");
			_verify_btn.attr('disabled', true)
			scrollTimer = setInterval(function() {
				scroll_time(old_text)
			}, 1000);
		}

		function changeVerfiyCode(obj) {
			var timestamp = Date.parse(new Date());
			$("#" + obj).attr("src", "customer/verify_pic?t=" + timestamp);
		}

		function scroll_time(old_text) {
			_time--;
			_verify_btn.text(_time + '秒');
			console.log("_time:" + _time);
			if (_time == 0) {
				clearInterval(scrollTimer);
				_verify_btn.attr('disabled', false)
				_verify_btn.text(old_text);
			}
		}

		function closePop() {

			var index = parent.layer.getFrameIndex(window.name);
			console.log('close ' + index);
			parent.layer.close(index);
		}

		function doLogin(obj) {
			if ( $(obj).closest('form').find('[name="account"]').val() == '') {
				alert('手机号不能为空');
				 $(obj).closest('form').find('[name="account"]').focus();
				return false;
			}
			
			if ($(obj).closest('form').find('[name="loginMethod"]').val()==1){

				if ( $(obj).closest('form').find('[name="smsCode"]').val() == '') {
					alert('短信验证码不能为空');
					 $(obj).closest('form').find('[name="smsCode"]').focus();
					return false;
				}
			}else{
				if ( $(obj).closest('form').find('[name="password"]').val() == '') {
					alert('登录密码不能为空');
					 $(obj).closest('form').find('[name="password"]').focus();
					return false;
				}
			}
			
			$.ajax({
				url : 'customer/json/doLogin',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					console.log(res);
					if (res.hasOwnProperty('result') && !res.result ){
						alert(res.data);
					}else{
						parentLogin();
					}
				}
			});
			
		}

		function chaxun(obj) {

			if ( $(obj).closest('form').find('[name="account"]').val() == '') {
				alert('手机号不能为空');
				 $(obj).closest('form').find('[name="account"]').focus();
				return false;
			}

			if ( $(obj).closest('form').find('[name="validateCode"]').val() == '') {
				alert('验证码不能为空');
				 $(obj).closest('form').find('[name="validateCode"]').focus();
				return false;
			}
			
			
			console.log(obj);
			$(obj).text('发送中..');

			$.ajax({
				url : 'customer/json/sendsms',
				type : 'post',
				dataType : "json",
				data : {
					'account' : $(obj).closest('form').find('[name="account"]')
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
						$(obj).text('获取验证码')
					}
				}
			});

		}
	</script>

</body>
</html>