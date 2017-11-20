package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessConsumerDetailMapper;
import com.gt.mess.entity.MessConsumerDetail;
import com.gt.mess.service.MessConsumerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MessConsumerDetailServiceImpl implements MessConsumerDetailService{

	@Autowired
	private MessConsumerDetailMapper messConsumerDetailMapper;
	
	@Override
	public Page<MessConsumerDetail> getMessConsumerDetailPageByCardIdAndMainId(Page<MessConsumerDetail> page,Map<String,Integer> mapId,Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messConsumerDetailMapper.getMessConsumerDetailPageByCardIdAndMainId(mapId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public MessConsumerDetail getMessConsumerDetailById(Integer id) {
		// TODO Auto-generated method stub
		try {
			return messConsumerDetailMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional
	@Override
	public int update(MessConsumerDetail messConsumerDetail) throws Exception {
		// TODO Auto-generated method stub
		return messConsumerDetailMapper.updateByPrimaryKeySelective(messConsumerDetail);
	}

}
