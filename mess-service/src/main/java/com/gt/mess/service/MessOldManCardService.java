package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessOldManCard;

/**
 * 老人卡表
 * @author Administrator
 *
 */
public interface MessOldManCardService {

	/**
	 * 根据主表ID获取老人卡分页
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCard> getMessOldManCardPageByMainId(Page<MessOldManCard> page,Integer mainId,Integer nums);
	
	/**
	 * 保存或更新老人卡
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveOldManCard(Map<String,Object> params)throws Exception;
	
	/**
	 * 老人卡管理搜索
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessOldManCard> selectOldManCardManage(Page<MessOldManCard> page,Map <String,Object> params,Integer nums);

	/**
	 * 老人卡补票（扣票）
	 * @param cardId
	 * @param ticketNum
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public int addOrDelTicket(Integer cardId, Integer ticketNum, Integer type)throws Exception;
	
	/**
	 * 删除老人卡
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int delOldManCard(Integer id)throws Exception;
}
