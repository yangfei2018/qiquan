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

.form-label {
	font-size: 16px;
	color: black;
}

.select {
	font-size: 16px;
	height: 25px;
}

.form-label {
	text-align: left;
}

div.form-label {
	text-align: left !important;
}

.Hui-article{
	padding: 10px;
	overflow: hidden;
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
			<jsp:param name="curr" value="账户" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<c:choose>
				<c:when test="${user.status==1 }">
					<div class="Huialert Huialert-error">
						当前账号未实名认证，部分功能无法使用，请尽快<a href="customer/certification">认证</a>。
					</div>
				</c:when>
				<c:when test="${user.status==2 }">
					<div class="Huialert Huialert-info">
						您的实名认证信息已提交，我们将尽快审核，请耐心等待。</a>。
					</div>
				</c:when>
				<c:when test="${user.status==3 }">
					<div class="Huialert Huialert-success">
						您的账号已通过实名认证。
					</div>
				</c:when>
			</c:choose>
			<article class="cl pd-20 pad">
				<!-- <p class="f-20 text-success">用户信息</p> -->
				<form class="form form-horizontal size-L" id="form-article-add">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">账号：</label>
						<div class="formControls form-label col-xs-8 col-sm-9">
							${user.name }</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">姓名：</label>
						<div class="formControls form-label col-xs-8 col-sm-9">
							${user.realName }</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">身份证号：</label>
						<div class="form-label col-xs-8 col-sm-9">
							${user.idCardFormat }</div>
					</div>
					<!-- <p class="f-20 text-success" style="margin-top: 20px;">余额</p> -->
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">账号余额：</label>
						<div class="form-label col-xs-8 col-sm-9">
							￥${user.amountFormat }
							
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">银行卡信息：</label>
						<div class="form-label col-xs-8 col-sm-9">
							${user.bankOfDeposit } ${user.bankCardNoFormat }</div>
					</div>
					<div class="row cl">
					<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
						<button onClick="gohref('customer/chongzhi');" class="btn btn-primary radius"
								type="button">充值</button>
							<button onClick="gohref('customer/withdrawCash');" class="btn btn-primary radius"
								type="button">提现</button>
						</div>		
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

	<script type="text/javascript">
		$(function() {

		})

		function xunjia(obj) {

			if ($('input[name="productCode"]').val() == '') {
				layer_alert('请填写产品代码', function() {
					$('input[name="productCode"]').focus();
				});
				return false;
			}

			if ($('input:radio[name="buyAndFall"]:checked').val() == null) {
				layer_alert('请选择方向');
				return false;
			}

			if (!amountReg.test($('input[name="amount"]').val())) {
				layer_alert('请正确填写权利金', function() {
					$('input[name="amount"]').focus();
				});
				return false;
			}

			if (!$('#xieyi').is(':checked')) {
				layer_alert('请先阅读并同意《大赢家产品交易规则》和《大赢家风险揭示书》。');
				return false;
			}

			$.ajax({
				type : "post",
				url : "customer/json/postTrade",
				data : $(this).closest("form").serialize(),
				dataType : "json",
				success : function(rep) {
					if (rep.result) {
						layer_success(rep.data, function() {

						});
					} else {
						layer_alert(rep.data);
					}
				}
			});

			//layer_alert('为了保证询价的可靠性，请确保您账户余额不低于2万');
		}

		function loadProduct(categoryId) {
			$.ajax({
				type : "get",
				url : "customer/json/listGoodsByCategoryId",
				data : {
					categoryId : categoryId
				},
				dataType : "json",
				success : function(json) {
					console.log(json);
					var _childOptions = $("select[name='productName']");
					_childOptions.html("");//清空字了列表  
					for (var i = 0; i < json.length; i++) {

						_childOptions
								.append("<option value='"+json[i].name+"'>"
										+ json[i].name + "</option>");
					}
				}
			});
		}

		function getFiles(e) {
			e = e || window.event;
			// 获取file input中的图片信息列表
			var files = e.target.files;
			var target = $(this).attr("data-target");

			// 验证是否是图片文件的正则
			// reg = /image\/.*/i;
			// console.log(files);
			if (files.length == 0) {
				return;
			}

			var f = files[0];
			uploadFun(f, target);
		}

		// 开始上传照片
		function uploadFun(singleImg, target) {
			// var singleImg = uploadImgArr[0];
			var xhr = new XMLHttpRequest();
			if (xhr.upload) {
				xhr.onreadystatechange = function(e) {
					if (xhr.readyState == 4) {
						if (xhr.status == 200
								&& eval("(" + xhr.responseText + ")").state == "SUCCESS") {
							var url = eval("(" + xhr.responseText + ")").url;
							$("#" + target).find("img").attr("src", url);
							$("#" + target).find("input").val(url);
						} else {
							alert(eval("(" + xhr.responseText + ")").state);
						}
					}
				};

				var formdata = new FormData();
				formdata.append("upfile", singleImg);
				formdata.append("method", target);
				// 开始上传
				xhr.open("POST", "third/upload_file", true);
				xhr.send(formdata);
				var startDate = new Date().getTime();
			}

		}
	</script>

</body>
</html>