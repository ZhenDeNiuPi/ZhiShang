package com.fire.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

/**
 * 首页Controller
 */
public class FIndexController extends Controller {

    public void index(){
        //加载关于我们数据
        setAttr("info", Db.findFirst("select * from info_tb"));
        //加载证书数据
        setAttr("books",Db.find("select b.id,from_unixtime(b.get_time,'%Y-%m-%d') getTime,b.name,b.content,b.if_show from book_tb limit 1,4"));
        render("index.html");
    }
}
