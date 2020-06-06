package com.fire.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fire.intercepter.FormOperaIntercepter;
import com.fire.model.News;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.fire.util.Str2TimeStamp;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

public class NewsController  extends Controller{
	private static String rootPath = "";
	static {
		rootPath = PathKit.getWebRootPath()+"/img/news";
	}
	private IBaseServiceImpl is = new IBaseServiceImpl();
	private Str2TimeStamp sts = new Str2TimeStamp();
	
	public void getDatas() {
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select n.id,from_unixtime(n.time,'%Y-%m-%d') ntime,n.title,n.stitle,n.creator,"
        		+ "n.content,n.if_show ";//select xxx,xxx,xxx 
        String from = " from news_tb n where 1=1 ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
	
	@Before(FormOperaIntercepter.class)
	public void addData() {
		News n = getModel(News.class,"n");
		String ntime = getPara("ntime");
		if(ntime != null || !"".equals(ntime)) n.set("time", sts.getTimeStamp(ntime)/1000);
		n.save();
		String content = getPara("content");
		content = transferPicNames(content, Long.parseLong(n.get("id")+""));
		n.set("content", content);
		renderJson("num",n.update()?1:0);
	}
	
	public void getData() {
		String id = getPara("id");
		String select  = "select n.id,from_unixtime(n.time,'%Y-%m-%d') ntime,n.title,n.stitle,n.creator,"
        		+ "n.content,n.if_show "
        		+ " from news_tb n where id="+id;
		renderJson(Db.findFirst(select));
	}
	
	@Before(FormOperaIntercepter.class)
	public void updateData() {
		News n = getModel(News.class,"n");
		String ntime = getPara("ntime");
		if(ntime != null || !"".equals(ntime)) n.set("time", sts.getTimeStamp(ntime)/1000);
		String content = getPara("content");
		content = transferPicNames(content, Long.parseLong(n.get("id")+""));
		n.set("content", content);
		renderJson("num",n.update()?1:0);
	}
	
	public void deleteDatas() {
		String ids = getPara("idStr");
		String sql = "delete from news_tb where id in ("+ids+")";
		renderJson("num",Db.update(sql));
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		String id = getPara("id");
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.getName().startsWith(id+".")||file.getName().startsWith(id+"_")) file.delete(); 
		}
	}
	
	public void ifSend() {
		String id = getPara("id");
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		File file = new File(dir,id+".jpeg");
		if(file.exists())renderJson("path","news/getPic?id="+id);
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
	
	public void preHtml() {
		String id = getPara("id");
		renderJson(Db.findFirst("select content from news_tb where id="+id));
	}
	
	public void uploadContentPic(){
		UploadFile file = getFile();
		String filename = file.getFileName();
//		System.out.println(file.getFileName());
//		System.out.println(file.getOriginalFileName());
//		System.out.println(rootPath);
		File dir = new File(rootPath);
		if(!dir.exists()) dir.mkdir();
		String newName = "un_"+UUID.randomUUID();
		file.getFile().renameTo(new File(rootPath,newName+filename.substring(filename.indexOf("."), filename.length())));
		renderJson("path","news/getContentPic?filename="+newName+"&s="+Math.random());
	}

	public void getContentPic() {
		String filename = getPara("filename");
		File dir = new File(rootPath);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.getName().startsWith(filename+".")) {
				renderFile(file);
				break;
			}
		}
	}
	
	
	public String transferPicNames(String content,Long id) {
		//1.先拿出来所有文件名
		//2.对比一下文件系统里有没有这个 有的话 就改名 避免文件等下按顺序改名冲突 放到filelist里
		//3.删除所有剩下的 un前缀的 以及如果是更新的话 还包括id_开头的
		//4.filelist挨个按顺序改名 content把原来文件名都替换一遍
		//5.返回content
		//如果上来就没带图 那就别忙活了
		if(content.indexOf("filename=")<0) return content; 
//		System.out.println(content);
		String temp = content;
		List<String> filenames = new ArrayList<>();
		List<File> files = new ArrayList<>();
		File dir = new File(rootPath);
		File[] allFiles = dir.listFiles();
		while(temp.indexOf("filename=")>-1) {//9
			int sindex = temp.indexOf("filename=")+9;
			int eindex = temp.indexOf("&amp;s=");
			//前缀
			String name = temp.substring(sindex,eindex);
//			System.out.println(name);
			filenames.add(name);
			//temp刷新
			temp = temp.substring(eindex);
//			System.out.println(temp);
			for(File file : allFiles) {
				if(file.getName().startsWith(name+".")) {
					//后缀
					String oldName = file.getName();
					String last = oldName.substring(oldName.indexOf("."),oldName.length());
					//改名
					String firstChange = "in_"+UUID.randomUUID()+last;
					file.renameTo(new File(rootPath,firstChange));
					files.add(new File(rootPath,firstChange));
					break;
				}
			}
		}
		//重新读一遍 清理没用上的文件
		allFiles = dir.listFiles();
		for(File file : allFiles) {
			if(file.getName().startsWith("un")||file.getName().startsWith(id+"_")) {
				file.delete();
			}
		}
		//再按顺序改一遍名字 content替换文件名 避免文件改名冲突
		for(int i=0;i<files.size();i++) {
			File file = files.get(i);
			String oldName = file.getName();
			String last = oldName.substring(oldName.indexOf("."),oldName.length());
			file.renameTo(new File(rootPath,id+"_"+i+last));
			content = content.replace(filenames.get(i), id+"_"+i);
		}
		return content;
	}
	
	
	
	
}