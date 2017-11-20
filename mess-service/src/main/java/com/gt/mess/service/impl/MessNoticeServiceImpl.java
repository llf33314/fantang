package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessNoticeMapper;
import com.gt.mess.entity.MessNotice;
import com.gt.mess.service.MessNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessNoticeServiceImpl implements MessNoticeService{

	@Autowired
	private MessNoticeMapper messNoticeMapper;
	
	@Override
	public Page<MessNotice> getMessNoticePageByMainId(Page<MessNotice> page,Integer mainId,Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messNoticeMapper.getMessNoticePageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	@Override
	public int saveOrUpdateNotice(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int dataType = 0;
		String setType = params.get("setType").toString();
		MessNotice messNotice = null;
		if("save".equals(setType)){
			messNotice = new MessNotice();	
		}else{
			messNotice = messNoticeMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		}
		messNotice.setMainId(Integer.valueOf(params.get("mainId").toString()));
		messNotice.setNotice(params.get("notice").toString());
		messNotice.setTime(new Date());
		messNotice.setTitle(params.get("title").toString());
		if("save".equals(setType)){
			dataType = messNoticeMapper.insertSelective(messNotice);	
		}else{
			dataType = messNoticeMapper.updateByPrimaryKeySelective(messNotice);
		}
		return dataType;
	}

	@Override
	public int delNotice(Integer nId) throws Exception {
		// TODO Auto-generated method stub
		return messNoticeMapper.deleteByPrimaryKey(nId);
	}

	@Override
	public List<MessNotice> getMessNoticeListByMainId(Integer mainId,Integer nums) {
		// TODO Auto-generated method stub、
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("mainId", mainId);
			mapId.put("nums", nums);
			return messNoticeMapper.getMessNoticeListByMainId(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
