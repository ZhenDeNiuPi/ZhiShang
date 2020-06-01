package com.fire.controller;

import com.fire.model.User;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.MD5Util;
import com.fire.util.Str2TimeStamp;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class UserController extends Controller{
	Str2TimeStamp sts = new Str2TimeStamp();
	private IBaseServiceImpl is = new IBaseServiceImpl();


	public void update() throws Exception{
    	User user = getModel(User.class,"u");
    	user.set("password", MD5Util.getMD5Code(getPara("u.password")));
    	Record u = getSessionAttr("user");
    	u.set("password", getPara("u.password"));
    	setSessionAttr("user", u);
    	int num = 1;
    	if(!user.update())num = 0;
    	renderJson("num",num);
	}

	public void getData() {
		User u = new User().findById(((Record)getSessionAttr("user")).getLong("id"));
		u.set("password", ((Record)getSessionAttr("user")).getStr("password"));
		renderJson(u);
	}
}
