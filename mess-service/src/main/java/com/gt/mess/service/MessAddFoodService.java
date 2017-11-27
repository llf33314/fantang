package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessAddFood;
import com.gt.mess.entity.MessCard;

/**
 * 加菜管理表
 * @author Administrator
 *
 */
public interface MessAddFoodService {

	/**
	 * 根据主表ID获取加菜记录列表
	 * @return
	 */
	public Page<MessAddFood> getMessAddFoodPageByMainId(Page<MessAddFood> page,Integer mainId,Integer nums);

	/**
	 * 保存或更新加菜表
	 * @param saveType
	 * @param mainId
	 * @param id
	 * @param comment
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateAddFood(String saveType,Integer mainId,Integer id,String comment,Double price) throws Exception;
	
	/**
	 * 根据ID删除加菜价格
	 * @param afId
	 * @return
	 * @throws Exception
	 */
	public int delAddFood(Integer afId) throws Exception;
	
	/**
	 * 根据ID查询
	 * @param fId
	 * @return
	 */
	public MessAddFood getMessAddFoodById(Integer fId);
	
//	手机端接口
	
	/**
	 * 加菜核销
	 * @param member
	 * @param fdId
	 * @return
	 * @throws Exception
	 */
	public int addFood(MessCard messCard,Integer fdId)throws Exception;
}
