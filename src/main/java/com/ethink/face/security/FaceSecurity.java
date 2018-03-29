/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.security;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ethink.face.FaceException;
import com.ethink.face.db.SystemDAO;

/**
 *
 * 描述: 安全授权管理
 * @author wangjing.dc@qq.com
 */
@Component
public class FaceSecurity {
	
	private static final Logger log = LoggerFactory.getLogger(FaceSecurity.class);
	private static final String datePattern = "yyyy-MM-dd HH:mm:ss";
	
	private final Base64.Decoder decoder = Base64.getDecoder();
	private final Base64.Encoder encoder = Base64.getEncoder();
	
	@Value("${app.security.rdp}")
	private String rdp_sdkey;
	
	@Autowired
	private SystemDAO systemDao;
	
	/**
	 * 校验taoken合法性
	 * @param token
	 * @return
	 */
	public boolean auth(String token) {
		log.debug("token=" + token);
		FaceToken authToken = TokenBuilder.parseToken(token);
		if(authToken == null) {
			return false;
		}
		
		if(LocalDate.parse(authToken.getUpdateTime()).compareTo(LocalDate.now()) != 0) {
			log.info("token过期");
			return false;
		}
		
		if(authToken.getAppName().equals("rdp")) {
			return authRdp(authToken);
		}else {
			return authCommonApp(authToken);
		}
		
	}
	
	/**
	 * 验证普通app token鉴权
	 * @param userId
	 * @param sdkey
	 * @return
	 */
	private boolean authCommonApp(FaceToken atoken) {
		FaceToken secInfo = wrapFaceToken(systemDao.selectTokenById(atoken.getId()));
		if(secInfo == null) {
			log.info("token信息不存在");
			return false;
		}else {
			String dbSdkey = secInfo.getSdkey();
			String dbStatus = secInfo.getStatus();
			
			if(! dbSdkey.equals(atoken.getSdkey())) {
				log.info("非法token");
				return false;
			}
			if(! dbStatus.equals("active")) {
				return false;
			}
			
			return true;
		}
		
	}


	/**
	 * RDP系统token鉴权
	 * @param userId
	 * @param sdkey
	 * @return
	 */
	private boolean authRdp(FaceToken authToken) {
		
		if(authToken.getId().equals(rdp_sdkey)) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 用户登录逻辑
	 * @param userId
	 * @param password
	 * @return
	 * @throws FaceException
	 */
	public FaceToken login(String userId, String password) throws FaceException {
		Map<String, String> userInfo = systemDao.selectUserById(userId);
		
		if(userInfo == null) {
			throw new FaceException(FaceException.SERVICE_ERROR, "用户不存在");
		}
		
		if(! password.equalsIgnoreCase(userInfo.get("PASSWORD"))){
			throw new FaceException(FaceException.SERVICE_ERROR, "密码错误");
		}
		
		FaceToken token = wrapFaceToken(systemDao.selectTokenById(userId));
		if(token == null) {
			token = insertNewToken(userId);
		}else {
			token = activeToken(token);
		}
		token.setRole(userInfo.get("ROLE"));
		
		return token;
	}

	/**
	 * 更新token为激活状态
	 * @param token
	 * @return
	 */
	private FaceToken activeToken(FaceToken token) {
		token.setSdkey(TokenBuilder.newSdkey(token.getId()));
		token.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
		token.setStatus("active");
		systemDao.updateFaceToken(token);
		
		return token;
	}

	/**
	 * 新建一个token并入库
	 * @param userId
	 * @return
	 */
	private FaceToken insertNewToken(String userId) {
		FaceToken token = new FaceToken();
		token.setId(UUID.randomUUID().toString().replace("-", ""));
		token.setAppName("web");
		token.setUserId(userId);
		token.setStatus("active");
		token.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
		token.setSdkey(buildNewToken("web", userId, token.getId()));
		
		systemDao.insertFaceToken(TokenBuilder.toMap(token));
		return token;
	}

	/**
	 * 构建一个新sdkey
	 * @param userId
	 * @return
	 */
	private String buildNewToken(String appName, String userId, String tokenId) {
		StringBuilder sdkey = new StringBuilder();
		sdkey.append(appName).append("***")
			.append(userId).append("***")
			.append(TokenBuilder.newSdkey(tokenId)).append("***")
			.append(LocalDate.now().toString());
		
		return encoder.encodeToString(sdkey.toString().getBytes());
	}

	/**
	 * 用户登出逻辑
	 * @param userId
	 */
	public void logout(String token) {
		FaceToken authToken = TokenBuilder.parseToken(new String(decoder.decode(token)));
		FaceToken currentToken = wrapFaceToken(systemDao.selectTokenById(authToken.getUserId()));
		if(currentToken != null) {
			logoutToken(currentToken);
		}
	}
	
	/**
	 * 封装taoken对象
	 * @param list
	 * @return
	 */
	private FaceToken wrapFaceToken(List<Map<String, Object>> list) {
		if(list == null || list.size() < 1) {
			return null;
		}else {
			FaceToken token = new FaceToken();
			Map<String, Object> map = list.get(0);
			
			token = TokenBuilder.parseToken(map);
			
			return token;
		}
		
	}

	/**
	 * 修改token状态为登出
	 * @param token
	 */
	private void logoutToken(FaceToken token) {
		token.setStatus("logout");
		token.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
		
		systemDao.updateFaceToken(token);
	}
	

	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	private String randomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }

	public static void main(String[] args) {
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern)));
		System.out.println(
				new String(
						new FaceSecurity().decoder.decode("d2ViKioqYWRtaW4qKipMM0UyRVJFakhTOUoyWW9uM25RTCoqKjIwMTgtMDMtMTQ=")));
	}

}
