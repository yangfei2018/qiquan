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
<style>
@
navbar-inverse-link-active-color


:


"
white
";





@
navbar-inverse-link-active-bg


:


"
#fc683b


";
</style>
<link rel="stylesheet" type="text/css"
	href="static/bootcss/css/bootstrap.min.css" />
<style>


.jumbotron {
	margin-bottom: 0px !important;
	padding-bottom: 0px !important;
}

.qgh {
	padding-top: 10px;
	padding-bottom: 10px;
	background: #f7f8f9;
}

.qghb {
	padding-top: 10px;
	padding-bottom: 10px;
}

.qghbw {
	padding-top: 10px;
	padding-bottom: 10px;
	background: url(static/img/bg_zhishuqiquan.png) repeat center center;
}

.b_1024 {
	max-width: 1024px;
	margin-left: auto;
	margin-right: auto;
	overflow: hidden;
}

.footer {
	background: #2d2f2d;
	padding-top: 20px;
	padding-bottom: 20px;
}

.footer .about {
	color: white;
}

.footer a {
	color: #777;
}

.navbar-inverse {
	background: white;
}

.navbar-inverse .navbar-nav>li>a:hover {
	color: #9d9d9d;
}

.icon-bar {
	color: #9d9d9d !important;
}

.rkbtn {
	margin: 10px !important;
	background: #fc683b;
	color: white !important;
	border-radius: 5px;
	height: 30px !important;;
	line-height: 30px !important;;
	margin-top: 10px !important;;
	padding-top: 0px !important;;
	padding-bottom: 0px !important;;
}

.rkbtn:hover {
	color: white !important;
}

.masthead h1 {
	color: white;
	text-align: center;
}


