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

h5{
	text-align: center;
	font-size:20px;
	height:40px;
}

p{
	padding: 0px;
}


#kanbuq{
	width:180px;
	height:auto;
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
			<h5>注册</h5>
			<p>
				<input name="account" type="text" placeholder="请输入中国大陆手机号"
					class="input-text size-L" tabIndex="1" autofocus="autofocus">
			</p>
			<p>
				<input class="input-text size-L" type="text" placeholder="验证码:"
					name="validateCode" value="" style="width: 158px;" tabIndex="2">
				<a id="kanbuq" href="javascript:;" style="color: #fff"
					onclick="changeVerfiyCode('v_img');"><img
					src="customer/verify_pic" id="v_img"></a>
			</p>
			<p>
				<input class="input-text size-L" type="text" placeholder="手机短信验证码"
					name="code" value="" style="width: 158px;" tabIndex="3">
				<button id="yzm" onClick="chaxun(this);" class="btn btn-success radius"
					type="button">获取验证码</button>
			</p>
			<p>
				<input name="partnerCode" type="text" placeholder="请输入激活码"
					class="input-text size-L" tabIndex="4" >
			</p>
			<p>
				<input name="password" type="password" placeholder="请输入登录密码"
					class="input-text size-L" tabIndex="5" >
			</p>
			<p>
			<div class="col-xs-5">
				<button onClick="doReg(this);" class="btn btn-primary radius"
					type="button">注册</button>
			</div>
			<div class="col-xs-7">
				<button onClick="closePop();" class="btn btn-link radius"
					type="button">取消</button>
			</div>
			</p>
		</form>
	</article>


	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js?ver=1.0.1"></script>
	<script type="text/javascript" src="static/h-ui/js/jquery.media.js"></script>

	<script type="text/javascript">
		var _time = 121;
		var scrollTimer;
		var _verify_btn;
		
		$(function(){
			openLx();
		})
		
		function parentLogin(){
			parent.goIndex();
		}
		
		function timer(old_text) {
			console.log("time");
			_verify_btn.attr('disabled', true)
			scrollTimer = setInterval(function() {
				scroll_time(old_text)
			}, 1000);
		}
		
		function openLx() {
			layer.open({
				type : 2,
				maxmin : false,
				shadeClose : true,
				title : "协议",
				area : [ '100%', '100%' ],
				/* content : [ 'agreement/' + path + '?userid=${user.userId}',
						'yes' ] */
				content:['http://dl.wanjianginfo.com/reg/fxjs.pdf','yes']
			})
		}
		

		function closePop() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}

		function changeVerfiyCode(obj) {
			var timestamp = Date.parse(new Date());
			$("#" + obj).attr("src", "customer/verify_pic?t=" + timestamp);
		}

		function scroll_time(old_text) {
			_time--;
			_verify_btn.text(_time + '秒');
			console.log("_time:"+_time);
			if (_time == 0) {
				clearInterval(scrollTimer);
				_verify_btn.attr('disabled', false)
				_verify_btn.text(old_text);
			}
		}

		function doReg(obj) {
			if ($('input[name="account"]').val() == '') {
				alert('手机号不能为空');
				$('input[name="account"]').focus();
				return false;
			}
			
			if ($('input[name="validateCode"]').val()==''){
				alert('验证码能为空');
				$('input[name="validateCode"]').focus();
				return false;
			}
			
			if ($('input[name="code"]').val()==''){
				alert('手机短信验证码能为空');
				$('input[name="code"]').focus();
				return false;
			}
			
			if ($('input[name="partnerCode"]').val()==''){
				alert('激活码不能为空');
				$('input[name="partnerCode"]').focus();
				return false;
			}
			
			if ($('input[name="password"]').val()=='' || $('input[name="password"]').val().length<4){
				alert('登录密码长度不能小于4个字符');
				$('input[name="password"]').focus();
				return false;
			}
			
			$.ajax({
				url : 'customer/json/doRegist',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					if (!res.result){
						alert(res.data);
					}else{
						parentLogin();
					}
				}
			});
			
		}

		function chaxun(obj) {
			
			if ($('input[name="account"]').val() == '') {
				alert('手机号不能为空');
				$('input[name="account"]').focus();
				return false;
			}

			if ($('input[name="validateCode"]').val() == '') {
				alert('验证码不能为空');
				$('input[name="validateCode"]').focus();
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