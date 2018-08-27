package com.accp.cdjj.wangyao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.accp.cdjj.wangyao.dao.IConnection;
import com.accp.cdjj.wangyao.utils.ReadConfig;

public class ConnectionImpl implements IConnection{
	Connection connection;
	//数据库地址
	public static String serverAddress;
	//数据库名字
	public static String serverName;
	//数据库用户名
	public static String userName;
	//数据库用户密码
	public static String userPwd;
	//数据库驱动
	public static String driver;
	//数据库端口号
	public static String serverPort;
	//数据库URL
	public static String serverUrl;
	//数据库连接类型
	public static String driverType;
	/**
	 * 创建Connection连接
	 * return 返回数据库连接
	 */
	public Connection getConnection() {
		connection = null;
		serverAddress = ReadConfig.serverAddress;
		serverPort = ReadConfig.serverPort;
		serverName = ReadConfig.serverName;
		userName = ReadConfig.userName;
		userPwd = ReadConfig.userPwd;
		driver = ReadConfig.driver;
		serverUrl = ReadConfig.serverUrl;
		driverType = ReadConfig.driverType;
		String URL = null;
		if(driverType.equals("JDBC")){
			URL = serverUrl + serverAddress + ":" + serverPort + ";" +"DatabaseName="+serverName;
		}else if(driverType.equals("ODBC")){
			URL = serverUrl + serverName;
		}
		if(null != URL){
			try{
				Class.forName(driver);
				
				connection = DriverManager.getConnection(URL,userName,userPwd);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("数据库连接方式错误");
		}
		return connection;
	}

	/**
	 * 关闭连接
	 */
	public void close(ResultSet resultset,PreparedStatement ps , Connection connection) {
		if(resultset != null){
			try {
				resultset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ps != null){
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
