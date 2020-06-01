package com.fire.controller;

import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.service.IBaseServiceImpl;
import com.fire.util.Page;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
/**
 * 
 * @Title: OrderController.java
 * @Description: 续费记录 包括支付、推广
 * @author Dragon
 * @date 2020-04-19 09:24:39
 */
public class OrderController extends Controller{
	Logger logger = Logger.getLogger(OrderController.class);
	private IBaseServiceImpl is = new IBaseServiceImpl();
	
	public void index() {
		render("order.html");
	}

	public void getDatas() {
		Record user = getSessionAttr("user");
		String id = user.get("id")+"";
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select id,amount,from_unixtime(paytime) ptime,days,paytype ";//select xxx,xxx,xxx 
        String from = " from order_tb where userid="+id+" ";//from xxx ... where 1=1 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}

	public void getAllDatas() {
		Record user = getSessionAttr("user");
		if(!"1".equals(user.get("id")+"")) {
			renderJson("有病");
			return;
		} 
        Map<String,String[]> allParams=getParaMap();//获取前台传来的分页以及排序所需的参数
        String select  = "select o.id,o.userid,u.username,o.amount,from_unixtime(o.paytime) ptime,"
        		+ "o.days,o.content,o.paytype ";//select xxx,xxx,xxx 
        String from = " from order_tb o left join user_tb u on o.userid=u.id where 1=1 "; 
        Page page = is.query(select, from, allParams);
    	renderJson(page);
	}
}
