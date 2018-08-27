package com.accp.cdjj.wangyao.dao;

import java.sql.ResultSet;
import java.util.List;

/**
 * query class interface
 * @author yao wang
 *
 */
public interface IRunFind {

	/**
	 * depends on table name
	 * @param tableName table name
	 * @return
	 */
	public ResultSet runFindByTabelName(String tableName);
	
	/**
	 * depends on SQL
	 * @param sql SQL
	 * @param obj 
	 * @return
	 */
	public ResultSet runFindBySql(String sql , Object[] obj);
	
	/**
	 * depends on stored procedure
	 * @param proc stored procedure
	 * @param obj
	 * @return
	 */
	public ResultSet runFindeByProc(String proc , Object[] obj);
	
	public List getList(ResultSet rs,Object[] objects);
	
}
