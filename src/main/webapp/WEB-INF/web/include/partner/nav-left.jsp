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
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">${currentPartnerUser.name }</strong>
                             </span> <span class="text-muted text-xs block">管理员 <b class="caret"></b></span> </span> </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            		 <li><a onclick="lostpwd(this)" href="javascript:;">修改密码</a></li>
                                <li><a onclick="logout(this)" href="javascript:;">退出</a></li>
                            </ul>
                    </div>
                    <div class="logo-element">
                        代理商
                    </div>
                </li>
                <li <% if (url.indexOf("/partner/index.")>=0) {%> class="active"<% }%> >
                    <a href="partner/index"><i class="fa fa-th-large"></i> <span class="nav-label">有限合伙管理</span></a>
                </li>
                <li <% if (url.indexOf("/partner/userlist.")>=0) {%> class="active"<% }%> >
                    <a href="partner/userlist"><i class="fa fa-th-large"></i> <span class="nav-label">会员管理</span> </a>
                </li>
                <li <% if (url.indexOf("/partner/trade")>=0||url.indexOf("/partner/orderlist")>=0 ) {%> class="active"<% }%> >
                    <a><i class="fa fa-th-large"></i> <span class="nav-label">交易管理</span> </a>
                    <ul class="nav nav-second-level collapse">
                        <li><a href="partner/tradelist">询价</a></li>
                        <li><a href="partner/orderlist">购买</a></li>
                    </ul>
                </li>
               
                <li <% if (url.indexOf("/partner/exchangelist")>=0) {%> class="active"<% }%> >
                    <a href="partner/exchangelist"><i class="fa fa-th-large"></i> <span class="nav-label">充值记录</span> </a>
                </li>
               	 <li <% if (url.indexOf("/partner/withdrawcashlist")>=0) {%> class="active"<% }%> >
                    <a href="partner/withdrawcashlist"><i class="fa fa-th-large"></i> <span class="nav-label">提现记录</span> </a>
                </li>
            </ul>

        </div>
    </nav>