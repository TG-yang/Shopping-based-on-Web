var E3MALL = {
	checkLogin : function(){
        // alert(3333);
		var _ticket = $.cookie("token");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + _ticket,
			//  jsonp ：执行 JSONP 请求时需要制定的回调函数名称，默认值是“callback”。
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var href = "http://localhost:8088/user/logout/"+_ticket
					var username = data.data.username;
					var html = username + "，欢迎来到宜立方购物网！<a href="+href+" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){

	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});