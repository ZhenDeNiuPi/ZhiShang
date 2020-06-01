/**  
 * @Title: EndTask.java
 * @Description: TODO(描述)
 * @author Dragon
 * @date 2020-02-26 12:42:00 
 */
package com.fire.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fire.config.dbcp.DataSourceConnectPool;
import com.fire.service.SendMail;
import com.fire.test.DataCol;
import com.fire.util.Str2TimeStamp;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.cron4j.ITask;

/**  
 * @Title: EndTask.java
 * @Description: 收盘之后更新最高净值 更新最高净值跌幅 并向用户发送总结邮件
 * @author Dragon
 * @date 2020-02-26 12:42:00 
 */
public class FundEndTask implements ITask{
	static Logger logger = Logger.getLogger(FundEndTask.class);
	static boolean flag = false;//是否到最后一次 判定条件为当前时间的小时和分钟等于8:50
	Str2TimeStamp sts = new Str2TimeStamp();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(sts.timeStamp2Minute(System.currentTimeMillis()).endsWith("23:55")) flag = true;
		else flag = false;
		//要都发完了就别折腾了
		Record rec = Db.findFirst("select ifnull(count(*),0) count from user_tb where "
				+ "id in (select user_id from fund_position_tb group by user_id) "
				+ "and if_send=0 and level>0 ");
		if(Integer.parseInt(rec.get("count")+"") == 0)return;
		try {
			logger.error("FundEndTask start");
			DataCol.getData();
			setData();	
			send();
			logger.error("FundEndTask end");
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
	public static void main(String[] args) {
		DataSourceConnectPool.startPlugin();
//		DataCol.getData();
//		setData();	
		try {
			send();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//同步最高净值
	public static void setData() {
		List<Record> userList = Db.find("select id, email mail , username name from user_tb where " 
								+ "id in (select user_id from fund_position_tb group by user_id) "  
								+ "and if_send=0 and level>0 ");
		List<String> sqls = new ArrayList<>();
		for(Record user:userList) {
			List<Record> fundList = Db.find("select fp.id fpid,ifnull(top_race,0) top_race,"
					+ "ifnull(fp.position_point,0) ppoint,ifnull(fp.position_price,0) pprice,"
					+"ifnull(fp.enough,0) evalue, ifnull(net,net1) net "
					+ " from fund_position_tb fp,fund_today_tb ft where fp.fund_id=ft.id and fp.user_id="
					+user.get("id"));
			for(Record fund:fundList) {
				//实际净值
				Double price = 0D;
				try {
					price = Double.parseDouble(fund.get("net","0")+"");
				} catch (Exception e) {
					price = 0D;
				}
				//之前最高净值
				Double top_race = Double.parseDouble(fund.get("top_race")+"");
				//回撤幅度
				Double after = 0D;
				if(price>top_race) top_race = price;
				else after = (price-top_race)*100/top_race;
				String sql = "update fund_position_tb set race="+price+",top_race="+top_race+
						",after_top_callback="+String.format("%.4f", after);
				Double ppoint = Double.parseDouble(fund.get("ppoint")+"");
				Double pprice = Double.parseDouble(fund.get("pprice")+"");
				Double enough = Double.parseDouble(fund.get("evalue")+"");
				if(ppoint>0 && pprice>0 && enough>0 ) {
					Double payrace = pprice/ppoint;
					Double a = (price-payrace)*100/payrace;//收益率 百分之多少
					int ifenough = 0;
					if(a>enough) 
						ifenough = 1;
					else 
						ifenough = 0;
					sql += ",if_enough="+ifenough;
				}
				sql += " where id="+fund.get("fpid");
//				System.out.println(sql);
				sqls.add(sql);
			}
		}
		Db.batch(sqls, sqls.size());
	}
	public static void send(){
		//查找未发送的
		List<Record> userList = Db.find("select id, email mail , username name,level from user_tb where "
				+ "id in (select user_id from fund_position_tb group by user_id) "
				+ " and if_send=0 and level>0");
		for(Record user:userList) {
			String contents1 = "亲爱的"+user.get("name","用户")+"，晚上好！<br/>您订阅的基金今日收盘信息如下：<br/><table border='8'>"+
					"<thead>"+
					"<tr>"+
					"<td width='80'>基金代码</td>"
					+ "<td width='150'>基金名称</td>"
					+ "<td width='80'>日期</td>"
					+ "<td width='80'>昨日净值</td>"
					+ "<td width='80'>今日净值</td>"
					+ "<td width='80'>今日增长率</td>"
//					+ "<td width='70'>最高净值</td><td width='80'>止盈回调线</td>"
					+ "<td width='70'>止盈线</td>";
			List<Record> fundList = Db.find("select fp.fund_id,top_race,fp.race,"
					+ "ifnull(fp.position_point,0) ppoint,ifnull(fp.position_price,0) pprice,"
					+ "ifnull(fp.callback,0) cback,"
					+"ifnull(fp.enough,0) evalue,ft.* "
					+ " from fund_position_tb fp,fund_today_tb ft where fp.fund_id=ft.id and fp.user_id="
					+user.get("id"));
			String contents = "";
			Double nowvalue = 0D;//当前总价值
			Double nowpay = 0D;//当前总成本
			Double gsljsy = 0D;//估算累积收益
			Double gssy = 0D;//估算收益
			int num = 0;
			for(Record fund:fundList) {
				contents+= "<tr>";
				contents += "<td>"+fund.get("fund_id")+"</td>";
				contents += "<td>"+fund.get("name")+"</td>";
				contents += "<td>"+fund.get("date")+"</td>";
				contents += "<td>"+fund.get("preprice")+"元</td>";
				contents += "<td>"+fund.get("net","净值未更新")+"元</td>";
				contents += "<td"+
						((fund.get("rate")+"").startsWith("-")?" style=\"color:#FF0000\"":"")
						+ ">"+fund.get("rate")+"%</td>";
				Double gsrace = 0D;
				try {
					gsrace = Double.parseDouble(fund.get("net")+"");
				} catch (Exception e) {
					gsrace = 0D;
				}
				//如果今日净值还未更新 就跳过该用户
				if(gsrace == 0 && !flag) {
					num++;
					break;
				}
				Double race = Double.parseDouble(fund.get("preprice"));
//				Double top_race = Double.parseDouble(fund.get("top_race",race)+"");
				Double point = Double.parseDouble(fund.get("ppoint")+"");
				Double price = Double.parseDouble(fund.get("pprice")+"");
				String cback = fund.get("cback")+"";
				if(cback.equals("0")) cback = "未设置";
				else cback += "%";
				String evalue = fund.get("evalue")+"";
				if(evalue.equals("0")) evalue = "未设置";
				else evalue += "%";
//				contents+= "<td>"+String.format("%.4f", top_race)+"元</td>";
//				contents+= "<td>"+cback+"</td>";
				contents+= "<td>"+evalue+"</td>";
				if(price == 0||point == 0) {
					contents+= "<td>未同步持仓</td>";
					contents+= "<td>未同步持仓</td>";
					contents+= "<td>未同步持仓</td>";
				}else if(gsrace == 0){
					contents+= "<td>净值未更新</td>";
					contents+= "<td>净值未更新</td>";
					contents+= "<td>净值未更新</td>";
				}else {
					Double getRace =  (point*gsrace-price)*100/price;
					nowvalue += point*gsrace;
					nowpay += price;
					contents+= "<td"+
							(getRace<0?" style=\"color:#FF0000\"":"")
							+">"+String.format("%.2f", getRace)+"%</td>";
					gssy = point*(gsrace-race);
					gsljsy += Double.parseDouble(String.format("%.2f", gssy));
					contents+= "<td"+
							(gssy<0?" style=\"color:#FF0000\"":"")
							+">"+String.format("%.2f", gssy)+"元</td>";
					Double sy = point*gsrace-price;
					contents+= "<td"+
							(sy<0?" style=\"color:#FF0000\"":"")
							+">"+String.format("%.2f", sy)+"元</td>";
				}
				contents+= "</tr>";
			}
			//如果今日净值还未更新 就跳过该用户
			if(num > 0 && !flag) continue;
			contents1 += "<td width='150'>总收益率"+(nowpay>0?"："+String.format("%.2f", (nowvalue-nowpay)*100/nowpay):"")
			+"%</td><td width='150'>今日收益："+String.format("%.2f", gsljsy)+"元</td>"
					+ "<td width='150'>累计收益："+String.format("%.2f", nowvalue-nowpay)+"元</td>"+
					"</tr>"+
					"</thead><tbody>";
			contents += "</tbody></table></br>";
			long level = Long.parseLong(user.get("level")+"");
			if(level<=5) {
				if(level>1)
					contents += "</br>亲爱的"+user.get("name","用户")+"，您的基金助手服务还有"+(level-1)+"天到期，明天不见不散，晚安。</br>";
				else
					contents += "</br>亲爱的"+user.get("name","用户")+"，您的基金助手服务今日到期，感谢您的使用和支持。</br>";
			}else
				contents += "亲爱的"+user.get("name","用户")+"，感觉好用请分享给小伙伴们，老用户推荐新用户各免费获得7天时长哦~明天不见不散，晚安。</br>";
			contents += "你的基金助手官网地址："+PropKit.get("URL_ADDRESS");
			SendMail.sendMail(contents1+contents, user);
			Db.update("update user_tb set if_send=1,level=level-1 where id="+user.get("id"));
		}
		Record rec = Db.findFirst("select ifnull(count(*),0) count from user_tb where "
				+ "id in (select user_id from fund_position_tb group by user_id) "
				+ "and if_send=0 and level>0 ");
		int count = Integer.parseInt(rec.get("count")+"");
		if(count == 0) {
			SendMail.sendMail("所有用户都发完了", Db.findFirst("select email mail from user_tb where id=1"));
		}else 
        	logger.error("FundEndTask还有"+count+"个用户未发送邮件");
	}
}
