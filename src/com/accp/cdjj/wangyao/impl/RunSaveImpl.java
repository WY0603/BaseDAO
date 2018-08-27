package com.accp.cdjj.wangyao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.accp.cdjj.wangyao.dao.IConnection;
import com.accp.cdjj.wangyao.dao.IRunSave;
import com.accp.cdjj.wangyao.utils.GetConnection;
/**
 * 添加的处理类
 * @author 王曜
 *
 */
public class RunSaveImpl implements IRunSave{
	boolean bool;
	//存储过程处理语句
	CallableStatement cs;
	//数据库连接
	Connection connection;
	//预处理语句
	PreparedStatement ps;
	//静态预处理语句
	Statement statement;
	//数据库影响条数
	int number;
//	数据库连接接口
	IConnection iConnection;
	
	public RunSaveImpl(){
		if(iConnection == null){
			//获取实现数据库连接的类
			iConnection = GetConnection.getConnection();
		}
	}
	/*
	 * 根据存储过程添加(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunSave#runSaveByProc(java.lang.String, java.lang.Object[])
	 */
	public boolean runSaveByProc(String proc, Object[] obj) {
		bool = false;
		number = 0;
		//获得连接
		if(connection == null){
			connection = iConnection.getConnection();
		}
		
		try{
			//呼叫存储过程
			cs = connection.prepareCall("{" + proc + "}");
			if(obj != null){
				//如果有参数执行方法
				pretreatment(cs , obj);
			}
			//获取数据库影响行数
			number = cs.executeUpdate();
			if(number != 0){
				//如果影响行数不为0
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			iConnection.close(null, cs, connection);
		}
		return bool;
	}

	/*
	 * 根据SQL语句添加(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunSave#runSaveBySql(java.lang.String, java.lang.Object[])
	 */
	public boolean runSaveBySql(String sql, Object[] obj) {
		bool = false;
		number = 0;
		//获得连接
		if(connection == null){
			connection = iConnection.getConnection();
		}
		try{
			//SQL预处理语句
			ps = connection.prepareStatement(sql);
			if(obj != null){
				//如果有参数执行方法
				pretreatment(ps , obj);
			}
			//获取数据库影响行数
			number = ps.executeUpdate();
			if(number != 0){
				//如果影响行数不为0
				bool = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			iConnection.close(null, ps, connection);
		}
		return bool;
	}

	/*
	 * 根据POJO类添加方法(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunSave#runSaveByPojo(java.lang.String, java.lang.Object)
	 */
	public boolean runSaveByPojo(String tableName, Object obj) {
		bool = false;
		//初始化数据库影响行数
		number = 0;
		//初始化结果集
		ResultSet resultset = null;
		//获取数据库连接
		if(connection == null){
			connection = iConnection.getConnection();
		}
		
		//实力化查询方法
		RunFindImpl runFind = new RunFindImpl();
		//执行SQL查询方法获得结果集
		resultset =  runFind.runFindBySql("select top 1 * from " + tableName, null);
		
		try {
			//获得结果集列的属性
			ResultSetMetaData rsmd = resultset.getMetaData();
			
			//获得结果集有多少列
			int col = rsmd.getColumnCount();
			//添加的SQL语句
			String sql = "insert into " + tableName + " values(";
			//获得类
			Class clazz = obj.getClass();
			//获得类的所有方法
			Method[] methods = clazz.getMethods();
			//实例化参数数组
			Object[] object = new Object[col];
			//初始化参数个数
			int count = 0;
			//循环所有列
			for(int i = 1 ; i <= col ; i++){
				//获得列名字
				String colName = rsmd.getColumnName(i);
				//循环所有方法
				for(int j = 0 ; j < methods.length ; j++){
					//获得方法名字
					String methodName = methods[j].getName();
					//判断方法名字是否是GET开头,并且和列明对应
					if(methodName.indexOf("get") != -1 && methodName.substring(3).equalsIgnoreCase(colName) && !methodName.substring(3).equalsIgnoreCase("id")){
						try {
							try {
								if(i <= col && count != 0){
									//如果不是最后一列
									sql += ",";
								}
								//判断值是否为空
								if(methods[j].invoke(obj) != null){
									//将值赋值给参数数组
									object[count] = methods[j].invoke(obj);
									sql += "?";
									count++;
									
								}else{
									sql += "default";
								}
								
								
								j = methods.length;
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			
			sql += ")";
			//调用SQL语句添加方法
			bool = runSaveBySql(sql,object);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return bool;
	}
	
	/**
	 * 处理预处理方法
	 * @param ps 预处理
	 * @param obj 参数数组
	 */
	public void pretreatment(PreparedStatement ps,Object[] obj){
		try {
			//循环获取参数
			for(int i = 1 ; i <= obj.length ; i++){
				//获得参数
				Object var = obj[i - 1];
				if(var == null){
					break;
				}else if (var.getClass().getName().equals("java.lang.Integer")) {
					ps.setInt(i, (Integer) var);
				} else if (var.getClass().getName().equals("java.lang.String")) {
					ps.setString(i, (String) var);
				} else if (var.getClass().getName().equals("java.lang.Double")) {
					ps.setDouble(i , (Double) var);
				} else if (var.getClass().getName().equals("java.lang.Boolean")) {
					ps.setBoolean(i, (Boolean) var);
				} else if (var.getClass().getName().equals("java.lang.Float")) {
					ps.setFloat(i , (Float) var);
				} else if (var.getClass().getName().equals("java.sql.Date")) {
					ps.setDate(i, (Date) var);
				} else {
					System.out.println(var.getClass().getName());
					ps.setObject(i, var);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

}
