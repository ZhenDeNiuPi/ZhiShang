package com.fire.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.fire.util.AlipayClientFactory;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 
 * @Title: PaymentController.java
 * @Description: TODO(描述)
 * @author Dragon
 * @date 2020-04-18 11:01:56
 */

public class PaymentController extends Controller{
	Logger logger = Logger.getLogger(PaymentController.class);
	
	public void index() {
		render("payment.html");
	}

	@Clear
	public void alipayNotify() throws IOException, Exception {
        try {
        	long time = System.currentTimeMillis()/1000;
            Map<String, String> paramMap = readFormPara(getRequest().getInputStream());
    		logger.error("收到alipay/notify消息：" + JsonKit.toJson(paramMap));
            String out_trade_no = paramMap.get("out_trade_no")+"";
            String trade_no = paramMap.get("trade_no")+"";
            String amount = paramMap.get("total_amount")+"";
            String callbackParams = paramMap.get("body")+"";
            String[] callbackInfo = callbackParams.split("-");//.split("%25");
            String userid = callbackInfo[0];
            int days = Integer.parseInt(callbackInfo[1]);
            Db.update("update user_tb set level=level+"+days+" where id="+userid);
    		Db.update("insert into order_tb (userid,days,amount,paytime,paytype,outtradeno,tradeno,content) values"
    				+ "("+userid+","+days+","+amount+","+time+",0,'"+out_trade_no+"','"+trade_no+"','支付宝订单支付')");//0支付宝1微信
		} catch (Exception e) {
			logger.error("Alipay回调出错",e);
		}
		renderJson("success");
	}
	
	public void alipayQrCode() throws Exception {
		Record user = getSessionAttr("user");
		int days = getParaToInt("days");//7天卡 30天卡  90天卡  360卡
		Record priceRec = Db.findFirst("select price from price_tb where days="+days);
		Double amount = Double.parseDouble(priceRec.get("price")+"");
		AlipayClient alipayClient = AlipayClientFactory.getClient();
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setNotifyUrl(PropKit.get("alipay.notifyurl"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("out_trade_no", getOutTradeNo());
        params.put("total_amount", amount);
        params.put("subject", "【你的基金助手】续费"+days+"天");
        params.put("body", user.get("id")+"-"+days);//邮箱-续费天数
		request.setBizContent(JsonKit.toJson(params));
		AlipayTradePrecreateResponse response = alipayClient.execute(request);
		renderQrCode(response.getQrCode(),200,200);
	}
	
	public void getPrices() {
		String sql = "select * from price_tb order by days asc";
		renderJson(Db.find(sql));
	}

	/**
	 * 支付生成out_trade_no
	 * 当前时间 yyyyMMddHHmmss + 四位随机数字，共18位
	 * @return
	 */
	public static String getOutTradeNo() {
		// 取当前时间
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = outFormat.format(now);
		// 加上四位随机数
		Random random = new Random();
		int end4 = random.nextInt(9999);
		// 如果不足四位前面补0
		String str = time + String.format("%04d", end4);
		
		return str;
	}
	

	public static Map<String, String> readFormPara(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String paramStr = new String(outSteam.toByteArray(), "UTF-8");
//        logger.error("post方式传入form数据：/n"+paramStr);
		Map<String, String> paramMap = new HashMap<String, String>();
		String[] paramArray = paramStr.split("&");
		for(String str : paramArray) {
			String[] keyValue = str.split("=");
			paramMap.put(keyValue[0], keyValue[1]);
		}
        return paramMap;
    }
}
