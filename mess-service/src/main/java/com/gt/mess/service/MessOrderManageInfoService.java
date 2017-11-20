package com.gt.mess.service;

import com.gt.mess.entity.MessOrderManageInfo;

import java.util.List;

/**
 * 订餐管理info表
 * @author Administrator
 *
 */
public interface MessOrderManageInfoService {

	/**
	 * 根据订餐管理表ID获取当月订餐日子
	 * @param omId
	 * @return
	 */
	public List<MessOrderManageInfo> getMessOrderManageInfoListByOmId(Integer omId);
}
