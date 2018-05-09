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
<link href="static/inspinia/js/plugins/gritter/jquery.gritter.css"
	rel="stylesheet">
<link href="static/inspinia/css/animate.css" rel="stylesheet">
<link href="static/inspinia/css/style.css" rel="stylesheet">

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../include/gm/nav-left.jsp" />
		<div id="page-wrapper" class="gray-bg">
			<jsp:include page="../include/gm/navbar-top.jsp" />
			<jsp:include page="../include/gm/nav-breadcrumb.jsp">
				<jsp:param name="curr" value="商品管理" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div class="row">
									<div class="col-sm-4 pull-left">
										<button type="button" class="btn btn-sm btn-danger"
											onclick="updateGoods(0)">新增商品</button>
									</div>
									<div class="col-sm-4 pull-right" style="text-align: right">
										<button type="button" class="btn btn-sm btn-success"
											onclick="$('#importFile').click()">导入费率</button>

										<input type="file" style="display: none" id="importFile"
											accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"  />
									</div>
								</div>
								<div class="row">
									<table class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>商品分类</th>
												<th>商品编号</th>
												<th>商品名称</th>
												<th>每手单位</th>
												<th>起投手数</th>
												<th>15天费率</th>
												<th>30天费率</th>
												<th>15天费率到期日</th>
												<th>30天费率到期日</th>
												<th>最后更新时间</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${goodsList }" var="item">
												<tr class="gradeX">
													<td align="center">${item.categoryName }</td>
													<td>${item.code }</td>
													<td>${item.name }</td>
													<td>${item.unit }  ${item.danwei }</td>
													<td>${item.min_shou }</td>
													<td><fmt:formatNumber value="${item.feilv_15 }" minFractionDigits="2"/>
													</td>
													<td>
													<fmt:formatNumber value="${item.feilv_30 }" minFractionDigits="2"/></td>
													<td>${item.feilv_15_time }</td>
													<td>${item.feilv_30_time }</td>
													<td>${item.lastUpdateTime }</td>
													<td><button onclick="updateGoods('${item.id}')"
															class="btn btn-xs btn-primary">修改</button></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>

			<jsp:include page="../include/gm/footer.jsp" />
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
	<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>


	<script>
		$(function() {

			if (window.File && window.FileList && window.FileReader
					&& window.Blob) {
				importFile.addEventListener("change", getFiles, false);
			}
		})

		function reload_page() {
			window.location.href = window.location.href;
		}

		function updateGoods(id) {
			layer.open({
				type : 2,
				title : '新增产品',
				shadeClose : true,
				shade : 0.8,
				area : [ '70%', '80%' ],
				maxmin : true,
				content : 'gm/goodsView/' + id
			});
		}
		
		function showFeiLv(key){
			layer.open({
				type : 2,
				title : '费率更新提示',
				shadeClose : true,
				shade : 0.8,
				area : [ '70%', '80%' ],
				maxmin : true,
				content : 'gm/feilvView?tmpKey=' + key
			});
		}
		
		function getFiles(e) {
			e = e || window.event;
			// 获取file input中的图片信息列表
			var files = e.target.files;
			// 验证是否是图片文件的正则
			// reg = /image\/.*/i;
			// console.log(files);
			if (files.length == 0) {
				return;
			}

			var f = files[0];
			uploadFun(f);
		}

		// 开始上传照片
		function uploadFun(singleFile) {
			// var singleImg = uploadImgArr[0];
			var xhr = new XMLHttpRequest();
			if (xhr.upload) {
				xhr.onreadystatechange = function(e) {
					if (xhr.readyState == 4) {
						if (xhr.status == 200
								&& eval("(" + xhr.responseText + ")").result) {
							showFeiLv(eval("(" + xhr.responseText + ")").data);
						} else {
							swal(eval("(" + xhr.responseText + ")").desc, "", "error");
						}
					}
				};

				var formdata = new FormData();
				formdata.append("upfile", singleFile);
				// 开始上传
				xhr.open("POST", "third/imoprtFeilv", true);
				xhr.send(formdata);
				var startDate = new Date().getTime();
			}

		}
	</script>
</body>

</html>