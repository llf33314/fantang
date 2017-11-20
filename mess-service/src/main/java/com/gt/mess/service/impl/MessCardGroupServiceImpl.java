package com.gt.mess.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.service.MessCardGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
@Service
public class MessCardGroupServiceImpl implements MessCardGroupService{

	@Autowired
	private MessCardGroupMapper messCardGroupMapper;
	
	@Override
	public Page<MessCardGroup> getMessCardGroupPageByMainId(Page<MessCardGroup> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCardGroupMapper.getMessCardGroupPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int saveOrUpdateCardGroup(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		MessCardGroup messCardGroup = null;
		if("save".equals(params.get("saveType").toString())){
			messCardGroup = new MessCardGroup();
		}else{
			messCardGroup = 
					messCardGroupMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		}
		messCardGroup.setMainId(Integer.valueOf(params.get("mainId").toString()));
		messCardGroup.setName(params.get("name").toString());
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("bitUse", params.get("bitUse").toString());
		jsonMap.put("breakfastPrice", params.get("breakfastPrice").toString());
		jsonMap.put("lunchPrice", params.get("lunchPrice").toString());
		jsonMap.put("dinnerPrice", params.get("dinnerPrice").toString());
		jsonMap.put("nightPrice", params.get("nightPrice").toString());
		jsonMap.put("universalPrice", params.get("universalPrice").toString());
		messCardGroup.setAuthority(JSON.toJSONString(jsonMap).toString());
		if("save".equals(params.get("saveType").toString())){
			data = messCardGroupMapper.insertSelective(messCardGroup);
		}else{
			data = messCardGroupMapper.updateByPrimaryKeySelective(messCardGroup);
		}
		return data;
	}

	@Override
	public int delCardGroup(Integer id) {
		// TODO Auto-generated method stub
		return messCardGroupMapper.deleteByPrimaryKey(id);
	}

	@Override
	public MessCardGroup getCardGroupById(Integer id) {
		// TODO Auto-generated method stub
		try {
			return messCardGroupMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
