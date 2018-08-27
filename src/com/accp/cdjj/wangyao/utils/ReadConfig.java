package com.accp.cdjj.wangyao.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 读取数据库XML配置文件
 * @author 王曜
 *
 */
public class ReadConfig {
	//数据库类型
	public static String serverType;
	//实际工厂类地址
	public static String factoryAddress;
	//数据库服务器地址
	public static String serverAddress;
	//数据库服务器端口
	public static String serverPort;
	//数据库名字
	public static String serverName;
	//数据库用户名
	public static String userName;
	//数据库用户密码
	public static String userPwd;
	//数据库驱动
	public static String driver;
	//数据库连接
	public static String serverUrl;
	//数据库连接实现类地址
	public static String connection;
	//数据库连接类型
	public static String driverType;
	
	public static boolean bool = false;
	
	public ReadConfig(){
		bool = true;
		read();
	}
	
	/**
	 * 读取XML方法
	 */
	public void read(){
		try{
			//创建File实例
			File f = new File("Config.xml");
			//获取 DocumentBuilderFactory 的新实例。
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			//使用当前配置的参数创建一个新的 DocumentBuilder 实例
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			//将给定文件的内容解析为一个 XML 文档，并且返回一个新的DOM对象
			Document doc = db.parse(f);
			
			//获得数据库类型
			serverType = doc.getElementsByTagName("ServerType").item(0).getFirstChild().getNodeValue();
			//获得实际工厂类地址
			factoryAddress = doc.getElementsByTagName("FactoryAddress").item(0).getFirstChild().getNodeValue();
			//获得数据库服务器地址
			serverAddress = doc.getElementsByTagName("ServerAddress").item(0).getFirstChild().getNodeValue();
			//获得数据库服务器端口
			serverPort = doc.getElementsByTagName("ServerPort").item(0).getFirstChild().getNodeValue();
			//获得数据库名称
			serverName = doc.getElementsByTagName("ServerName").item(0).getFirstChild().getNodeValue();
			//获得数据库用户名
			userName = doc.getElementsByTagName("UserName").item(0).getFirstChild().getNodeValue();
			//获得数据库用户密码
			userPwd = doc.getElementsByTagName("UserPwd").item(0).getFirstChild().getNodeValue();
			//获取数据库连接类型
			driverType = doc.getElementsByTagName("DriverType").item(0).getFirstChild().getNodeValue();
			//获取数据库连接实现类地址
			connection = doc.getElementsByTagName("Connection").item(0).getFirstChild().getNodeValue();
			
			NodeList nodeDriverType = doc.getElementsByTagName(driverType).item(0).getChildNodes();
			for(int i = 0 ; i < nodeDriverType.getLength() ; i++){
				String nodename = nodeDriverType.item(i).getNodeName();
				if("Driver".equals(nodename)){
					for(int j = 0 ; j < nodeDriverType.item(i).getChildNodes().getLength() ; j++){
						if(serverType.equals(nodeDriverType.item(i).getChildNodes().item(j).getNodeName()))
							driver = nodeDriverType.item(i).getChildNodes().item(j).getFirstChild().getNodeValue();
					}
				}else if("ServerUrl".equals(nodename)){
					for(int j = 0 ; j < nodeDriverType.item(i).getChildNodes().getLength() ; j++){
						if(serverType.equals(nodeDriverType.item(i).getChildNodes().item(j).getNodeName())){
							serverUrl = nodeDriverType.item(i).getChildNodes().item(j).getFirstChild().getNodeValue();
						}
					}
				}
				
				
			}
			
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}	
	}
	
	/**
	 * 根据名字获得对应配置
	 * @param name 配置名字
	 * @return
	 */
	public static String getCogfig(String name){
		if(!bool){
			new ReadConfig();
		}
		if(name.equals("serverType"))
			return serverType;
		else if(name.equals("serverAddress"))
			return serverAddress;
		else if(name.equals("serverPort"))
			return serverPort;
		else if(name.equals("serverName"))
			return serverName;
		else if(name.equals("userName"))
			return userName;
		else if(name.equals("userPwd"))
			return userPwd;
		else if(name.equals("driver"))
			return driver;
		else if(name.equals("factoryAddress"))
			return factoryAddress;
		else if(name.equals("serverUrl"))
			return serverUrl;
		else if(name.equals("connection"))
			return connection;
		return null;
	}
}
