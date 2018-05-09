<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
<base href="<%=request.getContextPath()%>/" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>管理后台 | 登录</title>

<link href="static/inspinia/css/bootstrap.min.css" rel="stylesheet">
<link href="static/inspinia/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="static/inspinia/css/plugins/toastr/toastr.min.css"
	rel="stylesheet">
<link href="static/inspinia/css/plugins/sweetalert/sweetalert.css"
	rel="stylesheet">
<link href="static/inspinia/js/plugins/gritter/jquery.gritter.css"
	rel="stylesheet">
<link href="static/inspinia/css/animate.css" rel="stylesheet">
<link href="static/inspinia/css/style.css" rel="stylesheet">
<style>
.pd {
	overflow: hidden;
	padding: 10px;
}
</style>
</head>

<body style="background: white;">
	<div id="wrapper">
		<div class="row">
			<div class="col-sm-6 b-r">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
						<form class="form-horizontal">
							<input type="hidden" name="userId" value="${partner.userId }" />
							<c:if test="${create==1}">
							<div class="form-group">
								<label class="col-lg-2 control-label">代理商登录账号</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="name" placeholder="请设置代理商登录账号（手机号/邮箱）"  />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">代理商登录密码</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="password" placeholder="请设置代理商登录密码，建议大于4个字符。" />
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							</c:if>
							<div class="form-group">
								<label class="col-lg-2 control-label">代理商编号</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.partnerNo }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="partnerNo"
												value="${partner.partnerNo }" />
										</c:when>
									</c:choose>

								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">代理商名称</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.companyName }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="companyName"
												value="${partner.companyName }" />
										</c:when>
									</c:choose>

								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">法人名称</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.realName }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="realName"
												value="${partner.realName }" />
										</c:when>
									</c:choose>

								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">联系电话</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.mobile }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="mobile"
												value="${partner.mobile }" />
										</c:when>
									</c:choose>

								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">银行开户行</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.bankOfDeposit }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="bankOfDeposit"
												placeholder="请输入银行账户" value="${partner.bankOfDeposit }" />
										</c:when>
									</c:choose>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">银行账户</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.bankCardNo }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="bankCardNo"
												placeholder="请输入银行账户" value="${partner.bankCardNo }" />
										</c:when>
									</c:choose>
								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<label class="col-lg-2 control-label">客户激活码</label>
								<div class="col-lg-10">
									<c:choose>
										<c:when test="${edit==0 }">
											<p class="form-control-static">${partner.code }</p>
										</c:when>
										<c:when test="${edit==1 }">
											<input type="text" class="form-control" name="code"
												value="${partner.code }" />
										</c:when>
									</c:choose>

								</div>
							</div>
							<!-- <div class="hr-line-dashed"></div> -->
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-2">
									<c:choose>
										<c:when test="${edit==0 }">
											<button class="btn btn-primary" type="button"
												onclick="edit()">修改</button>
										</c:when>
										<c:when test="${edit==1 }">
											<button class="btn btn-white" type="button"
												onclick="cancelEdit()">取消</button>
											<button class="btn btn-primary" type="button"
												onclick="save(this)">保存</button>
										</c:when>
									</c:choose>

								</div>
							</div>
							<input type="hidden" name="businessLicencePic"
								value="${partner.businessLicencePic }" /> <input type="hidden"
								name="idCardFrontPic" value="${partner.idCardFrontPic }" /> <input
								type="hidden" name="idCardBackgroundPic"
								value="${partner.idCardBackgroundPic }" />
						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<c:choose>
					<c:when test="${edit==0 }">
						<div class="pd">
							<h4>营业执照</h4>
							<p class="text-center">
								<a href="javascript:;"> <c:choose>
										<c:when test="${empty partner.businessLicencePic }">
											<img class="thumbnail" src="static/inspinia/img/empty.png"
												alt="营业执照">

										</c:when>
										<c:otherwise>
											<img class="thumbnail" src="${partner.businessLicencePic }"
												alt="营业执照">
										</c:otherwise>
									</c:choose>
								</a>
							</p>
							<h4>法人身份证（正）</h4>
							<p class="text-center">
								<a href="javascript:;"> <c:choose>
										<c:when test="${empty partner.idCardFrontPic }">
											<img class="thumbnail" src="static/inspinia/img/empty.png"
												alt="">

										</c:when>
										<c:otherwise>
											<img class="thumbnail" src="${partner.idCardFrontPic }"
												alt="法人身份证（正）">
										</c:otherwise>
									</c:choose>

								</a>
							</p>
							<h4>法人身份证（反）</h4>
							<p class="text-center">
								<a href="javascript:;"> <c:choose>
										<c:when test="${empty partner.idCardBackgroundPic }">
											<img class="thumbnail" src="static/inspinia/img/empty.png"
												alt="">
										</c:when>
										<c:otherwise>
											<img class="thumbnail" src="${partner.idCardBackgroundPic }"
												alt="法人身份证（反）">
										</c:otherwise>
									</c:choose>
								</a>
							</p>
						</div>
					</c:when>
					<c:when test="${edit==1 }">
						<h4>营业执照</h4>
						<p class="text-center">
						<div class="uploader-list-container" id="pic1Container">
							<img src="static/h-ui/images/upload.png" class="img-thumbnail"
								onclick="$('#pic1').click()" />
						</div>
						<input type="file" style="display: none" id="pic1"
							accept="image/*" data-target="pic1Container"
							data-targetinput="businessLicencePic" />
						</p>

						<h4>法人身份证（正）</h4>
						<p class="text-center">
						<div class="uploader-list-container" id="pic2Container">
							<img src="static/h-ui/images/upload.png" class="img-thumbnail"
								onclick="$('#pic2').click()" />
						</div>
						<input type="file" style="display: none" id="pic2"
							accept="image/*" data-target="pic2Container"
							data-targetinput="idCardFrontPic" />
						</p>

						<h4>法人身份证（反）</h4>
						<p class="text-center">
						<div class="uploader-list-container" id="pic3Container">
							<img src="static/h-ui/images/upload.png" class="img-thumbnail"
								onclick="$('#pic3').click()" />
						</div>
						<input type="file" style="display: none" id="pic3"
							accept="image/*" data-target="pic3Container"
							data-targetinput="idCardBackgroundPic" />
						</p>

					</c:when>
				</c:choose>


			</div>
		</div>
	</div>
	<!-- Mainly scripts -->
	<script src="static/inspinia/js/jquery-3.1.1.min.js"></script>
	<script src="static/inspinia/js/bootstrap.min.js"></script>
	<script src="static/inspinia/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script
		src="static/inspinia/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="static/inspinia/js/inspinia.js"></script>
	<script src="static/inspinia/js/plugins/pace/pace.min.js"></script>
	<script type="text/javascript" src="static/h-ui/js/common.js"></script>
	<!-- Sweet alert -->
	<script src="static/inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>


	<script>
		<c:if test="${edit==1 }">
		$(function() {

			if (window.File && window.FileList && window.FileReader
					&& window.Blob) {
				pic1.addEventListener("change", getFiles, false);
				pic2.addEventListener("change", getFiles, false);
				pic3.addEventListener("change", getFiles, false);
			}
		})

		</c:if>

		function edit() {
			window.location.href = window.location.href + "?edit=1";
		}

		function cancelEdit() {
			gohref('gm/partnerView/${partner.partnerNo}');
		}

		function save(obj) {
			if ($(obj).closest('form').find('[name="account"]').val() == '') {
				alert('手机号不能为空');
				$(obj).closest('form').find('[name="account"]').focus();
				return false;
			}

			if ($(obj).closest('form').find('[name="loginMethod"]').val() == 1) {

				if ($(obj).closest('form').find('[name="smsCode"]').val() == '') {
					alert('短信验证码不能为空');
					$(obj).closest('form').find('[name="smsCode"]').focus();
					return false;
				}
			} else {
				if ($(obj).closest('form').find('[name="password"]').val() == '') {
					alert('登录密码不能为空');
					$(obj).closest('form').find('[name="password"]').focus();
					return false;
				}
			}

			$.ajax({
				url : 'gm/json/updatePartner',
				type : 'post',
				dataType : "json",
				data : $(obj).closest("form").serialize(),
				success : function(res) {
					console.log(res);
					if (res.hasOwnProperty('result') && !res.result) {
						swal(res.data, "", "error")
					} else {
						swal({
							title : "更新成功",
							type : "success"
						},
								function() {
									var index = parent.layer
											.getFrameIndex(window.name);
									parent.reload_page();
									parent.layer.close(index);
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
			var inputTar = $(this).attr("data-targetinput");
			// 验证是否是图片文件的正则
			// reg = /image\/.*/i;
			// console.log(files);
			if (files.length == 0) {
				return;
			}

			var f = files[0];
			uploadFun(f, target, inputTar);
		}

		// 开始上传照片
		function uploadFun(singleImg, target, inputTar) {
			console.log('inputTar:' + inputTar);
			// var singleImg = uploadImgArr[0];
			var xhr = new XMLHttpRequest();
			if (xhr.upload) {
				xhr.onreadystatechange = function(e) {
					if (xhr.readyState == 4) {
						if (xhr.status == 200
								&& eval("(" + xhr.responseText + ")").state == "SUCCESS") {
							var url = eval("(" + xhr.responseText + ")").url;
							$("#" + target).find("img").attr("src", url);
							$("#" + inputTar).find("input").val(url);
						} else {
							alert(eval("(" + xhr.responseText + ")").state);
						}
					}
				};

				var formdata = new FormData();
				formdata.append("upfile", singleImg);
				formdata.append("method", target);
				// 开始上传
				xhr.open("POST", "upload/uploadFile", true);
				xhr.send(formdata);
				var startDate = new Date().getTime();
			}

		}
	</script>
</body>

</html>