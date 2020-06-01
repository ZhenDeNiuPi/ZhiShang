package com.fire.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fire.config.dbcp.DataSourceConnectPool;
import com.fire.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;

public class DataCol {
	static Logger logger = Logger.getLogger(DataCol.class);
	public static void main(String[] args) throws Exception{
//		saveFundList();
//		getFundHistoryInfo();
//		testDom();
		testData();
	}
	
/**
 * 真正使用的基金同步方法 调取同花顺
 */
	@SuppressWarnings({"rawtypes" })
	public static void getData() {
//		String text = "g({\"data\":"
//				+ "{\"f960029\":{\"code\":960029,\"name\":\"\\u5efa\\u4fe1\\u53cc\\u606f\\u7ea2\\u5229\\u503a\\u5238H\",\"orgid\":\"T000009108\",\"price\":\"1.20441\",\"preprice\":\"1.2030\",\"priceRate\":\"0.12\",\"date\":\"2020-02-27\",\"buy\":0,\"dt\":\"0\",\"net\":\"1.2020\",\"rate\":\"-0.08\",\"net1\":\"1.2030\",\"date1\":\"2020-02-26\",\"gzpc\":\"0.20\"},\"f960027\":{\"code\":960027,\"name\":\"\\u535a\\u65f6\\u4fe1\\u7528\\u503a\\u5238R\",\"orgid\":\"00010223\",\"price\":\"1.00438\",\"preprice\":\"1.0060\",\"priceRate\":\"-0.16\",\"date\":\"2020-02-27\",\"buy\":0,\"dt\":\"0\",\"net\":\"\",\"rate\":\"\",\"gzpc\":\"\",\"net1\":\"1.0060\",\"date1\":\"2018-11-14\"},\"f000001\":{\"code\":\"000001\",\"name\":\"\\u534e\\u590f\\u6210\\u957f\\u6df7\\u5408\",\"orgid\":\"00079099\",\"price\":\"1.19711\",\"preprice\":\"1.1970\",\"priceRate\":\"0.01\",\"date\":\"2020-02-27\",\"buy\":1,\"dt\":\"1\",\"net\":\"1.1950\",\"rate\":\"-0.17\",\"net1\":\"1.1970\",\"date1\":\"2020-02-26\",\"gzpc\":\"0.18\"}}"
//				+ ",\"error\":{\"id\":0,\"msg\":\"is access\"}})";
		String text = HttpUtil.doGet("http://fund.ijijin.cn/data/Net/gz/all_code_asc_0_0_1_9999_0_0_0_jsonp_g.html", "", null);
		text = text.substring(2,text.length()-1);
		logger.error("数据爬取成功");
//		Db.update("delete from fund_today_tb");
		//第一种方式  
		Map map = (Map)JSON.parse(text);  
//		String datas = map.get("data")+"";
		Map datas = (Map)JSON.parse(map.get("data")+""); 
		List<String> sqls = new ArrayList<>();
		for(Object data : datas.entrySet()) {
//			System.out.println(((Map.Entry)data).getKey()+"     " + ((Map.Entry)data).getValue()); 
			Map details = (Map)JSON.parse(((Map.Entry)data).getValue()+""); 
			String date = details.get("date")+"";//今日日期
			String name = details.get("name")+"";//基金名称
			String code = details.get("code")+"";//基金编码
			String orgid = details.get("orgid")+"";//公司编码
			String price = details.get("price")+"";//估算净值
			String preprice = details.get("preprice")+"";//昨日净值
			String priceRate = details.get("priceRate")+"";//估值增长率
			String buy = details.get("buy")+"";//是否开放购买1开放,0暂停
			String dt = details.get("dt")+"";//不知道啥意思 1,0 赎回？
			String net = details.get("net")+"";//收盘净值
			String rate = details.get("rate")+"";//收盘增长率
			String net1 = details.get("net1")+"";//上次净值
			String date1 = details.get("date1")+"";//上次日期
			String gzpc = details.get("gzpc")+"";//今日估值偏差（估算增长率-实际增长率）
			sqls.add("replace into fund_today_tb "
					+ "(id,name,date,orgid,price,preprice,priceRate,buy,dt,net,rate,net1,date1,gzpc) "
					+ "values('"+code+"','"+name+"','"+date+"','"+orgid+"','"+price+"',"
					+ "'"+preprice+"','"+priceRate+"','"+buy+"','"+dt+"',"+(net.length()==0?"null":"'"+net+"'")+",'"+rate+"',"
					+ "'"+net1+"','"+date1+"','"+gzpc+"')");
		}
		logger.error("数据整理成功");
		Db.batch(sqls, sqls.size());
		logger.error("数据同步成功");
	}
	
	
	@SuppressWarnings({"rawtypes" })
	public static void testData() {
//		String text = "g({\"data\":"
//				+ "{\"f960029\":{\"code\":960029,\"name\":\"\\u5efa\\u4fe1\\u53cc\\u606f\\u7ea2\\u5229\\u503a\\u5238H\",\"orgid\":\"T000009108\",\"price\":\"1.20441\",\"preprice\":\"1.2030\",\"priceRate\":\"0.12\",\"date\":\"2020-02-27\",\"buy\":0,\"dt\":\"0\",\"net\":\"1.2020\",\"rate\":\"-0.08\",\"net1\":\"1.2030\",\"date1\":\"2020-02-26\",\"gzpc\":\"0.20\"},\"f960027\":{\"code\":960027,\"name\":\"\\u535a\\u65f6\\u4fe1\\u7528\\u503a\\u5238R\",\"orgid\":\"00010223\",\"price\":\"1.00438\",\"preprice\":\"1.0060\",\"priceRate\":\"-0.16\",\"date\":\"2020-02-27\",\"buy\":0,\"dt\":\"0\",\"net\":\"\",\"rate\":\"\",\"gzpc\":\"\",\"net1\":\"1.0060\",\"date1\":\"2018-11-14\"},\"f000001\":{\"code\":\"000001\",\"name\":\"\\u534e\\u590f\\u6210\\u957f\\u6df7\\u5408\",\"orgid\":\"00079099\",\"price\":\"1.19711\",\"preprice\":\"1.1970\",\"priceRate\":\"0.01\",\"date\":\"2020-02-27\",\"buy\":1,\"dt\":\"1\",\"net\":\"1.1950\",\"rate\":\"-0.17\",\"net1\":\"1.1970\",\"date1\":\"2020-02-26\",\"gzpc\":\"0.18\"}}"
//				+ ",\"error\":{\"id\":0,\"msg\":\"is access\"}})";
		String text = HttpUtil.doGet("http://fund.ijijin.cn/data/Net/gz/all_code_asc_0_0_1_9999_0_0_0_jsonp_g.html", "", null);
		text = text.substring(2,text.length()-1);
		//第一种方式  
		Map map = (Map)JSON.parse(text);  
//		String datas = map.get("data")+"";
		Map datas = (Map)JSON.parse(map.get("data")+""); 
		for(Object data : datas.entrySet()) {
//			System.out.println(((Map.Entry)data).getKey()+"     " + ((Map.Entry)data).getValue()); 
			Map details = (Map)JSON.parse(((Map.Entry)data).getValue()+""); 
//			String date = details.get("date")+"";//今日日期
			String name = details.get("name")+"";//基金名称
			String code = details.get("code")+"";//基金编码
//			String orgid = details.get("orgid")+"";//公司编码
//			String price = details.get("price")+"";//估算净值
//			String preprice = details.get("preprice")+"";//昨日净值
//			String priceRate = details.get("priceRate")+"";//估值增长率
//			String buy = details.get("buy")+"";//是否开放购买1开放,0暂停
//			String dt = details.get("dt")+"";//不知道啥意思 1,0 赎回？
			String net = details.get("net")+"";//收盘净值
			if(net.trim().length() != 0 )System.out.println(code+" : "+name);
//			String rate = details.get("rate")+"";//收盘增长率
//			String net1 = details.get("net1")+"";//上次净值
//			String date1 = details.get("date1")+"";//上次日期
//			String gzpc = details.get("gzpc")+"";//今日估值偏差（估算增长率-实际增长率）
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getFundHistoryInfo() throws Exception{
		String fundId = "000001";
		String sdate = "2010-02-22";
		String edate = "2010-03-20";
		String url = "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code="+fundId+"&page=1&per=100&sdate="+sdate+"&edate="+edate;
		String result = HttpUtil.doGet(url, "", null);
		result = result.substring(23,result.length()-32);
		Document document = DocumentHelper.parseText(result);
		Element rootElement = document.getRootElement();// table
		for (Iterator iter = rootElement.elementIterator(); iter.hasNext();) {
			Element tbody = (Element) iter.next();// thead
			tbody = (Element) iter.next();//tbody
			for (Iterator bodys = tbody.elementIterator(); bodys.hasNext();) {
				Element tr = (Element) bodys.next();// tr
				String str = "";
				List<Element> tds = tr.elements();
				str += "净值日期:"+tds.get(0).getText()+" , ";
				str += "单位净值:"+tds.get(1).getText()+" , ";
				str += "累计净值:"+tds.get(2).getText()+" , ";
				str += "日增长率:"+tds.get(3).getText()+" , ";
				str += "申购状态:"+tds.get(4).getText()+" , ";
				str += "赎回状态:"+tds.get(5).getText();
				System.out.println(str);
			}
		}
	}
	public static void saveFundList() {
//		DataSourceConnectPool.startPlugin();
//		Db.update("delete from fund_tb");
		String url = "http://fund.eastmoney.com/js/fundcode_search.js";
		String result = HttpUtil.doGet(url, "", null);
		if(result != null && result.length()>0)
		result = result.substring(11, result.length()-3);
		String[] infos = result.split("\\],\\[");
		List<String> sqls = new ArrayList<>();
		for(String str : infos) {
			String[] strs = str.split(",");
			String id = strs[0].substring(1,strs[0].length()-1);
			String sim = strs[1].substring(1,strs[1].length()-1);
			String name = strs[2].substring(1,strs[2].length()-1);
			String type = strs[3].substring(1,strs[3].length()-1);
			String eng = strs[4].substring(1,strs[4].length()-1);
			sqls.add("insert into fund_tb (id,name,sim_eng,type,eng) "
					+ "values('"+id+"','"+name+"','"+sim+"','"+type+"','"+eng+"')");
			String a = "'"+id+"','"+name+"','"+sim+"','"+type+"','"+eng+"'";
			System.out.println(a);
		}
//		Db.batch(sqls, sqls.size());
	}
	public static void getFundNowInfo(String fundId) {
//		String fundId = "001186";
		String url = "http://fundgz.1234567.com.cn/js/"+fundId+".js";
		String result = HttpUtil.doGet(url, "", null);
		System.out.println(result);
	}
	
	@SuppressWarnings("unchecked")
	public static void testDom() throws DocumentException {
		String str = 
				"<VMS id=\"设备编号\" cmdid=\"命令字\">\r\n" + 
				"<SCREEN>\r\n" + 
				"<PARAS>\r\n" + 
				"<PARA name=\"color\" value=\"3\" />\r\n" + 
				"<PARA name=\"speed\" value=\"3\" />\r\n" + 
				"<PARA name=\"fontsize\" value=\"0\" />\r\n" + 
				"<PARA name=\"disptype\" value=\"20\" />\r\n" + 
				"<PARA name=\"position\" value=\"3\" />\r\n" + 
				"<PARA name=\"infoindex\" value=\"1\" />\r\n" + 
				"</PARAS>\r\n" + 
				"</SCREEN>\r\n" + 
				"</VMS>";
		Document document = DocumentHelper.parseText(str);
		Element vms = document.getRootElement();
//		Element screen = vms.element("SCREEN");
		Element paras = vms.element("SCREEN").element("PARAS");
//		resultData.put("color", paras.e)
		List<Element> paraElements = paras.elements();
		for(Element para:paraElements) {
			System.out.println(para.attributeValue("name")+"   "+para.attributeValue("value"));
			
		}
	}
//	var json={datas:[['80000220','南方基金管理股份有限公司','1998-03-06',
//	'346','杨小松','NFJJ','','6160.69','★★★★','南方基金','15','2020/2/27 0:00:00']]}
	@SuppressWarnings("rawtypes")
	public static void saveCompanyInfo() {
		DataSourceConnectPool.startPlugin();
		String text = HttpUtil.doGet("http://fund.eastmoney.com/Data/FundRankScale.aspx?_=1583114470118", "", null);
//		String text = "var json={datas:[['80000221','南方基金管理股份有限公司','1998-03-06','346','杨小松','NFJJ','','6160.69','★★★★','南方基金','15','2020/2/27 0:00:00'],['80000220','南方基金管理股份有限公司','1998-03-06','346','杨小松','NFJJ','','6160.69','★★★★','南方基金','15','2020/2/27 0:00:00']]}";
		text = text.substring(9);
		Db.update("delete from fund_company_tb");
		//第一种方式  
		Map map = (Map)JSON.parse(text);  
		List datas = (List)JSON.parse(map.get("datas")+""); 
		List<String> sqls = new ArrayList<>();
		for(Object strData : datas) {
			List str = (List)JSON.parse(strData+"");
			String nums = str.get(3)+"";
			if(nums.length() == 0) nums = "0";
			String range = str.get(7)+"";
			if(range.length() == 0) range = "0";
			String sql = "insert into fund_company_tb "
					+ "(id,name,start_date,fund_nums,manager,eng_simple,manage_range,stars,ch_simple) values"
					+ "('"+str.get(0)+"','"+str.get(1)+"','"+str.get(2)+"',"+nums+","
					+ "'"+str.get(4)+"','"+str.get(5)+"','"+range+"','"+str.get(8)+"',"
					+ "'"+str.get(9)+"')";
			sqls.add(sql);
//			System.out.println(sql);
//			Db.update(sql);
		}
		Db.batch(sqls, sqls.size());
	}
}
