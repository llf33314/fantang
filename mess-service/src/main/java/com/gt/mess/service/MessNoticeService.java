package com.gt.mess.service;

import com.gt.mess.entity.MessNotice;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * 公告管理表
 * @author Administrator
 *
 */
public interface MessNoticeService {

	/**
	 * 根据主表ID获取公告管理列表
	 * @param mainId
	 * @return
	 */
	public Page<MessNotice> getMessNoticePageByMainId(Page<MessNotice> page,Integer mainId,Integer nums);

	/**
	 * 保存或更新公告
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateNotice(Map<String,Object> params) throws Exception;
	
	/**
	 * 根据nId删除公告
	 * @param nId
	 * @return
	 * @throws Exception
	 */
	public int delNotice(Integer nId) throws Exception;
	
//	手机端
	
	/**
	 * 根据主表ID查询公告
	 * @param mainId
	 * @return
	 */
	public List<MessNotice> getMessNoticeListByMainId(Integer mainId,Integer nums);
	
}
