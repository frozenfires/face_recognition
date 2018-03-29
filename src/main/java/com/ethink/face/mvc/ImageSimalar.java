/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;
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
import com.ethink.face.bean.ImageBean;
import com.ethink.face.bean.Result;
import com.ethink.face.core.FaceEnginer;
import com.ethink.face.service.ImageComparaService;
import com.ethink.face.utils.FileSearch;
/**
 *
 * 描述:图片相似度匹配
 * @author 
 */
@Controller
public class ImageSimalar {
		@Autowired
		private FaceEnginer faceEnginer;
		
		@Value("${app.basePath}")
		private String basePath;
		
		private static final Logger log = LoggerFactory.getLogger(ImageSimalar.class);
					
		/**
		 * 获取图片批次
		 * @param 
		 * @param 
		 */
		@ResponseBody
		@RequestMapping(value="/api/compara" , produces= {MediaType.APPLICATION_JSON_VALUE})	
		public Object compara() {				
			List<String> filenames=null;
			try {
				//获取当前图片目录
			    filenames=new FileSearch().getFiles(basePath+"/image");
			} catch (Exception e1) {
			    log.error("图片批次获取失败");
			} 
			Body<String, Object> body =Body.getBody();	
			
			return Result.build("0",body.add("filenames", filenames));
		    
		}
	
	
		/**
		 * 根据批次获取人证相似度
		 * @param 
		 * @param 
		 */
		@ResponseBody
		@RequestMapping(value="/api/imageMatch" , produces= {MediaType.APPLICATION_JSON_VALUE})
		public Object  imageMatch(String batch ) {
			ImageComparaService  ImageService= new ImageComparaService();		
			List<ImageBean> list = null;	
			if (batch.trim()=="全部批次"||"全部批次".equals(batch.trim())) {
				batch="";  
			}
			float avrage = 0;
			try {			
				 list = ImageService.oneMatchOTher(basePath,faceEnginer,batch);	      						 
			} catch (Exception e) {
				 log.error("图片比对失败！");
			}
			int data = ImageService.getAvrage();	   
			avrage = (float)data/(10000*list.size());	
			Body<String, Object> body =Body.getBody();			
			if (!list.isEmpty()&&list.size()>0) {						
				 body.add("matchData", list)			 
				     .add("maxData", list.get(0).getSimilar())    
				     .add("minData", list.get(list.size()-1).getSimilar())
				     .add("avrageData", avrage);	
			}
			 return Result.build("0",body);
		     	   		
		}
		
	
}
