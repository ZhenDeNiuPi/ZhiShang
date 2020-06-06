package com.fire.controller;

import java.io.File;
import java.util.Map;

import com.fire.intercepter.FormOperaIntercepter;
import com.fire.model.Book;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.fire.util.Str2TimeStamp;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

public class BookController  extends Controller{
	private static String rootPath = "";
	static {
		rootPath = PathKit.getWebRootPath()+"/img/book";
	}
	private IBaseServiceImpl is = new IBaseServiceImpl();
	private Str2TimeStamp sts = new Str2TimeStamp();
	
	public void getDatas() {
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select b.id,from_unixtime(b.get_time,'%Y-%m-%d') getTime,b.name,b.content,b.if_show ";//select xxx,xxx,xxx 
        String from = " from book_tb b where 1=1 ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
	
	@Before(FormOperaIntercepter.class)
	public void addData() {
		Book b = getModel(Book.class,"b");
		String getTime = getPara("getTime");
		if(getTime != null || !"".equals(getTime)) b.set("get_time", sts.getTimeStamp(getTime)/1000);
		renderJson("num",b.save()?1:0);
	}
	
	public void getData() {
		String id = getPara("id");
		String select  = "select b.id,from_unixtime(b.get_time,'%Y-%m-%d') getTime,b.name,b.content,b.if_show "
        		+ " from book_tb b where id="+id;
		renderJson(Db.findFirst(select));
	}
	
	@Before(FormOperaIntercepter.class)
	public void updateData() {
		Book b = getModel(Book.class,"b");
		String getTime = getPara("getTime");
		if(getTime != null || !"".equals(getTime)) b.set("get_time", sts.getTimeStamp(getTime)/1000);
		renderJson("num",b.update()?1:0);
	}
	
	public void deleteDatas() {
		String ids = getPara("idStr");
		String sql = "delete from book_tb where id in ("+ids+")";
		renderJson("num",Db.update(sql));
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		String id = getPara("id");
		File old = new File(rootPath,id+".jpeg");
		if(old.exists()) old.delete();
	}
	
	public void ifSend() {
		String id = getPara("id");
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		File file = new File(dir,id+".jpeg");
		if(file.exists())renderJson("path","book/getPic?id="+id);
		else renderJson("path","");
	}
	
	public void uploadPic(){
		UploadFile file = getFile();
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		String id = getPara("id");
		File old = new File(rootPath,id+".jpeg");
		if(old.exists()) old.delete();
		file.getFile().renameTo(new File(rootPath,id+".jpeg"));
		renderJson("num",1);
	}

	public void getPic() {
		String id = getPara("id");
		File file = new File(rootPath,id+".jpeg");
		renderFile(file);
	}
}