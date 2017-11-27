package com.gt.mess.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.service.MessCardGroupService;
import com.gt.mess.vo.SaveOrUpdateCardGroupVo;
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
	public int saveOrUpdateCardGroup(SaveOrUpdateCardGroupVo saveVo) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		MessCardGroup messCardGroup = null;
		if("save".equals(saveVo.getSetType())){
			messCardGroup = new MessCardGroup();
		}else{
			messCardGroup = 
					messCardGroupMapper.selectByPrimaryKey(saveVo.getId());
		}
		messCardGroup.setMainId(saveVo.getMainId());
		messCardGroup.setName(saveVo.getName());
		Map<String,Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("bitUse", saveVo.getBitUse());
		jsonMap.put("breakfastPrice", saveVo.getBreakfastPrice());
		jsonMap.put("lunchPrice", saveVo.getLunchPrice());
		jsonMap.put("dinnerPrice", saveVo.getDinnerPrice());
		jsonMap.put("nightPrice", saveVo.getNightPrice());
		jsonMap.put("universalPrice", saveVo.getUniversalPrice());
		messCardGroup.setAuthority(JSON.toJSONString(jsonMap).toString());
		if("save".equals(saveVo.getSetType())){
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
