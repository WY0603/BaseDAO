package com.accp.cdjj.wangyao.utils;
/**
 * get data type
 * @author yao wang
 *
 */
public class CheckClass {
	//type
	public static Class cla;
	
	/**
	 * get data type
	 * @param objects
	 * @return
	 */
	public static Class check(Object[] objects){
		if(objects.length == 1){
			//if only one parameter
			if(objects[0].getClass().getName().equals("java.lang.String")){
				String pojoPath = ReadPojo.readTableName(objects[0].toString());
				try {
					cla = Class.forName(pojoPath);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				cla = objects[0].getClass();
			}
		}else if(objects.length == 3){
			//if there are three parameter
			if(objects[2].getClass().getName().equals("java.lang.String")){
				String pojoPath = ReadPojo.readTableName(objects[0].toString());
				try {
					cla = Class.forName(pojoPath);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				cla = objects[2].getClass();
			}
		}else{
			System.out.println("参数个数错误!");
		}
		return cla;
	}
}
