package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessCardMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.service.MessDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessDepartmentServiceImpl implements MessDepartmentService{

	@Autowired
	private MessDepartmentMapper messDepartmentMapper;
	
	@Autowired
	private MessCardMapper messCardMapper;
	
	@Override
	public Page<MessDepartment> getMessDepartmentPageByMainId(Page<MessDepartment> page, Integer mianId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messDepartmentMapper.getMessDepartmentPageByMainId(mianId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int saveOrUpdateDepartment(String saveType,Integer mainId,Integer id,String name) throws Exception {
		// TODO Auto-generated method stub
		MessDepartment messDepartment = null;
		int data = 0;
		if("save".equals(saveType)){
			messDepartment = new MessDepartment();
		}else{
			messDepartment = 
					messDepartmentMapper.selectByPrimaryKey(id);
		}
		messDepartment.setMainId(mainId);
		messDepartment.setName(name);
		if("save".equals(saveType)){
			data = messDepartmentMapper.insertSelective(messDepartment);
		}else{
			data = messDepartmentMapper.updateByPrimaryKeySelective(messDepartment);
			if(data == 1){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("mainId", messDepartment.getMainId());
				map.put("depId", messDepartment.getId());
				map.put("name", messDepartment.getName());
				messCardMapper.changeCardDepartment(map);
			}
		}
		return data;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int delDepartment(Integer depId) throws Exception {
		// TODO Auto-generated method stub
		return messDepartmentMapper.deleteByPrimaryKey(depId);
	}

}
