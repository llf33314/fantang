package com.gt.mess.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gt.mess.dao.MessOrderManageInfoMapper;
import com.gt.mess.entity.MessOrderManageInfo;
import com.gt.mess.service.MessOrderManageInfoService;

@Service
public class MessOrderManageInfoServiceImpl implements MessOrderManageInfoService{

	@Autowired
	private MessOrderManageInfoMapper messOrderManageInfoMapper;
	
	@Override
	public List<MessOrderManageInfo> getMessOrderManageInfoListByOmId(Integer omId) {
		// TODO Auto-generated method stub
		try {
			return messOrderManageInfoMapper.getMessOrderManageInfoListByOmId(omId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
