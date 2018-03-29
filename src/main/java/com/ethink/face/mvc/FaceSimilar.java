/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ethink.face.FaceException;
import com.ethink.face.bean.Body;
import com.ethink.face.bean.Result;
import com.ethink.face.service.FaceSimilarService;
import com.github.pagehelper.PageInfo;

/**
 *
 * 描述: 人脸相似度
 * 
 * @author wangjing.dc@qq.com
 */
@RestController
public class FaceSimilar {

	private static final Logger log = LoggerFactory.getLogger(FaceSimilar.class);

	@Autowired
	private FaceSimilarService faceSimilarService;

	/**
	 * 人脸相似度比对
	 */
	@RequestMapping(value = "/api/match", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object match(String face1, String face2) {
		float value;
		log.info("两张图片相似度比对开始----");
		// log.info("face1为："+face1);
		// log.info("face2为："+face2);
		try {
			value = faceSimilarService.match(face1, face2);
			log.info("相似度比对结果:" + value);
			return Result.build("0", "请求处理成功", String.valueOf(value));
		} catch (FaceException e) {
			// log.error(e.msg(), e);
			return Result.build(e.code(), e.msg());
		}

	}

	/**
	 * 验证人脸相似度
	 * 
	 * @param phone
	 *            电话号码
	 * @param image
	 *            待验证的图片Base64编码
	 * @return 相似度
	 */
	@RequestMapping(value = "/api/matchbyid", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object similar(String id, String face) {
		log.info(String.format("人脸相似度比对开始：phone=%s, image=''", id, face));
		try {
			float value = faceSimilarService.similar(id, face);
			log.info("相似度比对结果:" + value);
			return Result.build("0", "请求处理成功", String.valueOf(value));

		} catch (FaceException e) {
			// log.error(e.msg(), e);
			return Result.build(e);
		}
	}

	@RequestMapping(value = "/api/matchInfoByDate", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryMatchInfo(String beforetime, String aftertime, String start, String page) {
		log.info("开始分页查询,从" + beforetime + "至" + aftertime + ";第" + start + "条开始,每页" + page);
		Map<String, String> mapQuery = new HashMap<String, String>();
		mapQuery.put("beforetime", beforetime);
		mapQuery.put("aftertime", aftertime);
		int pageNum = Integer.parseInt(start) / Integer.parseInt(page) + 1;
		mapQuery.put("start", pageNum + "");
		mapQuery.put("page", page);
		PageInfo<Map<String, Object>> pageInfo = faceSimilarService.queryMatchInfo(mapQuery);

		return Result.build("0", Body.getBody().add("count", pageInfo.getTotal()).add("data", pageInfo.getList()));
	}

	@RequestMapping(value = "/api/faceRetrieve", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Result faceRetrieve(String face) {
		log.info("人脸检索1:N");
		Body<String, Object> body = Body.getBody();
		if (face == null || "".equals(face)) {
			body.add("code", FaceException.SERVICE_ERROR).add("desc", "图片为空");
			return Result.build("0", body);
		}
		List<Map<String, Object>> faceList = faceSimilarService.faceRetrieve(face);
		Map<String, Object> user = null;
		if (faceList.size() > 0) {
			user = faceList.get(faceList.size() - 1);

		}
		body.add("usersInfo", user);

		return Result.build("0", body);
	}

	@RequestMapping(value = "/api/faceRetrieveOneToOne", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Result faceRetrieveOneToOne(String face, String carNum) {
		log.info("人脸检索1:1");
		Body<String, Object> body = Body.getBody();
		if (face == null || "".equals(face) || carNum == null || "".equals(carNum)) {
			body.add("code", FaceException.SERVICE_ERROR).add("desc", "图片或车牌为空");
			return Result.build(FaceException.SERVICE_ERROR, "图片或车牌为空", null);
		}
		try {
			Map<String, Object> result = faceSimilarService.retrieveOneToOne(face, carNum);
			return Result.build("0", "请求处理成功", result);
		} catch (FaceException e) {
			body.add("code", e.code()).add("desc", e.msg());
			return Result.build(e.code(), e.msg());
		}

	}

	@RequestMapping(value = "/api/lossDrivingRegister", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Result lossDrivingRegister(String dateTime, String location, String image,String carNum) {
		log.info("失驾人员信息增加");
		Body<String, Object> body = Body.getBody();
		if (image == null || "".equals(image)) {
			body.add("code", FaceException.SERVICE_ERROR).add("desc", "图片");
			return Result.build(FaceException.SERVICE_ERROR, "图片或车牌为空", null);
		}
		try {
			boolean result = faceSimilarService.insertLossDrivingInfo(dateTime, location, image,carNum);
			return Result.build("0", "请求处理成功", result == true ? "成功" : "失败");
		} catch (FaceException e) {
			body.add("code", e.code()).add("desc", e.msg());
			return Result.build(e.code(), e.msg());
		}

	}

	@RequestMapping(value = "/api/getLossDrivingInfo", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Result getLossDrivingInfo() {
		log.info("获得失驾人员信息列表");

		List<Map<String, Object>> lossList = faceSimilarService.findLossDrivingByCarNum();
		return Result.build("0", "请求处理成功", lossList);

	}
	
	/**
	 *更新失驾人员状态
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/api/updateLossDrivingStatus", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Result updateLossDrivingStatus(String status) {
		log.info("更新失驾人员状态");
		boolean ret = faceSimilarService.updateLossDrivingStatus(status);
		return Result.build("0", "请求处理成功", ret);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new File("d:/asdfssdf.txt").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
