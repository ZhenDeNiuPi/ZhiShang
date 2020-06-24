package com.fire.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fire.intercepter.FormOperaIntercepter;
import com.fire.intercepter.LoginInterceptor;
import com.fire.util.HttpClientResult;
import com.fire.util.MD5Util;
import com.fire.util.ZsSmsUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.captcha.CaptchaRender;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class LoginController extends Controller{
	static Logger logger = Logger.getLogger(LoginController.class);

	public void index() {
		render("index.html");
	}
	
	public void menu() {
		render("menu.html");
	}

	public void welcome() {
		render("welcome.html");
	}

	public void us() {
		render("information.html");
	}

	public void rc() {
		render("rc.html");
	}
	
	public void book() {
		render("book.html");
	}
	
	public void news() {
		render("news.html");
	}
	
	public void cases() {
		render("case.html");
	}
	
	public void history() {
		render("history.html");
	}

	@Clear
	public void img(){
		renderCaptcha();
	}

	public void dologin() {
		
		String ip = getIp(getRequest());
		Record ipcount = Db.findFirst("select ifnull(error_time,0) count from login_iperror_tb where lock_ip='"+ip+"'");
		String count = ipcount==null?"0":(ipcount.get("count")==null?"0":ipcount.get("count")+"");
		int count1 = Integer.parseInt(count);
		if(count1>0) {
			long time = Long.parseLong(Db.findFirst("select ifnull(max(lock_time),0) time from login_iperror_tb where lock_ip='"+ip+"'").get("time")+"");//锁定的时间
			if((System.currentTimeMillis()-(time*1000))<1800000){
				//继续给出提示 直到冻结时间结束 这里通过定时服务每分钟判断时间来解冻ip
				renderJson("errormessage","非法登录五次！IP已锁定，请半小时后再试！");
				return;
			}
		}
		
		String username = getPara("u.account");
		String password = getPara("u.password");
		String validate = getPara("validate");
		
		boolean valiResult = true;
		
		boolean devMode = PropKit.getBoolean("devMode", true);
		if(!devMode) {//非开发模式
			valiResult = CaptchaRender.validate(this, validate);
		}
		if ((username == null || "".equals(username)) || (password == null || "".equals(password)
				|| "".equals(validate)|| "".equals(validate))) {
			renderJson("errormessage", "账户、密码、验证码必须输入！");
			return;
		}
		Record user = null;
		try{
			user = Db.findFirst("select * from user_tb where account='"+username+
					"' and password='"+MD5Util.getMD5Code(password)+"'");
		}catch(Exception e) {
			user = null;
		}
		
		if (!valiResult||user == null) {
			int count2 = Integer.parseInt(Db.findFirst("select ifnull(count(0),0) count from login_iperror_tb where lock_ip='"+ip+"'").get("count")+"");
			if(count2>0) {
				Db.update("update login_iperror_tb set error_time = error_time+1");				
			}else {
				Db.update("insert into login_iperror_tb (lock_ip,error_time) values('"+ip+"',1)");	
			}
			int times = Integer.parseInt(Db.findFirst("select error_time from login_iperror_tb where lock_ip='"+ip+"'").get("error_time")+"");
			if(times>=5) {
					Db.update("update login_iperror_tb set lock_time = "+ (System.currentTimeMillis()/1000)+" where lock_ip='"+ip+"'");
					renderJson("errormessage", "非法登录五次！IP已锁定，请半小时后再试！");
					return;
			}
			renderJson("errormessage", ""+(!valiResult?"验证码":"账号密码")+"不正确！非法登录五次将锁定IP！");
			return;
		}
//		Long uid = Long.parseLong(user.get("id")+"");
		user.set("password", password);
		setSessionAttr("user", user);
//		Db.update("update user_tb set if_login=1,last_ip='"+ip+"',last_login_time="+System.currentTimeMillis()/1000+" where id="+uid);
		Db.update("update login_iperror_tb set error_time=0 where lock_ip='"+ip+"'");
		
		renderJson("login",1);
    }
	@Clear
	public void logout(){
		 HttpSession session = getSession(false);
		 if(session==null){  
			 renderJson("num",2); 
	            return;  
	     } 
		 removeSessionAttr("user");
		 renderJson("num",1); 
	}
	
	@Clear
	public void getIfUser(){
		 Record user = getSessionAttr("user");
		 Map<String,Object> map = new HashMap<>();
		 if(user == null) 
			 map.put("num", 0);
		 else 
			 map.put("num", 1);
		 
		 renderJson(map); 
	}

	@Clear(LoginInterceptor.class)
	@Before(FormOperaIntercepter.class)
	public void findPass() throws Exception {
		String ip = getIp(getRequest());
		String validate = getPara("validate");
		boolean valiResult = true;
		boolean devMode = PropKit.getBoolean("devMode", true);
		if(!devMode) {//非开发模式
			valiResult = CaptchaRender.validate(this, validate);
		}

		String account = getPara("u.account");
		String password = getPara("u.password","");
		String passwordnew = getPara("u.passwordnew","");
		Record user = null;
		try{
			user = Db.findFirst("select * from user_tb where account='"+account+
					"' and password='"+MD5Util.getMD5Code(password)+"'");
		}catch(Exception e) {
			user = null;
		}

		if (!valiResult||user == null) {
			int count2 = Integer.parseInt(Db.findFirst("select ifnull(count(0),0) count from login_iperror_tb where lock_ip='"+ip+"'").get("count")+"");
			if(count2>0) {
				Db.update("update login_iperror_tb set error_time = error_time+1");				
			}else {
				Db.update("insert into login_iperror_tb (lock_ip,error_time) values('"+ip+"',1)");	
			}
			int times = Integer.parseInt(Db.findFirst("select error_time from login_iperror_tb where lock_ip='"+ip+"'").get("error_time")+"");
			if(times>=5) {
					Db.update("update login_iperror_tb set lock_time = "+ (System.currentTimeMillis()/1000)+" where lock_ip='"+ip+"'");
					renderJson("errormessage", "非法登录五次！IP已锁定，请半小时后再试！");
					return;
			}
			renderJson("errormessage", "验证码或密码不正确！非法登录五次将锁定IP！");
			return;
		}
		String md5Pass=MD5Util.getMD5Code(passwordnew);
		Db.update("update user_tb set password='"+md5Pass+"' where account='"+account+"'");
		renderJson("num",1);
	}
	
	public static String getIp(HttpServletRequest request){

		  String ip = request.getHeader("x-forwarded-for"); 
		  if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		  ip = request.getHeader("Proxy-Client-IP"); 
		  } 
		  if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		  ip = request.getHeader("WL-Proxy-Client-IP"); 
		  } 
		  if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getRemoteAddr(); 
		  } 
		  return ip; 
	}
	
	public static void getHttpFrom(HttpServletRequest request) {
		String requestHeader = request.getHeader("user-agent");
        if(isMobileDevice(requestHeader)){
            logger.debug("使用手机浏览器");
        }else{
            logger.debug("使用web浏览器");
        }
	}
	
	public static boolean  isMobileDevice(String requestHeader){
        /**
         * android : 所有android设备
         * mac os : iphone ipad
         * windows phone:Nokia等windows系统的手机
         */
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        if(requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){
                return true;
            }
        }
        return false;
}
	
	public void contactUs() throws Exception {

		String ip = getIp(getRequest());
		Long before24 = System.currentTimeMillis()/1000-86400;
		Record ipcount = Db.findFirst("select ifnull(count(*),0) count from contact_ip_tb where ip='"+ip+"' and time>"+before24);
		int count1 = Integer.parseInt(ipcount.get("count")+"");
		if(count1>4) {
			renderJson("errormessage","24小时内最多发送五次联系信息！");
			return;
		}
		String recieve = Db.findFirst("select mobile from info_tb ").get("mobile")+"";
		String mobile = getPara("mobile","");
		String content = getPara("content","");
		String name = getPara("name","");
		HttpClientResult hcr = 
				ZsSmsUtil.sendSms(recieve, mobile, content, name);
		System.out.println(hcr.getCode()+"..."+ hcr.getContent());
		Db.update("insert into contact_ip_tb (ip,time) values "
				+ "('"+ip+"',"+System.currentTimeMillis()/1000+")");
		if(hcr.getCode() != 200) renderJson("errormessage","网络错误！"); 
		else renderJson(hcr);
	}
}






