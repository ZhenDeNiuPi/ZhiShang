package com.fire.intercepter;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;


public class LoginInterceptor implements Interceptor{
	
	/**
	 * 用户登录的全局拦截器
	 */
	public void intercept(Invocation inv) {
		
		Controller controller =inv.getController();
		String actKey = inv.getActionKey(); // 默认就是ActionKey
		
		String error = controller.getSessionAttr("errormessage");
		Object user = controller.getSessionAttr("user");
		if(user == null){
			String zs = controller.getPara("zs");
			if(zs!=null){
				inv.invoke();
			}else if(actKey.equals("/manage/dologin")||
					actKey.equals("/manage")||
					actKey.equals("/news/getContentPic")||
					actKey.equals("/user/sendValidate")
					) 
				inv.invoke();
			else {
				if(controller.getRequest().getHeader("x-requested-with") !=null && 
					controller.getRequest().getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
					if("系统发生未知错误！请稍候再尝试登录！".equals(error)) controller.getResponse().addHeader("sessionstatus", "break");
					else controller.getResponse().addHeader("sessionstatus", "timeout");
					return;
				}else {
					extractedLogin(controller);
				}
			}
		}else{
			inv.invoke();
		}
	}
	private void extractedLogin(Controller controller) {
		removeSession(controller);
		controller.redirect("/manage");
	}
	
	private void removeSession(Controller controller){
		controller.getSession().removeAttribute("errormessage");
		controller.getSession().removeAttribute("authlist");
		controller.getSession().removeAttribute("menuauthlist");
		controller.getSession().removeAttribute("loginuin");
	}
}
