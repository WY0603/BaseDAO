package com.accp.cdjj.wangyao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * close database connection
 * @author yao wang
 *
 */
public interface IClose {

	/**
	 * close database connection
	 *
	 */
	public void close(ResultSet resultset,PreparedStatement ps , Connection connection);
}
