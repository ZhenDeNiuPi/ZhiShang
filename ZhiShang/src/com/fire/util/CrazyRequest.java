package com.fire.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

@SuppressWarnings({ "rawtypes", "unused" })
public class CrazyRequest {
	private static Field requestField;  
	  
	private static Field parametersParsedField;  
	  
	private static Field coyoteRequestField;  
	  
	private static Field parametersField;  
	  
	private static Field hashTabArrField;  
	
	static {
		try {  
            Class clazz = Class.forName("org.apache.catalina.connector.RequestFacade");  
            requestField = clazz.getDeclaredField("request");  
            requestField.setAccessible(true);  
  
  
            parametersParsedField = requestField.getType().getDeclaredField("parametersParsed");  
            parametersParsedField.setAccessible(true);  
  
  
            coyoteRequestField = requestField.getType().getDeclaredField("coyoteRequest");  
            coyoteRequestField.setAccessible(true);  
  
  
            parametersField = coyoteRequestField.getType().getDeclaredField("parameters");  
            parametersField.setAccessible(true);  
//            Field[] fs = parametersField.getType().getDeclaredFields();
            
//            hashTabArrField = parametersField.getType().getDeclaredField("paramHashStringArray");  
            hashTabArrField = parametersField.getType().getDeclaredField("paramHashValues");  
            hashTabArrField.setAccessible(true);  
        } catch (Exception e) {  
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  
        } 
	}
	
	@SuppressWarnings("unchecked")  
    public Map<String , List<String>> getRequestMap(ServletRequest request) {  
        try {  
            Object innerRequest = requestField.get(request);  
            parametersParsedField.setBoolean(innerRequest, true);  
            Object coyoteRequestObject = coyoteRequestField.get(innerRequest);  
            Object parameterObject = parametersField.get(coyoteRequestObject);  
            return (Map<String,List<String>>)hashTabArrField.get(parameterObject);  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
            return Collections.EMPTY_MAP;  	
        }  
    } 
}
