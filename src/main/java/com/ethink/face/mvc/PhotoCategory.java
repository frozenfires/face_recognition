/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ethink.face.bean.Body;
import com.ethink.face.bean.Result;
import com.ethink.face.service.FaceLibBuilder;
import com.ethink.face.service.ImageDeal;
import com.ethink.face.utils.FileSearch;

/**
 * 描述:
 * @author 图片标准化处理
 */
@Controller
public class PhotoCategory {
	
	private static final Logger log = LoggerFactory.getLogger(PhotoCategory.class);
	@Value("${app.basePath}")
	private String basePath;
	
	 @Autowired
	 FaceLibBuilder faceLibBuilder;
	 
	 @Autowired
	 ImageDeal imageDeal;
	 
	 @ResponseBody
	 @RequestMapping(value="/api/photoDeal" , produces= {MediaType.APPLICATION_JSON_VALUE})	
	 public Object PhotoDeal (){		  		   
		    boolean  flag=  imageDeal.deal(basePath);
			List<String> filenames = null;
		    Body<String, Object> body =Body.getBody();	
		    if (flag) {	
		         try {
				     filenames=new FileSearch().getFiles(basePath+"/image");
				} catch (Exception e) {
					body.add("filenames",null);
				}
		        body.add("filenames",filenames);
		    	body.add("code", 0);
			}else{				
				body.add("code", -1);	
		    	log.info("图片标准化处理失败！___________________");
			}
		  
		    return Result.build("0",body);
	 }
	 			     	   		
	 
	 @ResponseBody
	 @RequestMapping(value="/api/deal" , produces= {MediaType.APPLICATION_JSON_VALUE})	
	 public Object deal(String batch ){
 
			boolean flag=false;
		    Body<String, Object> body =Body.getBody();	
	        String	 filePath= basePath+"//image//"+batch;				       
	        long listFiles =  new File(filePath).length();  
	        if(listFiles > 30){  
	        //文件夹下有文件  
	        	faceLibBuilder.build(filePath);
	        	flag = new FileSearch().changeNane(basePath+"//facelib");
	        } else {
	        	 body.add("code", -1);	
	        	 return Result.build("0",body);
	          //文件夹下没有文件  
	        }  
			if (flag) {
				body.add("code", 0);	
			}else{
				 body.add("code", -1);	
				 
			    log.info("图片标准化处理失败！___________________");		
			}		  
		    return Result.build("0",body);
	  }
  	 			    
  }
