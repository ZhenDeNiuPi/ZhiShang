package com.fire.controller;

import java.util.HashMap;
import java.util.Map;

import com.fire.intercepter.LoginInterceptor;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

@Clear(LoginInterceptor.class)
public class FCaseController extends Controller {
	private IBaseServiceImpl is = new IBaseServiceImpl();

    public void index(){
    	String pageNum = getPara("pageNumber","1");
   	 Map<String,String[]> allParams= new HashMap<>();//获取前台传来的分页以及排序所需的参数
        String select  = "select id,title,content ";//select xxx,xxx,xxx 
        String from = " from case_tb where if_show=1 ";//from xxx ... where 1=1 
        allParams.put("pageSize", new String[]{"6"});
        allParams.put("pageNumber", new String[]{pageNum});
        Page page = is.query(select, from, allParams);
        page.setCurNum(Integer.parseInt(pageNum));
    	setAttr("page",page);
        setAttr("time",System.currentTimeMillis());
    	renderTemplate("product_list.html");
    }
}
