package com.fire.intercepter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fire.util.Str2TimeStamp;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.validate.Validator;

/*by dragon*/
public abstract class FormValidator extends Validator {
	/*
	 * 在表单验证中  统一使用"error"作为返回错误信息的key
	 * fieldName作为传递表单项name的key
	 * success:function(result){
	 * 	alert(result["error"]);
	 * }
	 * 要求fieldName跟表单项name值完全相同
	 */
	//大部分正则表达式  以提供正则校验 1.0系统里带的
	protected static final String error = "errormessage";
	protected static final String pass8to16 = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
	protected static final String nickname = "^[0-9A-Za-z\u0391-\uFFE5]+$";
	protected static final String mobile = "^(((\\+86)?1[34578]\\d{9})|(400\\d{7}))$";
	protected static final String emailPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	
	protected static final String pin ="^\\d{4,8}$";
	protected static final String jingweidu ="^(([1-9][0-9]{0,2})(\\.{1}[0-9]{1,6}))?|(([0]{1})(\\.{1}[0-9]{1,6}))?$";
	protected static final String anzhuo ="^[0-9]{1,6}[m]{1}\\_{1}[0-9]{1,6}[m]{1}$";
	protected static final String numAndWord = "^[a-zA-Z0-9]+$";
	protected static final String special = "^([\u4e00-\u9fa5]|[a-zA-Z0-9])+$";
	protected static final String special2 = "^([\u4e00-\u9fa5]|[a-zA-Z0-9]|\\(|\\)|\\（|\\）|\\.|\\。|\\,|\\，)+$";
	protected static final String address = "^(?=.*?[\u4E00-\u9FA5])[\\dA-za-z\u4E00-\u9FA5]+";
	protected static final String carNumberPattern = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1}$";
	protected static final String carNameRegex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z0-9挂学警港澳]{0,30}$";
	protected static final String englishPattern = "^[A-Za-z]+$";
	protected static final String chinesePattern = "^[\u0391-\uFFE5]+$";
	protected static final String urlPattern = "^(http:\\/\\/)?[A-Za-z0-9]+\\.[A-Za-z0-9]+[\\/=\\?%\\-&_~`@[\\]\\':+!]*([^<>\\'\\'])*$";
	protected static final String ipPattern = "^(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5]).(0|[1-9]\\d?|[0-1]\\d{2}|2[0-4]\\d|25[0-5])$";
	protected static final String zip = "^[1-9]\\d{5}$";
	protected static final String qq = "^[1-9]\\d{4,15}$";
	protected static final String number = "^[0-9]\\d{0,30}$";
	protected static final String sbnumber = "^[0-9]+$";
	protected static final String qqnum = "^[0-9]\\d{4,15}$";
	protected static final String usercardid = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
	protected static final String username = "^[\u4e00-\u9fa5]{2,20}$";
	protected static final String funcname = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
	protected static final String doub = "^\\d{1,8}(\\.\\d{1,3})?$";
	protected static final String alpha = "^[0-9a-zA-Z\\_]+$";
	protected static final String strid = "^[a-zA-Z\\_]*[0-9]*[a-zA-Z\\_]+[0-9]*[a-zA-Z\\_]*$";
	protected static final String phone = "^((\\+86)?(0[1-9]{1}\\d{9,10})|([2-9]\\d{6,7}))|(400\\d{7})$";
	protected static final String callnum = "^(1[4358]\\d{9})|[8]\\d{7}$";
	protected static final String callnum1 = "^(^((0\\d{2,3})(-)?)?(\\d{7,8})(-(\\d{3,}))?$)|(^(((\\+86)?1\\d{10})|(400\\d{7}))$)$";
	protected static final String jw = "^-?((0|[1-9]\\d?|1[1-7]\\d)(\\.\\d{1,7})?|180(\\.0{1,7})?)?$";
	protected static final String wd = "^-?((\\d|[1-8]\\d)(\\.\\d{1,7})?|90(\\.0{1,7})?)?$";
	protected static final String numAndWordLength = "^[0-9a-zA-Z]{2,20}$";;
	protected static final String CHINESE = "^[^\u4e00-\u9fa5]{0,}$";
	protected static final String CHINESENAME = "^[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*$";
	protected static final String ISIPADDRESS = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$";
	protected static final String NOSINGLECOLUMN = "^[^']*$";
	protected static final String OnlyNum = "^[0-9]+$";
	protected static final String DateTime="^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01]) (0\\d{1}|1\\d{1}|2[0-3]):([0-5]\\d{1})$";//2017年12月13日添加
	protected static final String Date="^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$";
	protected Str2TimeStamp sts = new Str2TimeStamp();
	//校验输入时间是否后面的时间大于前面的时间  需传入时间差 	添加和修改都只需要输入进场时间 出场时间可以不填 填的话必须大于前面的时间
	protected void validateCorrectTime(String field1,String field2,String timeName1,String timeName2){
	String time1 = controller.getPara(field1);
	String time2 = controller.getPara(field2);
	if(StrKit.isBlank(time1)){
		addError2(error, timeName1+"必须输入！",field1);
		return;
	}
	if(StrKit.isBlank(time2)){
		return;
	}
	try{
	long l1 = sts.getTimeStamp(time1);
	long l2 = sts.getTimeStamp(time2);
		if ((l2-l1)<0){
			addError2(error, timeName2+"不能小于"+timeName1+"!",field2);
			return;
		}
		}catch(Exception e){
//			addError2(error, "请检查您输入的时间！",field1);
			addError2(error, timeName2+"不能小于"+timeName1+"!",field2);
		}
	}
	//校验输入金额必须大于等于0 小于等于1000 小数点后最多两位
	protected void validateCorrectMoney(String field){
		String str = controller.getPara(field).trim();
		if(!StrKit.isBlank(str)){
			try {
				Double.parseDouble(str);
			}catch(Exception e){
				addError2(error, "请输入正确的整数或小数！",field);
				return;
			}

			if(str.contains(".")){
//				String big = str.substring(0,str.indexOf("."));
				String small = str.substring(str.indexOf(".")+1);
				if(small.length()>2){
					addError2(error, "小数点后最多两位！",field);
					return;
				}
			}
			Double total = Double.parseDouble(str);
			if(total>9999||total<0) {
				addError2(error, "金额范围0到9999！",field);
				return;
			}
			/*
			
			
			if(str.contains(".")){//小数
				String[] strs = str.split(".");
				if(strs.length>2){
					addError2(error, "请输入整数或小数！",field);
					return;
				}
				String big = str.substring(0,str.indexOf("."));
				String small = str.substring(str.indexOf(".")+1);
				if(!ifInteger2(big,"请输入整数或小数！",field))return;
				if(!ifInteger2(small,"请输入整数或小数！",field))return;
				int bigNum = Integer.parseInt(big);
				if(bigNum<0 || bigNum>9999){
					//System.out.println("field:" + field + " controller:" + controller);
					addError2(error, "金额范围0到9999！",field);
					return;
				}
				if(small.length()>2){
					addError2(error, "小数点后最多两位！",field);
					return;
				}
				if((bigNum+Integer.parseInt(small))>9999) {
					addError2(error, "金额范围0到9999！",field);
					return;
				}
			}else{//整数
				if(!ifInteger2(str,"请输入整数或小数！",field))return;
				int total = Integer.parseInt(str);
				if(total<0||total>9999){
					addError2(error, "金额范围0到9999！",field);
					return;
				}
			}*/
		}else{
			addError2(error, "金额必须输入！",field);
			return;
		}
	}
	//判断是否是数字   传入的是fieldName
	protected boolean ifInteger(String field,String errormsg){
		boolean result = true;
		
		try{
			Long.parseLong(controller.getPara(field));
		}catch(Exception e){
			addError2(error, errormsg,field);
			result = false;
			return result;
		}
		return result;
	}
	//判断是否是数字   传入的是fieldName
	protected boolean ifInteger(String field,String errormsg,int length){
		boolean result = true;
		try{
			Long.parseLong(controller.getPara(field));
		}catch(Exception e){
			addError2(error, errormsg,field);
			result = false;
			return result;
		}
		if(controller.getPara(field).length()>length){
			addError2(error, "限制输入最多"+length+"位！",field);
		}
		return result;
	}
	
	protected boolean ifLong(String field,String errormsg){
		boolean result = true;
		try{
			Long.parseLong(controller.getPara(field));
		}catch(Exception e){
			addError2(error, errormsg,field);
			result = false;
			return result;
		}
		return result;
	}
	
	protected boolean ifDouble(String field,String errormsg){
		boolean result = true;
		
		try{
			Double.parseDouble(controller.getPara(field));
		}catch(Exception e){
			addError2(error, errormsg,field);
			result = false;
			return result;
		}
		return result;
	}
	//判断是否是数字   传入的是fieldValue
	protected boolean ifInteger2(String str,String errormsg,String field){
		boolean result = true;
		try{
			Long.parseLong(str);
		}catch(Exception e){
			addError2(error, errormsg,field);
			result = false;
		}
		return result;
	}
	//判断小数点后最多两位 不限大小
	protected void isDouble(String field,String errormsg){
		String str = controller.getPara(field)+"";
		if(!str.contains(".")){
			if(!ifLong(field,errormsg))return;
			if(str.length()>10)
				addError2(error, "数额过大，请输入合理数据！",field);
		}else{
			 if(str.split(".").length>2){
				 addError2(error, errormsg,field);
					return;	 
			 }else{
					String big = str.substring(0,str.indexOf("."));
					String small = str.substring(str.indexOf(".")+1);
					if(!ifInteger2(big,errormsg,field))return;
					if(!ifInteger2(small,errormsg,field))return;
					if(big.length()>10){
						addError2(error, "数额过大，请输入合理数据！",field);
						return;
					}
					if(small.length()>2){
						addError2(error, "小数点后最多两位！",field);
					}
			 }
		}
	}
	//判断小数点后最多两位 不限大小
		protected void isDoublePrice(String field,String errormsg){
			String str = controller.getPara(field)+"";
			if(!str.contains(".")){
				if(!ifLong(field,errormsg))return;
				if(str.length()>10)
					addError2(error, "数额过大，请输入合理数据！",field);
			}else{
				 if(str.split(".").length>2){
					 addError2(error, errormsg,field);
						return;	 
				 }else{
						String big = str.substring(0,str.indexOf("."));
						String small = str.substring(str.indexOf(".")+1);
						if(!ifInteger2(big,errormsg,field))return;
						if(!ifInteger2(small,errormsg,field))return;
						if(big.length()>10){
							addError2(error, "数额过大，请输入合理数据！",field);
							return;
						}
						if(small.length()>4){
							addError2(error, "小数点后最多两位！",field);
						}
				 }
			}
		}
	//多正则校验 满足一个即可
	protected void validateRegexMany(String field, String[] regExpression, boolean isCaseSensitive, String errorKey, String errorMessage) {
        String value = controller.getPara(field);
        if (value == null) {
        	addError2(errorKey, errorMessage,field);
        	return ;
        }
        int y = 0;
        for(int x=0;x<regExpression.length;x++){
            Pattern pattern = isCaseSensitive ? Pattern.compile(regExpression[x]) : Pattern.compile(regExpression[x], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()){
//            	addError2(errorKey, errorMessage);
            	y=1;
            }
        }
        if(y==0)addError2(errorKey, errorMessage,field);
	}
	//排重 需要当前要查重的值 表名 字段名 错误key 错误信息 限于字符串类型 添加的时候
	protected void ifRepeat(String field,String tName,String wName,String errorKey, String errorMessage){
		String value = controller.getPara(field);
		if(value==null||value.equals(""))return;
		Record record = Db.findFirst("select count(0) count from "+tName+" where "+wName+"='"+value+"'");
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
	}
	//排重 泊位段
	protected void ifRepeatbes(String field,String tName,String wName,String errorKey, String errorMessage,String field1){
		String value = controller.getPara(field);
		String value1 = controller.getPara(field1);
		if(value==null||value.equals(""))return;
		if(value1==null||value1.equals(""))return;
		Record record = Db.findFirst("select count(0) count from "+tName+" where "+wName+"='"+value+"' and is_active = 0 and comid ="+value1);
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
	}
	protected void ifRepeat(String field,String countSql,String errorKey, String errorMessage){
		String value = controller.getPara(field);
		if(value==null||value.equals(""))return;
		Record record = Db.findFirst(countSql);
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
	}
	//排重 需要当前要查重的值 表名 字段名 错误key 错误信息 限于字符串类型 更新的时候
	protected void ifRepeat2(String filedId,String field,String tName,String wName,String errorKey, String errorMessage){
		String idValue = controller.getPara(filedId);
		String value = controller.getPara(field);
		if(idValue==null||value==null||value.equals("")||idValue.equals(""))return;
		Record record = Db.findFirst("select count(0) count from "+tName+" where "+wName+"='"+value+"' and id<>"+idValue);
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
	}
	protected void ifRepeat2(String field,String countSql,String errorKey, String errorMessage){
		String value = controller.getPara(field);
		if(value==null||value.equals(""))return;
		Record record = Db.findFirst(countSql);
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
	}
	//表里有这个值 但如果状态允许则可以添加 限于字符串类型 状态值为int 且不等于 添加的时候
	protected void ifRepeatInState(String field,String tName,String wName,String wName2,String wValue2,String errorKey, String errorMessage,String type,String tValue){
		String value = controller.getPara(field);
		if(value==null||value.equals(""))return;
		Record record = Db.findFirst("select count(0) count from "+tName+" where "+wName+"='"+value+"' and "+wName2+"<>"+wValue2+" and "+type+"="+tValue);
		//System.out.println(record);
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
	}
	//表里有这个值 但如果状态允许则可以添加 限于字符串类型 状态值为int 且不等于 更新的时候
	protected void ifRepeatInState2(String filedId,String field,String tName,String wName,String wName2,String wValue2,String errorKey, String errorMessage,String type,String tValue){
		String idValue = controller.getPara(filedId);
		String value = controller.getPara(field);
		if(idValue==null||value==null)return;
		Record record = Db.findFirst("select count(0) count from "+tName+" where "+wName+"='"+value+"' and "+wName2+"<>"+wValue2+" and "+type+"="+tValue+" and id<>"+idValue);
		//System.out.println(record);
		if(!(record.getLong("count")==0))addError2(errorKey, errorMessage,field);
}
	//用于验证共享车位数是否合理
	protected boolean comShareNumber(String field,String field2){
		//拿取停车场编号
		String comid = controller.getPara(field2);
		//拿取要校验的共享车位数
		int num = controller.getParaToInt(field);
		//查询表中已用共享车位数 要设置的数字要大于等于这个数
		Record record = Db.findFirst("select (ifnull(c.share_number,0)-ifnull(min(cp.remaining_number),0)) num from com_park_card_number cp,com_info_tb c where cp.park_id=c.id and cp.park_id="+comid);
		if(record==null||record.get("num")==null||"0".equals(record.get("num")+"")){
			return true;
		}
		int maxNum = Integer.parseInt(record.get("num")+"");
		if(num>=maxNum){
			return true;
		}else{
			addError2(error, "共享车位数需要大于当前车场所售的有效卡片的最大数！至少为"+maxNum+"个！",field);
			return false;
		}
	}
	//组合验证 排空 验证长度
	protected boolean comBindValidate(String field,int num,String errorName){
		boolean result = false;
		String value = controller.getPara(field)+"";
		if (StrKit.isBlank(value)){
			addError2(error, errorName+"必须输入！输入的内容长度需在2到"+num+"个字符之间！",field);
			return result;
		}
		if(value.contains(" ")){
			addError2(error, errorName+"输入的内容中不能带有空格！",field);
			return result;
		}
		if(value.length()>num){
			addError2(error, errorName+"输入的内容长度需在2到"+num+"个字符之间！",field);
			return result;
		}
		if(value.length()<2){
			addError2(error, errorName+"输入的内容长度需在2到"+num+"个字符之间！",field);
			return result;
		}
		result = true;
		return result;
	}

	//组合验证 排空 验证长度  由于有楼层这种特殊的存在 不得不再加一个
	protected boolean comBindValidate2(String field,int num,String errorName){
		boolean result = false;
		String value = controller.getPara(field)+"";
		if (StrKit.isBlank(value)){
			addError2(error, errorName+"必须输入！输入的内容长度需在1到"+num+"个字符之间！",field);
			return result;
		}
//		validateRegex(field, special2, error, errorName+"不能输入特殊字符，请重新输入！");
		if(value.contains(" ")){
			addError2(error, errorName+"输入的内容中不能带有空格！",field);
			return result;
		}
		if(value.length()>num){
			addError2(error, errorName+"输入的内容长度需在1到"+num+"个字符之间！",field);
			return result;
		}
//		if(value.contains("@")||value.contains("#")||value.contains("$")||value.contains("%")||value.contains("^")||value.contains("~")||value.contains("*")||value.contains("(")||value.contains(")")||value.contains("+")||value.contains("=")||value.contains("￥")||value.contains("?")||value.contains("!")||value.contains("？")||value.contains("！")||value.contains("…")||value.contains("（")||value.contains("）")||value.contains("【")||value.contains("】")||value.contains("《")||value.contains("》")||value.contains("{")||value.contains("}")){
//			addError2(error, errorName+"含有特殊字符！");
//			result = false;
//		}
		result = true;
		return result;
	}
	protected void comBindValidateForSearch(String field,String errorName){
		String value = controller.getPara(field)+"";	
//		if(value.contains("'")||value.contains("\""))addError2(error,"车牌号不可输入非法字符！","noname");
//		if(value.length()>30){
//			addError2(error, errorName+"输入的内容长度只能在0到30个字符之间！",field);
//		}
		//System.out.println("车牌号为："+value);
//		if (!StrKit.isBlank(value)){
			if(value.contains(" ")){
				addError2(error, errorName+"输入的内容中不能带有空格！",field);
			}
//		}
	}
	
	protected boolean SpaceValidate(String field,int num,String errorName){
		boolean result = true;
		String value = controller.getPara(field)+"";
		if (StrKit.isBlank(value)){
			addError2(error, errorName+"必须输入！",field);
			result = false;
		}
		if(value.contains(" ")){
			if (!field.startsWith("cts.")) {
				addError2(error, errorName+"输入的内容中不能带有空格！",field);
				result = false; 
			}
		}
		if(value.length() > num){
			addError2(error, errorName+"输入的内容过长,长度需要小于等于" + num + "个字符",field);
			result = false;
		}
		return result;
	}
	
	protected boolean LengthValidate(String field,int num,String errorName){
		boolean result = true;
		String value = controller.getPara(field)+"";
		if(value.contains(" ")){
			if (!field.startsWith("cts.")) {//客服热线管理模块中部分文本框可输入空格
				addError2(error, errorName+"输入的内容中不能带有空格！", field);
				result = false;
			}
			
		}
		if(value.length()>num){
			addError2(error, errorName+"输入的内容过长,长度需要小于等于" + num + "个字符！", field);
			result = false;
		}
		return result;
	}
	
	protected boolean SpaceValidate2(String field,String errorName){
		boolean result = true;
		String value = controller.getPara(field)+"";
		if(value.contains(" ")){
			addError2(error, errorName+"输入的内容中不能带有空格！", field);
			result = false;
		}
		return result;
	}
	@Override
	protected void validate(Controller c) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleError(Controller c) {
		//addError2后  都需要renderJson才能返回给前台正确解析 相当于 renderJson("error","错误信息");
		c.renderJson();
	}
	protected void addError2(String errorKey, String errorMessage,String fieldName) {
		controller.setAttr("fieldName", fieldName);
		addError(errorKey, errorMessage);
	}
	
	
	@Override
	protected void validateRegex(String field, String regExpression, boolean isCaseSensitive, String errorKey, String errorMessage) {
        String value = controller.getPara(field);
        if (value == null) {
        	addError2(errorKey, errorMessage,field);
        	return ;
        }
        Pattern pattern = isCaseSensitive ? Pattern.compile(regExpression) : Pattern.compile(regExpression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches())
        	addError2(errorKey, errorMessage,field);
	}
	protected void validateInteger(String field, String errorKey, String errorMessage) {
		validateIntegerValue(controller.getPara(field), errorKey, errorMessage,field);
	}
	private void validateIntegerValue(String value, String errorKey, String errorMessage,String field) {
		if (StrKit.isBlank(value)) {
			addError2(errorKey, errorMessage,field);
			return ;
		}
		try {
			Integer.parseInt(value.trim());
		}
		catch (Exception e) {
			addError2(errorKey, errorMessage,field);
		}
	}
	protected void validateRequiredString(String field, String errorKey, String errorMessage) {		
		if (StrKit.isBlank(controller.getPara(field)))
			addError2(errorKey, errorMessage,field);
	}
	
	/**
	 * 时间大小的比较
	 * @param string  开始时间
	 * @param string2  结束时间
	 * @param errorMessage 提示信息
	 */
	protected void validatecompareToTime(String string, String string2, String errorMessage) {//开始时间不能大于结束时间！
		
		if(controller.getPara(string).compareTo(controller.getPara(string2))>=0 && !controller.getPara(string2).equals("")){
			addError2(error, errorMessage,string);
		}
		
	}
