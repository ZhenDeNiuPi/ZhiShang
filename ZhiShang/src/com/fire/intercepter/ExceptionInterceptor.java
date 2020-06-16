package com.fire.intercepter;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fire.util.CrazyRequest;
import com.fire.util.Str2TimeStamp;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Db;

public class ExceptionInterceptor implements Interceptor{
		Logger logg=Logger.getLogger(ExceptionInterceptor.class);
		CrazyRequest cr = new CrazyRequest();
		Str2TimeStamp sts = new Str2TimeStamp();
	   public void intercept(Invocation ai) {
		   Long now = System.currentTimeMillis()/1000;
		      try {
		    	 Controller c = ai.getController();
		         HttpServletRequest request = ai.getController().getRequest();
		 		 String ip = getIp(request);
		         Map<String,String> queryParams = new HashMap<>(); 
		         String ajax = c.getPara("05fsKaKm343jELhTpa15fpgx");
		         if (ajax!=null&&decodeDragon(ajax).equals("ajax")) {
			         String url = request.getQueryString();
			         if(url!=null && !"".equals(url)) {
			        	 String[] ps = url.split("&");
				         for(String pkv:ps) {
				        	 String[] kv = pkv.split("="); 
				        	 queryParams.put(kv[0], kv[1]);
				         }
			         }
	    			 Map<String,String> params = new HashMap<String,String>();
			    	 Enumeration<String> objs3 = c.getParaNames();
			    	 int checkSum = 0;//嘻嘻
			    	 String key = "";
			    	 String value = "";
			    	 int paramNum = 0;
			    	 while(objs3.hasMoreElements()) {
			    		 String a = objs3.nextElement();
			    		 if(queryParams.containsKey(a)) continue;
			    		 if(a.equals("05fsKaKm343jELhTpa15fpgx"))continue;
				    	 value = c.getPara(a);
				    	 value = decodeDragon(value);
			    		 key = decodeDragon(a);
			    		 //正常来说搞事的话这里就报错了
			    		 checkSum += Integer.parseInt(key.substring(key.length()-2));
			    		 key = key.substring(0, key.length()-2);
			    		 params.put(key, value);
			    		 paramNum++;
			    	 }
			    	 int i = 1;
			    	 int check = 0;
			    	 for(;i<=paramNum;i++) {
			    		 check+=i;
			    	 }
			    	 Long tempNow = Long.parseLong(params.get("ajaxTime"));
			    	 if(check!=checkSum || now-tempNow>600 || tempNow>now+600) {//有人搞事 先不管 以后忙完别的封ip
//				    	 System.out.println(checkSum+":"+paramNum);
				    	 logg.error("前台上传："+tempNow+"；后台接收："+now+"；"+checkSum+":"+check);
			    		int count2 = Integer.parseInt(Db.findFirst("select ifnull(count(0),0) count from login_iperror_tb where lock_ip='"+ip+"'").get("count")+"");
			 			if(count2>0) {
			 				Db.update("update login_iperror_tb set error_time = error_time+1");				
			 			}else {
			 				Db.update("insert into login_iperror_tb (lock_ip,error_time) values('"+ip+"',1)");	
			 			}
				         deal(ai,"fuck");
			    		 return;
			    	 }
			    	 Map<String , List<String>> map = cr.getRequestMap(request);  
			    	 for(Entry<String, String> param : params.entrySet()) {
				    	 List<String> list = new ArrayList<String>();
				    	 list.add(param.getValue());
				         map.put(param.getKey(), list);
			    	 }
			    	 for(Entry<String, String> param : queryParams.entrySet()) {
				    	 List<String> list = new ArrayList<String>();
				    	 list.add(param.getValue());
				         map.put(param.getKey(), list);
			    	 }
			    	 map.remove("05fsKaKm343jELhTpa15fpgx");
		    	 }
		         ai.invoke();
		      } catch (Exception e) {
		    	 logWrite(ai,e);
		         deal(ai,"break");
		      }
		   }
	    private void logWrite(Invocation inv,Exception e){
	        //开发模式
	        if (JFinal.me().getConstants().getDevMode()){
	            e.printStackTrace();
	        }
	        StringBuilder sb =new StringBuilder("\n---Exception Log Begin---\n");
	        sb.append("Controller:").append(inv.getController().getClass().getName()).append("\n");
	        sb.append("Method:").append(inv.getMethodName()).append("\n");
	        sb.append("Exception Type:").append(e.getClass().getName()).append("\n");
	        sb.append("Exception Details:");
	        logg.error(sb.toString(),e);
	        System.out.println(sb.toString());
//	        if(PropKit.getBoolean("devMode"))return;
//		    SendMail.sendExceptionAllinformation(e);
	    }
	    private static String decodeDragon(String str){
	    	if(str==null||str.length()==0||str.equalsIgnoreCase("null"))return "";
//	    	str = str.replaceAll("%25", "%").replaceAll("%3D", "=").replaceAll("%3E", ">").replaceAll("%3C", "<").replaceAll("%3A", ":").replaceAll("%2C", ",").replaceAll("%7C", "|").replaceAll("\\+", " ");
	    	int length = str.length();
	    	int num = length/6;
	    	for(int i=1;i<=num;i++) {
	    		str = str.substring(0, str.length()-5-i)+str.substring(str.length()-i);
	    	}
	    	str = str.replaceAll("\\+", " ").replaceAll("%25", "%").replaceAll("%3D", "=")
	    		.replaceAll("%3E", ">").replaceAll("%3C", "<").replaceAll("%3A", ":")
	    		.replaceAll("%2C", ",").replaceAll("%7C", "|").replaceAll("%26", "&")
	    		.replaceAll("%5E", "^").replaceAll("%2B", "+").replaceAll("%2F", "/")
	    		.replaceAll("%3B", ";").replaceAll("%3A", ":").replaceAll("%3F", "?")
	    		.replaceAll("%22", "\"").replaceAll("%7B", "{").replaceAll("%7D", "}")
	    		.replaceAll("%60", "`");	    	
	    		String temp = str;
	    		String front = "";
	    		String back = "";
    		while(temp.indexOf("%")>-1) {//第一个不是最后一个
    			if(temp.indexOf("%")+9>temp.length())break;
    			if(temp.indexOf("%")!=0)front += temp.substring(0,temp.indexOf("%"));//前段
    			back = temp.substring(temp.indexOf("%")+9,temp.length());//后段
    	    	try {//一次次将前段的增长
    	    		front += URLDecoder.decode(temp.substring(temp.indexOf("%"),temp.indexOf("%")+9), "utf-8");
    	    	} catch (Exception e) {
    	    		//如果出错就把原来的加上
    	    		front += temp.substring(temp.indexOf("%"),temp.indexOf("%")+9);
    	    	}
    	    	temp = back;
    		}
    		str = front + temp ;
	    	return str;
	    }
	    public static void main(String[] args) {
			
			/*String all = "isCN9oevMYo.ofYEoc3HmiPo1PqLAmkkiKji6p00adMlMAC%BzceX2crpPU5lFpc4iqcKIR%tOiFc3M6WnMDXyGs80GQczo1=&Zpr7EoQwduJ.BzVQXoUCyTPphJ7b0eXkaoJrLRtVwageoKetkn5YZo9cIBGrntFV9_qyR5uimB3nhd70qqU%38ZmW2Yu5P95wO1ycsdaFq8%SZGRi3SIF5lDCyr7X05V2t22=&LKHp0om0Anv.ENIVTcJxQvXaHvoJBrVPxIA_zfeUmnjvrwIuSgLYVm73khlb1JugreHGCBdrNfSW8%y3G022PeBGr5FQyPJscj8Rz~Ne1Ba05eBaQ3=&wLEaxoVhGAt.SGIw5chiZKmrboARNeofgESaJxlRCtP8fEpeQ9Ved_CUci8tFEnJNibCvkbmTfonke7G673%SvCM12xf5n3507j3AtYdNZ8%zXc523vcp4eEbU3CI0XjSN74=EdafR2ZtAn40l9Ce61b6CCI8QUJj1-qcoX504n9vd7SwxDO-gt0lw04964U1Smexi+DAwxe064u1T0cJaHu%efudy3fnktUAwb56M0kcTFC0&65X9voM3Leg.riOd0cjvsC5rSfwd8eOeFeua5su0ktsePAbeVLE03_LXodptDveSaijGpOEmUUspHeua0EN%BluPk2WOCpQ5XuLF2tXzvaK%jwNHS3bj0GjCZDEU40CgEuZ5=cdb8P2QoJHg0qmkni1aroDb8rhkAq-XaUP50j5ho37bcR3k-IhgK23CcNxw1lWbCU+Gw4Bl21lcdh3Kvi86%JTEd53jYG0cAzNaOz5ZBHzr9&xC54xoq74ZF.7RlUDtwcD5XogiJL1tr52vvaNrv3Altnl9r%BAxMU2wrM6a562mZIiOvRxj%YzvmT3IFW4zEYpMkn0RpZnZ6=nkCFI1&lDXSDo8LqN1.N7vARe0piNGnpanG0dNDqv8_p8NmgtDa5qpioYpbRmAiWsXeWadAe%FaVpz2umZgk5yD225tp6RR2%hY2o635gn2tEZSsTK0NvdXH7=ACaHW2PV1qe0uHPJT1vNZ0S8OTwDO-KOU7n054Dfw73LIEM-5vym00XL0kF14BMxT+5uZac0yUKgW0OYo7q%KBEis3IfexDASXmfz0e6NNu0&d96K1ojuZk8.m7BpoetHfw8nqPdbTdbXqAW_PBurxtRa5BJiTN3Cgm4pjF0e5ufXA%h4Mkd2HheNU5ayvPjtBFLGn%Qod7C3AcGoVC1LPeb0FFxH58=v51am2jEaOd0RdisE1OjmAy870fB2-k9yRO0Zks6U7uCx3S-aR32T3z99JB19X0Zw+doNRX2w6WMU3BEXrX%HzrF338nXJBAEXa3o54HOfe9&J0fGSoQqkUE.XW3HWtxwv0bopULxStAARoXambvvOlXkmCz%HGqr52FLbE75dPwPyi8D1GJ%Nkbts3oVXsxCtZoVC005j2c9=8vh7h2&0ay4tooe4he.x8BV9pyUT45aIbcJMyQCnd8_aXFCLtiV0C9yALRR3pzutUXeRX1QJ%0YAyH2f5aYI5pCGYsipkbWe%dARmA3fJ34QDtQfQg127Lbx0=&F4tFzo2zMVn.ND19acWF3XYatiFvCrv2dUT_VxQnWiPdLFEn4EsbiTcDu0byYBOtcprAsTteCarg1%vZHNz2fl2KW5Ebjxui5jmwd%1gpOu3Nq9rVD2MERh17KaC41=";
			String[] strs = all.split("&");
			for(String str : strs) {
				String[] ins = str.split("=");
				System.out.println(decodeDragon(ins[0]));
			}*/
		}
		
