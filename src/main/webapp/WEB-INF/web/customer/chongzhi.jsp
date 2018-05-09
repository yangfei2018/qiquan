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
			<jsp:param name="curr" value="充值" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20 pad">
				<c:choose>
					<c:when test="${empty error }">
						<c:choose>
							<c:when test="${empty partnerBankInfo }">
								<p class="f-20 text-success">系统错误，请联系您的代理商。</p>
							</c:when>
							<c:when test="${!empty user.agreementTime }">
								<h1>请汇款至以下账户</h1>
								<p>
									<span class="lb">公司名称：</span> ${partnerBankInfo.companyName }
								</p>
								<p>
									<span class="lb">开户银行：</span> ${partnerBankInfo.bankName }
								</p>
								<p>
									<span class="lb">银行账户：</span> <span
										style="color: #f37b1d; font-weight: 800">${partnerBankInfo.bankNo }</span>
								</p>
								<p>
									查看 <a onclick="openL('有限合伙企业合伙协议')" href="javascript:;">《有限合伙企业合伙协议》</a>和<a
										onclick="openL('合伙协议补充协议')" href="javascript:;">《合伙协议补充协议》</a>
								</p>

								<br />
								<br />
								<br />

								<div class="row cl" style="font-weight: 800">温馨提示：</div>
								<div class="row cl">1、为了及时到账，建议在股市交易时间入金。</div>
								<div class="row cl">2、如果遇到其它入金问题，请联系客服。</div>
							</c:when>
							<c:otherwise>
								<h1 style="text-align: center;font-size:50px;">
									请仔细阅读 <a onclick="openLx('有限合伙企业合伙协议')" href="javascript:;">《有限合伙企业合伙协议》</a>和<a
										onclick="openLx('合伙协议补充协议')" href="javascript:;">《合伙协议补充协议》</a>。
								</h1>
							<br/><br/><br/>

								<p style="text-align: center;">
									<button onClick="agreement();"
										class="btn btn-secondary size-XL radius" type="button">我已完整阅读了协议，并同意。
									</button>
								</p>

							</c:otherwise>
						</c:choose>
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

		function openL(path) {
			layer.open({
				type : 2,
				maxmin : false,
				shadeClose : true,
				title : "协议",
				area : [ '100%', '100%' ],
				content:['http://dl.wanjianginfo.com/signed/${user.userId}_'+path+'_signed.pdf','yes']
			})
		}
		
		function openLx(path) {
			layer.open({
				type : 2,
				maxmin : false,
				shadeClose : true,
				title : "协议",
				area : [ '100%', '100%' ],
				/* content : [ 'agreement/' + path + '?userid=${user.userId}',
						'yes' ] */
				content:['http://dl.wanjianginfo.com/signed/${user.userId}_'+path+'.pdf','yes']
			})
		}

		function agreement() {
			$.ajax({
				url : 'customer/json/agreement',
				type : 'post',
				dataType : "json",
				data : {},
				success : function(res) {
					gohref('customer/chongzhi');
				}
			});
		}
	</script>

</body>
</html>