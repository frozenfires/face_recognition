package com.ethink.face.bean;

import java.util.HashMap;

/**
 * @author wangluliang
 *	Body bean
 * @param <K>
 * @param <V>
 */
public class Body<K,V> extends HashMap<K,V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Body() {};
	
	public static <K,V> Body<K,V> getBody(){
		Body<K,V> body = new Body<K,V>();
		return body;
	}
	
	public Body<K,V> add(K key,V value) {
		this.put(key, value);
		return this;
	}
}
