/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ethink.face.bean.ImageBean;
import com.ethink.face.core.FaceEnginer;
import com.ethink.face.utils.FileSearch;
import com.ethink.face.utils.ImageComparator;

/**
 *
 * 描述: 图片处理
 * @author 
 */
@Service
public class ImageComparaService {
	
	   private static final Logger log = LoggerFactory.getLogger(ImageComparaService.class);
	   private static int data;
	   	
	   
	   /**
		 * 认证图片排序并获取相似度
		 * @param basePath
		 * @param faceEnginer
		 * @param batch
		 * 
		 */
	   public List<ImageBean> oneMatchOTher(String basePath, FaceEnginer faceEnginer, String batch){			
			data=0;			
			List<String> registerList = new ArrayList<String>();
			Map<String,	List<String>> map = new HashMap<String,List<String>>();		
			FileSearch.findFiles(basePath+"/image"+"//"+batch, "*3.jpg",registerList,true,""); 
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
			   List<ImageBean> sortRs = imageDeal(map,faceEnginer);
			   return sortRs;	
		}
		
		
	   
	   
	   
	   /**
		 * 认证图片排序并获取相似度
		 * @param map
		 * @param faceEnginer		
		 * 
		 */
	   public  List<ImageBean> imageDeal(Map<String,List<String>> map,FaceEnginer faceEnginer){
		    //相似度
		    float value=0 ;
		    //数据集合(已排序)
			List<ImageBean> sortRs = new ArrayList<ImageBean>();
			//数据集合(未排序)
			List<ImageBean> unsortRs = new ArrayList<ImageBean>();					
			for (String imagePath : map.keySet()) {	      
	             List<String> list = map.get(imagePath);
		         for (int j = 0; j < list.size(); j++) {		       
			        try {				        	
			              value= faceEnginer.similar(imagePath, list.get(j));		        			        		        	 		      					
					 } catch (Throwable e) {
						  log.info("识别出错"+e.getMessage());						
					 }	
			         if (value>0) {
			        	 data+=value*10000;
					  }			        
		        	 ImageBean imageBean = new ImageBean();         
		 	         imageBean.setImagOne("/api/showImage"+imagePath.substring(9, imagePath.lastIndexOf(".")));
		 	         imageBean.setImagTwo("/api/showImage"+list.get(j).substring(9, list.get(j).lastIndexOf(".")));
		 	         imageBean.setSimilar(value);
		 	         unsortRs.add(imageBean);
				  }	        	        
		    }
			
			Set<ImageBean> imageSet = new TreeSet<ImageBean>(new ImageComparator());			
			for (int i = 0; i < unsortRs.size(); i++) {
		    	 imageSet.add(unsortRs.get(i));
			}   		  
			for (Iterator<ImageBean> iterator = imageSet.iterator(); iterator.hasNext();) {
				 ImageBean imageBean = (ImageBean) iterator.next();
				 sortRs.add(imageBean);
			}			  
			  
		return sortRs;
					   
       }  
	   
	   
  
	   //获取相似度总值，便于求均值
	   public int getAvrage(){	
		   
		    return data;		   
	   }
}
