package com.fire.controller;

import com.fire.intercepter.LoginInterceptor;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

@Clear(LoginInterceptor.class)
public class FBookController extends Controller {

    public void index(){
        render("book_list.html");
    }
}
