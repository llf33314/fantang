package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessBuyTicketOrder;

/**
 * 购票记录表
 * @author Administrator
 *
 */
public interface MessBuyTicketOrderService {

	/**
	 * 根据主表ID获取购票记录列表
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessBuyTicketOrder> getMessBuyTicketOrderPageMainId(Page<MessBuyTicketOrder> page,Integer mainId,Integer nums);

	/**
	 * 购票统计(根据条件查询)
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessBuyTicketOrder> selectBuyTicketStatistics(Page<MessBuyTicketOrder> page,Map<String,Object> params,Integer nums);
	
	/**
	 * 导出购票记录
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> exports(Map <String,Object> params);
	
	/**
	 * 根据主表ID获取补助饭票记录
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessBuyTicketOrder> getSubsidyTicketOrderPageMainId(Page<MessBuyTicketOrder> page,Integer mainId,Integer nums);

	/**
	 * 商家补助(根据条件查询)
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessBuyTicketOrder> selectSubsidyTicket(Page<MessBuyTicketOrder> page,Map<String,Object> params,Integer nums);
	
	/**
	 * 导出补助记录
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> exportsSubsidyTicket(Map <String,Object> params);
	
//	手机端
	
	/**
	 * 购买饭票
	 * @return
	 * @throws Exception
	 */
	public int buyTicket(Map<String,Object> params) throws Exception;
}
