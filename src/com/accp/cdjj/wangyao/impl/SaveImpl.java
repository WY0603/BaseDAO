package com.accp.cdjj.wangyao.impl;

import com.accp.cdjj.wangyao.dao.IRunSave;
import com.accp.cdjj.wangyao.dao.ISave;
import com.accp.cdjj.wangyao.utils.CheckString;
import com.accp.cdjj.wangyao.utils.ReadPojo;

/**
 * 增加接口实现类
 * @author 王曜
 *
 */

public class SaveImpl implements ISave{
	boolean bool;
	IRunSave runSave;
	
	public SaveImpl(){
		if(runSave == null){
			runSave = new RunSaveImpl();
		}
	}

	
	/*
	 * 实现接口ISave(non-Javadoc)
	 * @see com.accp.cdjj.wangyao.dao.ISave#save(java.lang.Object[])
	 */
	public synchronized boolean save(Object... objects) {
		//
		if(objects.length == 1 || objects.length == 2){
			bool = getBoolean(objects);
		}else{
			System.out.print("参数个数错误!");
		}
		return bool;
	}
	
	public boolean getBoolean(Object[] obj){
		boolean temp = false;
		//如果只有一个参数
		if(obj.length == 1){
			//如果只有一个,获得参数类型
			String className = obj[0].getClass().getName();
			//根据参数类型读取XML配置文件判断是否有对应表,并获得表名
			String tableName = ReadPojo.read(className);
			//如果表名不为空
			if(tableName != null){
				temp  = runSave.runSaveByPojo(tableName,obj[0]);
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
					temp = runSave.runSaveByProc(str, null);
				}else{
					//如果存储过中问号数量和参数数量相同
					temp = runSave.runSaveByProc(str, obj);
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
					temp = runSave.runSaveBySql(str, null);
				}else{
					//如果SQL语句中问号数量和参数数量相同
					temp = runSave.runSaveBySql(str, obj);
				}
			}else{
				//如果都不是
				System.out.println("输入的不是SQL语句或存储过程");
			}
		}
		return temp;
	}


}
