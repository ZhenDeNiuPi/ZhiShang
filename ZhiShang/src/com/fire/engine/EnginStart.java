package com.fire.engine;

import com.fire.config.dbcp.DataSourceConnectPool;
import com.fire.config.dbcp.MappingKit;
import com.fire.config.route.FrontRoutes;
import com.fire.intercepter.ExceptionInterceptor;
import com.fire.intercepter.LoginInterceptor;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

public class EnginStart extends JFinalConfig {

    /**
     * 配置常量
     * @param me
     */
    @Override
    public void configConstant(Constants me) {
        PropKit.use("dbconfig.properties");
        me.setDevMode(PropKit.getBoolean("devMode",false));
        //设置编码格式
        me.setEncoding("utf-8");
        //设置视图类型
        me.setViewType(ViewType.JSP);
    }

    /**
     * 配置路由
     * @param me
     */
    @Override
    public void configRoute(Routes me) {

    	//使用前台路由、后台路由管理
    	me.add(new FrontRoutes());
    }

    /**
     * 配置插件
     * @param me
     */
    @Override
    public void configPlugin(Plugins me) {
    	DruidPlugin druid_mySql=DataSourceConnectPool.getMySqlDruid();
    	me.add(druid_mySql);
    	ActiveRecordPlugin Arp_mySql=new ActiveRecordPlugin("ms",druid_mySql);
    	me.add(Arp_mySql);
    	MappingKit.mappingMySql(Arp_mySql);
		/*Cron4jPlugin cp = new Cron4jPlugin("job.properties");
		me.add(cp);*/
    }

    /**
     * 配置全局拦截器，对所有请求进行拦截,类和方法使用@Before(Class.class)即能使用
     * @param me
     */
    @Override
    public void configInterceptor(Interceptors me) {
    	me.add(new SessionInViewInterceptor());
    	me.addGlobalActionInterceptor(new LoginInterceptor());    
    	me.addGlobalActionInterceptor(new ExceptionInterceptor());
    }

    /**
     * 配置处理器
     * @param me
     */
    @Override
    public void configHandler(Handlers me) {
    	//me.add(new FakeStaticHandler(".do"));
    }

    @Override
    public void afterJFinalStart() {
    	// TODO Auto-generated method stub
    	System.out.println("至上净化后台管理启动成功");

    }

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

}
