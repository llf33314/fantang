package com.gt.mess.service;

import java.util.List;
import java.util.Map;

import com.gt.mess.entity.MessOrderManage;

/**
 * 订餐管理表
 * @author Administrator
 *
 */
public interface MessOrderManageService {

	/**
	 * 保存或更新不可订餐日子
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateMessOrderManage(Map<String,Object> params) throws Exception;
	
	/**
	 * 根据主表ID获取不可订餐管理列表
	 * @param mainId
	 * @return
	 */
	public List<MessOrderManage> getMessOrderManageListByMainId(Integer mainId);
}
