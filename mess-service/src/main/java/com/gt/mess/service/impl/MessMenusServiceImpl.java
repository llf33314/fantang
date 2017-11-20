package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessMenusMapper;
import com.gt.mess.entity.MessMenus;
import com.gt.mess.properties.FtpProperties;
import com.gt.mess.service.MessMenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MessMenusServiceImpl implements MessMenusService{

	@Autowired
	private MessMenusMapper messMenusMapper;

	@Autowired
	private FtpProperties ftpProperties;

	@Override
	public Page<MessMenus> getMessMenusPageByMainIdAndWeekNum(Page<MessMenus> page, Map<String, Object> map,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messMenusMapper.getMessMenusPageByMainIdAndWeekNum(map) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	@Override
	public int saveOrUpdateMenu(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int dataType = 0;
		String setType = params.get("setType").toString();
		MessMenus messMenus = null;
		if("save".equals(setType)){
			messMenus = new MessMenus();	
		}else{
			messMenus = messMenusMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		}
		
		messMenus.setComment(params.get("comment").toString());
		messMenus.setImages(params.get("images").toString().replace(ftpProperties.getImageAccess(), ""));
		messMenus.setMainId(Integer.valueOf(params.get("mainId").toString()));
		messMenus.setName(params.get("name").toString());
		messMenus.setPrice(0.0);
		messMenus.setSort(Integer.valueOf(params.get("sort").toString()));
		messMenus.setType(Integer.valueOf(params.get("type").toString()));
		messMenus.setWeek(Integer.valueOf(params.get("week").toString()));
		
		if("save".equals(setType)){
			dataType = messMenusMapper.insertSelective(messMenus);
		}else{
			dataType = messMenusMapper.updateByPrimaryKeySelective(messMenus);
		}
		return dataType;
	}

	@Transactional
	@Override
	public int delMenu(Integer mId) throws Exception {
		// TODO Auto-generated method stub
		return messMenusMapper.deleteByPrimaryKey(mId);
	}

	@Override
	public List<MessMenus> getMessMenusListByTypeAndWeekNumforMainId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		try {
			return messMenusMapper.getMessMenusListByTypeAndWeekNumforMainId(map);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
