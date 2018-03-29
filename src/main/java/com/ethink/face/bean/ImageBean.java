package com.ethink.face.bean;

import java.io.Serializable;

public class ImageBean implements Serializable  {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//图片1
	private String imagOne;
	//图片2
	private String imagTwo;
	//相似度
	private float similar;

	
//	public ImageBean(String string, String string2, String string3, double dds) {
//		this.phone=string;
//		this.imagOne=string2;
//		this.imagTwo=string3;
//		this.similar=dds;
//	}
	
	public String getImagOne() {
		return imagOne;
	}
	public void setImagOne(String imagOne) {
		this.imagOne = imagOne;
	}
	public String getImagTwo() {
		return imagTwo;
	}
	public void setImagTwo(String imagTwo) {
		this.imagTwo = imagTwo;
	}
	public double getSimilar() {
		return similar;
	}
	public void setSimilar(float similar) {
		this.similar = similar;
	}
	
	@Override
	public String toString() {
		return "ImageBean [ imagOne=" + imagOne + ", imagTwo=" + imagTwo + ", similar=" + similar
				+ "]";
	}
	
	
}
