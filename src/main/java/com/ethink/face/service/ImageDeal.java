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
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ethink.face.utils.FileSearch;

/**
 *
 * 描述: 图片分类处理
 * @author wangjing.dc@qq.com
 */
@Service
public class ImageDeal {
	
	private static final Logger log = LoggerFactory.getLogger(ImageDeal.class);

	/**
	 * 匹配查找图片文件
	 * @param basePath 
	 * 					    
	 */
	public boolean deal (String basePath){
		boolean flag=false;	
		List<String> registerList = new ArrayList<String>();
		Map<String,	List<String>> map = new HashMap<String,List<String>>();			
		FileSearch.findFiles(basePath+"/photoTmp", "*3.jpg",registerList,true,""); 
		int  choosePath=0;		
		for (int i = 0; i < registerList.size(); i++) {	
			choosePath=registerList.get(i).lastIndexOf("\\")+1;
			List<String> resultLists = new ArrayList<String>();					
			FileSearch.findFiles(registerList.get(i).substring(0,choosePath ),registerList.get(i).
					             substring(choosePath, registerList.get(i).lastIndexOf(".")-1)+"*2.jpg",resultLists,true,"");			
			if (!resultLists.isEmpty()) {
				map.put(registerList.get(i), resultLists);
			}				
		}
		
		if (!map.isEmpty()) {
			flag= photoSave(map,basePath);
			log.info("图片标准化处理完成_______________________________________");	
			return flag;
		}
		return flag;
					
	 }
	
	
	/**
	 * 图片标准化处理
	 * @param map 
	 * @param basePath 
	 */
	public boolean photoSave(Map<String,List<String>> map,String basePath ){
		String filePath=null;
		String path=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String fileName = formatter.format(new Date());
		for (String imagePath : map.keySet()) {	      
			 path= imagePath.substring(imagePath.lastIndexOf("\\")+1, imagePath.lastIndexOf(".")-1)  ;  		                  			        		        	 		      					
			 filePath= basePath+"//image//"+fileName.replaceAll("-", "_")+"_postBack"+"//"+path;			
			 boolean flag = new FileSearch().mkDirectory(filePath);
			 if (flag) {							
	               List<String> list = map.get(imagePath);
			       for (int j = 0; j < list.size(); j++) {		       		       			        	
			           try {
			        	    new FileSearch().copy(new File(list.get(j)),new File(filePath));
			     	   } catch (Exception e) {
			     			log.info("图片复制错误"+e.getMessage());
			     			return false;}				     		
				   }
		         
		           try {
		        	   new FileSearch().copy(new File(imagePath),new File(filePath));
		 		   } catch (Exception e) {
		 			   log.info("图片复制错误"+e.getMessage());
		 			   return false;}  
			 }else{
				  log.info("文件夹创建失败————————————————————————————————————————");
	     		  return false;
			 }
	     }		
		return true;		
	 }
	
	
	
 }
	
	
	
	
	

