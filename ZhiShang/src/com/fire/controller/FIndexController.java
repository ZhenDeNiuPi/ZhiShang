package com.fire.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
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
        setAttr("books",Db.find("select * from book_tb where if_show=1 order by get_time desc limit 4"));
        //加载案例数据
        setAttr("cases",Db.find("select * from case_tb where if_show=1 order by ctime desc limit 8"));
        //加载新闻数据
        setAttr("news",Db.find("select * from news_tb where if_show=1 and type=0 order by time desc limit 4"));
        //加载轮播图
        setAttr("rcs",getIndexPics("/img/rc"));
        //加载战略合作图
        setAttr("scs",getIndexPics("/img/sc"));
        setAttr("time",System.currentTimeMillis());
        renderTemplate("index.html");
    }

    public List<Integer> getIndexPics(String path) {
    	String rootPath = PathKit.getWebRootPath()+path;
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		File[] files = new File(rootPath).listFiles();
		List<Integer> names = new ArrayList<>();
		if(files != null && files.length > 0) {
			for(File file : files) {
				names.add(Integer.parseInt(file.getName().substring(0,file.getName().indexOf("."))));
			}
			Collections.sort(names);
		}
		return names;
    }
}
