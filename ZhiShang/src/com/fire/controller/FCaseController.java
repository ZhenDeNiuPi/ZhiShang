package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

@Clear(LoginInterceptor.class)
public class FCaseController extends Controller {

    public void index(){
    	renderTemplate("product_list.html");
    }
}
