package com.fire.controller;

import com.jfinal.core.Controller;

/**
 * 关于我们Controller
 */
public class FAboutusController extends Controller {

    //加载关于页面
    public void index(){
        render("about.html");
    }

}
