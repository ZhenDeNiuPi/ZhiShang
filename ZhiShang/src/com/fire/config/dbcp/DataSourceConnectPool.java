package com.fire.config.dbcp;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;


/**
 * 数据库连接池
 * @author durunguo
 *
 */
public class DataSourceConnectPool {
	
	static {
		 PropKit.use("dbconfig.properties");
	}

    /**
     * 配置Druid数据库连接池插件：MySql连接（非使用，供参考）
     * @return
     */
    public static DruidPlugin getMySqlDruid(){
    	Prop dbconfig=PropKit.use("dbconfig.properties");
    	DruidPlugin mySqlDruid=new DruidPlugin(	dbconfig.get("MYSQL_URL"),
    											dbconfig.get("MYSQL_USERNAME"),
    											dbconfig.get("MYSQL_PASSWORD").trim());
    	mySqlDruid.set(dbconfig.getInt("MYSQL_INITIALPOOLSIZE"), dbconfig.getInt("MYSQL_MINPOOLSIZE"), dbconfig.getInt("MYSQL_MAXPOOLSIZE"));
    	mySqlDruid.setFilters("stat,wall");
        return mySqlDruid;
    }
    /*
     * start方法 为了直接使用main方法测试
     */
    public static void startPlugin(){
    	PropKit.use("dbconfig.properties");
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("MYSQL_URL"), PropKit.get("MYSQL_USERNAME"), PropKit.get("MYSQL_PASSWORD").trim());
		ActiveRecordPlugin arp = new ActiveRecordPlugin("mysql",druidPlugin);
		
		druidPlugin.start();
		arp.start();
    }
}
