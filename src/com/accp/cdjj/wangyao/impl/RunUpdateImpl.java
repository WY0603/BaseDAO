package com.accp.cdjj.wangyao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.accp.cdjj.wangyao.dao.IConnection;
import com.accp.cdjj.wangyao.dao.IRunUpdate;
import com.accp.cdjj.wangyao.utils.GetConnection;
/**
 * 修改处理方法实现类
 * @author 王曜
 *
 */
public class RunUpdateImpl implements IRunUpdate {
	boolean bool;
	//数据库影响行数
	int number;
	//数据库连接
	Connection connection;
	//结果集
	ResultSet resultset;
	//存储过程
	CallableStatement cs;
	//预处理语句
	PreparedStatement ps;
	
//	数据库连接接口
	IConnection iConnection;
	
	public RunUpdateImpl(){
		if(iConnection == null){
			//获取实现数据库连接的类
			iConnection = GetConnection.getConnection();
		}
	}
	/*
	 * 根据POJO类修改方法(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunUpdate#runUpdateByPojo(java.lang.String, java.lang.Object)
	 */
	public boolean runUpdateByPojo(String tableName, Object obj) {
		bool = false;
		//初始化影响行数
		number = 0;
		//获取数据库连接
		connection = iConnection.getConnection();

		
		//SQL语句
		String sql = "update " + tableName + " set ";
		//获取类
		Class clazz = obj.getClass();
		//获取所有属性
		Field[] fields = clazz.getDeclaredFields();
		//获取所有方法
		Method[] methods = clazz.getMethods();
		//创建参数数组
		Object[] objects = new Object[fields.length];
		//参数个数初始化
		int count = 0;
		//条件ID初始化
		String id = null;
		//循环所有属性
		for(int i = 0 ; i < fields.length ; i++){
			//获取属性名字
			String fieldName = fields[i].getName();
			//循环所有方法
			for(int j = 0 ; j < methods.length ; j++){
				//获取方法名字
				String methodName = methods[j].getName();
				//判断方法是否为get方法,并且是对应属性名字,名字不是ID
				if(methodName.indexOf("get") != -1 && methodName.substring(3).equalsIgnoreCase(fieldName) && !methodName.substring(3).equalsIgnoreCase("id")){
					try {
						//判断方法值是否为空
						if(methods[j].invoke(obj) != null){
							if(fields.length >= i + 1 && count != 0){
								//如果属性不是最后一个
								sql += ",";
							}
							//如果不为空,获得值,并放入参数数组
							objects[count] = methods[j].invoke(obj);
							count++;
							//拼接SQL语句
							sql += (fieldName + "=?");
							j = methods.length;
							
						}
						
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
				}else if(methodName.indexOf("get") != -1 && methodName.substring(3).equalsIgnoreCase(fieldName) && methodName.substring(3).equalsIgnoreCase("id")){
					try {
						id = (String)methods[j].invoke(obj);
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
				}
				
			}
			
		}
		
		sql += " where id =" +id; 
		//调用SQL语句修改方法
		bool = runUpdateBySql(sql , objects);
		return bool;
	}

	/*
	 * 根据存储过程修改(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunUpdate#runUpdateByProc(java.lang.String, java.lang.Object[])
	 */
	public boolean runUpdateByProc(String proc, Object[] obj) {
		bool = false;
		//数据库影响行数初始化
		number = 0;
		//获取数据库连接
		connection  = iConnection.getConnection();
		
		try{
			//呼叫存储过程
			cs = connection.prepareCall("{" + proc + "}");
			if(obj != null){
				//如果参数不为空的执行方法
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
			iConnection.close(resultset, cs, connection);
		}
		return bool;
	}

	/*
	 * 根据SQL语句修改(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunUpdate#runUpdateBySql(java.lang.String, java.lang.Object[])
	 */
	public boolean runUpdateBySql(String sql, Object[] obj) {
		bool = false;
		//初始化数据库影响行数
		number = 0;
		//获取数据库连接
		connection  = iConnection.getConnection();
		
		try{
			//预处理语句
			ps = connection.prepareStatement(sql);
			if(obj != null){
				//如果参数不为空执行的方法
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
			iConnection.close(resultset, ps, connection);
		}
		return bool;
	}

	/**
	 * 处理预处理方法
	 * @param ps 预处理
	 * @param obj 参数数组
	 */
	public void pretreatment(PreparedStatement ps,Object[] obj){
		try{
			//循环所有参数
			for(int i = 0 ; i < obj.length ; i++){
				//获取参数
				Object temp = obj[i];
				if(temp == null){
					break;
				}else if(temp.getClass().getName().equals("java.lang.Integer")){
					ps.setInt(i+1, (Integer)temp);
				}
				else if(temp.getClass().getName().equals("java.lang.String")){
					ps.setString(i+1, (String)temp);
				}
				else if (temp.getClass().getName().equals("java.lang.Double")) {
					ps.setDouble(i+1, (Double)temp);
				}
				else if (temp.getClass().getName().equals("java.lang.Boolean")) {
					ps.setBoolean(i+1, (Boolean)temp);
				}
				else if(temp.getClass().getName().equals("java.lang.Float")){
					ps.setFloat(i+1, (Float)temp);
				}
				else if (temp.getClass().getName().equals("java.sql.Date")) {
					ps.setDate(i+1, (Date)temp);
				}
				else{
					System.out.println(temp.getClass().getName());
					ps.setObject(i+1, temp);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
