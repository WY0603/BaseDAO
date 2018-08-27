package com.accp.cdjj.wangyao.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 读取POJO配置XML
 * @author IBM
 *
 */
public class ReadPojo {
	//pojo类名
	public static String pojoName;
	//对应表名
	public static String talbeName;
	//临时存放表名
	public static String temp;
	
	public ReadPojo(){
		
	}
	
	/**
	 * 读取POJO配置XML
	 *
	 */
	public static String read(String pojoPath){
		
		try{
			//创建File实例
			File f = new File("PojoConfig.xml");
			//获取 DocumentBuilderFactory 的新实例。
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			//使用当前配置的参数创建一个新的 DocumentBuilder 实例
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			//将给定文件的内容解析为一个 XML 文档，并且返回一个新的DOM对象
			Document doc = db.parse(f);
			NodeList nodes = doc.getElementsByTagName("pojopath");
			for(int i = 0 ; i < nodes.getLength() ; i++){
				temp = nodes.item(i).getFirstChild().getNodeValue();
				if(pojoPath.equals(temp)){
					pojoName = doc.getElementsByTagName("pojoname").item(i).getFirstChild().getNodeValue();
					talbeName = doc.getElementsByTagName("tablename").item(i).getFirstChild().getNodeValue();
					break;
				}
			}
			
			return talbeName;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
public static String readTableName(String tableName){
		String pojopath = null;
		try{
			//创建File实例
			File f = new File("PojoConfig.xml");
			//获取 DocumentBuilderFactory 的新实例。
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			//使用当前配置的参数创建一个新的 DocumentBuilder 实例
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			//将给定文件的内容解析为一个 XML 文档，并且返回一个新的DOM对象
			Document doc = db.parse(f);
			NodeList nodes = doc.getElementsByTagName("tablename");
			for(int i = 0 ; i < nodes.getLength() ; i++){
				temp = nodes.item(i).getFirstChild().getNodeValue();
				if(tableName.equalsIgnoreCase(temp)){
					pojoName = doc.getElementsByTagName("pojoname").item(i).getFirstChild().getNodeValue();
					pojopath = doc.getElementsByTagName("pojopath").item(i).getFirstChild().getNodeValue();
					break;
				}
			}
			
			return pojopath;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
