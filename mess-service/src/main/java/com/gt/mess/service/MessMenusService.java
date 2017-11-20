package com.gt.mess.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessMenus;

/**
 * 菜单表
 * @author Administrator
 *
 */
public interface MessMenusService {

	/**
	 * 根据主表ID和星期几获取菜单列表
	 * @return
	 */
	public Page<MessMenus> getMessMenusPageByMainIdAndWeekNum(Page<MessMenus> page,Map<String,Object> map,Integer nums);

	/**
	 * 保存或更新菜单
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateMenu(Map<String,Object> params) throws Exception;
	
	/**
	 * 删除菜品
	 * @param mId
	 * @return
	 * @throws Exception
	 */
	public int delMenu(Integer mId) throws Exception;
	
//	手机端
	/**
	 * 根据主表ID查询星期几的菜单
	 * @param map
	 * @return
	 */
	public List<MessMenus> getMessMenusListByTypeAndWeekNumforMainId(Map<String,Object> map);
	
}
