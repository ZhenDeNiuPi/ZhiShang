package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Clear(LoginInterceptor.class)
public class FBookController extends Controller {
	private IBaseServiceImpl is = new IBaseServiceImpl();

    public void index(){
        //加载公司数据
    	setAttr("info", Db.findFirst("select * from info_tb"));
        String pageNum = getPara("pageNumber","1");
        Map<String,String[]> allParams= new HashMap<>();//获取前台传来的分页以及排序所需的参数
        String select  = "select id,name,content,"
        		+ "from_unixtime(get_time,'%Y-%m-%d') gtime ";//select xxx,xxx,xxx 
        String from = " from book_tb where if_show=1 ";//from xxx ... where 1=1 
        allParams.put("pageSize", new String[]{"6"});
        allParams.put("pageNumber", new String[]{pageNum});
        Page page = is.query(select, from, allParams);
        page.setCurNum(Integer.parseInt(pageNum));
        setAttr("page",page);
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
    	renderTemplate("book_list.html");
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
