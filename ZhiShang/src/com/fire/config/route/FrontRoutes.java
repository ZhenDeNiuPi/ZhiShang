package com.fire.config.route;

import com.fire.controller.*;
import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {

	private static final String path = "/WEB-INF/pages/";

	//至上前端页面路径
	private static final String zsPath = "/WEB-INF/zsps/";

	@Override
	public void config() {
		add("/manage", LoginController.class, path );
		add("/user", UserController.class,path);
		add("/rc", RCController.class,path);
		add("/book", BookController.class,path);
		add("/news", NewsController.class,path);
		add("/case", CaseController.class,path);
		add("/", FIndexController.class, zsPath);
		add("/fabout", FAboutusController.class, zsPath);
		add("/fbook", FBookController.class, zsPath);
		add("/fcase", FCaseController.class, zsPath);
		add("/fcontact", FContactController.class, zsPath);
		add("/fnews", FNewsController.class, zsPath);
		add("/fmessage", FMessageController.class, zsPath);
	}
}