.lop{
	margin-left:auto;
	margin-right:auto;
	background: black;
	height:100px;
	width:100px;
	border-radius: 50%;
	color:white;
	text-align: center;
	line-height: 100px;
	font-weight:800;
	font-size:35px;
	background: url(static/img/mask.png) repeat center center;
}
</style>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title><%=Constant.PAGE_INFO.TITLE%></title>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button class="navbar-toggle collapsed" type="button"
					data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand hidden-sm" href="javascript:;"> <img
					src="static/img/logo.png" />
				</a>
			</div>
			<div class="navbar-collapse collapse" role="navigation">
				<ul class="nav navbar-nav">
					<li class="hidden-sm hidden-md"><a href="javascript:;">官网首页</a></li>
					<li><a href="javascript:alert('根据公安部新规，服务器正在升级，目前除交易外，其他服务暂时不能提供')">股市行情</a></li>
					<li><a href="javascript:alert('根据公安部新规，服务器正在升级，目前除交易外，其他服务暂时不能提供')">股权课堂</a></li>
					<li><a href="javascript:alert('根据公安部新规，服务器正在升级，目前除交易外，其他服务暂时不能提供')">商品期权</a></li>
					<li><a href="javascript:alert('根据公安部新规，服务器正在升级，目前除交易外，其他服务暂时不能提供')">指数期权</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right hidden-sm">
					<c:choose>
						<c:when test="${empty currentUser }">
							<li><a href="javascript:;" class="rkbtn" onclick="login()">登入</a></li>
							<li><a href="javascript:;" class="rkbtn" onclick="reg()">注册</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="customer/index" class="rkbtn">会员入口</a></li>
						</c:otherwise>
					</c:choose>

				</ul>
			</div>
		</div>
	</div>
	<div class="jumbotron masthead">
		<!-- News Sticker -->
		<div class="bgimage enabled-news-ticker"
			style="background: url('static/img/banner.png') repeat center center;">
			<div class="container" style="padding: 30px;">
				<h1>带您走进期权市</h1>
				<h1>场的期权专家</h1>
				<div class="row text-center">
					<div class="b_1024" style="margin-top:40px;">
						<div class="col-xs-12 col-sm-4 blurb-cta ">
							<div class="lop">股票</div>
						</div>
						<div class="col-xs-12 col-sm-4 blurb-cta">
							<div class="lop">商品</div>
						</div>
						<div class="col-xs-12 col-sm-4 blurb-cta">
							<div class="lop">指数</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row text-center qgh">
		<div class="col-xs-12 valuepropsheading ">
			<h1>为你提供更优秀的服务</h1>
		</div>
	</div>
	<div class="row text-center qgh">
		<div class="b_1024">
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_kuaishukaihu.png">
				<h2>快速开户</h2>
				<p>全程服务，高效为您办理开户</p>
				<!-- <a href="https://www.azul.com/products/zing/" role="button"
				class="greenbtn" target="_self">Learn More About Zing</a> -->
			</div>
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_bianxieruj.png">
				<h2>便捷入金</h2>
				<p>支持多家银行直接入金，入金快捷</p>
			</div>
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_youzhitiyan.png">
				<h2>优质体验</h2>
				<p>优质流畅的用户体验，让您查询更便捷</p>
			</div>
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_zijinanquan.png">
				<h2>资金安全</h2>
				<p>固定的风险，高额的收益</p>
			</div>
		</div>
	</div>
	<div class="row text-center qghb">
		<div class="col-xs-12 valuepropsheading ">
			<h1>商品期权</h1>
			<p>作为期货市场的一个重要组成部分，是当前资本市场最具活力的风险管理工具之一</p>
		</div>
		<div class="col-xs-12 qghb">
			<div class="b_1024">
				<div class="col-xs-12 col-sm-4 blurb-cta">
					<img src="static/img/img_left.png">

				</div>
				<div class="col-xs-12 col-sm-4 blurb-cta">
					<img src="static/img/img_center.png">

				</div>
				<div class="col-xs-12 col-sm-4 blurb-cta">
					<img src="static/img/img_right.png">

				</div>
			</div>
		</div>
	</div>
	<div class="row text-center qghbw">
		<div class="col-xs-12 valuepropsheading ">
			<h1>指数期权</h1>
		</div>
		<div class="col-xs-12 qghb">
			<div class="b_1024">
				<div class="col-xs-12 col-sm-3 blurb-cta">
					<img src="static/img/01_zonghezhishu.png"
						style="width: 80%; height: auto;">
				</div>
				<div class="col-xs-12 col-sm-3 blurb-cta">
					<img src="static/img/02_gainianzhishu.png"
						style="width: 80%; height: auto;">
				</div>
				<div class="col-xs-12 col-sm-3 blurb-cta">
					<img src="static/img/03_hangyezhishu.png"
						style="width: 80%; height: auto;">
				</div>
				<div class="col-xs-12 col-sm-3 blurb-cta">
					<img src="static/img/04_shangpinzhishu.png"
						style="width: 80%; height: auto;">
				</div>
			</div>
		</div>
	</div>
	<div class="row text-center qghb">
		<div class="col-xs-12 valuepropsheading ">
			<h1>股票期权</h1>
		</div>
	</div>
	<div class="row text-center qghb">
		<div class="b_1024">
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_gaoshouyi.png">
				<h2>高收益</h2>
				<p>全程服务，高效为您办理开户</p>
				<!-- <a href="https://www.azul.com/products/zing/" role="button"
				class="greenbtn" target="_self">Learn More About Zing</a> -->
			</div>
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_zichanpeizhi.png">
				<h2>资产配置</h2>
				<p>支持多家银行直接入金，入金便捷</p>
			</div>
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_caifuguanli.png">
				<h2>财富管理</h2>
				<p>优质流畅的用户体验，让您查询更便捷</p>
			</div>
			<div class="col-xs-12 col-sm-3 blurb-cta">
				<img src="static/img/icon_gaoshouyi.png">
				<h2>流动性强</h2>
				<p>固定的风险，高额的收益</p>
			</div>
		</div>
	</div>
	<div class="row qgh">
		<div class="b_1024">
			<div class="col-xs-12 col-sm-4">
				<img src="static/img/img_dibu.png" alt="" />
			</div>
			<div class="col-xs-12 col-sm-8">
				<h1>沪深两市选择众多</h1>
				<span> 沪深两市拥有 3000
					多家上市公司，整体市盈率较低，想象空间巨大，拥有更多的挖掘潜力，创业板具有高送配，高成长，题材多等特点，具有较大的投资价值。 </span>

			</div>
		</div>
	</div>

	<footer class="footer ">
		<div class="container">
			<div class="row footer-top">
				<div class="col-sm-6 col-lg-6">
					<h4>
						<img src="static/img/logo&txt.png">
					</h4>
					<p></p>
				</div>
				<div class="col-sm-6  col-lg-5 col-lg-offset-1">
					<div class="row about">
						<div class="col-xs-3">
							<h4>关于</h4>
							<ul class="list-unstyled">
								<li><a href="javascript:;">关于我们</a></li>
								<li><a href="javascript:;">广告合作</a></li>
								<li><a href="javascript:;">友情链接</a></li>
							</ul>
						</div>
						<div class="col-xs-3">
							<h4>联系方式</h4>
							<ul class="list-unstyled">
								<li><a>电子邮件</a></li>
							</ul>
						</div>
					</div>

				</div>
			</div>
			<hr>
			<!--  <div class="row footer-bottom">
        <ul class="list-inline text-center">
          <li>京ICP备11008151号</li><li>京公网安备11010802014853</li>
        </ul>
      </div> -->
		</div>
	</footer>

	<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
	<script type="text/javascript" src="static/bootcss/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="static/h-ui/js/common.js?ver=1.0.03"></script>

	<script type="text/javascript">
		function goIndex() {
			gohref('customer/index');
		}

		function login() {
			layer.open({
				type : 2,
				title : false,
				closeBtn : 0,
				shade : [ 0.7 ],
				area : [ '500px', '300px' ],
				anim : 2,
				content : [ 'customer/login', 'no' ],
			});
		}

		function reg() {
			layer.open({
				type : 2,
				title : false,
				closeBtn : 0,
				shade : [ 0.7 ],
				area : [ '500px', '400px' ],
				anim : 2,
				content : [ 'customer/reg', 'no' ],
			});
		}
	</script>

</body>
</html>