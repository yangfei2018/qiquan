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
	width: 95%;
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
	padding-left: 20px;
	padding-right: 20px;
	overflow: hidden;
}


form {
	width: 100%;
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

.radio-box{
text-align: left;
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
			<input type="hidden" name="mobile" value="${user.mobile }" /> <input
				type="hidden" name="id" value="${id }" />
			<p>为了确保操作为本人，请发送短信验证码至手机号 <span style="color:orange">${mobile }</span></p>
			
			<div class="row cl">
				<label class="form-label col-xs-3 col-sm-3"><span
					class="c-red">*</span>验证码：</label>
				<div class="formControls col-xs-9 col-sm-9">
					<input class="input-text size-L" type="text" placeholder="验证码:"
					name="validateCode" value="" style="width: 150px;" tabIndex="3">
				<a id="kanbuq" href="javascript:;" style="color: #fff"
					onclick="changeVerfiyCode('v_img');"><img
					src="customer/verify_pic" id="v_img"></a>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-3 col-sm-3"><span
					class="c-red">*</span>短信验证码：</label>
				<div class="formControls col-xs-9 col-sm-9">
					<input class="input-text size-L" type="text" placeholder="手机短信验证码"
					name="code" value="" style="width: 150px;" tabIndex="3">
				<button id="yzm" onClick="chaxun(this);"
					class="btn btn-secondary radius" type="button">获取验证码</button>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-3 col-sm-3"><span
					class="c-red">*</span>平仓方式：</label>
				<div class="formControls col-xs-9 col-sm-9">
				<c:if test="${item.categoryId!=1 }">
				<div class="radio-box">
				<input type="radio" id="pingcangType-1" name="pingcangType" value="1" onclick="sqt(this)">
				<label for="pingcangType-1">市价交易</label>
			</div>
			</c:if>
			<div class="radio-box">
				<input type="radio" id="pingcangType-2" name="pingcangType"  value="2" onclick="sqt(this)">
				<label for="pingcangType-2">收盘价交易</label>
			</div>
			<c:if test="${item.categoryId!=1 }">
			<div class="radio-box">
				<input type="radio" id="pingcangType-3" name="pingcangType"  value="3" onclick="sqt(this)">
				<label for="pingcangType-3">挂价交易</label>
			</div>
			</c:if>
				</div>
			</div>
			<div class="row cl" id="xq" style="display: none">
				<label class="form-label col-xs-3 col-sm-3"><span
					class="c-red">*</span>行权价格：</label>
				<div class="formControls col-xs-9 col-sm-9">
					<input class="input-text size-L" type="text" placeholder="请输入行权价格"
					name="zhishu" value="" style="width: 150px;" tabIndex="3">
				</div>
			</div>
			<div class="row cl">
				<div class="col-xs-9 col-sm-9 col-xs-offset-4 col-sm-offset-2" style="margin-top:10px">

					<button onClick="dopingcang(this);"
					class="btn btn-success size-L radius" type="button">确认平仓</button>

				</div>
			</div>
			<!-- <p>
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
			<div class="radio-box">
				<input type="radio" id="pingcangType-1" name="pingcangType" value="1">
				<label for="pingcangType-1">市价</label>
			</div>
			<div class="radio-box">
				<input type="radio" id="pingcangType-2" name="pingcangType"  value="2">
				<label for="pingcangType-2">收盘价</label>
			</div>
			<div class="radio-box">
				<input type="radio" id="pingcangType-3" name="pingcangType"  value="3">
				<label for="pingcangType-3">行权报价</label>
			</div>
			</p>
			<p>
				<input class="input-text size-L" type="text" placeholder="请输入行权报价"
					name="zhishu" value="" style="width: 150px;" tabIndex="3">
			</p>
			<p>
			<div class="col-xs-5">
				<button onClick="dopingcang(this);"
					class="btn btn-success size-L radius" type="button">确认平仓</button>
			</div>
			</p>-->
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
		function timer(old_text) {
			console.log("time");
			_verify_btn.attr('disabled', true)
			scrollTimer = setInterval(function() {
				scroll_time(old_text)
			}, 1000);
		}

		
		function sqt(obj){
			if ($(obj).val()==1 || $(obj).val()==2){
				$("#xq").hide();
				$("#xq").find("input").val('');
			}else{
				$("#xq").show();
			}
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

		function parentRedirect() {
			parent.gohref('customer/jiaoyilist?status=2');
		}

		function dopingcang(obj) {
			if ($('input[name="code"]').val() == '') {
				layer_alert('请填写手机短信验证码', function() {
					$('input[name="code"]').focus();
				});
				return false;
			}

			$(obj).attr('disabled', true);
			$.ajax({
				url : 'customer/json/doPingCang',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					$(obj).attr('disabled', false);
					if (res.result) {
						layer_success(res.data, function() {
							parentRedirect();
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
						$(obj).text('获取验证码')
					}
				}
			});

		}
	</script>

</body>
</html>