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

.no-border {
	border: 0px;
	width: 100%;
	height: 100%;
}
</style>
</head>

<body style="background: white;">
	<div id="wrapper">
		<div class="row">
			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>有限合伙列表</h5>

					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-sm-4 pull-left">
								<button type="button" class="btn btn-sm btn-danger"
									onclick="addRow('0')">新增有限合伙</button>
							</div>
						</div>
						<div class="row">
							<table class="table table-striped table-bordered table-hover"
								id="tableT">
								<thead>
									<tr>
										<th>公司</th>
										<th>普通合伙人</th>
										<th>法人名称</th>
										<th>注册地址</th>
										<th>银行</th>
										<th>卡号</th>
										<th>已关联用户数</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${bankInfoList }" var="item">
										<tr class="gradeX" id="row_${item.id }">
											<td data-type="canEdit"><span>${item.companyName }</span>
												<input type="text" name="companyName"
												value="${item.companyName }" class="form-control no-border"
												style="display: none" /></td>
											<td data-type="canEdit"><span>${item.hehuoName }</span>
												<input type="text" name="hehuoName"
												value="${item.hehuoName }" class="form-control no-border"
												style="display: none" /></td>
											<td data-type="canEdit"><span>${item.realName }</span>
												<input type="text" name="realName"
												value="${item.realName }" class="form-control no-border"
												style="display: none" /></td>
											<td data-type="canEdit"><span>${item.address }</span>
												<input type="text" name="address"
												value="${item.address }" class="form-control no-border"
												style="display: none" /></td>			
											<td data-type="canEdit"><span>${item.bankName }</span> <input
												type="text" name="bankName" value="${item.bankName }"
												class="form-control no-border" style="display: none" /></td>
											<td data-type="canEdit"><span>${item.bankNo }</span> <input
												type="text" name="bankNo" value="${item.bankNo }"
												class="form-control no-border" style="display: none" /></td>
											<td>${item.usedCount }</td>
											<td>
												<button onclick="editRow('${item.id}')"
														class="btn btn-xs btn-primary">修改</button>

													<button onclick="saveRow('${item.id}')"
														class="btn btn-xs btn-success" style="display: none">
														保存</button>

													<button onclick="cancelRow('${item.id}')"
														class="btn btn-xs btn-default" style="display: none">
														取消</button>
											</td>
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

		var editTag = false;

		function addRow(id) {
			if (!editTag) {
				var $tr = $('<tr class="gradeX" id="row_'+id+'">\
				<td data-type="canEdit"><span style="display:none"></span>\
				<input type="text" name="companyName"\
				value="" class="form-control no-border"\
				/></td>\
			<td data-type="canEdit"><span style="display:none"></span> <input\
				type="text" name="bankName" value=""\
				class="form-control no-border" /></td>\
			<td data-type="canEdit"><span style="display:none"></span> <input\
				type="text" name="bankNo" value=""\
				class="form-control no-border"  /></td>\
			<td>${item.usedCount }</td>\
			<td>\
				<div class="btn-group">\
					<button onclick="editRow(0)"\
						class="btn btn-xs btn-primary" style="display: none">修改</button>\
					<button onclick="saveRow(0)"\
						class="btn btn-xs btn-success" >\
						保存</button>\
					<button onclick="cancelRow(0)"\
						class="btn btn-xs btn-default">\
						取消</button>\
				</div>\
			</td>\
		</tr>');

				$("#tableT").append($tr);

			}
		}

		function editRow(id) {
			editTag = true;
			$("#row_" + id).find('td').each(function() {
				if ($(this).attr('data-type') == 'canEdit') {
					$(this).find('span').hide().end().find('input').show();
				}
			})

			$("#row_" + id).find('button.btn-primary').hide();
			$("#row_" + id).find('button.btn-success').show();
			$("#row_" + id).find('button.btn-default').show();
		}

		function saveRow(id) {
			var companyName = $("#row_" + id).find('input[name="companyName"]')
					.val();
			var bankName = $("#row_" + id).find('input[name="bankName"]').val();
			var bankNo = $("#row_" + id).find('input[name="bankNo"]').val();
			var realName =  $("#row_" + id).find('input[name="realName"]').val();
			var address =  $("#row_" + id).find('input[name="address"]').val();
			var hehuoName =  $("#row_" + id).find('input[name="hehuoName"]').val();
			if (companyName == '' || bankName == '' || bankNo == '' || realName=='' || hehuoName=='') {
				swal('更新内容不完整', "", "error");
				return false;
			}

			$.ajax({
				url : 'gm/json/updatePartnerBankInfo',
				type : 'post',
				dataType : "json",
				data : {
					id : id,
					companyName : companyName,
					bankName : bankName,
					bankNo : bankNo,
					realName: realName,
					address: address,
					hehuoName: hehuoName,
					partnerId: ${partnerId}
				},
				success : function(res) {
					console.log(res);
					if (res.hasOwnProperty('result') && !res.result) {
						swal(res.data, "", "error")
					} else {
						swal({
							title : "更新成功",
							type : "success"
						}, function() {
							
							if (id==0){
								window.location.href = window.location.href;
								return;
							}
							
							$("#row_" + id).find('td:eq(0)>span').html(
									companyName);
							$("#row_" + id).find('td:eq(1)>span')
									.html(bankName);
							$("#row_" + id).find('td:eq(2)>span').html(bankNo);
							cancelRow(id);
						});
					}
				}
			});
		}

		function cancelRow(id) {

			if (id == 0) {
				$("#row_" + id).remove();
			} else {
				$("#row_" + id).find('td').each(function() {
					if ($(this).attr('data-type') == 'canEdit') {
						$(this).find('input').hide().end().find('span').show();
					}
				})

				$("#row_" + id).find('button.btn-primary').show();
				$("#row_" + id).find('button.btn-success').hide();
				$("#row_" + id).find('button.btn-default').hide();
			}
			editTag = false;
		}

		function edit() {
			window.location.href = window.location.href + "?edit=1";
		}

		function cancelEdit() {
			gohref('gm/partnerView/${partner.partnerNo}');
		}

		
	</script>
</body>

</html>