package com.accp.cdjj.wangyao.dao;

import java.util.List;

/**
 * query method
 * @author yao wang
 *
 */
public interface IFind {

	/**
	 * query method
	 * @param objects 
	 * @return
	 */
	public List<Object> find(Object ...objects);
}
