package com.accp.cdjj.wangyao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import com.accp.cdjj.wangyao.dao.IFind;
import com.accp.cdjj.wangyao.dao.IRunFind;
import com.accp.cdjj.wangyao.utils.CheckString;
import com.accp.cdjj.wangyao.utils.ReadPojo;
/**
 * 查询接口实现类
 * @author 王曜
 *
 */
public class FindImpl implements IFind {
	//返回的集合
	List list;
	//结果集
	ResultSet resultset;
	//处理查找接口
	IRunFind runFind;
	public FindImpl(){
		if(runFind == null){
			runFind = new RunFindImpl();
		}
	}
	
	/*
	 * 实现IFind接口方法(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.IFind#find(java.lang.Object[])
	 */
	public synchronized List find(Object... objects) {
		if(objects.length == 1 || objects.length == 3){
			list = new ArrayList();
			resultset = getResultSet(objects);
			list = runFind.getList(resultset,objects);
		}else{
			System.out.print("参数个数错误!");
		}
		return list;
	}
	
	/**
	 * 判断参数类型并调用处理类
	 * @param obj 参数数组
	 * @return 结果集
	 */
	public ResultSet getResultSet(Object[] obj){
		ResultSet rs = null;
		//判断参数是否只有一个
		if(obj.length == 1){
			String tableName =	null;
			if(obj[0].getClass().getName() == "java.lang.String"){
				String pojopath = ReadPojo.readTableName((String)obj[0]);
				if(pojopath != null){
					tableName = (String)obj[0];
				}
			}else{
				//如果只有一个,获得参数类型
				String className = obj[0].getClass().getName();
				//根据参数类型读取XML配置文件判断是否有对应表,并获得表名
				tableName = ReadPojo.read(className);
			}
			//如果表名不为空
			if(tableName != null){
				rs = runFind.runFindByTabelName(tableName);
			}else{
				System.out.println("Pojo类错误!");
			}
		}else{
			//将第一个参数转换为String类型
			String str = obj[0].toString();
			//获得参数数组
			Object[] objects = (Object[])obj[1];
			
			if(CheckString.isProcedure(str)){
				//如果是存储过程
				//储存过程中问号的个数
				int lenght = 0;
				//判断存储过程中有多少个问号
				for(int i = 0 ; i < str.length() ; i++ ){
					if(str.charAt(i) == '?'){
						lenght++;
					}
				}
				//参数个数
				int sqlLen = 0;
				if(objects != null){
					//如果参数不为空,获得参数个数
					sqlLen = objects.length;
				}
				//如果存储过程中问号数量和参数个数不想同
				if(lenght != sqlLen){
					System.out.println("存储过程或参数数量错误");
				}else if(lenght == 0){
					//如果存储过程中问号数量为0
					rs = runFind.runFindeByProc(str, null);
				}else{
					//如果存储过中问号数量和参数数量相同
					rs = runFind.runFindeByProc(str, objects);
				}
			}else if(CheckString.isSQL(str)){
				//如果是SQL语句
				//SQL语句中问号的个数
				int lenght = 0;
				//判断存SQL语句中有多少个问号
				for(int i = 0 ; i < str.length() ; i++ ){
					if(str.charAt(i) == '?'){
						lenght++;
					}
				}
				//参数个数
				int sqlLen = 0;
				if(objects != null){
					//如果参数不为空,获得参数个数
					sqlLen = objects.length;
				}
				//如果SQL语句中问号数量和参数个数不想同
				if(lenght != sqlLen){
					System.out.println("存储过程或参数数量错误");
				}else if(lenght == 0){
					//如果SQL语句中问号数量为0
					rs = runFind.runFindBySql(str, null);
				}else{
					//如果SQL语句中问号数量和参数数量相同
					rs = runFind.runFindBySql(str, objects);
				}
			}else{
				//如果都不是
				System.out.println("输入的不是SQL语句或存储过程");
			}
			
		}
		return rs; 
	}
	
}
