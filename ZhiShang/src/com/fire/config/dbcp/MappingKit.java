package com.fire.config.dbcp;

import com.fire.model.Info;
import com.fire.model.User;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class MappingKit {
	/**
	 * PostgreSql数据库中数据表映射关系
	 * @param arp
	 */
	public static void mappingPostgreSql(ActiveRecordPlugin arp) {
		
	}
	
	/**
	 * MySql数据库中数据表映射关系
	 * @param arp
	 */
	public static void mappingMySql(ActiveRecordPlugin arp) {
	    arp.addMapping("user_tb", User.class);
	    arp.addMapping("info_tb", Info.class);
	}
}
