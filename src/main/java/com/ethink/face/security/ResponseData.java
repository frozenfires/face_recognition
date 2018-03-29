/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.security;

import com.ethink.face.bean.Result;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
public class ResponseData extends Result{

	

	public static ResponseData forbidden() {
		ResponseData data = new ResponseData();
		data.setCode("-99");
		data.setDesc("非法请求");
		return data;
	}

	public static ResponseData outtime() {
		ResponseData data = new ResponseData();
		data.setCode("-98");
		data.setDesc("token已过期");
		return data;
	}
	

}
