package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessOldManCardOrder;

/**
 * 老人卡订餐
 * @author Administrator
 *
 */
public interface MessOldManCardOrderService {

	/**
	 * 老人卡（手动核销列表）
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCardOrder> getMessOldManCardOrderPageByMainId(Page<MessOldManCardOrder> page,Integer mainId,Integer nums);

	/**
	 * 老人卡(搜索)
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCardOrder> selectMessOldManCardOrder(Page<MessOldManCardOrder> page,Map<String,Object> params,Integer nums);
	
	
	/**
	 * 普通卡（手动核销列表）
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCardOrder> getCommonMessCardOrderPageByMainId(Page<MessOldManCardOrder> page,Integer mainId,Integer nums);
	
	/**
	 * 普通卡(搜索)
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCardOrder> selectMessCommonCardOrder(Page<MessOldManCardOrder> page,Map<String,Object> params,Integer nums);

	/**
	 * 导出老人卡&普通卡核销记录
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> exports(Map <String,Object> params);
	
	/**
	 * 老人卡（商家扣票、补票记录）
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCardOrder> getMessOldManCardOrderPageByMainId2(Page<MessOldManCardOrder> page,Integer mainId,Integer nums);

	/**
	 * 老人卡(商家扣票、补票记录搜索)
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCardOrder> selectMessOldManCardOrder2(Page<MessOldManCardOrder> page,Map<String,Object> params,Integer nums);
}
