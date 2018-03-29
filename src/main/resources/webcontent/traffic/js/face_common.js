var Face = window.Face || (function(){

var that = {};
/**
 * 提交服务
 */
that.submit = function(apiurl, data, callback){
	data.token = sessionStorage.token || '';
	$.post(apiurl, data, function(ret){
		if(ret.code == '-99' || ret.code == '-98'){
			Ethink.error(ret.code + '||未登录', '', function(){
				window.location = "/index.html";
			});
		}else{
			callback(ret);
		}
	});
}

that.submitSync = function(apiurl, datas){
	datas.token = sessionStorage.token || '';
	var result = null;
	$.ajax({
		url:apiurl,
		type:"POST",
		data:datas, 
		async: false,
		success:function(ret){
    		if(ret.code == '-99' || ret.code == '-98'){
				Ethink.error(ret.code + '|未登录', '', function(){
					window.location = "/index.html";
				});
    		}else{
    			result = ret;
    		}
    	}
	});
	
	return result;
}

//页面跳转回主页
$('.menu li a').click(function(){ 
    var id = $(this).attr('id');
    localStorage.setItem("MenuId",id);  
    location.href = '/main.html';
})




return that;
})();
window.Face = Face;
