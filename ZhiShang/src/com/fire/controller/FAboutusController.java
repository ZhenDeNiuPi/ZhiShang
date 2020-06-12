package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

/**
 * 关于我们Controller
 */
@Clear(LoginInterceptor.class)
public class FAboutusController extends Controller {

    //加载关于页面
    public void index(){
        setAttr("info",Db.findFirst("select * from info_tb"));
        render("about.html");
    }

}
