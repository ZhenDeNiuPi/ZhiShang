package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

/**
 * 留言板
 */
@Clear(LoginInterceptor.class)
public class FMessageController extends Controller {

    public void index(){
        render("message.html");
    }
}
