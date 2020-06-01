package com.fire.config.route;

import com.fire.controller.FollowFundController;
import com.fire.controller.LoginController;
import com.fire.controller.OrderController;
import com.fire.controller.PaymentController;
import com.fire.controller.UserController;
import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {

	private static final String path = "/WEB-INF/pages/";

	@Override
	public void config() {
		add("/", LoginController.class, path );
		add("/user", UserController.class,path);
		add("/followfund", FollowFundController.class,path);
		add("/payment", PaymentController.class,path);
		add("/order", OrderController.class,path);
	}
}
