package com.fire.controller;

import java.util.Map;

import com.fire.intercepter.FormOperaIntercepter;
import com.fire.model.FollowFund;
import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class FollowFundController extends Controller{
	private IBaseServiceImpl is = new IBaseServiceImpl();
	
	public void getDatas() {
		Record user = getSessionAttr("user");
//		String userid = 
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select f.id,from_unixtime(f.follow_time,'%Y-%m-%d') fTime,f.fund_id,"
        		+ "f.position_point,f.position_price,"
        		+ "f.callback,f.enough,t.name,t.preprice,t.net ";//select xxx,xxx,xxx 
        String from = " from fund_position_tb f,fund_today_tb t where f.fund_id=t.id and "
        		+ "f.user_id="+user.get("id")+" ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
	
	@Before(FormOperaIntercepter.class)
	public void addData() {
		Record user = getSessionAttr("user");
		FollowFund o = getModel(FollowFund.class,"f");
		long ftime = System.currentTimeMillis()/1000;
		o.set("follow_time", ftime);
		o.set("user_id", Long.parseLong(user.get("id")+""));
		boolean flag = false;
		try {
			flag = o.save();
		} catch (Exception e) {
			// TODO: handle exception
			flag = o.update();
		}
		renderJson("num",flag?1:0);
	}
	
	public void getData() {
		String id = getPara("id");
		String select  = "select f.id,f.fund_id,f.user_id,"
        		+ "f.position_point,f.position_price,"
        		+ "f.callback,f.enough,t.name "
        		+ " from fund_position_tb f,fund_today_tb t where f.fund_id=t.id and f.id="+id;
		renderJson(Db.findFirst(select));
	}
	
	@Before(FormOperaIntercepter.class)
	public void updateData() {
		FollowFund o = getModel(FollowFund.class,"f");
		boolean flag = false;
		try {
			flag = o.update();
		} catch (Exception e) {
			// TODO: handle exception
			flag = o.save();
		}
		renderJson("num",flag?1:0);
	}
	
	public void deleteDatas() {
		String ids = getPara("idStr");
		String sql = "delete from fund_position_tb where id in ("+ids+")";
		renderJson("num",Db.update(sql));
	}
	
	public void getFundName() {
		Record user = getSessionAttr("user");
		try {
			String id = getPara("id");
			String sql = "select count(*) num,name from fund_today_tb where id ="+id
					+ " and id not in (select fund_id from fund_position_tb where user_id="+user.get("id")+")";
			renderJson(Db.findFirst(sql));
		} catch (Exception e) {
			renderJson("num",0);
		}
	}

	public void getAllFunds() {
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select * ";//select xxx,xxx,xxx 
        String from = " from fund_today_tb where 1=1 ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
	

	public void getDatas4Manager() {
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select f.id,from_unixtime(f.follow_time,'%Y-%m-%d') fTime,f.fund_id,"
        		+ "f.position_point,f.position_price,"
        		+ "f.callback,f.enough,t.name,t.preprice,t.net ";//select xxx,xxx,xxx 
        String from = " from fund_position_tb f,fund_today_tb t where f.fund_id=t.id ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
}