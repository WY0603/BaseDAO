package com.accp.cdjj.wangyao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 检查输入字符串类型
 * @author 王曜
 *
 */
public class CheckString {
//	返回的布尔值
	private static boolean bool;
	
	/**
	 * 判断字符串是否为表名
	 * @param str 输入的字符串
	 * @return 是,否
	 */
	public static boolean isTableName(String str){
		bool = false;
		String temp = "^\\s*\\w+\\s*$";
		Pattern p = Pattern.compile(temp);
		Matcher m = p.matcher(str);
		if(m.find()){
			bool = true;
		}
		return bool;
	}
	
	/**
	 * 检查字符串是否为SQL语句
	 * @param str 输入的字符串
	 * @return
	 */
	public static boolean isSQL(String str){
		bool = false;
		//表示检查规则
		String temp = "^(?i)\\s*(select|update|insert|delete)\\s*(\\w|\\s)*\\*?(\\w|\\s)*\\s*(from|into|set)\\s*[\\w\\[\\]]+.*$";
		//将规则导入
		Pattern p=Pattern.compile(temp);  
		//检查规则是否匹配
		Matcher m=p.matcher(str);		
		
		//一般的SQL语句
		if (m.find()) {
			bool = true;
		}
		return bool;
	}
	
	/**
	 * 检查字符串是否为存储过程
	 * @param str 输入的字符串
	 * @return
	 */
	public static boolean isProcedure(String str){
		bool = false;
		//表示检查规则
		String temp = "^\\s*(?i)call\\s+[\\w()?,]+\\s*$" ;
		//将规则导入
		Pattern p=Pattern.compile(temp);  
		//检查规则是否匹配
		Matcher m=p.matcher(str);		
		
		//一般的SQL语句
		if (m.find()) {
			bool = true;
		}
		return bool;
	}
}
