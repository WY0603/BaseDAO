package com.accp.cdjj.wangyao.dao;

import com.accp.cdjj.wangyao.utils.ReadConfig;

/**
 * DB abstract factory
 * @author yao wang
 *
 */
public abstract class AbstractFactoryDB {
	//real factory 
	private static String factoryAddress;
	
	/**
	 * get real factory
	 * @return real factory
	 */
	public static AbstractFactoryDB getFactory(){
		//read config
		ReadConfig readConfig = new ReadConfig();
		//get real factory address
		factoryAddress = readConfig.factoryAddress;
		
		try{
			//get real factory Class object
			Class factory = Class.forName(factoryAddress);
			
			return (AbstractFactoryDB)factory.newInstance();
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	
	public abstract ISave save();
	public abstract IDelete delete();
	public abstract IUpdate update();
	public abstract IFind find();
	
}
