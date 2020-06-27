package com.fire.controller;

import java.io.File;
import java.util.Map;

import com.fire.intercepter.FormOperaIntercepter;
import com.fire.model.History;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.fire.util.Str2TimeStamp;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

public class HistoryController  extends Controller{
	private static String rootPath = "";
	static {
		rootPath = PathKit.getWebRootPath()+"/img/history";
	}
	private IBaseServiceImpl is = new IBaseServiceImpl();
	private Str2TimeStamp sts = new Str2TimeStamp();
	
	public void getDatas() {
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select h.id,from_unixtime(h.time,'%Y-%m-%d') hTime,h.title,h.content,h.if_show ";//select xxx,xxx,xxx 
        String from = " from history_tb h where 1=1 ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
	
	@Before(FormOperaIntercepter.class)
	public void addData() {
		History h = getModel(History.class,"h");
		String hTime = getPara("hTime");
		if(hTime != null || !"".equals(hTime)) h.set("time", sts.getTimeStamp(hTime)/1000);
		renderJson("num",h.save()?1:0);
	}
	
	public void getData() {
		String id = getPara("id");
		String select  = "select h.id,from_unixtime(h.time,'%Y-%m-%d') hTime,h.title,h.content,h.if_show "
				+ " from history_tb h where id="+id;//from xxx ... where 1=1 
        renderJson(Db.findFirst(select));
	}
	
	@Before(FormOperaIntercepter.class)
	public void updateData() {
		History h = getModel(History.class,"h");
		String hTime = getPara("hTime");
		if(hTime != null || !"".equals(hTime)) h.set("time", sts.getTimeStamp(hTime)/1000);
		renderJson("num",h.update()?1:0);
	}
	
	public void deleteDatas() {
		String ids = getPara("idStr");
		String sql = "delete from history_tb where id in ("+ids+")";
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
		if(file.exists())renderJson("path","history/getPic?id="+id);
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