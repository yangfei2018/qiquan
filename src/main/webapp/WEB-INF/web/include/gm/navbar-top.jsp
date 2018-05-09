<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="static/inspinia/css/plugins/sweetalert/sweetalert.css"
	rel="stylesheet">
<!-- Sweet alert -->
<script src="static/inspinia/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="static/inspinia/js/jquery-3.1.1.min.js"></script>	
<script src="static/h-ui/js/common.js"></script>	

<div class="row border-bottom">
	<nav class="navbar navbar-static-top " role="navigation"
		style="margin-bottom: 0">
		<div class="navbar-header">
			<a class="navbar-minimalize minimalize-styl-2 btn btn-primary "><i class="fa fa-bars"></i> </a>
		</div>
		<ul class="nav navbar-top-links navbar-right">
			<li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary" id="unReadCount">0</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts" id="messageList">
                        <li>
                            <a href="mailbox.html">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="profile.html">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="grid_options.html">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="notifications.html">
                                    <strong>See All Alerts</strong>
                                    <i class="fa fa-angle-right"></i>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>
			<li><a href="javascript:;" onclick="logout(this)"> <i class="fa fa-sign-out"></i> 退出
			</a></li>
		</ul>
	</nav>
</div>

<script>

$(function(){
	unReadMessage();
	
	setInterval('unReadMessage()',20000);
})


function buildHref(mainId){
	switch(mainId){
	case 1:
		return 'gm/userlist?status=2';
		break;
	case 2:
		return ('gm/tradelist');
		break;
	case 3:
		return ('gm/orderlist');
		break;
	case 4:
		break;
	case 5:
		return ('gm/withdrawcashlist');	
	}
}

function unReadMessage(){
	$.ajax({
		url : 'gm/json/checkUnReadMessage',
		type : 'get',
		dataType : "json",
		data : {},
		success : function(res) {
			console.log(res);
			$("#messageList").html('');
			if (res.length>0){
				$("#unReadCount").html(res.length);
				$("#unReadCount").show();
				for(var i=0;i<res.length;i++){
					
					$("#messageList").append(' <li>\
                            <a href="'+buildHref(+res[i].mainId)+'">\
                            <div>\
                                <i class="fa fa-envelope fa-fw"></i>'+res[i].body+'\
                                <span class="pull-right text-muted small">'+res[i].createFormat+'</span>\
                            </div>\
                        </a>\
                    </li>\
                    <li class="divider"></li>');
				}
				
				$("#messageList").append('<li>\
                <div class="text-center link-block">\
                    <a href="gm/index">\
                        <strong>查看全部日志</strong>\
                        <i class="fa fa-angle-right"></i>\
                    </a>\
                </div>\
            </li>');
				
			}else{
				$("#unReadCount").hide();
				$("#messageList").append('<li>\
                <a href="gm/index">\
                    <div>\
                       暂无记录\
                    </div>\
                </a>\
            </li>');
			}
		}
	});
}

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
		gohref('gm/logout');
	});
}

function lostpwd(){
	layer.open({
		type: 2,
		maxmin: false,
		shadeClose: true,
		title: "修改密码",
		area: ['400px', '350px'],
		content: ['gm/changepwd', 'no']
	})
}
</script>