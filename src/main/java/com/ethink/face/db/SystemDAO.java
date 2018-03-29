/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.db;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ethink.face.security.FaceToken;

/**
 *
 * 描述: 系统管理Dao
 * @author wangjing.dc@qq.com
 */
@Mapper
public interface SystemDAO {


	/**
	 * 查询token状态信息
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> selectTokenById(String userId);

	/**
	 * 新增一条token记录
	 * @param token
	 */
	void insertFaceToken(Map<String, String> token);

	/**
	 * 查询用户信息
	 * @param userId
	 * @return
	 */
	Map<String, String> selectUserById(String userId);

	/**
	 * 更新token记录
	 * @param token
	 */
	void updateFaceToken(FaceToken token);

	
}
