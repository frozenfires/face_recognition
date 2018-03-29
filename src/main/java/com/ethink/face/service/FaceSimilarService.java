/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ethink.face.FaceException;
import com.ethink.face.core.FaceEnginer;
import com.ethink.face.db.FaceDAO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 描述:
 * 
 * @author wangjing.dc@qq.com
 */
@Service
public class FaceSimilarService {

	private static final Logger log = LoggerFactory.getLogger(FaceSimilarService.class);

	@Value("${app.basePath}")
	private String basePath;

	@Autowired
	private ImageService imageService;

	@Autowired
	private FaceDAO faceDAO;

	@Autowired
	private FaceEnginer faceEnginer;

	/**
	 * 对比两张人脸的相似度
	 * 
	 * @param faceaB64
	 * @param facebB64
	 * @return
	 * @throws FaceException
	 */
	public float match(String faceaB64, String facebB64) throws FaceException {
		if (faceaB64 == null || facebB64 == null || "".equals(faceaB64) || "".equals(facebB64)) {
			log.info("图片为空");
			throw new FaceException(FaceException.SERVICE_ERROR, "图片为空");
		}
		tryCreateDir();

		String tmp = "/tmp/match/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File dir = new File(basePath + tmp);
		dir.mkdirs();
		//
		String current = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String photo_a = tmp + "/" + current + "_a.png";
		String photo_b = tmp + "/" + current + "_b.png";

		imageService.saveBase64image(basePath + photo_a, faceaB64);
		imageService.saveBase64image(basePath + photo_b, facebB64);

		float value = -1;
		FaceException te = null;
		try {
			value = faceEnginer.similar(basePath + photo_a, basePath + photo_b);
		} catch (FaceException e) {
			log.error(e.getMessage(), e);
			te = e;
		} catch (Exception e) {
			te = new FaceException(FaceException.ENGINER_ERROR, "图片格式或有损坏");
			log.error(e.getMessage(), e);
		}
		Map<String, Object> match = new HashMap<String, Object>();
		match.put("match_type", FaceDAO.MATCH_TWO_PHOTO);
		match.put("photo_a", photo_a);
		match.put("photo_b", photo_b);
		match.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		match.put("result", value + "");
		match.put("errorMess", te != null ? te.getMessage() : "");

		int res = faceDAO.insertIntoMatchById(match);
		if (res != -1) {
			log.info("对比信息存储数据库成功");
		} else {
			log.info("对比信息存储数据库失败");
		}

		if (te != null) {

			throw te;
		}

		return value;
	}

	/**
	 * 人脸相似度比对
	 * 
	 * @param faceId
	 *            人脸id
	 * @param imageB64
	 *            被比对的人脸图像Base64
	 * @return
	 */
	public float similar(String faceId, String imageB64) throws FaceException {
		if (imageB64 == null || faceId == null || "".equals(imageB64) || "".equals(faceId)) {
			log.error("图片或id为空");
			throw new FaceException(FaceException.SERVICE_ERROR, "图片或id为空");
		}
		tryCreateDir();
		Map<String, String> faceMap = faceDAO.findPhotoByUserId(faceId);
		String facePath = "";
		if (faceMap != null) {
			facePath = faceMap.get("IMAGEPATH");
		}
		File fileA = new File(basePath + facePath);

		if (!fileA.isFile()) {
			String msg = String.format("[%s]未注册", faceId);
			log.error(msg);
			throw new FaceException(FaceException.SERVICE_ERROR, msg);
		}

		// 创建临时目录
		String current = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String tmp = "/tmp/match/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		File dir = new File(basePath + tmp);
		dir.mkdirs();

		String faceIdPhoto = tmp + "/" + faceId + "_" + current + "_a.png";
		String matchPhoto = tmp + "/" + faceId + "_" + current + "_b.png";
		// 拷贝注册图片
		float value = -1;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FaceException fe = null;
		try {
			File facePhoto = new File(basePath + faceIdPhoto);
			fis = new FileInputStream(fileA);
			fos = new FileOutputStream(facePhoto);

			IOUtils.copy(fis, fos);
			// 保存当前比对照片
			imageService.saveBase64image(basePath + matchPhoto, imageB64);

			value = faceEnginer.similar(basePath + faceIdPhoto, basePath + matchPhoto);

		} catch (IOException e) {
			fe = new FaceException(FaceException.SERVICE_ERROR, "文件拷贝错误");
			log.error(fe.getMessage(), e);
		} catch (FaceException e) {
			fe = e;
			log.error(fe.getMessage(), e);
		} catch (Exception e) {
			fe = new FaceException(FaceException.ENGINER_ERROR, "图片格式或有损坏");
			log.error(e.getMessage(), e);
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				fe = new FaceException(FaceException.SERVICE_ERROR, "文件流关闭错误");
				log.error(fe.getMessage(), e);
			}
		}
		Map<String, Object> match = new HashMap<String, Object>();
		match.put("match_type", FaceDAO.MATCH_BY_ID);
		match.put("photo_a", faceIdPhoto);
		match.put("photo_b", matchPhoto);
		match.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		match.put("result", value + "");
		match.put("userid", faceId);
		match.put("errorMess", fe != null ? fe.getMessage() : "");