		public static String getIp(HttpServletRequest request){

			  String ip = request.getHeader("x-forwarded-for"); 
			  if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			  ip = request.getHeader("Proxy-Client-IP"); 
			  } 
			  if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			  ip = request.getHeader("WL-Proxy-Client-IP"); 
			  } 
			  if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			      ip = request.getRemoteAddr(); 
			  } 
			  return ip; 
		}
		
		public void deal(Invocation ai,String status) {
			HttpServletResponse res = ai.getController().getResponse();
	    	 res.setHeader("sessionstatus", status);//在响应头设置session状态 
	    	 ai.getController().removeSessionAttr("authlist"); 
	    	 ai.getController().removeSessionAttr("menuauthlist"); 
	    	 ai.getController().removeSessionAttr("userinfo"); 
	    	 ai.getController().removeSessionAttr("loginroleid");
	    	 ai.getController().setSessionAttr("errormessage", "&nbsp;&nbsp;&nbsp;网络异常！请稍候再试！");
	         ai.getController().render("/WEB-INF/pages/index.html");

		   	  /*String idStr = ai.getController().getSession().getAttribute("loginuin")+"";
		   	  Long id = -1L;
		   	  try{
		   		  id = Long.parseLong(idStr);
		   	  }catch(Exception ex) {
		   		  id = -1L;
		   	  }
		   	  if(id>-1) {
		   		  Db.update("update user_tb set if_login=0 where id="+id);//更改登录状态
		   	  }
		     ai.getController().removeSessionAttr("loginuin");*/
		}
}
