package com.fire.controller;

import com.fire.model.Info;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class UserController extends Controller{

	public void update() throws Exception{
    	Info info = getModel(Info.class,"u");
    	int num = 1;
    	if(Db.findFirst("select * from info_tb") == null ) {
    		if(!info.save())num = 0;
        	renderJson("num",num);
    		return ;
    	}
    	if(!info.update())num = 0;
    	renderJson("num",num);
	}

	public void getData() {
		renderJson(Db.findFirst("select * from info_tb"));
	}
}
