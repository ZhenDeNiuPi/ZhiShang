package com.fire.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description   :
 * @author        :  nicai
 * @createDate    :  2020/2/23 4:16 PM
 * @updateUser    :  nicai
 * @updateDate    :  2020/2/23 4:16 PM
 * @updateRemark  :
 * @version       :  1.0
 */
public class ZsSmsUtil {

    public void test() throws Exception{
        String account = "15865524830";
        String mobile = "15865524830";

        String time = System.currentTimeMillis()+"";
        String order_id = time;
        String tpl_id = "TP2006082";
        String params = "name:老孙,tel:18888888888,code:速与我联系";
        String key = "e252769db0664acc90d0e8dbd2a55567";
        String sign = (mobile + "|" + account + "|" + time + "|" + tpl_id + "#" + key);
        String md5HashAsString = DigestUtils.md5Hex(sign).toUpperCase();


        String url = "http://api.yunzhixin.com:11140/txp/sms/send";
        Map<String, String> param = new HashMap<>();
        param.put("account",account);
        param.put("mobile",mobile);
        param.put("order_id",order_id);
        param.put("time",time);
        param.put("tpl_id",tpl_id);
        param.put("params",params);
        param.put("sign",md5HashAsString);
        HttpClientResult httpClientResult = HttpClientUtil.doPost(url, param);
//        int a = 1;
        System.out.println(httpClientResult);
    }

    /**
     * description   :  
     * 
     * @param mobile : 管理员配置的手机号，用于接受用户留言
     * @param tel : 用户传入的电话
     * @param code : 用户传入的备注内容
     * @param name : 用户填写的姓名
     * @return       : com.fire.back.util.HttpClientResult
     * @exception    : 
     * @date         : 2020/6/8 10:55 PM
     * @author       : Killian.Sun  Sun Hengyuan
     */
    public static HttpClientResult sendSms(String mobile,String tel, String code,String name)  throws Exception{
        String account = "15865524830";
        //String mobile = "15865524830";

        String time = System.currentTimeMillis()+"";
        String order_id = time;
        String tpl_id = "TP2006082";
        String params = "name:"+name+",tel:"+tel+",code:"+code;
        String key = "e252769db0664acc90d0e8dbd2a55567";
        String sign = (mobile + "|" + account + "|" + time + "|" + tpl_id + "#" + key);
        //String resultString = DigestUtils.md5Hex(text + key);
        String md5HashAsString = DigestUtils.md5Hex(sign).toUpperCase();

        String url = "http://api.yunzhixin.com:11140/txp/sms/send";
        Map<String, String> param = new HashMap<>();
        param.put("account",account);
        param.put("mobile",mobile);
        param.put("order_id",order_id);
        param.put("time",time);
        param.put("tpl_id",tpl_id);
        param.put("params",params);
        param.put("sign",md5HashAsString);
        HttpClientResult httpClientResult = HttpClientUtil.doPost(url, param);
        return httpClientResult;
    }

}
