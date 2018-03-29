/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ethink.face.db.FaceDAO;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
@RestController
public class DemoController {

	@Autowired
	private FaceDAO faceDAO;
	
	@RequestMapping(value="test")
	public void test() {
		System.out.println(faceDAO.findById("sdf"));
	}
}
