package com.accp.cdjj.wangyao.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * 读取数据库操作类配置XML
 * @author 王曜
 *
 */
public class ReadOperata {
	
	/**
	 * 读取数据库操作实现类
	 *
	 */
	public static String read(String name){
		String temp = null;
		try{
			//创建File实例
			File f = new File("DBOperate.xml");
			//获取 DocumentBuilderFactory 的新实例。
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			//使用当前配置的参数创建一个新的 DocumentBuilder 实例
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			//将给定文件的内容解析为一个 XML 文档，并且返回一个新的DOM对象
			Document doc = db.parse(f);
			temp = doc.getElementsByTagName(name).item(0).getFirstChild().getNodeValue();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return temp;
	}
}
