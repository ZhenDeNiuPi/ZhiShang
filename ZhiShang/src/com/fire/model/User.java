package com.fire.model;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class User extends Model<User>{
	
	public static final User dao = new User();
	
	public User findLoginUser(String username,String pass) {
		String sql = "select * from user_tb where username=? and password=? " ;
		return User.dao.findFirst(sql, username,pass);
	}

	public User findUserState(String username,String pass) {
		String sql = "select * from user_tb where strid=? and md5pass=? and is_delete = 0 and state = 0 and (auth_flag  = 0  or auth_flag = -1 or auth_flag is null)" ;
		return User.dao.findFirst(sql, username,pass);
	}
	
	public User findLoginUserByMobile(String mobile) {
		String sql = "select * from user_tb where mobile="+mobile+" and is_delete = 0 and state = 0 and (auth_flag  = 0  or auth_flag = -1 or auth_flag is null)" ;
		return User.dao.findFirst(sql);
	}
}
