package com.fire.controller;

import com.jfinal.core.Controller;

/**
 * 留言板
 */
public class FMessageController extends Controller {

    public void index(){
        render("message.html");
    }
}
