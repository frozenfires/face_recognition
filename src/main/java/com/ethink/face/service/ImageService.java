/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * 描述: 图片处理工具
 * @author wangjing.dc@qq.com
 */
@Service
public class ImageService {
	
	private static final Logger log = LoggerFactory.getLogger(ImageService.class);

	/**
	 * Base64保存为图片文件
	 * @param path
	 * @param base64
	 * @return
	 */
	public int saveBase64image(String path, String base64) {
		
		try {
			byte[] img = new sun.misc.BASE64Decoder().decodeBuffer(base64);
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			
			FileOutputStream fo = new FileOutputStream(file);
			fo.write(img);
			fo.flush();
			fo.close();
			
		} catch (IOException e) {
			log.error(null, e);
			return -1;
		}
		
		return 0;
	}
	/**
	 * 图片的复制
	 * @param 图片原路径
	 * @param 图片要复制到的路径
	 * @return
	 */
	
	public int copyFile(String srcPath, String destPath) {
		try {
			// 打开输入流
	        FileInputStream fis = new FileInputStream(srcPath);
	        // 打开输出流
	        FileOutputStream fos = new FileOutputStream(destPath);
	        
	        // 读取和写入信息
	        int len = 0;
	        // 创建一个字节数组，当做缓冲区
	        byte[] b = new byte[1024];
	        while ((len = fis.read(b)) != -1) {
	            fos.write(b, 0, len);
	        }
	        
	        // 关闭流  先开后关  后开先关
	        fos.close(); // 后开先关
	        fis.close(); // 先开后关
		}catch(IOException e) {
			log.error(null, e);
			return -1;
		}
		
		return 0;
		
	}
	
	
	
	
	

}
