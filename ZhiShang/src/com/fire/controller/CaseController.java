package com.fire.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fire.intercepter.FormOperaIntercepter;
import com.fire.intercepter.LoginInterceptor;
import com.fire.model.Case;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.fire.util.Str2TimeStamp;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

public class CaseController  extends Controller{
	private static String rootPath = "";
	static {
		rootPath = PathKit.getWebRootPath()+"/img/case";
	}
	private IBaseServiceImpl is = new IBaseServiceImpl();
	private Str2TimeStamp sts = new Str2TimeStamp();
	
	public void getDatas() {
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select c.id,from_unixtime(c.ctime,'%Y-%m-%d') ctime,c.title,c.address,c.content,c.if_show ";//select xxx,xxx,xxx 
        String from = " from case_tb c where 1=1 ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
	
	@Before(FormOperaIntercepter.class)
	public void addData() {
		Case c = getModel(Case.class,"c");
		String ctime = getPara("ctime");
		if(ctime != null || !"".equals(ctime)) c.set("ctime", sts.getTimeStamp(ctime)/1000);
		renderJson("num",c.save()?1:0);
	}
	
	public void getData() {
		String id = getPara("id");
		String select  = "select c.id,from_unixtime(c.ctime,'%Y-%m-%d') ctime,c.title,c.address,c.content,c.if_show "
        		+ " from case_tb c where id="+id;
		renderJson(Db.findFirst(select));
	}
	
	@Before(FormOperaIntercepter.class)
	public void updateData() {
		Case c = getModel(Case.class,"c");
		String ctime = getPara("ctime");
		if(ctime != null || !"".equals(ctime)) c.set("ctime", sts.getTimeStamp(ctime)/1000);
		renderJson("num",c.update()?1:0);
	}
	
	public void deleteDatas() {
		String ids = getPara("idStr");
		String sql = "delete from case_tb where id in ("+ids+")";
		renderJson("num",Db.update(sql));
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		String id = getPara("id");
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.getName().startsWith(id+"_")) file.delete(); 
		}
	}
	
	public void uploadPic(){
		UploadFile file = getFile();
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		String seeId = getPara("seeId");
		String picId = getPara("picId");
		File old = new File(rootPath,seeId+"_"+picId+".jpeg");
		if(old.exists()) old.delete();
		file.getFile().renameTo(new File(rootPath,seeId+"_"+picId+".jpeg"));
		renderJson("num",1);
	}

	public void getPic() {
		String id = getPara("id");
		File file = new File(rootPath,id+".jpeg");
		if(!file.exists())
			file = new File(PathKit.getWebRootPath()+"/img","nopic.png");
		renderFile(file);
	}
	

	@Clear(LoginInterceptor.class)
	public void getPics() {
		String id = getPara("id");
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
		renderJson(names);
	}
	
	public void delPic() {
		renderJson("num",1);
		String seeId = getPara("seeId");
		String picId = getPara("seeId");
		File file = new File(rootPath,seeId+"_"+picId+".jpeg");
		file.delete();
	}

}