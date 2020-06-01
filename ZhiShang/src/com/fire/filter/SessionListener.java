package com.fire.filter;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.jfinal.plugin.activerecord.Db;

public class SessionListener implements HttpSessionListener {

	 public void sessionCreated(HttpSessionEvent arg0) {

	  // session创建时执行

//	  SimpleDateFormat simpleFormat = new SimpleDateFormat("mm-ss-ms");

//	  String nowtimes = simpleFormat.format(new Date());

//	  User u=null;


//	  HttpSession ses= arg0.getSession();
//	  String id=ses.getId()+"_"+ses.getCreationTime();
//	  System.out.println(id+"执行。。 当前时间："+nowtimes);

	 }

	 public void sessionDestroyed(HttpSessionEvent arg0) {

	  // session失效时执行

//	  SimpleDateFormat simpleFormat = new SimpleDateFormat("mm-ss-ms");

//	  String nowtimes = simpleFormat.format(new Date()); 
	  HttpSession ses= arg0.getSession();
	  String idStr = ses.getAttribute("loginuin")+"";
	  Long id = -1L;
	  try{
		  id = Long.parseLong(idStr);
	  }catch(Exception e) {
		  id = -1L;
	  }
	  if(id>-1) {
		  Db.update("update user_tb set if_login=0 where id="+id);//更改登录状态
	  }
//	  System.out.println(id+"session失效了。。 结束时间： "+nowtimes);

	 }

	}
