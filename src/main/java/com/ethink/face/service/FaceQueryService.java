/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ethink.face.bean.FaceValue;
import com.ethink.face.db.FaceDAO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
@Service
public class FaceQueryService {

	@Value("${app.basePath}")
	private String basePath;
	
	@Autowired
	private FaceDAO FaceQuery;
	
	/**
	 * 查询当前页的注册用户
	 * @return
	 */
	public PageInfo<Map<String,Object>> listRegister(String userid,String dataTime1,String dataTime2,
			String start,String pageCount) {
	    int pageNum = Integer.parseInt(start)/Integer.parseInt(pageCount)+1;
        
        
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USERID", userid);
		map.put("DATETIME1", dataTime1);
		map.put("DATETIME2", dataTime2);
		PageHelper.startPage(pageNum, Integer.parseInt(pageCount));
		List<Map<String,Object>> queryList = FaceQuery.selectUserInfo(map);
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(queryList);
		return pageInfo;
	}

	/**
	 * 查询所有人脸比对结果
	 * @return
	 */
	public Map<String, List<FaceValue>> listResult() {
		Map<String, List<FaceValue>> result = new HashMap<>();
		
		String boot = basePath + "\\tmp";
		File[] files = new File(boot).listFiles();
		for(int i=0; i<files.length; i++) {
			FaceValue faceValue = getFaceValue(files[i]);
			
			if(! result.containsKey(faceValue.getPhone())) {
				result.put(faceValue.getPhone(), new ArrayList<FaceValue>());
			}
			List<FaceValue> phoneList = result.get(faceValue.getPhone());
			phoneList.add(faceValue);
		}
		return result;
	}
	
	
	/**
	 * 从文件中读取人脸比对信息
	 * @param file
	 * @return
	 */
	private FaceValue getFaceValue(File file) {
		FaceValue fv = new FaceValue();
		
		String fileName = file.getName();
		String[] strarr = fileName.split("_");
		fv.setPhone(strarr[0]);
		fv.setDate(strarr[1]);
		fv.setImageA("http://192.168.4.23:8080/faceimg/" + fileName + "/" + strarr[0] + "A.png");
		fv.setImageB("http://192.168.4.23:8080/faceimg/" + fileName + "/" + strarr[0] + "B.png");
		
		File[] files = file.listFiles();
		for(int i=0; i<files.length; i++) {
			String name = files[i].getName();
			if(name.endsWith(".txt")) {
				String value = name.substring(0, name.indexOf(".txt"));
				fv.setValue(value);
				break;
			}
		}
		
		return fv;
	}



	



	
	
	

}
