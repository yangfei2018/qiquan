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
	max-width: 650px;
	width: 80%;
	margin-left: auto;
	margin-right: auto;
	font-size: 16px;
}

form {
	margin-left: auto;
	margin-right: auto;
	width: 560px;
}

.form-label {
	font-size: 16px;
	color: black;
}

.select {
	font-size: 16px;
	height: 25px;
}

.form-label, .c {
	text-align: left;
	height: 40px;
	line-height: 40px;
}
</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>

	<jsp:include page="../include/nav-top.jsp" />

	<jsp:include page="../include/nav-left.jsp" />

	<section class="Hui-article-box">
		<jsp:include page="../include/nav-breadcrumb.jsp">
			<jsp:param name="curr" value="提现" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20 pad">
				<form class="form">
					<h1>提现到银行卡</h1>
					<p>
						<span class="lb">当前可账户余额：</span> ${user.amountFormat } 元
					</p>
					<p>
						<span class="lb">提现到银行信息：</span> ${user.bankOfDeposit }
						${user.bankCardNoFormat }
					</p>

					<div style="border-top:1px solid #E8E8E8;margin-top:20px;padding-top:20px;" >
					<input type="hidden" name="mobile" value="${user.mobile }" /> 
					<p>为了确保操作为本人，请发送短信验证码至手机号 ${mobile }</p>
					<p>
						<input class="input-text size-L" type="text" placeholder="验证码:"
							name="validateCode" value="" style="width: 150px;" tabIndex="3">
						<a id="kanbuq" href="javascript:;" style="color: #fff"
							onclick="changeVerfiyCode('v_img');"><img
							src="customer/verify_pic" id="v_img"></a>
					</p>
					<p>
						<input class="input-text size-L" type="text" placeholder="手机短信验证码"
							name="code" value="" style="width: 150px;" tabIndex="3">
						<button id="yzm" onClick="chaxun(this);"
							class="btn btn-secondary radius" type="button">获取验证码</button>
					</p>

					<p>
						<button class="btn btn-primary radius size-XL <c:if test="${user.amount<=0 }">disabled</c:if>" onclick="doWithDrawCash(this)"
							type="button" <c:if test="${user.amount<=0 }"> disabled="disabled" </c:if> >全部提现</button>
					</p>
					</div>

				</form>
			</article>
			<jsp:include page="../include/footer.jsp" />
		</div>
	</section>


	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript"
		src="static/h-ui.admin/js/H-ui.admin.page.js"></script>
	<script type="text/javascript" src="lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>
	<script type="text/javascript" src="static/h-ui/js/jquery.media.js"></script>
	<script type="text/javascript">
		var _time = 121;
		var scrollTimer;
		var _verify_btn;
		function timer(old_text) {
			console.log("time");
			_verify_btn.attr('disabled', true)
			scrollTimer = setInterval(function() {
				scroll_time(old_text)
			}, 1000);
		}
		
		function parentRedirect(){
			gohref('customer/withdrawcashlist');
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

		function chaxun(obj) {
			if ($('input[name="validateCode"]').val() == '') {
				layer_alert('验证码不能为空',function(){
					$('input[name="validateCode"]').focus();
				});
				
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
					// res = jQuery.parseJSON(res);
					$(obj).attr('disabled', false);
					// alert(res.result);
					if (res.result) {
						_time = 120;
						_verify_btn = $(obj);
						timer('获取验证码');
					} else {
						layer_alert(res.data,function(){
							$('input[name="validateCode"]').focus();
							$(obj).text('获取验证码')
						});
						
					}
				}
			});

		}
		
		
		function doWithDrawCash(obj) {
			if ($('input[name="code"]').val() == '') {
				layer_alert('请填写手机短信验证码',function(){
					$('input[name="code"]').focus();
				});
				return false;
			}
			
			$(obj).attr('disabled', true);
			$.ajax({
				url : 'customer/json/withdrawCash',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					$(obj).attr('disabled', false);
					if (res.result) {
						layer_success(res.data,function(){
							parentRedirect();
						});
					} else {
						layer_alert(res.data);
					}
				}
			});
		}
	</script>

</body>
</html>