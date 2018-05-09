<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="static/inspinia/css/plugins/sweetalert/sweetalert.css"
	rel="stylesheet">
<!-- Sweet alert -->
<script src="static/inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>	
<script src="static/h-ui/js/common.js"></script>	

<div class="row border-bottom">
	<nav class="navbar navbar-static-top " role="navigation"
		style="margin-bottom: 0">
		<div class="navbar-header">
			<a class="navbar-minimalize minimalize-styl-2 btn btn-primary "><i class="fa fa-bars"></i> </a>
		</div>
		<ul class="nav navbar-top-links navbar-right">
			<li><a href="javascript:;" onclick="logout(this)"> <i class="fa fa-sign-out"></i> 退出
			</a></li>
		</ul>
	</nav>
</div>

<script>
function logout(obj) {
	swal({
		title : "你确定要退出当前账号吗？？",
		text : "",
		type : "warning",
		showCancelButton : true,
		cancelButtonText :'取消',
		confirmButtonColor : "#DD6B55",
		confirmButtonText : "确定",
		closeOnConfirm : false
	},function(){
		gohref('partner/logout');
	});
}


function lostpwd(){
	layer.open({
		type: 2,
		maxmin: false,
		shadeClose: true,
		title: "修改密码",
		area: ['400px', '350px'],
		content: ['partner/changepwd', 'no']
	})
}


</script>