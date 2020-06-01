package com.fire.task;

import org.apache.log4j.Logger;

import com.fire.service.SendMail;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.cron4j.ITask;

/**
 * 
 * @Title: ClearValiTask.java
 * @Description: 清理过期体验注册码  15分钟后的就删了行了   level=7 state=0 apply_time>10分钟
 * @author Dragon
 * @date 2020-04-18 05:37:56
 */
public class ClearValiTask implements ITask{
	Logger logger = Logger.getLogger(ClearValiTask.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.error("ClearValiTask start");
			Db.update("delete from regnum_tb where level=7 and state=0 and unix_timestamp(now())-apply_time>900");
			logger.error("ClearValiTask end");
		} catch (Exception e) {
			// TODO: handle exception
		    SendMail.sendExceptionAllinformation(e);
		    logger.error(e);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
