<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		$('[data-toggle="popover"]').popover({
			html : true
		})
	})
</script>
<header class="navbar-wrapper">
	<div class="navbar navbar-fixed-top">
		<div class="container-fluid cl">
			<a class="logo navbar-logo f-l mr-10 hidden-xs"
				href="#">欢迎进入“期权”会员中心 </a> 

			<nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
				<ul class="cl">
					<li class="dropDown dropDown_hover"><a onclick="cal()"
						class="dropDown_A"><i class="Hui-iconfont">&#xe61e;</i>期权计算器</a>
					</li>
					<li class="dropDown dropDown_hover"><a 
						class="dropDown_A">${currentUser.name } <i class="Hui-iconfont">&#xe6d5;</i></a>
						<ul class="dropDown-menu menu radius box-shadow">
							<li><a href="javascript:;" onClick="lostpwd()">修改密码</a></li>
							<li><a onclick="logout(this)">退出</a></li>
						</ul></li>
				</ul>
			</nav>
		</div>
	</div>
</header>
<script>
function logout(obj) {
	layer.confirm('确定要退出当前账号吗？', {
		icon : 3,
		title : '提示'
	}, function(index) {
		layer.close(index);
		gohref('customer/logout');
	});
}


function cal(){
	layer.open({
		type: 2,
		maxmin: false,
		shadeClose: true,
		title: "期权计算器",
		area: ['800px', '600px'],
		content: ['customer/calcu', 'no']
	})
}

function lostpwd(){
	layer.open({
		type: 2,
		maxmin: false,
		shadeClose: true,
		title: "修改密码",
		area: ['400px', '350px'],
		content: ['customer/changepwd', 'no']
	})
}

</script>