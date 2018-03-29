/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.bean;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
public class FaceValue {

	private String phone;
	private String date;
	private String imageA;
	private String imageB;
	private String value;
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the imageA
	 */
	public String getImageA() {
		return imageA;
	}
	/**
	 * @param imageA the imageA to set
	 */
	public void setImageA(String imageA) {
		this.imageA = imageA;
	}
	/**
	 * @return the imageB
	 */
	public String getImageB() {
		return imageB;
	}
	/**
	 * @param imageB the imageB to set
	 */
	public void setImageB(String imageB) {
		this.imageB = imageB;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("phone=").append(phone).append(",")
			.append("value=").append(value).append(",")
			.append("imageA=").append(imageA).append(",")
			.append("imageB=").append(imageB).append(",")
			.append("date=").append(date);
		
		return sb.toString();
	}
}
