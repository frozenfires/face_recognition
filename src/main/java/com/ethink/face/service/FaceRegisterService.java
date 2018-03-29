/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.service;

import java.io.File;
//import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ethink.face.FaceException;
import com.ethink.face.core.FaceEnginer;
import com.ethink.face.db.FaceDAO;
import com.ethink.face.utils.FileSearch;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
@Service
public class FaceRegisterService {
	
	private static final Logger log = LoggerFactory.getLogger(FaceRegisterService.class);
	
	@Value("${app.basePath}")
	private String basePath;
	
	@Autowired
	private ImageService imageService;
	@Autowired
	private FaceDAO register;
	@Autowired
	private FaceEnginer faceEnginer;

	/**
	 * 根据faceId查找人脸注册信息  无用！
	 * @param faceId 人脸唯一标识
	 * @return
	 */
	public File findById(String faceId){
		File fileA = new File(basePath + "/register/" + faceId + ".png");
		if(!fileA.exists()) {
			return null;
		}else {
			return fileA;
		}
	}

	/**
	 * 人脸信息注册
	 * @param faceId
	 * @param imageB64
	 * @throws FaceException 
	 */
	public void register(String faceId, String imageB64, String imgType) throws FaceException {
		//创建路径
		String dir = basePath + "/register";
		File facedir = new File(dir);
		if(!facedir.exists()) {
			facedir.mkdirs();
		}
		//照片存储
		imgType = selectType(imgType);
		String imagePath = basePath + "/register/" + faceId + imgType;
		imageService.saveBase64image(imagePath,  imageB64);
		//注册信息存储
		List<Map<String,Object>> regInfoList = new ArrayList<Map<String,Object>>();
		String savePath = "/register/" + faceId + imgType;
		Map<String, Object> map = new HashMap<>();
		map.put("USERID", faceId);
		map.put("IMAGEPATH", savePath);
		map.put("DATETIME", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		//人脸特征识别
		byte[] featrue;
		try {
			featrue = faceEnginer.extractFaceFeature(imagePath);//人脸特征入库
		} catch (FaceException e) {
			//throw new FaceException(FaceException.SERVICE_ERROR, "人脸照片不清晰");
			new File(imagePath).delete();
			throw  e;
		}
		map.put("FEATURE", featrue);
		regInfoList.add(map);
		register.insertRegisterInfo(regInfoList);//信息注册
	}
	
	/**
	 * 查询指定用户信息
	 * @param faceId
	 */
	public List<Map<String, String>> selectUserId(String faceId){
		List<String> list = new ArrayList<String>();
		list.add(faceId);
		return register.selectUserId(list);
	}
	
	/**
	 * 判断图片格式
	 * @param faceId
	 */
	public String selectType(String imgType) {
		if(imgType.equals("image/jpeg") ) {
			imgType = ".jpg";
		}else if(imgType.equals("image/png")){
			imgType = ".png";
		}else if(imgType.equals("image/bmp")){
			imgType = ".bmp";
		}else {
			imgType = ".png";
		}
		return imgType;
	} 
	
	/**
	 * 人脸删除
	 * @param faceId
	 * @param imageB64
	 */
	public void deleteUser(String faceId, String type){
		//存储照片删除
		String boot = basePath + "/register";
		String path = boot+"/"+faceId+type;
		File[] files = new File(boot).listFiles();
		for(int i=0; i<files.length; i++) {
			String item = files[i].getName();
			item = item.substring(0,item.indexOf("."));
			if(item.equals(faceId)){
				new File(path).delete();
				break;
			}
		} 
		//注册信息删除
		register.deleteUser(faceId);
	}
	
	/**
	 * 修改指定id的注册信息
	 * @param faceId 用户名
	 * @param imageB64 
	 * @param imgType 修改后的文件格式
	 * @param oldImgType 修改前的文件格式
	 * @throws FaceException
	 */
	
	public void updateUser(String faceId, String imageB64, String imgType,String oldImgType) throws FaceException {
		imgType = selectType(imgType);
		String imagePath = basePath + "/register/" + faceId + imgType;
		String oldImagePath = basePath + "/register/" + faceId + oldImgType;
		String savepath = "/register/" + faceId + imgType;
		//照片信息替换
		new File(oldImagePath).delete();
		imageService.saveBase64image(imagePath,  imageB64);
		byte[] featrue = null;
		//注册信息存储
		Map map = new HashMap();
		map.put("USERID", faceId);
		map.put("IMAGEPATH", savepath);
		try {	
			featrue = faceEnginer.extractFaceFeature(imagePath);
		}catch (Exception e) {
			//throw new FaceException(FaceException.SERVICE_ERROR, "人脸照片不清晰");
			throw  e;
		}
		map.put("FEATURE", featrue);
		register.updateUser(map);
	}
	/**
	 * 批处理注册
	 * @throws Exception
	 */
	
	public List<Map<String,String>> batchRegister() {
		List<Map<String,Object>> regInfoList = new ArrayList<Map<String,Object>>();
		List<Map<String,String>> errorList = new ArrayList<Map<String,String>>();
		//获取处理过的批量出来信息
		List<Map<String,String>> handlePhotoInfo = getHandlePhotoInfo(errorList);
		if(handlePhotoInfo.size()<=0) {
			errorList.clear();
			Map<String, String> map = new HashMap<>();
			map.put("code", "-1");
			map.put("userid", "");
			map.put("message", "请勿重复批量注册");
			errorList.add(map);
			return errorList;
		}
		//创建目录
		String dir = basePath + "/register";
		File facedir = new File(dir);
		if(!facedir.exists()) {
			facedir.mkdirs();
		}
		//循环录入注册信息
		for(int i= 0;i<handlePhotoInfo.size();i++) {
			String name = handlePhotoInfo.get(i).get("name");//用户名
			String photoPath = handlePhotoInfo.get(i).get("photoPath");//原地址
			int startSign = photoPath.lastIndexOf(".");
			String imgType = photoPath.substring(startSign,photoPath.length());//后缀
			String imagePath = basePath + "/register/" + name + imgType;//存储地址
			try {
			register1(name,photoPath,imagePath,regInfoList);
			}catch (FaceException e) {
				Map<String, String> map = new HashMap<>();
				map.put("code", "-3");
				map.put("userid", name);
				map.put("message", e.getMessage());
				errorList.add(map);
				continue;
			}
		}
		//批量注册
//		regInfoList.clear();
//		Map<String, Object> map1 = new HashMap<>();
//		map1.put("USERID", "-3");
//		map1.put("DATETIME", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//		regInfoList.add(map1);
		System.out.println("regInfoList>>>>");
		System.out.println(regInfoList);
		if(regInfoList.size()>0) {
			register.insertRegisterInfo(regInfoList);//信息注册
		}
		return errorList;
	}
	
	public void register1(String name, String path1,String path2,
			List<Map<String,Object>> regInfoList) throws FaceException{
		//注册信息存储
		int endSign = path2.indexOf("/register/");
		String savePath = path2.substring(endSign,path2.length());
		Map<String, Object> map = new HashMap<>();
		map.put("USERID", name);
		map.put("IMAGEPATH", savePath);
		map.put("DATETIME", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		//map.put("DATETIME",new Date());
		
		//人脸特征识别
		byte[] featrue;
		try {
			featrue = faceEnginer.extractFaceFeature(path1);//人脸特征入库
		} catch (FaceException e) {
			//throw new FaceException(FaceException.SERVICE_ERROR, "人脸照片不清晰");
			throw  e;
		}
		//照片存储
		imageService.copyFile(path1,  path2);
		map.put("FEATURE", featrue);
		regInfoList.add(map);
		//register.insertRegisterInfo(map);//信息注册
	}
	
	//获取批量注册信息
	public List getPhotoInfo() {
		List<String> list = new ArrayList<String>();
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		FileSearch.findFiles(basePath+"/facelib", "base_*",list,true,""); 
		
		int endSign = list.get(0).lastIndexOf("\\");
		int start = list.get(0).substring(0,endSign).lastIndexOf("\\");
		for(int i = 0;i<list.size();i++) {
			Map<String,String> map = new HashMap<String,String>();
		    String name = list.get(i).substring(start+1,endSign);
	        String photoPath = list.get(i);
	        map.put("name", name);
	        map.put("photoPath", photoPath);
	        resultList.add(map);
		}
		return resultList;
	}
	
	//处理批量注册数据
		public List getHandlePhotoInfo(List<Map<String,String>> errorList){

			List<Map<String,String>> photoInfoList = getPhotoInfo();//用户id和地址
			List<String> photoList = new ArrayList<String>();//批处理id
			   for (Map<String, String> map : photoInfoList) {
				   photoList.add(map.get("name"));
		        }
			   
			List<Map<String, String>> registerInfoList =register.selectUserId(photoList);//查询已注册的信息
			List<String> registerList = new ArrayList<String>();//已注册的id
			for (Map<String, String> map : registerInfoList) {
				registerList.add(map.get("USERID"));
	        }
			//删除已注册信息
			 Iterator<Map<String,String>> it = photoInfoList.iterator();
				 while(it.hasNext()) {
					  String name =  it.next().get("name");
						 if(registerList.contains(name)){
							    Map<String, String> map = new HashMap<>();
							    map.put("code", "-2");
								map.put("userid", name);
								map.put("message", "用户id已经存在");
								errorList.add(map);
								log.info("[%s]用户id已存在........."+name);
								it.remove(); 
						 }
				 }
			return photoInfoList;
		}
	

}
