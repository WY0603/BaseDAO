package com.accp.cdjj.wangyao.dao;

/**
 * update class interface
 * @author yao wang
 *
 */
public interface IRunUpdate {
	
	/**
	 * depends on POJO class
	 * @param tableName type name
	 * @param obj POJO
	 * @return
	 */
	public boolean runUpdateByPojo(String tableName,Object obj);
	
	/**
	 * depends on SQL
	 * @param sql SQL
	 * @param obj 
	 * @return
	 */
	public boolean runUpdateBySql(String sql , Object[] obj);
	
	/**
	 * depends on stored procedure
	 * @param proc stored procedure
	 * @param obj 
	 * @return
	 */
	public boolean runUpdateByProc(String proc , Object[] obj);
}
