package com.accp.cdjj.wangyao.dao;

import java.sql.Connection;

/**
 * connect database
 * @author 王曜
 *
 */
public interface IConnection extends IClose{

	/**
	 * get connection method
	 * @return connection
	 */
	public Connection getConnection();
}