		int res = faceDAO.insertIntoMatchById(match);
		if (res != -1) {
			log.info("对比信息存储数据库成功");
		} else {
			log.info("对比信息存储数据库失败");
		}

		if (fe != null)
			throw fe;
		return value;
	}

	private void tryCreateDir() {
		File base = new File(basePath);
		if (!base.exists())
			base.mkdirs();
	}

	/**
	 * 分页查询
	 * 
	 * @param mapQuery
	 * @return
	 */
	public PageInfo<Map<String, Object>> queryMatchInfo(Map<String, String> mapQuery) {
		PageHelper.startPage(Integer.parseInt(mapQuery.get("start")), Integer.parseInt(mapQuery.get("page")));
		List<Map<String, Object>> list = faceDAO.selectMatchInfoByDateTime(mapQuery);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

		return pageInfo;
	}

	/**
	 * 人脸检索一比N
	 * 
	 * @param face
	 * @return
	 */
	public List<Map<String, Object>> faceRetrieve(String face) {
		tryCreateDir();

		// 创建临时目录
		String current = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String tmp = "/tmp/retrieve/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		File dir = new File(basePath + tmp);
		dir.mkdirs();
		String photo_a = tmp + "/" + current + "retrieve.png";
		imageService.saveBase64image(basePath + photo_a, face);

		List<Map<String, Object>> retrieveList = new ArrayList<Map<String, Object>>();
		try {
			byte[] faceTarget = faceEnginer.extractFaceFeature(basePath + photo_a);
			List<Map<String, Object>> userList = faceDAO.findAllUserInfo();
			Map<String, Object> userMap;
			for (int i = 0; i < userList.size(); i++) {
				userMap = userList.get(i);
				Object obj = userMap.get("FEATURE");
				if (obj != null) {
					byte[] feature = (byte[]) obj;
					float value = faceEnginer.similarByFeature(faceTarget, feature);
					userMap.put("code", value);
					userMap.remove("FEATURE");
					AddListSortAsc(userMap, retrieveList);
				}
			}
		} catch (FaceException e) {
			log.error(e.getMessage(), e);
		}
		return retrieveList;
	}

	/**
	 * 人脸通过车牌找到人脸并比对
	 * 
	 * @param face
	 * @param carNum
	 * @return
	 */
	public Map<String, Object> retrieveOneToOne(String face, String carNum) throws FaceException {
		tryCreateDir();

		// 创建临时目录
		String current = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String tmp = "/tmp/retrieve/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		File dir = new File(basePath + tmp);
		dir.mkdirs();
		String facePath = tmp + "/" + current + "retrieve.png";
		imageService.saveBase64image(basePath + facePath, face);
		byte[] faceTarget = faceEnginer.extractFaceFeature(basePath + facePath);

		Map<String, Object> people = findUserByCarNum(carNum);

		float value = faceEnginer.similarByFeature(faceTarget, (byte[]) people.get("FEATURE"));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("user", people);
		result.put("code", value);
		return result;
	}

	/**
	 * 通过车牌找到人员
	 * 
	 * @param carNum
	 * @return
	 * @throws FaceException
	 */
	public Map<String, Object> findUserByCarNum(String carNum) throws FaceException {

		Map<String, Object> people = faceDAO.findUserByCarNum(carNum);
		if (people == null || !people.containsKey("FEATURE")) {
			throw new FaceException(FaceException.SERVICE_ERROR, "未找到车牌信息");
		}
		return people;
	}

	/**插入失驾信息
	 * @param dateTime
	 * @param location
	 * @param imageBase64
	 * @return
	 * @throws FaceException
	 */
	public boolean insertLossDrivingInfo(String dateTime, String location, String imageBase64,String carNum) throws FaceException {
		// 创建临时目录
		String current = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String tmp = "/tmp/lossDriving/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		File dir = new File(basePath + tmp);
		dir.mkdirs();
		String facePath = tmp + "/" + current + "lossDriving.png";
		imageService.saveBase64image(basePath + facePath, imageBase64);
		byte[] faceTarget = faceEnginer.extractFaceFeature(basePath + facePath);
		
		Map<String, Object> mapinfo = new HashMap<String, Object>();
		mapinfo.put("datetime", dateTime);
		mapinfo.put("location", location);
		mapinfo.put("imagePath", facePath);
		mapinfo.put("feature", faceTarget);
		mapinfo.put("carNum", carNum);
		mapinfo.put("status", "处理");
		
		int ret = faceDAO.insertLossDrivingInfo(mapinfo);
		if(ret!=-1) {
			return true;
		}
		return false;
	}

	
	/**通过1:1找出失驾人脸信息集合
	 * @return
	 */
	public List<Map<String, Object>> findLossDrivingByCarNum() {
		List<Map<String, Object>> dirvingList = faceDAO.findAllLossDrivingInfo();
		List<Map<String, Object>> lossUserList = new ArrayList<Map<String, Object>>();
		if(dirvingList!=null) {
			for(Map<String, Object> loss : dirvingList) {
				String carNum = (String) loss.get("PLATE_NUMBER");
				Map<String, Object> userMap = faceDAO.findUserByCarNum(carNum);
				if (userMap == null || !userMap.containsKey("FEATURE")) {
					loss.put("LOSS_STATUS", "无此车辆信息");
				}
				try {
					byte[] userFeature =  (byte[]) userMap.get("FEATURE");
					float value = faceEnginer.similarByFeature((byte[])loss.get("FEATURE"), userFeature);
					userMap.put("code",value+"");
					userMap.putAll(loss);
					lossUserList.add(userMap);
				} catch (FaceException e) {
					loss.put("LOSS_STATUS", "人脸比对失败");
					log.error(e.msg(),e.code());
					continue;
				}
			}
		}
		return lossUserList;
	}

	/**通过1:N找出失驾人脸信息集合
	 * @return
	 * @throws FaceException 
	 */
	public List<Map<String, Object>> findLossDrivingBySimilar() throws FaceException {
		//找出所有失驾信息
		List<Map<String, Object>> lossUserList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dirvingList = faceDAO.findAllLossDrivingInfo();
		List<Map<String, Object>> userList = faceDAO.findAllUserInfo();
		if(dirvingList!=null) {
			for(Map<String, Object> loss : dirvingList) {
					Object obj = loss.get("FEATURE");
					if (obj != null) {
						byte[] feature = (byte[]) obj;
						Map<String,Object> userMap = getMaxSimilarUser(feature,userList);
						lossUserList.add(userMap);
				}
			}
		}
		return lossUserList;
	}
	
	
	public Map<String,Object> getMaxSimilarUser(byte[] lossFeature,List<Map<String, Object>> userList) throws FaceException{
		Map<String,Object> userMax = null;
		for(Map<String, Object> user: userList){
			Object obj = user.get("FEATURE");
			if (obj != null) {
				byte[] feature = (byte[]) obj;
				float value = faceEnginer.similarByFeature(lossFeature, feature);
				user.put("code", value);
				user.remove("FEATURE");
				if(userMax!=null) {
					if(value>(float)userMax.get("code")) {
						userMax = user;
					}
				}else {
					userMax = user;
				}
			}
		}
		return userMax;
			
	}
	
	
	/**
	 * list添加元素升序排列
	 * 
	 * @param userMap
	 * @param retrieveList
	 */
	private void AddListSortAsc(Map<String, Object> userMap, List<Map<String, Object>> retrieveList) {
		if (retrieveList.size() > 0) {
			float value = (float) retrieveList.get(retrieveList.size() - 1).get("code");
			if (value > (float) userMap.get("code")) {
				retrieveList.add(retrieveList.size() - 1, userMap);
			} else {
				retrieveList.add(userMap);
			}
		} else {
			retrieveList.add(userMap);
		}
	}

	public boolean updateLossDrivingStatus(String status) {
		int flag = faceDAO.updateLossDrivingStatus(status);
		if(flag!=-1) {
			return true;
		}
		return false;
	}

}
