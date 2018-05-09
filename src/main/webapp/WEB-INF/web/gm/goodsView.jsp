<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
						<form class="form-horizontal">
							<input type="hidden" name="id" value="${goods.id }" />
							<div class="form-group">
								<label class="col-lg-2 control-label">商品分类</label>
								<div class="col-lg-10">
									<select class="form-control m-b" name="categoryId">
										<c:forEach items="${goodsCategory }" var="category">
											<option value="${category.id }"
												<c:if test="${category.id==goods.categoryId }">selected="selected"</c:if>>${category.name }</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">商品编号</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="code"
										placeholder="请填写商品编号" value="${goods.code }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">商品名称</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="name"
										placeholder="请填写商品名称" value="${goods.name }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">每手单位</label>
								<div class="col-lg-7">
									<input type="text" class="form-control" name="unit"
										placeholder="请填写每手单位" value="${goods.unit }" />
								</div>
								<div class="col-lg-3">
									<input type="text" class="form-control" name="danwei"
										placeholder="计量单位" value="${goods.danwei }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">15天费率</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="feilv_15"
										placeholder="请填写15天费率，2-100" value="${goods.feilv_15 }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">30天费率</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="feilv_30"
										placeholder="请填写30天费率，2-100"  value="${goods.feilv_30 }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">15天费率到期日</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="feilv_15_time"
										placeholder="请填写15天费率到期日，20180920" value="${goods.feilv_15_time }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">30天费率到期日</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="feilv_30_time"
										placeholder="请填写30天费率到期日，20180920" value="${goods.feilv_30_time }" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label">起投手数</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" name="min_shou"
										placeholder="起投手数" value="${goods.min_shou }" />
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-2">
									<button class="btn btn-white" type="button"
										onclick="cancelEdit()">取消</button>
									<button class="btn btn-primary" type="button"
										onclick="save(this)">保存</button>
								</div>
							</div>
						</form>
					</div>
				</div>
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
		function cancelEdit() {
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}

		function save(obj) {
			if ($(obj).closest('form').find('[name="code"]').val() == '') {
				swal("商品编码不能为空", "", "error")
				
				return false;
			}

			if ($(obj).closest('form').find('[name="name"]').val() == '') {
				swal("商品名称不能为空", "", "error")
				$(obj).closest('form').find('[name="name"]').focus();
				return false;
			}

			if ($(obj).closest('form').find('[name="feilv_15"]').val() == '') {
				swal("商品15天费率不能为空", "", "error")
				$(obj).closest('form').find('[name="feilv_15"]').focus();
				return false;
			}

			if ($(obj).closest('form').find('[name="feilv_30"]').val() == '') {
				swal("商品30天费率不能为空", "", "error")
				$(obj).closest('form').find('[name="feilv_30"]').focus();
				return false;
			}

			$.ajax({
				url : 'gm/json/updateGoods',
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
	</script>
</body>

</html>