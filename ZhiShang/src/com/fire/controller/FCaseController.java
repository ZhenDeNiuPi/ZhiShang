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
public class FCaseController extends Controller {
	private IBaseServiceImpl is = new IBaseServiceImpl();

    public void index(){
        //加载公司数据
        setAttr("info", Db.findFirst("select * from info_tb"));
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
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
    	renderTemplate("product_list.html");
    }

    public void toCaseInfo(){
        String id = getPara("id");
//        System.out.println(newsId);
        setAttr("info", Db.findFirst("select * from info_tb"));
        setAttr("case", Db.findFirst("select id,title,content"
        		+ ",address from case_tb "
    			+ "where id="+id));
        List<Integer> pics = getPics(id);
        setAttr("pics",pics);
        setAttr("picsize",pics.size());
        //加载轮播图
        setAttr("rcs",getIndexPics());
        setAttr("time",System.currentTimeMillis());
        renderTemplate("product_list_content.html");
    }
    

	public List<Integer> getPics(String id) {
		String rootPath = PathKit.getWebRootPath()+"/img/case";
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		File[] files = new File(rootPath).listFiles();
		List<Integer> names = new ArrayList<>();
		if(files != null && files.length > 0) {
			for(File file : files) {
				String fileName = file.getName();
				if(fileName.startsWith(id+"_"))
				names.add(Integer.parseInt(fileName.substring(fileName.indexOf("_")+1,fileName.indexOf("."))));
			}
			Collections.sort(names);
		}
		return names;
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
