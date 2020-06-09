package com.fire.config.route;

import com.fire.controller.BookController;
import com.fire.controller.CaseController;
import com.fire.controller.LoginController;
import com.fire.controller.NewsController;
import com.fire.controller.RCController;
import com.fire.controller.UserController;
import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {

	private static final String path = "/WEB-INF/pages/";

	@Override
	public void config() {
		add("/manage", LoginController.class, path );
		add("/user", UserController.class,path);
		add("/rc", RCController.class,path);
		add("/book", BookController.class,path);
		add("/news", NewsController.class,path);
		add("/case", CaseController.class,path);
		
	}
}
