/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ethink.face.bean.Body;
import com.ethink.face.bean.Result;
import com.ethink.face.service.FaceQueryService;
import com.github.pagehelper.PageInfo;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
@RestController
public class FaceQuery {
	
	@Autowired
	private FaceQueryService faceQueryService;

	/**
	 * 列出注册信息
	 * @return
	 */
	@RequestMapping(value="api/listRegister", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Object listRegister(String userid, String dataTime1,String dataTime2,
			String start,String pageCount) {

		PageInfo<Map<String,Object>> pageInfo = faceQueryService.listRegister(userid, dataTime1,dataTime2,
				start,pageCount);
		   
		return Result.build("0",Body.getBody().add("count",pageInfo.getTotal()).add("data", pageInfo.getList()));
	}
	
	/**
	 * 列出人脸比对结果
	 * @return
	 */
	@RequestMapping(value="api/listFaceResult", produces= {MediaType.APPLICATION_JSON_VALUE})
	public Object listResult() {
		
		return faceQueryService.listResult();
	}

	
	public static void main(String[] args) {
		System.out.println(new FaceQuery().listResult());
	}
	
}
