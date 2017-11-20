package com.gt.mess.service;

import java.util.Map;

import com.gt.mess.entity.MessMain;

/**
 * 食堂主表
 * @author Administrator
 *
 */
public interface MessMainService {

	/**
	 * 根据商家ID获取主表
	 * @param busId
	 * @return
	 */
	public MessMain getMessMainByBusId(Integer busId);
	
	/**
	 * 根据ID获取主表
	 * @param id
	 * @return
	 */
	public MessMain getMessMainById(Integer id);
	
	/**
	 * 保存主表
	 * @param messMain
	 * @return
	 */
	public int saveMessMain(MessMain messMain);

	/**
	 * 保存主表授权
	 * @param param
	 */
	public void saveMessMainAuthority(Map<String, Object> param);
}
