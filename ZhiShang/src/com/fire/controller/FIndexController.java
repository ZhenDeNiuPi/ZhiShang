package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

/**
 * 首页Controller
 */

@Clear(LoginInterceptor.class)
public class FIndexController extends Controller {

    public void index(){
        //加载关于我们数据
        setAttr("info", Db.findFirst("select * from info_tb"));
        //加载证书数据
        setAttr("books",Db.find("select * from book_tb where if_show=1 limit 4"));
        //加载案例数据
        setAttr("cases",Db.find("select * from case_tb where if_show=1 limit 4"));
        //加载新闻数据
        setAttr("news",Db.find("select * from news_tb where if_show=1 limit 4"));
        setAttr("time",System.currentTimeMillis());
        renderTemplate("index.html");
    }
}
