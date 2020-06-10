package com.fire.controller;

import com.jfinal.core.Controller;

/**
 * 联系我们
 */
public class FContactController extends Controller {

    public void index(){
        render("contact.html");
    }

    public void map(){
        render("map.html");
    }
}
