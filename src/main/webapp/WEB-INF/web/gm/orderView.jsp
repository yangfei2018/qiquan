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
								<label class="col-lg-2 control-label">交易编号</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.orderNo }</p>
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
								<label class="col-lg-2 control-label">购买时间</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.createTimeFormat }</p>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="col-lg-2 control-label">名义本金</label>
						<div class="col-lg-10">
							<p class="form-control-static">${tradeRecord.notionalPrincipalFormat }
								元</p>
						</div>
					</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-sm-5">
				<form class="form-horizontal">
					<input type="hidden" name="orderNo"
								value="${tradeRecord.orderNo }" />
					<input type="hidden" name="status"
								value="${tradeRecord.status==0?1:3 }" />			
					<div class="form-group">
						<label class="col-lg-2 control-label">购买人</label>
						<div class="col-lg-10">
							<p class="form-control-static">${tradeRecord.buyerName }
							<br/>
							(${tradeRecord.buyerPartnerCompanyName })
							</p>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">购买人联系电话</label>
						<div class="col-lg-10">
							<p class="form-control-static">${tradeRecord.buyerMobile }</p>
						</div>
					</div>
					
					<c:if test="${tradeRecord.status>0 }">
					<div class="form-group">
								<label class="col-lg-2 control-label">成交价格</label>
								<div class="col-lg-10">
									<p class="form-control-static">${tradeRecord.tradeAmountFormat }元</p>
								</div>
							</div>
					</c:if>
					<c:if test="${tradeRecord.status==3 }">
					<div class="form-group">
						<label class="col-lg-2 control-label">结算金额</label>
						<div class="col-lg-10">
							<p class="form-control-static">${tradeRecord.balanceAmountFormat } 元</p>
						</div>
					</div>
					</c:if>
					<div class="form-group">
						<label class="col-lg-2 control-label">状态</label>
						<div class="col-lg-10">
							<p class="form-control-static">
								<c:choose>
									<c:when test="${tradeRecord.status==0 }">
																	待成交
																</c:when>
									<c:when test="${tradeRecord.status==1 }">
																	已成交
																</c:when>
									<c:when test="${tradeRecord.status==2 }">
																	平仓中
																</c:when>
									<c:when test="${tradeRecord.status==3 }">
																	已结算
																</c:when>														
								</c:choose>
							</p>
						</div>
					</div>
					
					<c:if test="${tradeRecord.status==2}">
						<div class="hr-line-dashed"></div>
							<div class="form-group">
									<label class="col-lg-2 control-label">结算金额</label>
									<div class="col-lg-10">
										<input type="text" class="form-control" name="balanceAmount"
												value="0" placeholder="请输入最终结算给会员的金额" /> 元
									</div>
								</div>
							<div class="form-group">
									<label class="col-lg-2 control-label"></label>
									<div class="col-lg-10">
										<button class="btn btn-danger" type="button"
											onclick="baojia(this)">确认已结算</button>	
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
		
		function baojia(obj) {
				
			swal({
				title : "你确定要执行此操作吗？",
				text : "",
				type : "warning",
				showCancelButton : true,
				cancelButtonText : '取消',
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : 'gm/json/dealOrder',
					type : 'post',
					dataType : "json",
					data : $(obj).closest("form").serialize(),
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							swal(res.data, "", "error")
						} else {
							swal({
								title : "操作成功",
								type : "success"
							}, function() {
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