protected void validatecompareToTimeYYMMDD(String string, String string2, String errorMessage) throws ParseException {
		SimpleDateFormat format2 =  new SimpleDateFormat("yyyy-MM-dd"); 
		Date d1 = format2.parse(controller.getPara(string).toString());
		Date d2 = format2.parse(controller.getPara(string2).toString());
		if(d1.compareTo(d2)>0 && !controller.getPara(string2).equals("")){
			addError2(error, errorMessage,string);
		}
		
	}
	protected boolean comBindValidateNull(String field,int num,String errorName){
		boolean result = false;
		String value = controller.getPara(field)+"";
		if(value.contains(" ")){
			addError2(error, errorName+"输入的内容中不能带有空格！",field);
			return result;
		}
		if(value.length()>num){
			addError2(error, errorName+"输入的内容长度不能大于"+num+"个字符之间！",field);
			return result;
		}
		result = true;
		return result;
	}
	protected boolean compareTimes(String startTime, String endTime) {//用于比较时间 HH:mm的大小
		int t1 = Integer.parseInt(startTime.substring(0, 2))*60 + Integer.parseInt(startTime.substring(3, 5));
		int t2 = Integer.parseInt(endTime.substring(0, 2))*60 + Integer.parseInt(endTime.substring(3, 5));
		boolean result = t1 >= t2 ? true : false;
		return result;
	}
	protected boolean validateJINGWEIDU(){
		
		return false;
	}
	
	/**
	 * 输入数值必须为正数
	 * @param field
	 * @throws NullPointerException
	 */
	protected void positiveNumber(String field) throws NullPointerException{
		try{
			String temp=controller.getPara(field);
			double num=Double.parseDouble(temp);
			
			if(num>0){
				return;
			}else{
				addError2(error, "请输入必须为正数数字",field);//必须为整数的判断
				return;
			}
		}catch(NumberFormatException Nume){
			addError2(error,"请使用半角输入数字" ,field);
		}catch(Exception e){
			addError2(error,"请输入必须为正数数字" ,field);//必须为整数的判断
			return;
		}
		
	}

	/**
	 * 输入数值必须为非负数
	 * @param field
	 * @throws NullPointerException
	 */
	protected void positive0Number(String field) throws NullPointerException{
		try{
			String temp=controller.getPara(field);
			double num=Double.parseDouble(temp);
			
			if(num>=0){
				return;
			}else{
				addError2(error, "请输入必须为非负数字",field);//必须为整数的判断
				return;
			}
		}catch(NumberFormatException Nume){
			addError2(error,"请使用半角输入数字" ,field);
		}catch(Exception e){
			addError2(error,"请输入必须为非负数字" ,field);//必须为整数的判断
			return;
		}
	}
	//校验经纬度的合法性。。粗略校验青岛的
	protected void ifJingCorrect(String jingStr) {
		Double jing = Double.parseDouble(controller.getPara(jingStr));
		if(!(jing<=121.4&&jing>=119.4))
			addError2(error, "经度设定不合理！推荐使用地图标注！", jingStr);
	}
	protected void ifWeiCorrect(String weiStr) {
		Double wei = Double.parseDouble(controller.getPara(weiStr));
		if(!(wei>=35.4&&wei<=37.2))
			addError2(error, "纬度设定不合理！推荐使用地图标注！", weiStr);
	}
	/*非法字符判断
	 * field 对应js名称
	 * errorName 错误提示内容
	 */
	protected void ilCharJudge(String field,String errorName){
		String value = controller.getPara(field);
		 String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
	        Pattern p = Pattern.compile(regEx);
	        Matcher m = p.matcher(value);
		if(m.find()){
			addError2(error,errorName,field);
		}
	}
	
}
