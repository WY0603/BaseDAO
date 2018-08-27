package com.accp.cdjj.wangyao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.accp.cdjj.wangyao.dao.IConnection;
import com.accp.cdjj.wangyao.utils.ReadConfig;

public class JNDIConnection implements IConnection{
	Connection connection;
	private String url;
	
	public JNDIConnection(){
		if(null == null){
			url = ReadConfig.getCogfig("driver");
		}
	}
	
	public Connection getConnection() {
		
		Context ctx;
		try{
			ctx = new InitialContext();
			JNDIConnection.class.getResourceAsStream("");
			DataSource source = (DataSource)ctx.lookup("java:comp/env/" + url);
			connection = source.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("连接池错误!");
			e.printStackTrace();
		}
		return connection;
	}

	public void close(ResultSet resultset, PreparedStatement ps, Connection connection) {
		if(resultset != null){
			try {
				resultset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("结果集关闭错误");
				e.printStackTrace();
			}
		}
		if(ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("PS关闭错误");
				e.printStackTrace();
			}
		}
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection关闭错误");
				e.printStackTrace();
			}
		}
		
	}

}
