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
import java.util.ArrayList;
import java.util.List;

import com.accp.cdjj.wangyao.dao.IConnection;
import com.accp.cdjj.wangyao.dao.IRunFind;
import com.accp.cdjj.wangyao.utils.CheckClass;
import com.accp.cdjj.wangyao.utils.GetConnection;

/**
 * 查找的处理类
 * @author 王曜
 *
 */
public class RunFindImpl implements IRunFind {
	// 数据库连接
	Connection connection;

	// 结果集
	ResultSet resultset;

	// 预处理语句
	PreparedStatement ps;

	// 静态预处理语句
	Statement statement;

	// 呼叫存储过程
	CallableStatement cs;
	//数据库连接接口
	IConnection iConnection;
	
	List list;
	
	public RunFindImpl(){
		if(iConnection == null){
			//获取实现数据库连接的类
			iConnection = GetConnection.getConnection();
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.accp.cdjj.wangyao.dao.IRunFind#runFindBySql(java.lang.String,
	 *      java.lang.Object[])
	 */
	public ResultSet runFindBySql(String sql, Object[] obj) {
		// 初始化结果集
		resultset = null;
		// 获得连接
		connection = iConnection.getConnection();

		try {
			ps = connection.prepareStatement(sql);
			// 判断结果集是否为空
			if (obj != null) {
				pretreatment(ps, obj);
			}
			//获得结果集
			resultset = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.accp.cdjj.wangyao.dao.IRunFind#runFindeByProc(java.lang.String,
	 *      java.lang.Object[])
	 */
	public ResultSet runFindeByProc(String proc, Object[] obj) {
		// 初始化结果集
		resultset = null;
		// 获得连接
		connection = iConnection.getConnection();
		
		
		try {
			cs = connection.prepareCall("{" + proc + "}");
			// 判断参数是否为空
			if (obj != null) {
				pretreatment(cs, obj);
			}
			//获得结果集
			resultset = cs.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultset;
	}

	/**
	 * 处理预处理语句
	 * @param ps 预处理
	 * @param obj 参数
	 */
	public ResultSet pretreatment(PreparedStatement ps, Object[] obj) {

		try {
			// 循环参数赋值
			for (int i = 1; i <= obj.length; i++) {
				Object var = obj[i - 1];
				if (var.getClass().getName().equals("java.lang.Integer")) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultset;
	}

	public ResultSet runFindByTabelName(String tableName) {
		//拼接SQL语句
		String sql = "select * from " + tableName;
		resultset = null;
		//获得连接
		connection = iConnection.getConnection();
		
		
		try{
			statement = connection.createStatement();
			//获得结果集
			resultset = statement.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultset;
	}
	
	public void closeAll() {
		// TODO Auto-generated method stub
		iConnection.close(resultset, ps, connection);
	}
	public List getList(ResultSet rs,Object[] objects) {
		try {
			//获取关于 ResultSet 对象中列的类型和属性信息的对象
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//获得结果集的列数
			int row = rsmd.getColumnCount();
			
			//获得参数类型
			Class clazz = CheckClass.check(objects);
			
			
			
			list = new ArrayList();
			while(rs.next()){
				//创建实例
				Object obj = clazz.newInstance();
				
				//获得方法数组
				Method[] method = clazz.getMethods();
				
				//循环结果集所有列
				for(int i = 1 ; i <= row ; i++){
					//获得列的名字
					String rowName = rsmd.getColumnName(i); 
					//获得列的类型
					String rowType = rsmd.getColumnTypeName(i);
					
					//循环所有方法
					for(int j = 0 ; j < method.length ; j++){
						//获得方法名字
						String methodName = method[j].getName();
						//判断方法名是否为set开始,并和结果集列对应
						if(methodName.substring(3).equalsIgnoreCase(rowName) && methodName.indexOf("set") != -1){
							if(rowType.equals("int")){
								method[j].invoke(obj, rs.getString(rowName));
							}else if(rowType.equals("varchar")){
								method[j].invoke(obj, rs.getString(rowName));
							}else if(rowType.equals("bit")){
								method[j].invoke(obj, rs.getString(rowName));
							}
							
							j = method.length;
							
						}
					}
				}	
				//添加进集合
				list.add(obj);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("数据库返回类型与POJO不同");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			iConnection.close(resultset, ps, connection);
		}
		return list;
	}
	
	
	
	

}
