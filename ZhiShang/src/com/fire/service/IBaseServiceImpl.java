package com.fire.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fire.util.GetSqlByCondis;
import com.fire.util.Page;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/*by dragon*/
public class IBaseServiceImpl{
	private HttpServletRequest req;
	private HttpServletResponse res;
	private GetSqlByCondis gsb = new GetSqlByCondis();
	private static String ifSql = "";
	private static int pageNum = 0;
	private static long recordCounts = 0;
	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletResponse getRes() {
		return res;
	}

	public void setRes(HttpServletResponse res) {
		this.res = res;
	}
	// 取数量(无参) 
	public Long getCount(String sql){
		long len = Db.queryLong(sql);
		return len;
	}
	
	// 取数量 （带参）
	public Long getCount(String sql,Object[] paras){
		long len = Db.queryLong(sql,paras);
		return len;
	}
/*	// 查询
		public List<Record> getAll(String sql, Object[] values ){
			List<Record> list = Db.find(sql);
			return list;
		}*/
	
	/**
	 * 高级查询（无查询条件）
	 * @param sql			SQL语句
	 * @param pageNumber	页码
	 * @param pageSize		每页记录数
	 * @param sort			排序规则
	 * @param sixd			排序依据字段
	 * @return
	 */
	// 分页查询	headsql查询语句的前段	pagenumber前台传来的当前页码	pagesize前台传来的每页记录数 sord排序规则正序or倒序  前台传来根据哪个字段排序
	//			countsql 查询数量的sql
    public Page query(String selectSql,String fromSql,Map<String,String[]> allParams){
    	Map<String,Object> all=gsb.getDTO(allParams);
    	String sql = gsb.queryCondition(all);
    	String headSql = selectSql + fromSql + sql;
    	String countSql = "select count(0) count "+fromSql + sql;
        long realCount = 0;
        long records = getCount(countSql);
        int pageSize = Integer.parseInt(all.get("pageSize")+"");
        int pageNumber = Integer.parseInt(all.get("pageNumber")+"");
        String sortName = all.get("sortName")+"";
        String sortOrder = all.get("sortOrder")+"";
        int maxSize = (int) ((records+pageSize)/pageSize);
    	if(ifSql.equals(headSql)&&pageNumber==pageNum&&pageNum!=1){
        	realCount = getCount(countSql);
        	if(realCount!=recordCounts){
        		pageNumber = 1;
        	}
        }
    	if(!ifSql.equals(headSql)){
        	pageNumber=1;
        }
    	if(pageNumber>maxSize)pageNumber=1;
    	String fsql ="";//查询语句的结尾部分 排序 limit
        int startNumber = 0;
        if (pageNumber > 1)
        	startNumber = (pageNumber - 1) * pageSize;
        if(sortOrder.length()>0&&sortName.length()>0
        		&&(!"null".equals(sortOrder))&&(!"null".equals(sortName)))
        	fsql += " order by " + sortName +" "+ sortOrder;
        fsql +=  " limit " + startNumber + "," + pageSize + " ";
        String findsql = headSql + fsql;//最终查询语句
//        System.out.println(findsql);
        List<Record> orders = Db.find(findsql);
        Page page = new Page();//将数据放进page并设置其他属性
        page.setRows(orders);
        recordCounts = records;
        page.setTotal(records);
        ifSql = headSql;
        pageNum = pageNumber;
        return page;
    }
    
	
    //查询其他信息例如停车场id及名称 收费员id及名称 提供给前台下拉框			
    public List<Record> findInfo(String findsql){
    	List<Record> info = Db.find(findsql);
    	//System.out.println(info);
    	return info;
    }
    
    //单实例查询
    //表名+id
    public Record findById(String tableName,Integer id){
    	Record record = Db.findById(tableName, id);
    	return record;
    }
    //查询sql第一条
    public Record findById(String sql){
    	Record record = Db.findFirst(sql);
    	return record;
    }
    //单一删除 指定表名 字段值 字段名 
    public boolean deleteOne(String tableName,String id,String rowName){
    	int n = Db.update("delete from "+tableName+" where "+rowName+"="+id);
    	return n>0;
    }
    //批量删除  指定表名 主键字符串
    public boolean deleteBat(String tableName,String ids){
    	String[] idss = ids.split(",");//提取批量删除的id
    	List<String> list = new ArrayList<String>();
    	for(int i = 0;i<idss.length;i++){//生成批量删除方法所需要的sql list
    		list.add("delete from "+tableName+" where id="+idss[i]);
    	}
    	int[] ns = Db.batch(list, idss.length);
    	return ns.length>0;
    }
    //批量删除  指定表名 字段值字符串 字段名 不能删varchar的 如果有需要再重载一个
    public boolean deleteBat(String tableName,String ids,String rowName){
    	String[] idss = ids.split(",");//提取批量删除的id
    	List<String> list = new ArrayList<String>();
    	for(int i = 0;i<idss.length;i++){//生成批量删除方法所需要的sql list
    		list.add("delete from "+tableName+" where "+rowName+"="+idss[i]);
    	}
    	int[] ns = Db.batch(list, idss.length);
    	return ns.length>0;
    }
    public Long getTimesmorning(long date) {// 按日结存：获取date的00:00:00的秒
		Date d = new Date(date); 
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() / 1000;
	}
	public int getMonthDays(long date) {//获取某个月的天数
		Date d = new Date(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		return cal.get(Calendar.DATE);
	}
	
}
