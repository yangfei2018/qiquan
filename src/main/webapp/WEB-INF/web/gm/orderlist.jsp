<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cjy.qiquan.utils.Constant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<link href="static/inspinia/css/plugins/dataTables/datatables.min.css"
	rel="stylesheet">
</head>

<body>

	<div id="wrapper">

		<jsp:include page="../include/gm/nav-left.jsp" />
		<div id="page-wrapper" class="gray-bg">
			<jsp:include page="../include/gm/navbar-top.jsp" />
			<jsp:include page="../include/gm/nav-breadcrumb.jsp">
				<jsp:param name="curr" value="交易管理" />
			</jsp:include>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="row">
								<div class="tabs-container">
									<ul class="nav nav-tabs">
										<li <c:if test="${status==0 }">class="active"</c:if>><a
											href="gm/orderlist?status=0"> 待成交</a></li>
										<li <c:if test="${status==1 }">class="active"</c:if>><a
											href="gm/orderlist?status=1"> 已成交</a></li>
										<li <c:if test="${status==2 }">class="active"</c:if>><a
											href="gm/orderlist?status=2"> 待平仓</a></li>
										<li <c:if test="${status==3 }">class="active"</c:if>><a
											href="gm/orderlist?status=3"> 已结算</a></li>
										<li <c:if test="${status==-1 }">class="active"</c:if>><a
											href="gm/orderlist?status=-1">关闭交易</a></li>
									</ul>
									<div class="tab-content">
										<div id="tab-1" class="tab-pane active">
											<div class="panel-body">

												<div class="row">
													<div class="col-sm-4 pull-left">
														<button type="button" class="btn btn-sm btn-danger"
															onclick="excel(0)">导出明细excel</button>
														<%-- <c:if test="${status== 0}">
																<button type="button" class="btn btn-sm btn-warning"
																onclick="excel(1)">导汇总excel</button>
																</c:if> --%>
													</div>
													<div class="col-sm-6 pull-left">
														<input type="text" value="" id="startTimePicker">
														-
														<input type="text" value="" id="endTimePicker">
														<button type="button" class="btn btn-sm btn-search"
																onclick="search(${status})">查询</button>
													</div>


													<c:if test="${status== 0}">

														<div class="col-sm-4 pull-right" style="text-align: right">
															<button type="button" class="btn btn-sm btn-success"
																onclick="$('#importFile').click()">导入交易记录</button>

															<input type="file" style="display: none" id="importFile"
																accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" />
														</div>
													</c:if>
													<c:if test="${status== 2}">
														<div class="col-sm-4 pull-right" style="text-align: right;vertical-align: top;margin-top:-40px;">
															<button type="button" class="btn btn-sm btn-success"
																onclick="$('#importFile').click()">导入结算记录</button>

															<input type="file" style="display: none" id="importFile"
																accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" />
														</div>
													</c:if>
												</div>
												<div class="table-responsive">

													<table
														class="table table-striped table-bordered table-hover dataTables">
														<thead>
															<tr>
																<th><input type="checkbox" checked="checked"
																	onclick="selct(this)"></th>
																<c:if test="${status==0||status==2 }">
																	<th>期权买卖方向</th>
																</c:if>
																<th>报价方式</th>
																<th>订单号</th>
																<th>购买者</th>
																<th>代理商</th>
																<th>有限合伙</th>
																<th>名称</th>
																<th>代码</th>
																<th>涨跌</th>
																<th>权利金（元）</th>
																<th>期限</th>
																<th>名义本金</th>
																<th>手数</th>
																<c:if test="${status>0 }">
																	<th>成交价格（元）</th>
																</c:if>
																<th>期权类型</th>
																<th>操作时间</th>
																<c:if test="${status==3 }">
																	<th>结算时间</th>
																</c:if>
																<c:if test="${status==1 }">
																	<th>成交时间</th>
																</c:if>
																<th><c:choose>
																		<c:when test="${status==0 }">
																创建时间
															</c:when>
																		<c:otherwise>
																合同时间
															</c:otherwise>
																	</c:choose></th>
																<c:if test="${status==2||status==3 }">
																	<th>行权方式</th>
																</c:if>
																<c:if test="${status==3 }">
																	<th>行权价格（元）</th>
																	<th>结算金额（元）</th>
																</c:if>
																<th>状态</th>

																<th>操作</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${page.list }" var="item">
																<tr>
																	<td><input type="checkbox" value="${item.id }"
																		name="ids" checked="checked"></td>
																	<c:choose>
																		<c:when test="${status==0 }">
																			<td>买入</td>
																		</c:when>
																		<c:when test="${status==2 }">
																			<td>行权</td>
																		</c:when>
																	</c:choose>
																	<td>${item.getBuyType() == 1 ? "实时下单" : "收盘价下单" }</td>
																	<td>${item.orderNo }</td>
																	<td>${item.buyerName }</td>
																	<td>${item.buyerPartnerCompanyName }</td>
																	<td>${item.buyerHehuoCompanyName}</td>
																	<td>${item.productName }</td>
																	<td>${item.productCode }</td>
																	<td><c:choose>
																			<c:when test="${item.buyAndFall==1 }">
																				<span class="label label-success"> 看涨 </span>
																			</c:when>
																			<c:when test="${item.buyAndFall==2 }">
																				<span class="label label-danger"> 看跌 </span>
																			</c:when>
																		</c:choose></td>
																	<td>${item.amountFormat }</td>
																	<td>${item.period }天</td>
																	<td><c:choose>
																			<c:when test="${status==0 }">
													${item.notionalPrincipalbeforeFormat }
												</c:when>
																			<c:otherwise>
													${item.notionalPrincipalFormat }
												</c:otherwise>
																		</c:choose></td>
																	<td>${item.shou }</td>
																	<c:if test="${status>0 }">
																		<td>${item.tradeAmountFormat }</td>
																	</c:if>

																	<td><c:choose>
																			<c:when test="${item.type==1 }">
																	美式
																	</c:when>
																			<c:when test="${item.type==2 }">
																	欧式
																	</c:when>
																		</c:choose></td>
																	<td>${item.updateTimeFormat }</td>
																	<c:if test="${status==3 }">
																		<td>${item.balanceTimeFormat }</td>
																	</c:if>
																	<c:if test="${status==1 }">
																		<td>${item.dealTimeFormat }</td>
																	</c:if>
																	<c:if test="${status==-1 }">
																		<td>${item.optTimeFormat }</td>
																	</c:if>
																	<td><c:choose>
																			<c:when test="${status==0 }">
																${item.createTimeFormat }
															</c:when>
																			<c:otherwise>
																${item.orderStartTime } - ${item.orderEndTime }
															</c:otherwise>
																		</c:choose></td>
																	<c:if test="${status==2||status==3 }">
																		<td><c:choose>
																				<c:when test="${item.getPingcangType() == 1 }">
																	市价交易
																	</c:when>
																				<c:when test="${item.getPingcangType() ==3 }">
																	挂价交易 ${item.getZhishu() }
																	</c:when>
																				<c:otherwise>
																	收盘价交易
																	</c:otherwise>
																			</c:choose></td>
																	</c:if>
																	<c:if test="${status==3 }">
																		<td>
																		<fmt:formatNumber value="${item.executivePrice }" minFractionDigits="2"/>
																		</td>
																		<td>${item.balanceAmountFormat }</td>
																	</c:if>
																	<td><c:choose>
																			<c:when test="${item.status==0 }">
																	待成交
																</c:when>
																			<c:when test="${item.status==1 }">
																	已成交
																</c:when>
																			<c:when test="${item.status==2 }">
																	平仓记录
																</c:when>
																			<c:when test="${item.status==3 }">
																	已结算
																</c:when>
																			<c:when test="${item.status==-1 }">
																	已取消<br />
																	${item.closeReason }
																</c:when>
																		</c:choose></td>
																	<td>
																		<button onClick="orderView('${item.orderNo}');"
																			class="btn btn-xs btn-primary" type="button">
																			查看详细</button> <c:if test="${item.status==0 }">
																			<button onClick="orderCancel('${item.orderNo}');"
																				class="btn btn-xs btn-danger" type="button">
																				取消订单</button>
																		</c:if>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</div>
										</div>
										<div id="tab-2" class="tab-pane">
											<div class="panel-body"></div>
										</div>
									</div>


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
	<script src="static/inspinia/js/plugins/dataTables/datatables.min.js"></script>
	<script src="static/inspinia/js/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<link href="static/inspinia/js/plugins/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css"
		  rel="stylesheet">
	<%--<script src="static/h-ui/js/H-ui.js" charset="UTF-8"></script>--%>

	<script>
		function reload_page() {
			window.location.href = window.location.href;
		}

		$(function() {
			<c:if test="${status==0||status==2}">
			if (window.File && window.FileList && window.FileReader
					&& window.Blob) {
				importFile.addEventListener("change", getFiles, false);
			}

			</c:if>

			$('.dataTables').DataTable({
				pageLength : 25,
				responsive : true,
				dom : '<"html5buttons"B>lTfgitp',
				buttons : [ {
					extend : 'copy'
				},

				]

			});
		})

		function excel(hz) {

			var ids = new Array;

			$("input[name='ids']:checkbox:checked").each(function() {
				ids.push($(this).val());
			})

			gohref("gm/orderlist?status=${status}&excel=1&hz=" + hz + "&ids="
					+ ids.join(','));
		}

		function selct(obj) {
			console.log('sele:' + $(obj).is(':checked'));
			if ($(obj).is(':checked')) {
				$('input[name="ids"]').each(function() {
					$(this).prop('checked', true);
				})

			} else {
				$('input[name="ids"]').each(function() {
					$(this).prop('checked', false);
				})
				//$('input[name="ids"]').removeAttr('checked');

			}
		}

		function orderView(id) {
			layer.open({
				type : 2,
				title : '交易记录',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/orderView/' + id
			});
		}

		function showTradeView(tmpKey) {
			layer.open({
				type : 2,
				title : '交易记录确认',
				shadeClose : true,
				shade : 0.8,
				area : [ '80%', '80%' ],
				maxmin : true,
				content : 'gm/importOrder?tmpKey=' + tmpKey+"&status=${status}"
			});
		}

		function orderCancel(sn) {
			layer.prompt({
				title : '请填写取消交易理由',
				formType : 2
			}, function(text, index) {
				$.ajax({
					url : 'gm/json/closeOrder',
					type : 'post',
					dataType : "json",
					data : {
						orderNo : sn,
						reason : text
					},
					success : function(res) {
						console.log(res);
						if (res.hasOwnProperty('result') && !res.result) {
							layer.msg(res.data);

						} else {
							layer.close(index);
							layer.msg('操作成功');
							window.location.href = window.location.href;
						}
					}
				});
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
							showTradeView(eval("(" + xhr.responseText + ")").data);
						} else {
							swal(eval("(" + xhr.responseText + ")").desc, "",
									"error");
						}
					}
				};

				var formdata = new FormData();
				formdata.append("upfile", singleFile);
				// 开始上传
				xhr.open("POST", "third/importTradeExcel", true);
				xhr.send(formdata);
				var startDate = new Date().getTime();
			}

		}
		$('#startTimePicker').datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn:  1,
            autoclose: 1,
        });
		$('#endTimePicker').datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn:  1,
            autoclose: 1,
        });

		function search(status){
			var startTime = $('#startTimePicker').val();
			// var startTime = $('#startTimePicker').data("datetimepicker").getDate();
			// var endTime = $('#endTimePicker').data("datetimepicker").getDate();
			var endTime = $('#endTimePicker').val();
			window.location.href = "gm/orderlist?status=" + status + "&startTime="+startTime + "&endTime=" + endTime;
			// console.log("status:" + status + "\nstartTime:" + startTime + "\nendTime:" + endTime);
        }
	</script>
</body>

</html>