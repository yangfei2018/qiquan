<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String curr = request.getParameter("curr");
%>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2><%=curr%></h2>
		<ol class="breadcrumb">
			<li><a href="gm/index">首页</a></li>

			<li class="active"><strong><%=curr%></strong></li>
		</ol>
	</div>
	<div class="col-lg-2"></div>
</div>