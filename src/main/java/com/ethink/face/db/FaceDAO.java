/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.db;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 *
 * 描述: 业务逻辑Dao
 * @author wangjing.dc@qq.com
 */
@Mapper
public interface FaceDAO {

	//人脸比对类型
	static final String MATCH_TWO_PHOTO = "MATCH_TWO_PHOTO";  
	static final String MATCH_BY_ID = "MATCH_BY_ID";  
	
	@Select("select * from config")
	List<Map> findById(String username);

	/**
	 * 插入两张照片对比记录
	 * @param match
	 * @return
	 */
	public int insertIntoMatch(Map<String, Object> match);
	
	/**
	 * 通过id识别人脸
	 * @param match
	 * @return
	 */
	public int insertIntoMatchById(Map<String, Object> match);
	
	/**
	 * 批量注册用户信息
	 * @param reg
	 * @return
	 */
    public int insertRegisterInfo(List<Map<String,Object>> list);
	/**
	 *通过ID查找用户头像
	 * @param userId
	 * @return
	 */
	public Map<String, String> findPhotoByUserId(String userId);
	
	/**
	 * 批量根据Id查询用户信息
	 * @param reg
	 * @return
	 */
	public List<Map<String,String>> selectUserId(List<String> list);
	
	/**
	 * 动态查询用户信息
	 * @param reg
	 * @return
	 */
    public List<Map<String,Object>> selectUserInfo(Map<String,Object> reg);
    
    /** 删除指定用户
     * @param userId
	 * @return
	 */
    int deleteUser(String userId);

    /**
     * 修改用户信息
     * @param reg
     * @return
     */
    public int updateUser(Map<String,Object> reg);
	/**
	 * 根据时间清理数据库
	 * @param datetime
	 * @return
	 */
	int deleteMatchInfoByTime(String datetime);
	
	/**
	 * 根据时间区间查找比对信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectMatchInfoByDateTime(Map<String, String> map);

	/**
	 * 查询所有用户的信息
	 * @return
	 */
	List<Map<String, Object>> findAllUserInfo();

	/**通过车牌号找用户信息
	 * @param carNum
	 * @return
	 */
	Map<String, Object> findUserByCarNum(String carNum);

	int insertLossDrivingInfo(Map<String, Object> mapinfo);

	List<Map<String, Object>> findAllLossDrivingInfo();

	int updateLossDrivingStatus(String status);

}
