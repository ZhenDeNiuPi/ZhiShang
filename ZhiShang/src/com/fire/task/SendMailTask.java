package com.fire.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.fire.config.dbcp.DataSourceConnectPool;
import com.fire.service.SendMail;
import com.fire.test.DataCol;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.cron4j.ITask;
/**
 * 新版做法 感谢同花顺 每天爬一次就行
 * @author dragon
 *
 * Apr 7, 2020
 */
public class SendMailTask implements ITask{
	Logger logger = Logger.getLogger(SendMailTask.class);

	@Override
	public void run() {
		try {
			logger.error("SendMailTask start");
			// TODO Auto-generated method stub
			DataCol.getData();
			send();
			logger.error("SendMailTask end");
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
		send();
	}
	
	public static void send() {
		//这里重置一下所有有效用户的晚上邮件发送状态 都置为未发送
		Db.update("update user_tb set if_send = 0 where id in (select user_id from fund_position_tb group by user_id) and level>0");
		List<Record> userList = Db.find("select id, email mail , username name,level from user_tb u where level>0");
		for(Record user:userList) {
			String contents1 = "亲爱的"+user.get("name","用户")+"，下午好！<br/>今日临近收盘时间（14:50），您订阅的基金实时信息如下：<br/><table border='8'>"+
					"<thead>"+
					"<tr>"+
					"<td width='80'>基金代码</td>"
					+ "<td width='150'>基金名称</td>"
					+ "<td width='80'>日期</td>"
					+ "<td width='80'>单位净值</td>"
					+ "<td width='80'>今日估值</td>"
					+ "<td width='80'>估算增长率</td>"
					+ "<td width='70'>最高净值</td><td width='80'>最高净值涨跌</td><td width='80'>止盈回调线</td>"
					+ "<td width='70'>止盈线</td>"
					+ "<td width='80'>估算总收益率</td>";
			List<Record> fundList = Db.find("select fp.fund_id,top_race,fp.race,"
					+ "ifnull(fp.position_point,0) ppoint,ifnull(fp.position_price,0) pprice,"
					+ "ifnull(fp.callback,0) cback,"
					+"ifnull(fp.enough,0) evalue,ft.* "
					+ " from fund_position_tb fp,fund_today_tb ft where fp.fund_id=ft.id and fp.user_id="
					+user.get("id"));
			if(fundList == null || fundList.size() == 0) continue;
			String contents = "";
			Double gsljsy = 0D;//估算累积收益
			Double gssy = 0D;//估算收益
			for(Record fund:fundList) {
				contents+= "<tr>";
				contents += "<td>"+fund.get("fund_id")+"</td>";
				contents += "<td>"+fund.get("name")+"</td>";
				contents += "<td>"+fund.get("date")+"</td>";
				contents += "<td>"+fund.get("preprice")+"元</td>";
				contents += "<td>"+fund.get("price")+"元</td>";
				contents += "<td"+
						((fund.get("priceRate")+"").startsWith("-")?" style=\"color:#FF0000\"":"")
						+ ">"+fund.get("priceRate")+"%</td>";
				Double gsrace = Double.parseDouble(fund.get("price"));
				Double race = Double.parseDouble(fund.get("preprice"));
				Double top_race = Double.parseDouble(fund.get("top_race",race)+"");
				Double down_race = (gsrace-top_race)*100/top_race;
				Double point = Double.parseDouble(fund.get("ppoint")+"");
				Double price = Double.parseDouble(fund.get("pprice")+"");
				String cback = fund.get("cback")+"";
				String evalue = fund.get("evalue")+"";
				contents+= "<td>"+String.format("%.4f", top_race)+"元</td>";
				contents+= "<td"+
						(down_race<0?" style=\"color:#FF0000\"":"")
						+">"+String.format("%.4f", down_race)+"%</td>";
				if(price == 0||point == 0) {
					if(cback.equals("0")) 
						contents+= "<td>未设置</td>";
					else
						contents+= "<td>"+cback+"%</td>";
					contents+= "<td>未设置</td>";
					contents+= "<td>未同步持仓</td>";
					contents+= "<td>未同步持仓</td>";
				}else {
					Double getRace =  (point*gsrace-price)*100/price;
					if(evalue.equals("0")) {
						if(cback.equals("0")) 
							contents+= "<td>未设置</td>";
						else
							contents+= "<td>"+cback+"%</td>";
						contents+= "<td>未设置</td>";
					}else {
						if(getRace>Integer.parseInt(evalue)) {
							//止盈
							contents+= "<td "+
									((0>Integer.parseInt(cback)+down_race)? 
											" style=\"color:#FF0000\"":"")+">"+cback+"%</td>";
							contents+= "<td style=\"color:#00DB00\">"+evalue+"%</td>";
						}else {
							contents+= "<td>"+cback+"%</td>";
							contents+= "<td>"+evalue+"%</td>";
						}
					}
					contents+= "<td"+
							(getRace<0?" style=\"color:#FF0000\"":"")
							+">"+String.format("%.4f", getRace)+"%</td>";
					gssy = point*(gsrace-race);
					gsljsy += gssy;
					contents+= "<td"+
							(gssy<0?" style=\"color:#FF0000\"":"")
							+">"+String.format("%.4f", gssy)+"元</td>";
				}
				contents+= "</tr>";
			}
			contents1 += "<td width='150'>估算收益："+String.format("%.2f", gsljsy)+"元</td>"+
					"</tr>"+
					"</thead><tbody>";
			contents += "</tbody></table></br>";
			contents += "注：最高净值为订阅以来历史最高净值，提供止盈依据；最高净值涨跌是最高净值与当日最新估算净值运算得出，"
					+ "当达到止盈线后若该值低破止盈回调线(止盈线、止盈回调线均变色)，则建议及时卖出。</br>"
					+ "作者的止盈线设置为10%，止盈回调线设置为5%，仅供参考，不构成任何投资建议。</br>";
			long level = Long.parseLong(user.get("level")+"");
			if(level<=5) {
				if(level>1)
					contents += "</br>亲爱的用户，您的基金助手服务还有"+(level-1)+"天到期。</br>";
				else
					contents += "</br>亲爱的用户，您的基金助手服务今日到期。</br>";
			}
			contents += "你的基金助手官网地址："+PropKit.get("URL_ADDRESS");
			SendMail.sendMail(contents1+contents, user);
		}
	}
}
