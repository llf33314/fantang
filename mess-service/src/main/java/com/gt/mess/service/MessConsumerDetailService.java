package com.gt.mess.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessConsumerDetail;

/**
 * 总消费流水表
 * @author Administrator
 *
 */
public interface MessConsumerDetailService {

	
	/**
	 * 根据饭卡ID查询本活动总消费流水列表
	 * @param mapId
	 * @return
	 */
	public Page<MessConsumerDetail> getMessConsumerDetailPageByCardIdAndMainId(Page<MessConsumerDetail> page,Map<String,Integer> mapId,Integer nums);

//	手机端
	/**
	 * 根据ID获取流水表
	 * @param id
	 * @return
	 */
	public MessConsumerDetail getMessConsumerDetailById(Integer id);
	
	/**
	 * 更新
	 * @param messConsumerDetail
	 * @return
	 * @throws Exception
	 */
	public int update(MessConsumerDetail messConsumerDetail) throws Exception;
}
