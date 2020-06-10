package com.fire.controller;

import com.jfinal.core.Controller;

public class FBookController extends Controller {

    public void index(){
        render("book_list.html");
    }
}
