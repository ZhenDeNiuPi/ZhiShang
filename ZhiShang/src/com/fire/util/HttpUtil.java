package com.fire.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.jfinal.kit.JsonKit;

public class HttpUtil {
	public static void main(String[] args) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("parkId", "029bc8fd3599458b8a7febafdcad76d9");
		map.put("carNum", "京A12345");
		map.put("inDt", "20180516213810");
		map.put("tradeId", "6d697f7579724345881633a00ed3a671");
		map.put("outDt", "20180516230521");
		map.put("parkAmount", "5.00");
		String json = JsonKit.toJson(map);
		System.out.println(json);
		String token = "";
//		String url = "http://localhost:8080/HisI_Task/tjd/outMessage";
		String url = "http://150.138.119.252:8081/HisI_Task/tjd/outMessage";
		String result = doPost(url,token,json);
//		String result = doPost("http://39.96.51.202:8081/ArtOfConquest/user/getReg?qnmd=qnmd",token,null);
		System.out.println(result);
	}
	
	/*public static void main(String[] args) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> list = new ArrayList<String>();
		list.add("000001,鲁A12345,,第一通道,0,,,2019-06-13 10:28:02");
		list.add("000001,鲁A12345,,第二通道,1,,,2019-06-13 10:48:02");
		map.put("data", list);
		String json = JsonKit.toJson(map);
		System.out.println(json);
		String token = "eyJzdWIiOiJocnBhcmtpbmdAcGNvcC5jb20iLCJjcmVhdGVkIjoxNTU5NTg1Mzc4Mzk3LCJleHAiOjIxOTAzMDUzNzh9.guqAoN49KurQXu0XYa5qnjueA3c3iTTor54M7WfyTOPEV6Nlcmq0xA5e-bJLT4JqF_iGrreKq40e0NbXb0zdxw";
		String url = "http://pcopiot.glodon.com/stream/data/api";
		String result = doPost(url,token,json);
//		String result = doPost("http://39.96.51.202:8081/ArtOfConquest/user/getReg?qnmd=qnmd",token,null);
		System.out.println(result);
	}*/
	/** 
     * post请求（用于请求json格式的参数） 
     * @param url 接口地址
     * @param params 接口参数
     * @return 
     */  
    public static String doPost(String url,String token, String params) {      
    	HttpClientBuilder builder = HttpClients.custom();
    	builder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)"); 
        CloseableHttpClient httpclient = builder.build();;  
        HttpPost httpPost = new HttpPost(url);// 创建httpPost     
//		Header header = new Header("Authorization", "Bearer " + token);
        httpPost.setHeader("Accept", "application/json");    //接收报文类型
        httpPost.setHeader("Authorization", "Bearer "+token);    //接收报文类型
        httpPost.setHeader("Content-Type", "application/json");   //发送报文类型
        if(params != null && !"".equals(params)){
            StringEntity entity = new StringEntity(params, "UTF-8");  
            httpPost.setEntity(entity);    	
        }     
        CloseableHttpResponse response = null;     
        try {  
            response = httpclient.execute(httpPost);  
            StatusLine status = response.getStatusLine();  
            int state = status.getStatusCode();  
            if (state == HttpStatus.SC_OK) {  
                HttpEntity responseEntity = response.getEntity();  
                String jsonString = EntityUtils.toString(responseEntity,"UTF-8");  
                return jsonString;  
            }  
            else{  
                System.out.println(state);
            }  
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        finally { 
                try {  
                	if (response != null)response.close();  
                	httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
        }  
        return null;  
    }  
    
    
    public static String doGet(String url,String token, String params) {      
    	HttpClientBuilder builder = HttpClients.custom();
    	builder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)"); 
        CloseableHttpClient httpclient = builder.build();; 
//        HttpPost httpPost = new HttpPost(url);// 创建httpPost    
        HttpGet httpPost = new HttpGet(url);// 创建httpPost   
        RequestConfig requestConfig =  
        		RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
//		Header header = new Header("Authorization", "Bearer " + token);
//        httpPost.setHeader("Accept", "application/json");    //接收报文类型
//        httpPost.setHeader("Authorization", "Bearer "+token);    //接收报文类型
//        httpPost.setHeader("Content-Type", "application/json");   //发送报文类型
        /*if(params != null && !"".equals(params)){
            StringEntity entity = new StringEntity(params, "UTF-8");  
            httpPost.setEntity(entity);    	
        } */    
        CloseableHttpResponse response = null;     
        try {  
            response = httpclient.execute(httpPost);  
            StatusLine status = response.getStatusLine();  
            int state = status.getStatusCode();  
            if (state == HttpStatus.SC_OK) {  
                HttpEntity responseEntity = response.getEntity();  
                String jsonString = EntityUtils.toString(responseEntity,"UTF-8");  
                return jsonString;  
            }  
            else{  
                System.out.println(state);
            }  
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        finally { 
                try {  
                	if (response != null)response.close();  
                	httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
        }  
        return null;  
    }  
    
    public static String doGet(String url,String token, Map<String,String> params) {      
    	HttpClientBuilder builder = HttpClients.custom();
    	builder.setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)"); 
        CloseableHttpClient httpclient = builder.build();; 
//        HttpPost httpPost = new HttpPost(url);// 创建httpPost    
        HttpGet httpPost = new HttpGet(url);// 创建httpPost   
        RequestConfig requestConfig =  
        		RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        httpPost.setConfig(requestConfig);
//		Header header = new Header("Authorization", "Bearer " + token);
//        httpPost.setHeader("Accept", "application/json");    //接收报文类型
//        httpPost.setHeader("Authorization", "Bearer "+token);    //接收报文类型
//        httpPost.setHeader("Content-Type", "application/json");   //发送报文类型
        /*if(params != null && !"".equals(params)){
            StringEntity entity = new StringEntity(params, "UTF-8");  
            httpPost.setEntity(entity);    	
        } */    
        url += "?";
        for(String key : params.keySet()) {
        	url+= key + "=" + params.get(key) + "&";
        }
        url = url.substring(0,url.length()-1);
        System.out.println(url);
        CloseableHttpResponse response = null;     
        try {  
            response = httpclient.execute(httpPost);  
            StatusLine status = response.getStatusLine();  
            int state = status.getStatusCode();  
            if (state == HttpStatus.SC_OK) {  
                HttpEntity responseEntity = response.getEntity();  
                String jsonString = EntityUtils.toString(responseEntity,"UTF-8");  
                return jsonString;  
            }  
            else{  
                System.out.println(state);
            }  
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        finally { 
                try {  
                	if (response != null)response.close();  
                	httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
        }  
        return null;  
    }
}
