package com.fire.intercepter;


import com.fire.util.Str2TimeStamp;
import com.jfinal.core.Controller;
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
//		String controName = getControllerKey();
		String actionName = getActionKey();
		this.setShortCircuit(true);
		if(actionName.endsWith("findPass")) {
			validateRequiredString("u.account",error, "账户必须输入！");
			validateRequiredString("u.password",error, "密码必须输入！");
			validateRequiredString("u.passwordnew",error, "新密码必须输入！");
			validateRegex("u.passwordnew", pass8to16, error, "密码由8~16位数字和字母混合组成！");
			validateRequiredString("validate",error, "验证码必须输入！");
		}
	}
}

