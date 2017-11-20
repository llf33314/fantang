package com.gt.mess.service;

import java.util.List;
import java.util.Map;

import com.gt.mess.entity.MessMealTempOrder;

public interface MessMealTempOrderService {

	/**
	 * 保存主表
	 * @param messMain
	 * @return
	 */
	public int saveMessMain(MessMealTempOrder messMealTempOrder);
	
	/**
	 * 根据主ID获取饭票最新核销记录
	 * @param mainId
	 * @param nums
	 */
	public List<Map<String, Object>> getMessMealTempOrderByMainId(Integer mainId, int nums);
	
}
