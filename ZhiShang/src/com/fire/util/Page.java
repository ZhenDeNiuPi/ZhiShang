package com.fire.util;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 15-10-15.
 */
public class Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5044172343675296367L;

	/**
	 * 当前页
	 */
	private int curNum;
	
	/**
     * 总记录数
     */
    private long total;

    /**
     * 结果集合
     */
	@SuppressWarnings("rawtypes")
	private List rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}

	@SuppressWarnings("rawtypes")
	public Page(long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public Page() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCurNum() {
		return curNum;
	}

	public void setCurNum(int curNum) {
		this.curNum = curNum;
	}
}
