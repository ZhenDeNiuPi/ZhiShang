package com.fire.controller;

import java.io.File;

import com.fire.model.Info;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

public class UserController extends Controller{
	
	private static String rootPath = "";
	static {
		rootPath = PathKit.getWebRootPath()+"/img";
	}
	
	public void uploadPic(){
		UploadFile file = getFile();
		File old = new File(rootPath,"idea.jpeg");
		if(old.exists()) old.delete();
		file.getFile().renameTo(new File(rootPath,"idea.jpeg"));
		renderJson("num",1);
		
	}

	public void getPic() {
		File idea = new File(rootPath,"idea.jpeg");
		if(idea.exists())
			renderFile(idea);
		else
			renderFile(new File(rootPath,"nopic.png"));
	}
	
	public void update() throws Exception{
    	Info info = getModel(Info.class,"u");
    	int num = 1;
    	if(Db.findFirst("select * from info_tb") == null ) {
    		if(!info.save())num = 0;
        	renderJson("num",num);
    		return ;
    	}
    	if(!info.update())num = 0;
    	renderJson("num",num);
	}

	public void getData() {
		renderJson(Db.findFirst("select * from info_tb"));
	}
}
