package com.fire.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.fire.config.dbcp.DataSourceConnectPool;
import com.fire.service.SendMail;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.cron4j.ITask;

/**
 * 
 * @Title: FindNoFollowUserTask.java
 * @Description: 每周日找还没订阅任何基金的有效用户发邮件提醒
 * @author Dragon
 * @date 2020-04-20 09:18:30
 */
public class FindNoFollowUserTask implements ITask{
	Logger logger = Logger.getLogger(FindNoFollowUserTask.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.error("FindNoFollowUserTask start");
			find();
			logger.error("FindNoFollowUserTask end");
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
	
	public static void find() {
		String sql = "SELECT u.id,ifnull(f.count,0) count,u.email mail,username name FROM user_tb u left join "+
				"(select user_id,count(*) count from fund_position_tb fp group by user_id ) f "
				+ "on u.id=f.user_id where u.level>0";
		List<Record> list = Db.find(sql);
		for(Record user : list) {
			if(Integer.parseInt(user.get("count")+"")>0) continue ; 
			String contents = "亲爱的"+user.get("name","用户")+"，晚上好！<br/>"
					+ "系统检测到您还未订阅任何基金，为了保证对您的服务，请及时登录官网订阅基金，以获得更好的服务。<br/>"
					+ "明天就是交易日了，快来添加属于您的订阅吧，同步持仓信息体验更棒哦！<br/>"
					+ "你的基金助手官网地址："+PropKit.get("URL_ADDRESS");
			SendMail.sendMail(contents, user);
		}
	}
	public static void main(String[] args) {
		DataSourceConnectPool.startPlugin();
		find();
	}
}
