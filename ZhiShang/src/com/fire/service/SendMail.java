package com.fire.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.fire.task.FundEndTask;
import com.fire.util.Str2TimeStamp;
import com.fire.util.TestMail3;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SendMail {
	static Logger logger = Logger.getLogger(FundEndTask.class);
	static Str2TimeStamp sts = new Str2TimeStamp();
	public static void sendMail(String contents,Record user) {
		logger.error("邮件发送："+JsonKit.toJson(user)+"\n"+contents);
        final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
        			List<Record> sendList = new ArrayList<>();
        			sendList.add(user);
        			List<String> fileList = new ArrayList<>();
    				TestMail3.sendMail(contents, fileList, sts.timeStamp2Day(System.currentTimeMillis()), sendList);
					timer.cancel();// 停止定时器
                } catch (Exception e) {
					timer.cancel();// 停止定时器
                    logger.error("发送邮件线程出错",e);
                }
            }
		};
        timer.schedule(task, 0);
        if(user.get("id")!=null) {
        	Db.update("update user_tb set sendtotal = sendtotal+1 where id="+user.get("id"));
        }
	}
	

	
	public static void sendExceptionAllinformation(Exception ex) {  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        PrintStream pout = new PrintStream(out);  
        ex.printStackTrace(pout);  
        String ret = new String(out.toByteArray());  
        pout.close();  
        try {  
             out.close();  
        } catch (Exception e) {  
        }  
        ret = "服务器报错<br/>"+ret.replaceAll("at ", "<br/>at ");
		SendMail.sendMail(ret,
				Db.findFirst("select email mail from user_tb where id=1"));
	} 
}
