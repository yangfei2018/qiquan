<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
 String curr=request.getParameter("curr");

%>
<nav class="breadcrumb">
	<i class="Hui-iconfont"></i> <a href="/" class="maincolor">首页</a> <span
		class="c-999 en">&gt;</span> <span class="c-666"><%=curr%></span>
</nav>