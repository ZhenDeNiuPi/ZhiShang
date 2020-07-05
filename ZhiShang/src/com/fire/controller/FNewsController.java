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
import com.jfinal.plugin.activerecord.Record;

@Clear(LoginInterceptor.class)
public class FNewsController extends Controller {
	private IBaseServiceImpl is = new IBaseServiceImpl();

    public void index(){
    	//获取新闻类型 默认0新闻资讯 否则是1 则是行业资讯
    	String news_type = getPara("type","0");
        //加载公司数据
        setAttr("info", Db.findFirst("select * from info_tb"));
    	String pageNum = getPara("pageNumber","1");
    	 Map<String,String[]> allParams= new HashMap<>();//获取前台传来的分页以及排序所需的参数
         String select  = "select n.id,from_unixtime(n.time,'%Y-%m-%d') ntime,n.title,n.stitle,n.creator,"
         		+ "n.content,n.if_show ";//select xxx,xxx,xxx 
         String from = " from news_tb n where if_show=1 and type="+news_type;//from xxx ... where 1=1 
         allParams.put("pageSize", new String[]{"6"});
         allParams.put("pageNumber", new String[]{pageNum});
         Page page = is.query(select, from, allParams);
         page.setCurNum(Integer.parseInt(pageNum));
    	setAttr("news_3", Db.find("select id,title,stitle,content,creator,"
    			+ "from_unixtime(n.time,'%y-%m-%d') ntime from news_tb n "
    			+ "where if_show=1  and type="+news_type+" order by n.time desc limit 3"));
    	String type = "新闻资讯";
    	if("1".equals(news_type)) type = "行业资讯";
    	if("2".equals(news_type)) type = "产品模块";
    	//用于那个框的显示
    	setAttr("type",type);
    	//用于分页链接显示
    	setAttr("typeNum",news_type);
    	setAttr("page",page);
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
    	renderTemplate("news_list.html");
    }

    public void toNewsInfo(){
        String id = getPara("id");
//        System.out.println(newsId);
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
        setAttr("info", Db.findFirst("select * from info_tb"));
        Record rec = Db.findFirst("select id,title,stitle,content,creator,"
    			+ "from_unixtime(n.time,'%y-%m-%d') ntime,type from news_tb n "
    			+ "where id="+id);
        String typeStr = "新闻资讯";
        String type = rec.get("type")+"";
    	if("1".equals(type)) typeStr = "行业资讯";
    	if("2".equals(type)) typeStr = "产品模块";
    	rec.set("content", (rec.get("content")+"").replaceAll("style=\"width: 546px;\"", ""));
//    	System.out.println(rec.get("content")+"");
        setAttr("new", rec);
        setAttr("type", typeStr);
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
