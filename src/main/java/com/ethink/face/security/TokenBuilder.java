/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.security;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * 描述:
 * @author wangjing.dc@qq.com
 */
public class TokenBuilder {

	private static final Logger log = LoggerFactory.getLogger(TokenBuilder.class);
	
	private static final Base64.Decoder decoder = Base64.getDecoder();
	private static final String datePattern = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 解析token
	 * @param tokenstr
	 * @return
	 */
	public static FaceToken parseToken(String tokenstr) {
		
		String suc = null;
		String[] scuArray;
		try {
			suc = new String(decoder.decode(tokenstr), Charset.forName("UTF-8"));
			scuArray = suc.split("\\*\\*\\*");
		}catch(Exception e) {
			log.info("token格式错误", e);
			return null;
		}
		
		if(scuArray.length < 3) {
			log.info("token参数个数错误");
			return null;
		}
		
		List<String> arrList = new LinkedList<String>(Arrays.asList(scuArray));
		if(arrList.get(0).equals("rdp")) {
			arrList.add(1, arrList.get(0) + "systemid");
		}
//		log.debug(arrList.toString());
		
		FaceToken token = new FaceToken();
		token.setAppName(arrList.get(0));		
		token.setUserId(arrList.get(1));
		token.setSdkey(tokenstr);
		token.setUpdateTime(arrList.get(3));
		
		FaceSdkey sdkeyObject =parseSdkey(arrList.get(2));
		token.setId(sdkeyObject.getTokenId());
//		log.debug(sdkeyObject.toString());
		
		return token;
	}
	

	public static FaceToken parseToken(Map<String, Object> map) {
		FaceToken token = new FaceToken();
		
		token.setId((String) map.get("ID"));
		token.setSdkey((String) map.get("SDKEY"));
		token.setStatus((String) map.get("STATUS"));
		token.setUpdateTime((String) map.get("UPDATETIME"));
		token.setUserId((String) map.get("USERID"));
		
		return token;
	}
	
	/**
	 * 解析sdkey对象
	 * @param skey
	 * @return
	 */
	public static FaceSdkey parseSdkey(String skey) {

		ObjectMapper mapper = new ObjectMapper();		
		try {
			FaceSdkey sdkeyObject = mapper.readValue(skey.getBytes(), FaceSdkey.class);
			return sdkeyObject;
		} catch (Exception e) {
			log.info("解析sdkey对象错误, 构建特殊FaceSdkey对象");
			FaceSdkey sdkeyObject = new FaceSdkey();
			sdkeyObject.setTokenId(skey);
			sdkeyObject.setEndTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
			return sdkeyObject;
		}
		
	}
	
	/**
	 * 将token对象转换为Map
	 * @param token
	 * @return
	 */
	public static Map<String, String> toMap(FaceToken token) {
		Map<String, String> map = new HashMap<>();
		map.put("ID", token.getId());
		map.put("APPNAME", token.getAppName());
		map.put("SDKEY", token.getSdkey());
		map.put("STATUS", token.getStatus());
		map.put("UPDATETIME", token.getUpdateTime());
		map.put("USERID", token.getUserId());
		
		return map;
	}


	/**
	 * 构建一个新sdkey
	 * @param userId
	 * @return
	 */
	public static String newSdkey(String tokenId) {
		FaceSdkey sdkey = new FaceSdkey();
		sdkey.setTokenId(tokenId);
		sdkey.setEndTime("2020-12-12");
		
		try {
			return new ObjectMapper().writeValueAsString(sdkey);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	

	public static void main(String[] args) {
		TokenBuilder.parseToken("cmRwKioqZXlKcGMzTWlPaUpoZFhSb01DSjkqKioyMDE4LTAzLTIy");
	}

}
