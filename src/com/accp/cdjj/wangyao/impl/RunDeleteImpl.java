package com.accp.cdjj.wangyao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.accp.cdjj.wangyao.dao.IConnection;
import com.accp.cdjj.wangyao.dao.IRunDelete;
import com.accp.cdjj.wangyao.utils.GetConnection;

/**
 * 删除处理实现类
 * @author 王曜
 *
 */
public class RunDeleteImpl implements IRunDelete {
	boolean bool;
	//数据库连接
	Connection connection;
	//预处理语句
	PreparedStatement ps;
	//储存过程
	CallableStatement cs;
	//数据影响行数
	int number;
	//	数据库连接接口
	IConnection iConnection;
	
	public RunDeleteImpl(){
		if(iConnection == null){
//			获取实现数据库连接的类
			iConnection = GetConnection.getConnection();
		}
	}
	
	/*
	 * 根据POJO类删除(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunDelete#runDeleteByPojo(java.lang.String, java.lang.Object)
	 */
	public boolean runDeleteByPojo(String tableName, Object obj) {
		bool = false;
		//获取数据库连接
		connection = iConnection.getConnection();

		
		//SQL语句
		String sql = "delete from " + tableName + " where id =";
		//获取类
		Class clazz = obj.getClass();
		//获取所有方法
		Method[] methods = clazz.getMethods();
		//获取删除ID
		String id = null;
		for(int i = 0 ; i < methods.length ; i++){
			//获取方法的名字
			String methodName = methods[i].getName();
			if(methodName.indexOf("get") != -1 && methodName.substring(3).equalsIgnoreCase("id")){
				try {
					//获取ID的值
					id = (String)methods[i].invoke(obj);
					break;
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
		
		sql += id;
		//调用根据SQL语句删除方法
		bool = runDeleteBySql(sql , null);
		return bool;
	}

	/*
	 * 根据存储过程删除方法(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunDelete#runDeleteByProc(java.lang.String, java.lang.Object[])
	 */
	public boolean runDeleteByProc(String proc, Object[] obj) {
		bool = false;
		//初始化数据库影响行数
		number = 0 ;
		//创建连接
		connection = iConnection.getConnection();

		try{
			//呼叫存储过程
			cs = connection.prepareCall("{" + proc + "}");
			//判断是否有参数
			if(obj != null){
				//如果参数不为空执行方法
				pretreatment(cs , obj);
			}
			//获取影响行数
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
	 * 根据SQL语句删除方法(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IRunDelete#runDeleteBySql(java.lang.String, java.lang.Object[])
	 */
	public boolean runDeleteBySql(String sql, Object[] obj) {
		bool = false;
		//初始化数据库影响行数
		number = 0;
		//创建连接
		connection = iConnection.getConnection();

		try{
			//SQL语句预处理
			ps = connection.prepareStatement(sql);
			//判断是否有参数
			if(obj != null){
				//如果参数不为空执行方法
				pretreatment(ps , obj);
			}
			//获取影响行数
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
	
	/**
	 * 处理预处理语句
	 * @param ps 预处理
	 * @param obj 参数
	 */
	public void pretreatment(PreparedStatement ps , Object[] obj){
		try {
			//判断参数类型循环赋值
			for (int i = 0; i < obj.length; i++) {
				//获取参数
				Object var = obj[i];
				
				if(var.getClass().getName().equals("java.lang.Integer")){
					ps.setInt(i+1, (Integer)var);
				}
				else if(var.getClass().getName().equals("java.lang.String")){
					ps.setString(i+1, (String)var);
				}
				else if (var.getClass().getName().equals("java.lang.Double")) {
					ps.setDouble(i+1, (Double)var);
				}
				else if (var.getClass().getName().equals("java.lang.Boolean")) {
					ps.setBoolean(i+1, (Boolean)var);
				}
				else if(var.getClass().getName().equals("java.lang.Float")){
					ps.setFloat(i+1, (Float)var);
				}
				else if (var.getClass().getName().equals("java.sql.Date")) {
					ps.setDate(i+1, (Date)var);
				}
				else{
					System.out.println(var.getClass().getName());
					ps.setObject(i+1, var);
				}
			}
		} catch (SQLException e) {
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
