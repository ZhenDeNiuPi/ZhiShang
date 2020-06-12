package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

/**
 * 联系我们
 */
@Clear(LoginInterceptor.class)
public class FContactController extends Controller {

    public void index(){
        //加载关于联系我们数据
        setAttr("info", Db.findFirst("select * from info_tb"));
        renderTemplate("contact.html");
    }

    public void map(){
        renderTemplate("map.html");
    }
}
