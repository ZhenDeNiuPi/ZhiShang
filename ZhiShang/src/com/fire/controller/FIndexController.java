package com.fire.controller;

import com.jfinal.core.Controller;

/**
 * 首页Controller
 */
public class FIndexController extends Controller {

    public void index(){
        render("index.html");
    }
}
