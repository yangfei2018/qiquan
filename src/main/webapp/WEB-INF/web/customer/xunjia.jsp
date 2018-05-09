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


.form-control-static {
  padding-top: 2px;
  padding-bottom: 2px;
  margin-bottom: 0;
  min-height: 34px;
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
			<jsp:param name="curr" value="询价" />
		</jsp:include>
		<div class="Hui-article" style="margin-top: 40px;">
			<article class="cl pd-20 pad">
				<p class="f-20 text-success">提示：询价提交过后，30分钟内我们将给予回复，请您留意询价记录！</p>
				<p class="f-20 text-success">为了保证询价的可靠性，请确保您账户余额不低于2万。</p>
				<p style="color: red; font-size: 18px;">注意：请您填写完整后提交！</p>
				<form class="form form-horizontal size-L" id="form-article-add">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>产品种类：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<span class="select-box size-L"> <select name="categoryId"
								class="select">
									<c:forEach items="${goodsCategory }" var="category">
										<c:if test="${category.id!=1 }">
										<option value="${category.id }">${category.name }</option>
										</c:if>
									</c:forEach>
							</select>
							</span>
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>产品名称：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<span class="select-box size-L"> <select name="productName"
								class="select">

							</select>
							</span>
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>产品代码：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text size-L" value=""
								placeholder="请输入您选择的产品代码"  name="productCode" readonly="readonly">
						</div>
					</div>
					<div class="row cl ds">
						<label class="form-label col-xs-4 col-sm-3">起投手数：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<p class="form-control-static" id="min_shou">100</p>
						</div>
					</div>
					<div class="row cl ds">
						<label class="form-label col-xs-4 col-sm-3">每手单位：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<p class="form-control-static" id="unit">100</p>
						</div>
					</div>
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>方向：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<div class="radio-box size-L">
								<input type="radio" id="fangx-1" name="buyAndFall" value="1"> <label
									for="fangx-1">看涨</label>
							</div>
							<div class="radio-box">
								<input type="radio" id="fangx-2" name="buyAndFall" value="2"> <label
									for="fangx-2">看跌</label>
							</div>
						</div>
					</div>
					

					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>期权期限：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<span class="select-box size-L"> <select name="period" class="select">
									<option value="30">30天</option>
									<option value="15">15天</option>
							</select>
							</span>
						</div>
					</div>
					
					<div class="row cl ds">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>下单手数：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<input type="number" class="input-text size-L" value=""
								placeholder="请输入下单手数，整数" name="shou">
						</div>
					</div>
					
					<div class="row cl ds">
						<label class="form-label col-xs-4 col-sm-3">每手权利金：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<p class="form-control-static" id="per_amount"></p>
						</div>
					</div>
					
					<div class="row cl ds">
						<label class="form-label col-xs-4 col-sm-3">起投权利金：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<p class="form-control-static" id="min_amount"></p>
						</div>
					</div>
					
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>权利金：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text size-L" value=""
								placeholder="请输入权利金金额，保留两位小数。货币单位：元" name="amount">
						</div>
					</div>
					<div class="row cl ds">
						<label class="form-label col-xs-4 col-sm-3"><span
							class="c-red">*</span>名义本金：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<p class="form-control-static" id="notionalPrincipal"></p>
						</div>
					</div>
					
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3 size-L"><span
							class="c-red">*</span>期权类型：</label>
						<div class="formControls col-xs-8 col-sm-9">
							<span class="select-box size-L"> <select name="type" class="select">
									<option value="1">美式</option>
									<option value="2">欧式</option>
							</select>
							</span>
						</div>
					</div>

					<div class="row cl">
						<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-2">

							<button onClick="xunjia(this);" class="btn btn-secondary size-XL radius"
								type="button">
								<i class="Hui-iconfont">&#xe632;</i> 提交
							</button>

						</div>
					</div>
				</form>

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
		var amountReg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
		var prod = [];
		$(function() {
			$("select[name='categoryId']").change(function() {
				loadProduct($(this).val());
			})
			
			$("select[name='period']").change(function(){
				if ($("select[name='categoryId']").val()!=3){
					calGoods($("input[name='productCode']").val(),$("input[name='shou']").val(),$(this).val());
				}
			});
			
			
			$("input[name='shou']").keyup(function(){
				calGoods($("input[name='productCode']").val(),$(this).val(),$("select[name='period']").val());
			})

			loadProduct($("select[name='categoryId']").val());
			
			
			$("select[name='productName']").change(function() {
				for(var i=0;i<prod.length;i++){
					if (prod[i].name == $(this).val()){
						$('input[name="productCode"]').val(prod[i].code);
						calGoods($("input[name='productCode']").val(),$("input[name='shou']").val(),$("select[name='period']").val());
					}
				}
			})
			
		})
		
		function getPro(id){
			for(var i=0;i<prod.length;i++){
				if (prod[i].code==id){
					return prod[i];
				}
			}
		}
		
		function calGoods(id,shou,period){
			console.log('calGoods:'+id+",shou:"+shou+",period:"+period);
			$.ajax({
				type : "get",
				url : "third/calGoods",
				data : {
					id:id,
					period:period,
					shou:shou
				},
				dataType : "json",
				success : function(json) {
					console.log(json);
					$("#per_amount").html(json.per_amount);
					$("#notionalPrincipal").html(json.notionalPrincipal);
					$("#min_amount").html(json.min_amount);
					$("input[name='amount']").val(json.amount);
					$("#min_shou").html(json.min_shou);
					var pd = getPro(id);
					if (pd){
						$("#unit").html(pd.unit + " " +pd.danwei);
					}
					
				}
			});
		}

		function xunjia(obj) {

			if ($('input[name="productCode"]').val() == '') {
				layer_alert('请填写产品代码', function() {
					$('input[name="productCode"]').focus();
				});
				return false;
			}

			if ($('input:radio[name="buyAndFall"]:checked').val() == null) {
				layer_alert('请选择方向');
				return false;
			}

			if (!amountReg.test($('input[name="amount"]').val())) {
				layer_alert('请正确填写权利金', function() {
					$('input[name="amount"]').focus();
				});
				return false;
			}

			/* if (!$('#xieyi').is(':checked')) {
				layer_alert('请先阅读并同意《大赢家产品交易规则》和《大赢家风险揭示书》。');
				return false;
			} */
			
			$.ajax({
				type : "post",
				url : "customer/json/postTrade",
				data : $(obj).closest("form").serialize(),
				dataType : "json",
				success : function(rep) {
					if (rep.result){
						layer_success(rep.data,function(){
							gohref('customer/xunjialist');
						});
					}else{
						layer_alert(rep.data);
					}
				}
			});
			
			//layer_alert('为了保证询价的可靠性，请确保您账户余额不低于2万');
		}

		function loadProduct(categoryId) {
			
			if (categoryId==3){
				var _childOptions = $("select[name='productName']");
				_childOptions.html("");//清空字了列表
				_childOptions.parent().parent().parent().hide();
				
				$('input[name="productCode"]').val('');
				$('input[name="productCode"]').removeAttr('readonly');
				$("#fangx-2").attr("disabled","disabled");
				console.log($(".ds").length);
				$(".ds").hide();
			}else{
				$(".ds").show();
				$.ajax({
					type : "get",
					url : "customer/json/listGoodsByCategoryId",
					data : {
						categoryId : categoryId
					},
					dataType : "json",
					success : function(json) {
						prod = json;
						var _childOptions = $("select[name='productName']");
						_childOptions.html("");//清空字了列表
						_childOptions.parent().parent().parent().show();
						$("#fangx-2").removeAttr("disabled");
						for (var i = 0; i < json.length; i++) {

							_childOptions.append("<option value='"+json[i].name+"'>"
									+ json[i].name + "</option>");
							if (i==0){
								$('input[name="productCode"]').val(json[i].code);
								$("#min_shou").html(json[i].min_shou);
								$("#unit").html(json[i].unit + " " +json[i].danwei);
								
								console.log($("select[name='period']").val());
								
								calGoods($("input[name='productCode']").val(),$("input[name='shou']").val(),$("select[name='period']").val());
							}
						}
						$('input[name="productCode"]').attr('readonly','true');
					}
				});
			}
			
			
			
		}
	</script>

</body>
</html>