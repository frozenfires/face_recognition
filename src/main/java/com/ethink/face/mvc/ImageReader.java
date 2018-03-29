/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
@Controller
public class ImageReader {
	
	@Value("${app.basePath}")
	private String basePath;

	/**
	 * 请求指定路径的图片
	 * @param res.jpg
	 * @param imgPath
	 * @param imgName
	 */
	@RequestMapping(value="/faceimg/{imgPath}/{imgName}")
	public void fimg(HttpServletResponse res,
			@PathVariable("imgPath") String imgPath, 
			@PathVariable("imgName") String imgName) {
		
		File file = new File(basePath + "\\" + imgPath + "/" + imgName + ".png");
		try {
			IOUtils.copy(new FileInputStream(file), res.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 请求注册人脸图片
	 * @param res
	 * @param imgName 
	 */
	@RequestMapping(value="/api/regimg/{imgName}")
	public void regimg(HttpServletResponse res,
			@PathVariable("imgName") String imgName){
		File file = new File(basePath + "\\register\\" + imgName);
		try {
			IOUtils.copy(new FileInputStream(file), res.getOutputStream());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 请求注册人脸图片
	 * @param filePath
	 * @param createDtae
	 * @param imgPath
	 * @param imgName
	 */
	@RequestMapping(value="/api/showImage/{filePath}/{createDtae}/{imgPath}/{imgName}")
	public void showImage(HttpServletResponse res,
			@PathVariable("imgPath") String imgPath, 
			@PathVariable("createDtae") String createDtae, 			
			@PathVariable("filePath") String filePath, 
			@PathVariable("imgName") String imgName) {
		
		File file = new File(basePath + "\\" + filePath + "\\" + createDtae + "\\"+imgPath+ "\\" + imgName + ".jpg");
		try {
			IOUtils.copy(new FileInputStream(file), res.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/api/getImage/**")
	public void retrieveImg(HttpServletRequest req,HttpServletResponse res) {
		String imagePath = req.getRequestURI();
		imagePath = imagePath.substring(imagePath.indexOf("getImage/")+8);
		File file = new File(basePath+ imagePath);
		try {
			IOUtils.copy(new FileInputStream(file), res.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
