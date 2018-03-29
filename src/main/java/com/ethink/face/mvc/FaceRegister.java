/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;
   
import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ethink.face.FaceException;
import com.ethink.face.bean.Body;
import com.ethink.face.bean.Result;
import com.ethink.face.service.FaceRegisterService;

/**
 *
 * 描述:  人脸注册
 * 
 * @author wangjing.dc@qq.com
 */
@RestController
public class FaceRegister {
	
	private static final Logger log = LoggerFactory.getLogger(FaceRegister.class);

	@Autowired
	private FaceRegisterService faceRegisterService;
	@Value("${app.basePath}")
	private String basePath;
	/**
	 * 注册人脸
	 * @param userid 用户id
	 * @param image 人脸照片，Base64格式
	 * @param imgType 文件格式
	 */
	@RequestMapping(value="api/register", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Object register(String userid, String image, String imgType) {
		log.info(String.format("[%s]进入人脸注册,image=%s", userid, image));
		if (userid == null || userid.equals("")) {
			return Result.build("-20", "缺少参数[faceId]");
		}
		if (image == null || image.equals("")) {
			return Result.build("-21", "缺少参数[image]");
		}

		int len = faceRegisterService.selectUserId(userid).size();
		if (len > 0) {
			return Result.build("-1", "用户id已注册");
		}
		try {
			faceRegisterService.register(userid, image, imgType);
		} catch (FaceException e) {
			return Result.build(e.code(), e.msg());
		}

		log.info(String.format("[%s]注册完成.........", userid));
		return Result.build(String.valueOf(0), "");
	}
	
	/**
	 * 删除人脸信息
	 * @param userid 用户id
	 * @param imgType 图片格式
	 */
	@RequestMapping(value="api/deluser", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Object deleteUser(String userid, String imgType) {
		if(userid == null||userid.equals("")) {
			return Result.build("-20", "缺少参数[faceId]");
		}
		faceRegisterService.deleteUser(userid,imgType);
		log.info(String.format("[%s]删除成功.........", userid));
		return Result.build(String.valueOf(0), "");
	}
	
	/**
	 * 修改人脸信息
	 * @param userid 用户id
	 * @param imgType 图片格式
	 * @param newImgType 修改后的图片格式
	 * @param oldImgType 原先图片格式
	 */
	@RequestMapping(value="api/update", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Object updateUser(String userid, String image,String newImgType, String oldImgType) {
		if(image == null||image.equals("")) {
			return Result.build("-21", "缺少参数[image]");
		}
		try {
			faceRegisterService.updateUser(userid,image,newImgType,oldImgType);
		} catch (FaceException e) {
			return Result.build(e.code(), e.msg());
			//return Result.build(e);
		}
		log.info(String.format("[%s]修改成功.........", userid));
		return Result.build(String.valueOf(0),"");
	}
	
	/**
	 * 批量注册
	 * @param userid 用户id
	 * @param imgType 图片格式
	 * @param newImgType 修改后的图片格式
	 * @param oldImgType 原先图片格式
	 * @throws Exception 
	 */
	@RequestMapping(value="api/batchRegister", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Object batchAddUser(){
		List<Map<String,String>> errorObject = null;
		String	filePath= basePath+"//facelib";				       
	    long listFiles =  new File(filePath).length();  
		if(listFiles > 30){ 
			errorObject = faceRegisterService.batchRegister();
			System.out.println("errorObject>>>");
			System.out.println(errorObject);
			
		
			 if(errorObject.size() == 1){
				 if( errorObject.get(0).get("code").equals("-1")) {
					 return Result.build(errorObject.get(0).get("code"),errorObject.get(0).get("message"));
				 }
			 }
			return Result.build(String.valueOf(0),Body.getBody().add("data", errorObject));
	   } 
		return  Result.build(String.valueOf(0),Body.getBody().add("data",null));
			
	   
	}
	
	
}
