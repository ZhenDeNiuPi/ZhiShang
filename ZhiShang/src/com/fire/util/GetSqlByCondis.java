package com.fire.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*by dragon*/
public final class GetSqlByCondis {
    private Str2TimeStamp sts = new Str2TimeStamp();
    public String queryCondition(Map<String,Object> condis){
        Map<String,Object> map = new HashMap<String,Object>();
		String sql = "";
    	if(condis.size()==0)
    		return sql;//重置条件时也可将空map传入
    	Set<Map.Entry<String, Object>> condi = condis.entrySet();
    	for(Map.Entry<String, Object> me : condi){//排空操作 将有值的参数提取出来
    		if(me.getValue()!=null&&me.getValue().toString().trim().length()>0&&!me.getValue().toString().equals("null"))
    		map.put(me.getKey(), me.getValue());
    	}
    	//System.out.println(map);
    	Set<String> ks = map.keySet();//对有效参数进行遍历
    	
    	for(String str:ks){
			Object value = map.get(str);
			String newStr = str.substring(0, str.length()-3);
			 value = value.toString().trim();
			//这是我设定的判断规则 唯一需要的就是在前台传给后台的时候给不同参数加不同的后缀 能提高很大效率
			//然后拼装成适用于mysql的条件语句
    		 if(str.endsWith("%s~")){//如果是字符 模糊查询
				 value = value.toString()
						 .replaceAll("\\\\","\\\\\\\\\\\\")
							.replaceAll("\"","\\\\\"")
							 .replaceAll("\'", "\\\\\'");
				 if(str.contains("`")) {//代表要用or了
					 //System.out.println(str+"  "+newStr);
					 String[] strs = newStr.split("`");
					 //System.out.println(strs.length);
					 String finalSql = " (";
					 for(String s:strs) {
						 finalSql += " locate(\""+value+"\","+s+")>0 or";
					 }
					 finalSql = finalSql.substring(0,finalSql.length()-2);
					 finalSql += ") ";
					 sql += " and "+finalSql;
					 continue;
				 }
				 if(value.toString().contains("\\\\")) {
					 	sql += " and "+newStr+" like \'%"+value+"%\' ";
				 }else {
		    			sql += " and locate(\""+value+"\","+newStr+")>0 ";
				 }
    		}else if(str.endsWith("%s=")){//字符 指定
    			sql += " and "+newStr+"='"+value+"' ";
    		}else if(str.endsWith("%i>")){//数值 大于
    			sql += " and "+newStr+">="+value+" ";
    		}else if(str.endsWith("%i=")){//等于
    			sql += " and "+newStr+"="+value+" ";
    		}else if(str.endsWith("%i<")){//小于
    			sql += " and "+newStr+"<="+value+" ";
    		}else if(str.endsWith("%i~")){//包含于
    			sql += " and "+newStr+" in("+value+") ";
    		}else if(str.endsWith("%t>")){//时间大于
    			long time = (long)sts.getTimeStamp(value.toString())/1000;
    			sql += " and "+newStr+">="+time+" ";
    		}else if(str.endsWith("%t<")){//时间小于
        			long time = (long)sts.getCircleStamp(value.toString())/1000-1;
        			sql += " and "+newStr+"<="+time+" ";
    		}else if(str.endsWith("%tL<")){//时间小于
    			long time = (long)sts.getTimeStamp(value.toString())/1000;
    			sql += " and "+newStr.substring(0,newStr.length()-1)+"<="+time+" ";
		}
    		//System.out.println("sql:"+sql);
    	}
		return sql;
    } 

	public Map<String,Object> getDTO(Map<String,String[]> temp)
	   {
			Map<String, Object> dto=null;
			if (temp != null && !temp.isEmpty()) {
				dto = new HashMap<String,Object>();
				Set<Entry<String,String[]>> entrySet = temp.entrySet();
				String val[] = null;
				for(Entry<String,String[]> entry:entrySet)
				{
					val = entry.getValue();
					if (val.length==1) dto.put( entry.getKey(), val[0]);
					else dto.put( entry.getKey(), val);	
				}
			}
			return dto;
	   }
    
  //当含有单引号时做转义操作
  	@SuppressWarnings("unused")
	private String validateNoSingleColumn (String value) {
  		Pattern p = Pattern.compile( "[^\']");
  		Matcher m = p.matcher(value);
  		boolean asd = m.find();
   		int groups = m.groupCount();
  		StringBuilder sb = new StringBuilder(value);
  		for (int i = 1; i <= groups; i++) {
  			int start = m.start(i);
  			sb.insert(start, "\\");
  		}
  		value = sb.toString();
  		return value;
  	}
    
    
}
