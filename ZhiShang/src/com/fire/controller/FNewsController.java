package com.fire.controller;

import java.io.File;
import java.util.*;

import com.fire.intercepter.LoginInterceptor;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;

@Clear(LoginInterceptor.class)
public class FNewsController extends Controller {
	private IBaseServiceImpl is = new IBaseServiceImpl();

    public void index(){
    	String pageNum = getPara("pageNumber","1");
    	 Map<String,String[]> allParams= new HashMap<>();//获取前台传来的分页以及排序所需的参数
         String select  = "select n.id,from_unixtime(n.time,'%Y-%m-%d') ntime,n.title,n.stitle,n.creator,"
         		+ "n.content,n.if_show ";//select xxx,xxx,xxx 
         String from = " from news_tb n where if_show=1 ";//from xxx ... where 1=1 
         allParams.put("pageSize", new String[]{"6"});
         allParams.put("pageNumber", new String[]{pageNum});
         Page page = is.query(select, from, allParams);
         page.setCurNum(Integer.parseInt(pageNum));
    	setAttr("news_3", Db.find("select id,title,stitle,"
    			+ "from_unixtime(n.time,'%y-%m-%d') ntime from news_tb n "
    			+ "where if_show=1 order by n.time desc limit 3"));
    	setAttr("page",page);
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
    	renderTemplate("news_list.html");
    }

    public void toNewsInfo(){
        String id = getPara("id");
//        System.out.println(newsId);
        setAttr("new", Db.findFirst("select id,title,stitle,content,creator,"
    			+ "from_unixtime(n.time,'%y-%m-%d') ntime from news_tb n "
    			+ "where id="+id));
        renderTemplate("news_list_content.html");
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
