$(function(){
	
	//全局的ajax访问，处理ajax清求时sesion超时  
    $.ajaxSetup({   
        contentType:"application/x-www-form-urlencoded;charset=utf-8",   
        complete:function(XMLHttpRequest,textStatus){   
            var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，  
            if(sessionstatus=="timeout"){   
               //如果超时就处理 ，指定要跳转的页面  这里必须是alert 否则直接就跳了
               alert("连接超时，请重新登录！");
               top.location.href = postPath+"/";
              
            }else if(sessionstatus=="break"){
                alert("网络错误！");
            	top.location.href = postPath+"/";
            }else if(sessionstatus=="fuck"){
                alert("非法访问！");
            	top.location.href = postPath+"/";
            }  
        },
        beforeSend:function(XMLHttpRequest,arguments){
        	var objs = arguments.data;
        	if(arguments.data!=undefined && arguments.data!= null && arguments.data.length>0){
        		var encodeStr = '';
	        	var keys = arguments.data.split('&');//留下xxx=的数组
	        	for(var i = 0;i<keys.length;i++){
	        		var temp =  keys[i];
	        		var keyValue = temp.split('=');//留下xxx 如果有val的话还有val
	        		//搞点骚东西
	        		if(i<9)
//	        			encodeStr += encode64(keyValue[0]+'0'+(i+1)).replace(/=/g,'···').replace('+','*')+"·=";
	        			keyValue[0] = keyValue[0]+'0'+(i+1);
	        		else
//		        		encodeStr += encode64(keyValue[0]+(i+1)).replace(/=/g,'···').replace('+','*')+"·=";
	        			keyValue[0] = keyValue[0]+''+(i+1);
	        		encodeStr += encodeURIComponent(Dragon(keyValue[0]))+'=';
	        		if(keyValue.length>1&&keyValue[1].length>0) encodeStr += encodeURIComponent(Dragon(keyValue[1]));
	        		if(i<keys.length-1)encodeStr += '&';
//	        		else encodeStr += '&';
	        	}
	        	var time = "ajaxTime";
	        	var l = keys.length+1;
	        	if(l<=9) time += '0'+l;
	        	else time += l;
	        	encodeStr +="&"+encodeURIComponent(Dragon(time))+"="
	        	+encodeURIComponent(Dragon(Math.round(new Date().getTime()/1000)+""));
	        	encodeStr += "&"+encodeURIComponent("05fsKaKm343jELhTpa15fpgx")+"="
	        	+encodeURIComponent("Btojbaal1mPjiPBijaqW1kax");
//	        	var aaa = encodeURIComponent(encodeStr);
	        	console.log(encodeStr);
	        	arguments.data = encodeStr;
        	}
        }
    });
});

function Dragon(str){
	if(str==null||str==undefined||str.length==0)return '';
	var front = "";
	var back = "";
	var d = "";
	var length = str.length
	for(var i=length-1;i>=0;i--){
		d = getDragon();
		var front = str.substring(0,i);
		var back = str.substring(i);
		str = front + d + back;
	}
	return str;
}
function getDragon(){
	var a = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
			'Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f',
			'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
			'w','x','y','z','0','1','2','3','4','5','6','7','8','9'];//62个
	var b = "";
	for(var i=0;i<5;i++){
		var ran = Math.floor(Math.random()*62);//0~62
		b+=a[ran];
	}
	return b;
}

function ajaxImpl(type,url,data,dataType){
	var resultData = {};
	$.ajax({
		type:"post",
		url:url,
		data:$("#welcomeForm").serialize(),
		dataType:"json",
		error:function(XMLHttpRequest, textStatus, errorThrown){
			 BootstrapDialog.alert({title:"提示信息",message:"网络错误！"});
			 return null;
		 },
		success:function(result){
			resultData = result;
			return resultData;
		}
	});
}
