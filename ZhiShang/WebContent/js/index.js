const cards = document.querySelectorAll('.card');

/* View Controller
                                                  -----------------------------------------*/
const btns = document.querySelectorAll('.js-btn');
btns.forEach(btn => {
  btn.addEventListener('click', on_btn_click, true);
  btn.addEventListener('touch', on_btn_click, true);
});

function on_btn_click(e) {
	$("#loginError").text("");
	$("#changeError").text("");
  const nextID = e.currentTarget.getAttribute('data-target');
  const next = document.getElementById(nextID);
  if (!next) return;
  bg_change(nextID);
  view_change(next);
  return false;
}

/* Add class to the body */
function bg_change(next) {
  document.body.className = '';
  document.body.classList.add('is-' + next);
}

/* Add class to a card */
function view_change(next) {
  cards.forEach(card => {card.classList.remove('is-show');});
  next.classList.add('is-show');
}
var changeUrl = function(){
	$("#img").attr("src",'img?'+Math.random());
	$("#img2").attr("src",'img?'+Math.random());
}
$("#changePassButton").bind("click",function(){
	var account = $("#account2").val();
	var newpass = $("#passwordnew").val();
	$.ajax({
		type:"post",
		url:"findPass",
		data:$("#changeForm").serialize(),
		dataType:"json",
		error:function(XMLHttpRequest, textStatus, errorThrown){
			 BootstrapDialog.alert({title:"提示信息",message:"网络错误！"});
		 },
		success:function(result){
			if(result["num"]>0){
				$("#loginError").text("新密码设置成功！");
				$("account").val(account);
				$("password").val(newpass);
				  const next = document.getElementById("login");
				  bg_change("login");
				  view_change(next);
				//$("#changeError").html("新密码设置成功！<a class='btn btn-back js-btn' data-target='welcome'>返回</a>");
			}else {
				$("#changeError").text(result["errormessage"]);
			}
			changeUrl();
		}
	});
});
$("#submitButton").bind("click",function(){
	$.ajax({
		type:"post",
		url:"dologin",
		data:$("#loginForm").serialize(),
		dataType:"json",
		error:function(XMLHttpRequest, textStatus, errorThrown){
			 BootstrapDialog.alert({title:"提示信息",message:"网络错误！"});
		 },
		success:function(result){
			if(result["login"]==1){
				self.location='menu';
			}else {
				$("#loginError").text(result["errormessage"]);
			}
			changeUrl();
		}
	});
});

