package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

@Clear(LoginInterceptor.class)
public class FNewsController extends Controller {

    public void index(){
        render("news_list.html");
    }

    public void toNewsInfo(){
        String newsId = getPara("id");
        render("news_list_content.html");
    }
}
