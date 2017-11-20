package com.gt.mess.service;

import java.util.Map;

import com.gt.mess.entity.MessBasisSet;

/**
 * 基础设置表
 * @author Administrator
 *
 */
public interface MessBasisSetService {

	/**
	 * 根据主表ID获取基础设置
	 * @param mainId
	 * @return
	 */
	public MessBasisSet getMessBasisSetByMainId(Integer mainId);
	
	/**
	 * 保存或更新基础设置
	 * @param params
	 * @return
	 */
	public int saveOrUpdateBasisSet(Map <String,Object> params) throws Exception;
}
