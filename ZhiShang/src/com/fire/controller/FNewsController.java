package com.fire.controller;

import java.util.HashMap;
import java.util.Map;

import com.fire.intercepter.LoginInterceptor;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
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
}
