package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessDepartment;

/**
 * 部门管理
 * @author Administrator
 *
 */
public interface MessDepartmentService {

	/**
	 * 根据主表ID获取部门列表
	 * @param page
	 * @param mianId
	 * @param nums
	 * @return
	 */
	public Page<MessDepartment> getMessDepartmentPageByMainId(Page<MessDepartment> page,Integer mianId,Integer nums);

	/**
	 * 保存或更新部门
	 * @param saveType
	 * @param mainId
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateDepartment(String saveType,Integer mainId,Integer id,String name)throws Exception;
	
	/**
	 * 删除部门
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public int delDepartment(Integer depId)throws Exception;
}
