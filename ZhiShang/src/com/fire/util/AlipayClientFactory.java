/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fire.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.jfinal.kit.PropKit;


/**
 * @author alipay.demo
 *
 */
public class AlipayClientFactory{
	private static final Logger logger = LoggerFactory.getLogger(AlipayClientFactory.class);

	private static AlipayClient alipayClient = null;
	
	private AlipayClientFactory() {}

	public static AlipayClient getClient() throws Exception {
		// 请求方式 json
		String FORMAT = "json";
		// 编码格式，目前只支持UTF-8
		String CHARSET = "UTF-8";
		// 签名方式
		String SIGN_TYPE = "RSA2";
		// 网关
		String URL = PropKit.get("alipay.appconfig.gateway");
		// 商户APP_ID
		String APP_ID = PropKit.get("alipay.appconfig.appid");
		// 商户RSA 私钥
		String APP_PRIVATE_KEY = PropKit.get("alipay.appconfig.privatekey");
		// 支付宝公钥
		String ALIPAY_PUBLIC_KEY = PropKit.get("alipay.appconfig.publickey");
		if(alipayClient == null)
			alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, 
			ALIPAY_PUBLIC_KEY, SIGN_TYPE);
		logger.info("创建支付宝网关访问客户端完成, 网关地址：{}，appId:{}", URL, APP_ID);
		return alipayClient;
	}
}