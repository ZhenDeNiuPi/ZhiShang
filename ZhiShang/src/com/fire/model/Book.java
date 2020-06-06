package com.fire.model;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class Book extends Model<Book>{
	public static final Book dao = new Book();
}
