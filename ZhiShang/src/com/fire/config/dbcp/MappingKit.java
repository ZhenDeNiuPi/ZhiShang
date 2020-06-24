package com.fire.config.dbcp;

import com.fire.model.Book;
import com.fire.model.Case;
import com.fire.model.History;
import com.fire.model.Info;
import com.fire.model.News;
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
	    arp.addMapping("book_tb", Book.class);
	    arp.addMapping("info_tb", Info.class);
	    arp.addMapping("news_tb", News.class);
	    arp.addMapping("case_tb", Case.class);
	    arp.addMapping("history_tb", History.class);
	}
}
