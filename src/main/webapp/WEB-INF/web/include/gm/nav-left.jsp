<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String url = request.getRequestURI();
%>

<nav class="navbar-default navbar-static-side" role="navigation">
	<div class="sidebar-collapse">
		<ul class="nav metismenu" id="side-menu">
			<li class="nav-header">
				<div class="dropdown profile-element">
					<a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span
						class="clear"> <span class="block m-t-xs"> <strong
								class="font-bold">${currentGmUser.name }</strong>
						</span> <span class="text-muted text-xs block">${currentGmUser.role }
								<b class="caret"></b>
						</span>
					</span>
					</a>
					<ul class="dropdown-menu animated fadeInRight m-t-xs">
						<li><a onclick="lostpwd(this)" href="javascript:;">修改密码</a></li>
						<li><a onclick="logout(this)" href="javascript:;">退出</a></li>
					</ul>
				</div>
				<div class="logo-element">GM</div>
			</li>
			<c:if
				test="${currentGmUser.role=='系统管理员' || currentGmUser.role=='客服' || currentGmUser.role=='客服总管' }">
				<li <%if (url.indexOf("/gm/partnerlist.") >= 0) {%> class="active"
					<%}%>><a href="gm/partnerlist"><i class="fa fa-th-large"></i>
						<span class="nav-label">代理商管理</span></a></li>
			</c:if>
			<c:if
				test="${currentGmUser.role=='系统管理员' || currentGmUser.role=='客服'|| currentGmUser.role=='客服总管' }">
				<li <%if (url.indexOf("/gm/userlist.") >= 0) {%> class="active"
					<%}%>><a href="gm/userlist"><i class="fa fa-th-large"></i>
						<span class="nav-label">会员管理</span> </a></li>
			</c:if>
			<li <%if (url.indexOf("/gm/goodslist.") >= 0) {%> class="active"
				<%}%>><a href="gm/goodslist"><i class="fa fa-th-large"></i>
					<span class="nav-label">商品管理</span> </a></li>
			<li
				<%if (url.indexOf("/gm/trade") >= 0 || url.indexOf("/gm/order") >= 0) {%>
				class="active" <%}%>><a><i class="fa fa-th-large"></i> <span
					class="nav-label">交易管理</span> </a>
				<ul class="nav nav-second-level collapse">
					<li><a href="gm/tradelist">询价</a></li>
					<li><a href="gm/orderlist">购买</a></li>
				</ul></li>
			<c:if
				test="${currentGmUser.role=='系统管理员' || currentGmUser.role=='财务' }">
				<li <%if (url.indexOf("/gm/caiwureport") >= 0) {%> class="active"
					<%}%>><a href="gm/caiwureport"><i
						class="fa fa-th-large"></i> <span class="nav-label">财务报表</span> </a></li>
						
			</c:if>
			<c:if
				test="${currentGmUser.role=='系统管理员' || currentGmUser.role=='财务' }">
				<li <%if (url.indexOf("/gm/exchangelist") >= 0) {%> class="active"
					<%}%>><a href="gm/exchangelist"><i
						class="fa fa-th-large"></i> <span class="nav-label">充值管理</span> </a></li>
			</c:if>
			<c:if
				test="${currentGmUser.role=='系统管理员' || currentGmUser.role=='财务' }">
				<li <%if (url.indexOf("/gm/withdrawcashlist") >= 0) {%>
					class="active" <%}%>><a href="gm/withdrawcashlist"><i
						class="fa fa-th-large"></i> <span class="nav-label">提现管理</span> </a></li>
			</c:if>
			<c:if
				test="${currentGmUser.role=='系统管理员' || currentGmUser.role=='财务' }">
				<li <%if (url.indexOf("/gm/userlist") >= 0) {%>
					class="active" <%}%>><a href="gm/userlist"><i
						class="fa fa-th-large"></i> <span class="nav-label">客户余额</span> </a></li>
			</c:if>
			<c:if test="${currentGmUser.role=='系统管理员' }">
				<li <%if (url.indexOf("/gm/manager") >= 0) {%> class="active" <%}%>>
					<a><i class="fa fa-th-large"></i> <span class="nav-label">系统设置</span>
				</a>
					<ul class="nav nav-second-level collapse">
						<li><a href="gm/managerUser">账号管理</a></li>
					</ul>
				</li>
			</c:if>
		</ul>

	</div>
</nav>