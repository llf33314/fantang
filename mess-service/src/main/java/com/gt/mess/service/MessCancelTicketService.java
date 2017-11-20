package com.gt.mess.service;

import com.gt.mess.entity.MessCancelTicket;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * 饭票核销记录表
 * @author Administrator
 *
 */
public interface MessCancelTicketService {

	/**
	 * 根据主表ID获取饭票核销记录
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessCancelTicket> getMessCancelTicketPageByMainId(Page<MessCancelTicket> page,Integer mainId,Integer nums);

	/**
	 * 根据条件查询核销记录
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessCancelTicket> selectCancelRecord(Page<MessCancelTicket> page,Map<String,Object> params,Integer nums);

	/**
	 * 根据主ID获取饭票最新核销记录
	 * @param mainId
	 * @param nums
	 */
	public List<Map<String, Object>> getMessCancelTicketPageByMainIdNew(Integer mainId, int nums);
	
	/**
	 * 根据条件导出核销记录
	 * @param params
	 * @return
	 */
	public Map<String,Object> exports(Map <String,Object> params);
	
}
