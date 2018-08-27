package com.accp.cdjj.wangyao.utils;

import com.accp.cdjj.wangyao.dao.IConnection;

public class GetConnection {
	public static IConnection getConnection(){
		try {
			Class cla = Class.forName(ReadConfig.getCogfig("connection"));
			try {
				return (IConnection)cla.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
