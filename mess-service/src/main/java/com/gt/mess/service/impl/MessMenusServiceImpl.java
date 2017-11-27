package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessMenusMapper;
import com.gt.mess.entity.MessMenus;
import com.gt.mess.properties.FtpProperties;
import com.gt.mess.service.MessMenusService;
import com.gt.mess.vo.SaveOrUpdateMenuVo;
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
	public int saveOrUpdateMenu(SaveOrUpdateMenuVo saveVo) throws Exception {
		// TODO Auto-generated method stub
		int dataType = 0;
		MessMenus messMenus = null;
		if("save".equals(saveVo.getSetType())){
			messMenus = new MessMenus();	
		}else{
			messMenus = messMenusMapper.selectByPrimaryKey(saveVo.getId());
		}
		
		messMenus.setComment(saveVo.getComment());
		messMenus.setImages(saveVo.getImages().replace(ftpProperties.getImageAccess(), ""));
		messMenus.setMainId(saveVo.getMainId());
		messMenus.setName(saveVo.getName());
		messMenus.setPrice(0.0);
		messMenus.setSort(saveVo.getSort());
		messMenus.setType(saveVo.getType());
		messMenus.setWeek(saveVo.getWeek());
		
		if("save".equals(saveVo.getSetType())){
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
