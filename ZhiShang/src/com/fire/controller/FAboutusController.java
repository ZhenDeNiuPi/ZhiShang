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
 * 关于我们Controller
 */
@Clear(LoginInterceptor.class)
public class FAboutusController extends Controller {

    //加载关于页面
    public void index(){
        setAttr("info",Db.findFirst("select * from info_tb"));
        setAttr("history",Db.find("select h.*,from_unixtime(h.time,'%Y-%m-%d') htime from history_tb h where if_show = 1 order by time asc"));
//        setAttr("zs1",Db.find("select * from book_tb where if_show = 1 limit 0,3"));
//        setAttr("zs2",Db.find("select * from book_tb where if_show = 1 limit 3,3"));
        setAttr("books",Db.find("select b.*,from_unixtime(b.get_time,'%Y-%m-%d') btime from book_tb b where if_show = 1 order by get_time asc"));
//        setAttr("zs2",Db.find("select * from book_tb where if_show = 1 limit 3,3"));
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
        renderTemplate("about.html");
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
