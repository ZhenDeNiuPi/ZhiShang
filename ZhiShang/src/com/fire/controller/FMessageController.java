package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 留言板
 */
@Clear(LoginInterceptor.class)
public class FMessageController extends Controller {

    public void index(){
        //加载公司数据
        setAttr("info", Db.findFirst("select * from info_tb"));
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
    	renderTemplate("message.html");
    }

    public List<Integer> getIndexPics() {
        String rootPath = PathKit.getWebRootPath()+"/img/rc";
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
