/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.bean;

import com.ethink.face.FaceException;

/**
 *
 * 描述: 结果集对象
 * @author wangjing.dc@qq.com
 */
public class Result {

	private String code;
	private String desc;
	private Object body;
	
	public static Result build(String code, String desc) {
		Result result = new Result();
		result.setCode(code);
		result.setDesc(desc);
		return result;
	}
	
	public static Result build(String code, Object body) {
		Result result = new Result();
		result.setCode(code);
		result.setBody(body);
		return result;
	}
	public static Result build(String code,String desc, Object body) {
		Result result = new Result();
		result.setCode(code);
		result.setBody(body);
		result.setDesc(desc);
		return result;
	}
	public static Result build(FaceException e) {
		Result result = new Result();
		result.setCode(e.code());
		result.setDesc(e.msg());
		return result;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}




	
}
