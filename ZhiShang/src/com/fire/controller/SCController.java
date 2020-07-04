package com.fire.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class SCController  extends Controller{
	private static String rootPath = "";
	static {
		rootPath = PathKit.getWebRootPath()+"/img/sc";
	}
	
	public void uploadPic(){
		UploadFile file = getFile();
		//String fileName = file.getFileName();
		String id = getPara("id");
		File old = new File(rootPath,id+".jpeg");
		if(old.exists()) old.delete();
		file.getFile().renameTo(new File(rootPath,id+".jpeg"));
		renderJson("num",1);
		
	}
	
	public void delPic() {
		String id = getPara("id");
		File file = new File(rootPath,id+".jpeg");
		if(file.exists()) file.delete();
		renderJson("num",1);
	}

	public void getPics() {
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
		renderJson(names);
	}

	public void getPic() {
		String id = getPara("id");
		File file = new File(rootPath,id+".jpeg");
		renderFile(file);
	}
}
