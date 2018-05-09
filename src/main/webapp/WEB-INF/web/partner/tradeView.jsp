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
							

							<div class="form-group">
								<label class="col-lg-2 control-label">询价编号</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.tradeNo }</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">商品编号</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.productCode }</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">商品名称</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.productName }</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">涨跌</label>
								<div class="col-lg-10">
									<p class="form-control-static">
										<c:choose>
											<c:when test="${tradeRecord.buyAndFall==1 }">
												<span class="label label-success"> 涨 </span>
											</c:when>
											<c:when test="${tradeRecord.buyAndFall==2 }">
												<span class="label label-danger"> 跌 </span>
											</c:when>
										</c:choose>
									</p>
								</div>
							</div>


							<div class="form-group">
								<label class="col-lg-2 control-label">权利金</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.amountFormat }元</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">期限</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.period }天</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">期权类型</label>
								<div class="col-lg-10">
									<p class="form-control-static">
										<c:choose>
											<c:when test="${tradeRecord.type==1 }">
																	美式
																	</c:when>
											<c:when test="${tradeRecord.type==2 }">
																	欧式
																	</c:when>
										</c:choose>
									</p>
								</div>
							</div>

							<div class="form-group">
								<label class="col-lg-2 control-label">询价时间</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.createTimeFormat }</p>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-5">
				<form class="form-horizontal">
					<input type="hidden" name="tradeNo"
								value="${tradeRecord.tradeNo }" />
					<div class="form-group">
						<label class="col-lg-2 control-label">购买人</label>
						<div class="col-lg-10">
							<p class="form-control-static">${tradeRecord.buyerName }</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">购买人联系电话</label>
						<div class="col-lg-10">
							<p class="form-control-static">${tradeRecord.buyerMobile }</p>
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="col-lg-2 control-label">名义本金</label>
						<div class="col-lg-10">
							<c:if test="${baojia==1 }">
								<input type="text" class="form-control" name="notionalPrincipalFormat"
												value="${tradeRecord.notionalPrincipal }" placeholder="请输入计算后的名义本金" /> 元
							</c:if>
							<c:if test="${baojia==0 }">
								<p class="form-control-static">${tradeRecord.notionalPrincipalFormat }
								元</p>
							</c:if>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">状态</label>
						<div class="col-lg-10">
							<p class="form-control-static">
								<c:choose>
									<c:when test="${tradeRecord.status==0 }">
																	未报价
																</c:when>
									<c:when test="${tradeRecord.status==1 }">
																	已报价
																</c:when>
								</c:choose>
							</p>
						</div>
					</div>
					<c:if test="${baojia==1 }">
						<div class="hr-line-dashed"></div>
							<div class="form-group">
									<label class="col-lg-2 control-label"></label>
									<div class="col-lg-10">
										<button class="btn btn-danger" type="button"
											onclick="baojia(this)">确认报价</button>
											
										<button class="btn btn-default" type="button"
											onclick="cancelbaojia()">取消报价</button>	
									</div>
								</div>
					</c:if>
					<c:if test="${tradeRecord.status==0 && baojia==0 }">
					<div class="hr-line-dashed"></div>
					<div class="form-group">
									<label class="col-lg-2 control-label"></label>
									<div class="col-lg-10">
										<button class="btn btn-danger" type="button"
											onclick="showbaojia()">现在报价</button>
									</div>
								</div>
					</c:if>
				</form>
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
		
		function showbaojia(){
			window.location.href = window.location.href+"?baojia=1";
		}
		
		function cancelbaojia(){
			gohref('gm/tradeView/${id}');
		}
		
		function baojia(obj) {
			var amount = $(obj).closest('form').find('input[name="notionalPrincipalFormat"]').val();
			
			swal({
				title : "你确定要执行此操作吗？",
				text : "您确定要按 "+amount+" 元 金额报价吗？",
				type : "warning",
				showCancelButton : true,
				cancelButtonText : '取消',
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : 'gm/json/baojia',
					type : 'post',
					dataType : "json",
					data : $(obj).closest("form").serialize(),
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "报价成功",
								type : "success"
							}, function() {
								cancelbaojia();
								parent.reload_page();
								//parent.layer.close(index);
							});
						}
					}
				});
			});

		}

		
	</script>
</body>

</html>