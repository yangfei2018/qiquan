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

<link rel="stylesheet" type="text/css"
	href="static/bootcss/css/bootstrap.min.css" />
<style>
.jumbotron {
	margin-bottom: 0px !important;
	padding-bottom: 0px !important;
}

.qgh {
	padding-top: 10px;
	padding-bottom: 10px;
	background: #f7f8f9;
}

.qghb {
	padding-top: 10px;
	padding-bottom: 10px;
}

.qghbw {
	padding-top: 10px;
	padding-bottom: 10px;
	background:
		url(//1.s60i.faiusr.com/2/1133/AO0ICAIQsL61wQUYn82z7AUggA8o9AM.jpg)
		no-repeat 50% 50%;
}

.b_1024 {
	max-width: 1024px;
	margin-left: auto;
	margin-right: auto;
	overflow: hidden;
}

.footer {
	background: #2d2f2d;
	padding-top: 20px;
	padding-bottom: 20px;
	position: absolute;
	bottom:0px;
	width: 100%;
}

.footer .about {
	color: white;
}

.footer a {
	color: #777;
}

.navbar-inverse {
	background: white;
}

.navbar-inverse .navbar-nav>li>a:hover {
	color: #9d9d9d;
}

.icon-bar {
	color: #9d9d9d !important;
}

.rkbtn {
	margin: 10px !important;
	background: #fc683b;
	color: white !important;
	border-radius: 5px;
	height: 30px !important;;
	line-height: 30px !important;;
	margin-top: 10px !important;;
	padding-top: 0px !important;;
	padding-bottom: 0px !important;;
}

.rkbtn:hover {
	color: white !important;
}

.masthead h1 {
	color: white;
	text-align: center;
}

.lop {
	margin-left: auto;
	margin-right: auto;
	background: black;
	height: 100px;
	width: 100px;
	border-radius: 50%;
	color: white;
	text-align: center;
	line-height: 100px;
	font-weight: 800;
	font-size: 35px;
	background: url(static/img/mask.png) repeat center center;
}

.login-form {
	float: right;
	margin-right: 0px;
	width: 360px;
	height: 520px;
	display: block;
	background: white;
	padding: 0px;
	overflow: hidden;
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


#v_img{
	width:100%;
	height:auto;
}

.rqleft.selected {
	color: red;
}

.error {
	color: red;
}

.tb {
	clear: both;
	height: 60px;
	overflow: hidden;
	width: 90%;
	margin-left: auto;
	margin-right: auto;
	margin-top:10px !important;
	margin-bottom:20px !important;
	border-bottom:1px solid #f2f2f2;
}

.tb1{
	clear: both;
	height: 60px;
	line-height:60px;
	overflow: hidden;
	width: 90%;
	text-align:right;
	margin-left: auto;
	margin-right: auto;
	margin-top:10px !important;
	margin-bottom:20px !important;
	border-top:1px solid #f2f2f2;
	

}

.form-control{
	border-radius: 0px;
}

.form-horizontal{
	min-height: 200px;

}

</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button class="navbar-toggle collapsed" type="button"
					data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand hidden-sm" href="javascript:;"> <img
					src="static/img/logo.png" />
				</a>
			</div>
			<div class="navbar-collapse collapse" role="navigation">
				<ul class="nav navbar-nav">
					<li class="hidden-sm hidden-md"><a href="http://wanjianginfo.com/">返回</a></li>
					
				</ul>
			</div>
		</div>
	</div>
	<div class="jumbotron masthead">
		<!-- News Sticker -->
		<div class="bgimage enabled-news-ticker"
			style="background: url(//1.s60i.faiusr.com/2/1133/AO0ICAIQsL61wQUYn82z7AUggA8o9AM.jpg) no-repeat 50% 50%;">
			<div class="container" style="padding: 30px;">

					<div class="login-form">
						<div class="tb">
							<p style="text-align: center;line-height: 60px;padding:0px;margin:0px;">注册账号</p>
						</div>
						<form class="form-horizontal">
						<input type="hidden" name="loginMethod" value="1" />
							<div class="form-group">
								<label for="inputEmail3" class="col-sm-3 control-label">手机号</label>
								<div class="col-sm-9">
									<input name="account" type="text" placeholder="请输入中国大陆手机号"
									class="form-control" tabIndex="1" autofocus="autofocus">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword3" class="col-sm-3 control-label">验证码</label>
								<div class="col-sm-5">
									<input class="form-control" type="text" placeholder="验证码:"
									name="validateCode" value="" tabIndex="2">
								</div>
								<div class="col-sm-4">
								<a id="kanbuq" href="javascript:;" style="color: #fff"
									onclick="changeVerfiyCode('v_img');"><img
									src="customer/verify_pic" id="v_img"></a>
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword3" class="col-sm-3 control-label">手机验证码</label>
								<div class="col-sm-5">
									<input class="form-control" type="text" placeholder="手机验证码"
									name="code" value="" tabIndex="3">
								</div>
								<div class="col-sm-4">
								<button id="yzm" onClick="chaxun(this);"
									class="btn btn-default radius" type="button">获取验证码</button>
								</div>
							</div>
							<div class="form-group">
								<label for="inputEmail3" class="col-sm-3 control-label">激活码</label>
								<div class="col-sm-9">
									<input name="partnerCode" type="text" placeholder="请输入激活码"
									class="form-control" tabIndex="4" autofocus="autofocus">
								</div>
							</div>
							<div class="form-group">
								<label for="inputEmail3" class="col-sm-3 control-label">设置密码</label>
								<div class="col-sm-9">
									<input name="password" type="password" placeholder="请输入登录密码"
									class="form-control" tabIndex="5" autofocus="autofocus">
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<a href="javascript:;" onclick="openLx();">查看风险揭示书</a>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button onClick="doReg(this);"
									class="btn btn-primary radius btn-block" type="button">注册</button>
								</div>
							</div>
						</form>
						
						
						<div class="tb1">
							<a href="index">登录</a>
						</div>
						
					</div>
				
			</div>
		</div>
	</div>
	<div class="contanter">
	</div>

	<footer class="footer">
		<div class="container">
			<div class="row footer-top">
				<div class="col-sm-6 col-lg-6">
					
				</div>
				
			</div>
			
			<div class="row footer-bottom" >
				<p class="text-center" style="color:white">
				©2018 安徽万疆信息咨询服务 版权所有
				</p>
				<p class="text-center" style="color:white">
				皖ICP备18003762号
				</p>
      </div> 
		</div>
	</footer>

	<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
	<script type="text/javascript" src="static/bootcss/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="static/h-ui/js/common.js?ver=1.0.03"></script>

	<script type="text/javascript">

		var _time = 121;
		var scrollTimer;
		var _verify_btn;

		function openLx() {
			layer.open({
				type : 2,
				maxmin : false,
				shadeClose : true,
				title : "风险揭示书",
				area : [ '60%', '80%' ],
				/* content : [ 'agreement/' + path + '?userid=${user.userId}',
						'yes' ] */
				content:['http://dl.wanjianginfo.com/reg/fxjs.pdf','yes']
			})
		}
		
		
		function parentLogin() {
			gohref('customer/index');
		}

		$(function() {
			var f = $("form");
			$(".tb>div").each(function(i, item) {
				$(this).click(function() {

					$(".tb>div").removeClass('selected');
					$(this).addClass('selected');

					$('form').each(function(j) {
						if (i == j) {
							$(this).show();
						} else {
							$(this).hide();
						}
					})
				})
			})
			
			openLx();
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

			if ($(obj).closest('form').find('[name="account"]').val() == '') {
				alert('手机号不能为空');
				$(obj).closest('form').find('[name="account"]').focus();
				return false;
			}

			if ($(obj).closest('form').find('[name="validateCode"]').val() == '') {
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