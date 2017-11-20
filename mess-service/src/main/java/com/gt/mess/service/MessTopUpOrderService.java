package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessTopUpOrder;

/**
 * 充值记录表
 * @author Administrator
 *
 */
public interface MessTopUpOrderService {

	/**
	 * 根据主表ID获取充值记录列表
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessTopUpOrder> getMessTopUpOrderPageByMainId(Page<MessTopUpOrder> page,Integer mainId,Integer nums);

	/**
	 * 充值记录(根据条件查询)
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessTopUpOrder> selectTopUpOrder(Page<MessTopUpOrder> page,Map<String,Object> params,Integer nums);

	/**
	 * 导出充值记录
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> exports(Map <String,Object> params);
	
//	手机端
	
	/**
	 * 根据订单号查询充值订单
	 * @param orderNo
	 * @return
	 */
	public MessTopUpOrder getMessTopUpOrderByOrderNo(String orderNo);

	/**
	 * 更新
	 * @param messTopUpOrder
	 * @return
	 * @throws Exception
	 */
	public int update(MessTopUpOrder messTopUpOrder) throws Exception;
	
	/**
	 * 充值支付
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> topUpPay(Map<String,Object> params) throws Exception;
	
}
