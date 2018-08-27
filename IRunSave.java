package com.accp.cdjj.wangyao.dao;
/**
 * save class interface
 * @author yao wang
 *
 */
public interface IRunSave {

	/**
	 * depends on POJO
	 * @param tableName table name
	 * @param obj POJO
	 * @return
	 */
	public boolean runSaveByPojo(String tableName,Object obj);
	
	/**
	 * depends on SQL
	 * @param sql SQL
	 * @param obj 
	 * @return
	 */
	public boolean runSaveBySql(String sql,Object[] obj);
	
	/**
	 * depends on stored procedure
	 * @param proc stored procedure
	 * @param obj 
	 * @return
	 */
	public boolean runSaveByProc(String proc,Object[] obj);
}
