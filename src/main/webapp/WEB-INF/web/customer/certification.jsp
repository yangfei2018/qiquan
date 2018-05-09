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
			<jsp:param name="curr" value="实名认证" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20 pad">
				<c:choose>
					<c:when test="${user.status==1 }">
						<c:if test="${!empty error }">
							<p class="f-20 text-success" style="color:red">认证被驳回，理由：${error }</p>
						</c:if>
						<p class="f-20 text-success">温馨提示：为了给您提供更好的服务，请您先完成实名认证</p>
						<form class="form form-horizontal size-L" id="form-article-add">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>姓名：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<input type="text" class="input-text size-L" value="${user.realName }"
										placeholder="请输入您的真实姓名" name="realName">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>身份证号：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<input type="text" class="input-text size-L" value="${user.idCard }"
										placeholder="请输入18位身份证号码" name="idCard">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>详细地址：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<input type="text" class="input-text size-L" value="${user.address }"
										placeholder="请填写您的详细地址" name="address">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>开户银行：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<span class="select-box size-L"> <select
										name="bankOfDeposit" class="select">
											<option value="">请选择您的开户银行</option>
											<c:forEach items="${banklist }" var="bank">
												<option value="${bank }"  <c:if test="${bank==selectBank}">selected="selected"</c:if>  >${bank }</option>
											</c:forEach>
									</select>
									</span>
								</div>
							</div>
							<div class="row cl" id="khyy" <c:if test="${!otherBank}">style="display: none"</c:if> >
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>开户银行：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<input type="text" class="input-text size-L" value="${otherBank?user.bankOfDeposit:''}"
										placeholder="请填写开户银行" name="bankOfDepositOther">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>银行卡号：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<input type="text" class="input-text size-L" value="${user.bankCardNo }"
										placeholder="请输入您的银行卡号" name="bankCardNo">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><span
									class="c-red">*</span>开户行所属地址：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<input type="text" class="input-text size-L" value="${user.bankAddress }"
										placeholder="请输入您的开户行所属地址" name="bankAddress">
								</div>
							</div>

							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">上传身份证正面：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<div class="uploader-list-container" id="pic1Container">
										<img src="${empty user.idCardFrontPic?'static/h-ui/images/upload.png':user.idCardFrontPic }" class="img-thumbnail"
											onclick="$('#pic1').click()" /> <input type="hidden"
											name="idCardFrontPic" value="${user.idCardFrontPic }" />
									</div>
								</div>
								<input type="file" style="display: none" id="pic1"
									accept="image/*" data-target="pic1Container" />
							</div>

							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">上传身份证反面：</label>
								<div class="formControls col-xs-8 col-sm-9">
									<div class="uploader-list-container" id="pic2Container">
										<img src="${empty user.idCardBackgroundPic?'static/h-ui/images/upload.png':user.idCardFrontPic }" class="img-thumbnail"
											onclick="$('#pic2').click()" /> <input type="hidden"
											name="idCardBackgroundPic" value="${user.idCardBackgroundPic }" />
									</div>
								</div>
								<input type="file" style="display: none" id="pic2"
									accept="image/*" data-target="pic2Container" />
							</div>


							<div class="row cl">
								<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-2">

									<button onClick="submitData(this);"
										class="btn btn-secondary size-XL radius" type="button">
										<i class="Hui-iconfont">&#xe632;</i> 提交
									</button>

								</div>
							</div>
						</form>
					</c:when>
					<c:when test="${user.status==2 }">
						<p class="f-20 text-success">您的实名认证信息已提交，我们将尽快审核，请耐心等待。</p>
					</c:when>
					<c:otherwise>
						<p class="f-20 text-success">您已完成实名认证，无须重复认证！</p>
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

	<script type="text/javascript">
		<c:if test="${user.status==1 }">
		$(function() {

			if (window.File && window.FileList && window.FileReader
					&& window.Blob) {
				pic1.addEventListener("change", getFiles, false);
				pic2.addEventListener("change", getFiles, false);
			}
		})
		
		</c:if>
		
		$(function(){
			$("select[name='bankOfDeposit']").change(function(){
				if ($(this).val()=='其他'){
					$("#khyy").show();
				}else{
					$("#khyy").hide();
				}
			})
		})
		
		function submitData(obj) {

			if ($('input[name="realName"]').val() == '') {
				alert('姓名不能为空');
				$('input[name="realName"]').focus();
				return false;
			}
			console.log('身份证长度：'+$('input[name="idCard"]').val().length);
			if ($('input[name="idCard"]').val() == ''
					|| $('input[name="idCard"]').val().length != 18) {
				alert('身份证格式不正确');
				$('input[name="idCard"]').focus();
				return false;
			}

			if ($('input[name="address"]').val() == '') {
				alert('详细地址不能为空');
				$('input[name="address"]').focus();
				return false;
			}

			if ($('select[name="bankOfDeposit"]').val() == '') {
				alert('请选择开户银行');
				$('select[name="bankOfDeposit"]').focus();
				return false;
			}

			if ($('input[name="bankCardNo"]').val() == '') {
				alert('银行卡号不能为空');
				$('input[name="bankCardNo"]').focus();
				return false;
			}

			if ($('input[name="bankAddress"]').val() == '') {
				alert('开户行所属地址不能为空');
				$('input[name="bankAddress"]').focus();
				return false;
			}

			if ($('input[name="idCardFrontPic"]').val() == '') {
				alert('请上传身份证正面图片');

				return false;
			}

			if ($('input[name="idCardBackgroundPic"]').val() == '') {
				alert('请上传身份证背面图片');
				$('input[name="idCardBackgroundPic"]').focus();
				return false;
			}

			$.ajax({
				url : 'customer/json/certification',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					if (!res.result){
						layer_alert(res.data);
					}else{
						layer_success("您的认证信息已提交，我们将尽快为您审核，请耐心等待。",function(){
							window.location.href = window.location.href;
						});
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
				xhr.open("POST", "third/uploadFile", true);
				xhr.send(formdata);
				var startDate = new Date().getTime();
			}

		}
	</script>

</body>
</html>