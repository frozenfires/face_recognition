/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ethink.face.FaceException;
import com.ethink.face.bean.Body;
import com.ethink.face.bean.Result;

/**
 *
 * 描述: 登录、登出Controller
 * @author wangjing.dc@qq.com
 */
@RestController
public class FaceLoginController {

	@Autowired
	private FaceSecurity faceSecurity;
	
	@RequestMapping(value = "/api/login", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Object login(String userId, String password) {
		try {
			FaceToken token = faceSecurity.login(userId, password);
			return Result.build("0", "获得token成功",token);
		}catch(FaceException e) {
			return Result.build(e.code(), e.msg(),"");
		}
	}
	
	@RequestMapping(value = "/api/logout", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Object doLogout(String token) {
		faceSecurity.logout(token);
		return Result.build("0", "logout");
	}
	
}
