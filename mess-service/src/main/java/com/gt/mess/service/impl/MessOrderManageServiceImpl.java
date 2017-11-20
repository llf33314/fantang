package com.gt.mess.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gt.mess.dao.MessOrderManageInfoMapper;
import com.gt.mess.dao.MessOrderManageMapper;
import com.gt.mess.entity.MessOrderManage;
import com.gt.mess.entity.MessOrderManageInfo;
import com.gt.mess.service.MessOrderManageService;

@Service
public class MessOrderManageServiceImpl implements MessOrderManageService{

	@Autowired
	private MessOrderManageMapper messOrderManageMapper;
	
	@Transactional
	@Override
	public int saveOrUpdateMessOrderManage(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int dataType = 0;
		Integer mainId = Integer.valueOf(params.get("mainId").toString());
		messOrderManageMapper.delMessOrderManageByMainId(mainId);
		String [] days = params.get("days").toString().split(",");
		if(days.length == 0){
			return -1;
		}
		for(String day : days){
			MessOrderManage messOrderManage = new MessOrderManage();
			messOrderManage.setMainId(mainId);
			messOrderManage.setDay(day);
			dataType = messOrderManageMapper.insertSelective(messOrderManage);
		}
		return dataType;
	}

	@Override
	public List<MessOrderManage> getMessOrderManageListByMainId(Integer mainId) {
		// TODO Auto-generated method stub
		try {
			return messOrderManageMapper.getMessOrderManageListByMainId(mainId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
