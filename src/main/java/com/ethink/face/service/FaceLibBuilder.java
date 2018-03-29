/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ethink.face.FaceException;
import com.ethink.face.core.FaceEnginer;

/**
 *
 * 描述: 批量生成人脸库建库照片
 * @author wangjing.dc@qq.com
 */
@Service
public class FaceLibBuilder {
	
	private static class Onepair{
		private String id;
		private File base;
		private File live;
		private byte[] feature;
		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return the base
		 */
		public File getBase() {
			return base;
		}
		/**
		 * @param base the base to set
		 */
		public void setBase(File base) {
			this.base = base;
		}
		/**
		 * @return the live
		 */
		public File getLive() {
			return live;
		}
		/**
		 * @param live the live to set
		 */
		public void setLive(File live) {
			this.live = live;
		}
		/**
		 * @return the feature
		 */
		public byte[] getFeature() {
			return feature;
		}
		/**
		 * @param feature the feature to set
		 */
		public void setFeature(byte[] feature) {
			this.feature = feature;
		}
		
	}
	
	private static final Logger log = LoggerFactory.getLogger(FaceLibBuilder.class);
	
	@Value("${app.basePath}")
	private String basePath;
	@Autowired
	private FaceEnginer faceEnginer;
	
	/**
	 * 从给定源文件中生成人脸库所需文件
	 *       将属于同一人的人脸照片分检到一个文件夹
	 * @param path
	 */
	public void build(String path) {
		File dir = new File(path);
		File[] pair = dir.listFiles();
		Map<String, List<File>> rightFiles = new HashMap<>();
		
		long start = System.currentTimeMillis();
		List<Onepair> ff = countFacef(pair);
		log.debug("批量生成"+pair.length+"张人脸特征耗时" + (System.currentTimeMillis() - start));
		
		List<Onepair> leftFiles = pairFace(ff, rightFiles);
		if(leftFiles.size() == 1) {
			List<File> right = new ArrayList<>();
			right.add(leftFiles.get(0).getBase());
			right.add(leftFiles.get(0).getLive());
			rightFiles.put(leftFiles.get(0).getId(), right);
		}else {
			while(leftFiles.size() > 1) {
				leftFiles = pairFace(leftFiles, rightFiles);
			}
		}
		
		// 移动文件
		for(String userId : rightFiles.keySet()) {
			File userDir = new File(basePath + "/facelib"  + "/" + userId);
			userDir.mkdirs();
			
			for(File sourceImg : rightFiles.get(userId)) {
				try {
					FileUtils.copyFileToDirectory(sourceImg, userDir);
				}catch(Exception e) {
					log.error(null, e);
				}
			}
		}
		
		log.debug("人脸库照片构建总耗时=" + (System.currentTimeMillis() - start));
	}

	/**
	 * 批量计算人脸特征
	 * @param pair
	 * @return
	 */
	private List<Onepair> countFacef(File[] faces) {
		List<Onepair> ret = new ArrayList<>();
		
		
		for(int i=0; i<faces.length; i++) {
			Onepair one = new Onepair();
			one.setId(faces[i].getName().split("\\.")[0]);
			one.setBase(findBaseFile(faces[i]));
			one.setLive(findLiveFile(faces[i]));
			
			try {
				byte[] feature = faceEnginer.extractFaceFeature(findBaseFile(faces[i]).getAbsolutePath());
				one.setFeature(feature);
			} catch (FaceException e) {
				log.error(null, e);
				one.setFeature(null);
			}
			
			ret.add(one);
		}
		
		return ret;
	}

	/**
	 * 将给定人脸集合分类成左右两部分, 按照相似度分类，相似度大于90%的分到右侧，其他的左侧
	 * @param faces 待分类的文件数组
	 * @param rightFiles 右侧部分  
	 * @return 左侧部分
	 */
	private List<Onepair> pairFace(List<Onepair> faces, Map<String, List<File>> rightFiles) {
		List<Onepair> leftFiles = new ArrayList<>();
		List<File> rightFile = new ArrayList<>();
		Onepair source = faces.get(0);
		File sourceFile = faces.get(0).getBase();
		
		// 保存联网核查照片
		rightFile.add(sourceFile);
		rightFile.add(faces.get(0).getLive());
		rightFiles.put( faces.get(0).getId(), rightFile);
		
		long start = System.currentTimeMillis();
		int count = 0;
		for(int i=1; i<faces.size(); i++) {
			Onepair current = faces.get(i);
			// 如果没有特征值，无需处理，直接过滤出leftFiles
			if(source.getFeature() == null) {
				leftFiles.add(current);
				continue;
			}
			
			try {
				if(current.getFeature() != null) {
					float value = faceEnginer.similarByFeature(source.getFeature(), current.getFeature());
					count ++;
					
					if(value > 0.9) {
						rightFile.add(current.getLive());
					}else {
						leftFiles.add(current);
					}
				}else {
					leftFiles.add(current);
				}
			} catch (FaceException e) {
				leftFiles.add(current);
			}
		}
		
		log.debug("进行"+count+"次人脸特征比对耗时" + (System.currentTimeMillis() - start));
		return leftFiles;
	}

	/**
	 * 从给定目录中查找现场拍摄照片
	 * @param path
	 * @return
	 */
	private File findLiveFile(File path) {
		File[] files = path.listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].getName().indexOf("photo3") > -1) {
				
			}else {
				return files[i];
			}
		}
		return null;
	}

	/**
	 * 从给定目录中查找联网核查照片
	 * @param file
	 * @return
	 */
	private File findBaseFile(File path) {
		File[] files = path.listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].getName().indexOf("photo3") > -1) {
				return files[i];
			}else {
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		System.out.println("00100048photo3.jpg".split("\\.").length);
		
		FaceEnginer faceEnginer = new FaceEnginer();
		faceEnginer.initEnginer();
		
		FaceLibBuilder fb = new FaceLibBuilder();
		fb.basePath = "d:/etface";
		fb.faceEnginer = faceEnginer;
		
		fb.build("D:\\etface\\image\\2018_03_20_postBack");
	}
	
}
