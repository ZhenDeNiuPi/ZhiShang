package com.fire.intercepter;


import com.fire.util.Str2TimeStamp;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/*by dragon*/
public class FormOperaIntercepter extends FormValidator {
	Str2TimeStamp sts = new Str2TimeStamp();
	/* 表单校验器
	 * 含有多数表单项以及条件查询的输入校验 
	 * 通过每个方法不同的controllerKey和不同的actionKey来判断
	 * 查找类的话复制controlName去FrontRoutes找
	 * 扩展的时候请写在抽象父类FormValidator中*/
	@Override
	protected void validate(Controller c) {
		String controName = getControllerKey();
		String actionName = getActionKey();
		this.setShortCircuit(true);
		if(controName.equals("/user")){
			validateRequiredString("u.email",error, "邮箱必须输入！");
			validateRequiredString("u.password",error, "密码必须输入！");
			validateRegex("u.password", pass8to16, error, "密码由8~16位数字和字母混合组成！");
		}else if(controName.equals("/followfund")) {
			String fund_id = c.getPara("f.fund_id");
			if(c.getPara("f.id") == null || c.getPara("f.id").trim().length() == 0) {
				//新增
				Record user = c.getSessionAttr("user");
				Record countRec = Db.findFirst(
						"select ifnull(count(*),0) count from fund_position_tb where user_id="+user.get("id"));
				if(Integer.parseInt(countRec.get("count",0)+"") == 20)
					addError2(error, "订阅基金数已达到上限！", "");
				//判重
				Record countRec2 = Db.findFirst(
						"select ifnull(count(*),0) count from fund_position_tb where fund_id='"+fund_id
						+"' and user_id='"+user.get("id")+"'");
				if(Integer.parseInt(countRec2.get("count",0)+"") > 0)
					addError2(error, "该基金已订阅！", "");
			}
			
			validateRequiredString("fund_name",error, "请输入正确的基金编码！");
			if(c.getPara("f.position_point") != null && c.getPara("f.position_point").trim().length()>0)
				isDouble("f.position_point","请输入正确的持仓份额！");
			if(c.getPara("f.position_price") != null && c.getPara("f.position_price").trim().length()>0)
				isDouble("f.position_price","请输入正确的持仓成本！");
			if(c.getPara("f.callback") != null && c.getPara("f.callback").trim().length()>0)
				ifInteger("f.callback","请输入整数的止盈回调！",3);
			if(c.getPara("f.enough") != null && c.getPara("f.enough").trim().length()>0)
				ifInteger("f.enough","请输入整数的止盈率！",3);
		}else if(actionName.endsWith("findPass")) {
			validateRequiredString("u.email",error, "邮箱必须输入！");
			validateRegex("u.email", emailPattern, error, "请输入正确的邮箱！");
			validateRequiredString("u.password",error, "密码必须输入！");
			validateRegex("u.password", pass8to16, error, "密码由8~16位数字和字母混合组成！");
			validateRequiredString("vali_num",error, "邮件验证码必须输入！");
			String vali_num = c.getPara("vali_num");
			if(vali_num.length()!=4) {
				addError2(error, "请输入正确的邮件验证码！", "vali_num");
			}
			validateRequiredString("validate",error, "验证码必须输入！");
		}
	}
}

