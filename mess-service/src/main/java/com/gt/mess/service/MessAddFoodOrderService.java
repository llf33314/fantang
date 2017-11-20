package com.gt.mess.service;

import com.gt.mess.entity.MessAddFoodOrder;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;

/**
 * 加餐记录
 * @author Administrator
 *
 */
public interface MessAddFoodOrderService {

	/**
	 * 根据主表ID查询加餐记录列表
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessAddFoodOrder> getMessAddFoodOrderPageByMainId(Page<MessAddFoodOrder> page, Integer mainId, Integer nums);

	/**
	 * 导出加餐核销记录
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> exports(Map <String,Object> params);
	
	/**
	 * 搜索
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessAddFoodOrder> selectAddFoodOrder(Page<MessAddFoodOrder> page,Map<String,Object> params,Integer nums);
	
}
