/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
public class FaceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;

	// 业务逻辑错误
	public static final String SERVICE_ERROR = "-1";
	// 识别引擎错误
	public static final String ENGINER_ERROR = "-2";
	
	public FaceException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	public String msg() {
		return this.message;
	}

	public String code() {
		return this.code;
	}

}
