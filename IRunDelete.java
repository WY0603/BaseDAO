package com.accp.cdjj.wangyao.dao;
/**
 * delete class interface
 * @author 王曜
 *
 */
public interface IRunDelete {

	/**
	 * depends on Pojo
	 * @param tableName table name
	 * @param obj 
	 * @return
	 */
	public boolean runDeleteByPojo(String tableName, Object obj);
	
	/**
	 * depends on SQL
	 * @param sql SQL
	 * @param obj 
	 * @return
	 */
	public boolean runDeleteBySql(String sql , Object[] obj);
	
	/**
	 * depends on stored procedure
	 * @param proc stored procedure
	 * @param obj 
	 * @return
	 */
	public boolean runDeleteByProc(String proc , Object[] obj);
	
}
