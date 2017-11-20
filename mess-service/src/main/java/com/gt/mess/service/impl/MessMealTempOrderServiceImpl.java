package com.gt.mess.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gt.mess.dao.MessMealTempOrderMapper;
import com.gt.mess.entity.MessMealTempOrder;
import com.gt.mess.service.MessMealTempOrderService;

@Service
public class MessMealTempOrderServiceImpl implements MessMealTempOrderService{

	private Logger LOGGER = Logger.getLogger(MessMealTempOrderServiceImpl.class);
	
	@Autowired
	private MessMealTempOrderMapper messMealTempOrderMapper;

	@Override
	public int saveMessMain(MessMealTempOrder messMealTempOrder) {
		long count = messMealTempOrderMapper.countMessMealTempByMainId(messMealTempOrder.getMainId());
		if(count >= 5){
			try {
				messMealTempOrderMapper.deletLastByMainId(messMealTempOrder.getMainId());
			} catch (Exception e) {
				LOGGER.warn("删除临时数据失败" + e.getMessage());
			}
		}
		int num = messMealTempOrderMapper.insertSelective(messMealTempOrder);
		return num;
	}

	@Override
	public List<Map<String, Object>> getMessMealTempOrderByMainId(Integer mainId, int nums) {
		return messMealTempOrderMapper.getMessMealTempOrderByMainId(mainId, nums);
	}
	

}
