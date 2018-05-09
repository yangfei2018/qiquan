<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
 String url = request.getRequestURI();
%>

<aside class="Hui-aside">

	<div class="menu_dropdown bk_2">
		<dl>
			<dt <% if (url.indexOf("/customer/xunjia.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/xunjia')" >
				询价<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/xunjialist.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/xunjialist')"  >
				询价记录<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/jiaoyilist.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/jiaoyilist')">
				交易记录<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/index.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/index')" >
				账户<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/certification.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/certification')" >
				实名认证<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/chongzhi.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/chongzhi')" >
				充值<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/rechargelist.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/rechargelist')" >
				充值记录<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/withdrawCash.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/withdrawCash')" >
				提现<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
		
		<dl>
			<dt <% if (url.indexOf("/customer/withdrawCashlist.")>=0) {%> class="selected"
				<% }%> onclick="switchBar('customer/withdrawCashlist')"  >
				提现记录<i class="Hui-iconfont menu_dropdown-arrow">&#xe63d;</i>
			</dt>
			
		</dl>
	</div>
</aside>
<div class="dislpayArrow hidden-xs">
	<a class="pngfix" href="javascript:void(0);"
		onClick="displaynavbar(this)"></a>
</div>

<style>
.bk_2{
	font-size:16px;
	color:black;

}


.bk_2 dt.selected{
	font-weight: 800;
}

</style>
<script>
function switchBar(url){
	gohref(url);
}

</script>
<!--/_menu 作为公共模版分离出去-->