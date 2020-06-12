package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

/**
 * 联系我们
 */
@Clear(LoginInterceptor.class)
public class FContactController extends Controller {

    public void index(){
        render("contact.html");
    }

    public void map(){
        render("map.html");
    }
}
