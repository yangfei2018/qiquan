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


form{
	margin-left:auto;
	margin-right:auto;
	width:560px;
}

.form-label {
	font-size: 16px;
	color: black;
}

.select {
	font-size: 16px;
	height: 25px;
}


.form-label,.c{
	text-align: left;
	height:40px;
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
			<jsp:param name="curr" value="充值" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20 pad">
				<c:choose>
				<c:when test="${empty error }">
					<form class="form form-horizontal size-L" id="form-add" action="customer/chongzhi" method="POST">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-4">转入金额：</label>
						<div class="formControls col-xs-5 col-sm-5">
							<input type="text" class="input-text size-L" value=""
								placeholder="请输入金额" name="amount"> 
						</div>
						<div class="formControls col-xs-2 col-sm-3 c">
							元(>=100元)
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-4">银行卡开户名：</label>
						<div class="formControls col-xs-5 col-sm-5">
							<input type="text" class="input-text size-L" value=""
								placeholder="请输入银行卡开户名" name="bankcardname"> 
						</div>
						<div class="formControls col-xs-3 col-sm-3 c">
							（请填写于银行卡一致的姓名）
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-4">转入时间：</label>
						<div class="formControls col-xs-5 col-sm-5">
							<input type="text" class="input-text size-L" value="${createTime }"
								placeholder="" name="createTime" > 
						</div>
					</div>
					<div class="row cl">
						<div class="check-box size-L">
							<input type="checkbox" id="xieyi"> <label
								for="checkbox-moban">&nbsp;</label>
						</div>
						我已阅读并同意<a onclick="openL('yxhh')" href="javascript:;" >《有限合伙企业合伙协议》</a>和<a onclick="openL('hhxybc')" href="javascript:;">《合伙协议补充协议》</a>。 
					</div>
					
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-4"></label>
						<div class="formControls col-xs-5 col-sm-5">
							<button onClick="chongzhi(this);" class="btn btn-secondary size-XL radius"
								type="button">申请入金
							</button>
						</div>
					</div>
					
					<div class="row cl" style="font-weight:800">
						温馨提示：
					</div>
					<div class="row cl" >
						1、转账入金，请在提交申请后15分钟内完成入金。
					</div>
					<div class="row cl" >
						2、为了及时到账，建议在股市交易时间入金。
					</div>
					<div class="row cl" >
						3、如果遇到其它入金问题，请联系客服。
					</div>
				</form>
				</c:when>
				<c:otherwise>
					<p class="f-20 text-success">${error }</p>
				</c:otherwise>
				</c:choose>
				
				
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
		var amountReg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
	
		function openL(path){
			layer.open({
				type : 2,
				maxmin : false,
				shadeClose : true,
				title : "协议",
				area : [ '100%', '100%' ],
				content : [ 'agreement/'+path+'?userid=${user.userId}', 'yes' ]
			})
		}
		
		function chongzhi(obj){
			if (!amountReg.test($('input[name="amount"]').val())) {
				layer_alert('请正确填写转入金额', function() {
					$('input[name="amount"]').focus();
				});
				return false;
			}
			
			if (parseFloat($('input[name="amount"]').val())<100){
				layer_alert('转入金额必须大于100元', function() {
					$('input[name="amount"]').focus();
				});
				return false;
			}

			if ($('input[name="bankcardname"]').val()==''){
				layer_alert('请填写银行卡开户名', function() {
					$('input[name="bankcardname"]').focus();
				});
				return false;
			}
			//
			
			if (!$('#xieyi').is(':checked')) {
				layer_alert('请先阅读并同意《有限合伙企业合伙协议》和《合伙协议补充协议》。');
				return false;
			}
			
			$(obj).closest("form").submit();
			
		}
		
	</script>

</body>
</html>