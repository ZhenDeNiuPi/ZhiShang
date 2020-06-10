package com.fire.controller;

import com.jfinal.core.Controller;

public class FNewsController extends Controller {

    public void index(){
        render("news_list.html");
    }

    public void toNewsInfo(){
        String newsId = getPara("id");
        render("news_list_content.html");
    }
}
