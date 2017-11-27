package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.vo.SaveOrUpdateCardGroupVo;

/**
 * 饭卡组
 * @author Administrator
 *
 */
public interface MessCardGroupService {

	/**
	 * 根据主表ID获取饭卡组列表
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessCardGroup> getMessCardGroupPageByMainId(Page<MessCardGroup> page,Integer mainId,Integer nums);

	/**
	 * 保存或更新饭卡组
	 * @param saveVo
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateCardGroup(SaveOrUpdateCardGroupVo saveVo)throws Exception;
	
	/**
	 * 根据id删除饭卡组
	 * @param id
	 * @return
	 */
	public int delCardGroup(Integer id);
	
	/**
	 * 根据ID获取饭卡组
	 * @param id
	 * @return
	 */
	public MessCardGroup getCardGroupById(Integer id);
}
