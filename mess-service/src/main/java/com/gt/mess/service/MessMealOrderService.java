package com.gt.mess.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.exception.BaseException;
import com.gt.mess.entity.MessMealOrder;
import com.gt.mess.vo.SelectMealOrderVo;

/**
 * 订餐记录表
 * @author Administrator
 *
 */
public interface MessMealOrderService {

	/**
	 * 根据主表ID获取订餐记录列表
	 * @param page
	 * @param mainId
	 * @param nums
	 * @return
	 */
	public Page<MessMealOrder> getMessMealOrderPageByMainId(Page<MessMealOrder> page,Integer mainId,Integer nums);

	/**
	 * 根据条件查询订餐记录列表
	 * @param page
	 * @param params
	 * @param nums
	 * @return
	 */
	public Page<MessMealOrder> selectMealOrder(Page<MessMealOrder> page,Map<String,Object> params,Integer nums);
	
	/**
	 * 导出订餐记录
	 * @param saveVo
	 * @return
	 */
	public Map<String,Object> exports(SelectMealOrderVo saveVo);
	
	/**
	 * 导出订餐记录(月总)
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> exportsMealOrderForMonth(Integer mainId,Integer depId);
	
	/**
	 * 查询今日订餐记录(未取餐数)
	 * @param mainId
	 * @return
	 */
	public List<MessMealOrder> getMessMealOrderListforToday(Integer mainId);
	
	/**
	 * 查询今日订餐记录(总预订)
	 * @param mainId
	 * @return
	 */
	public List<MessMealOrder> getMessMealOrderListforToday2(Integer mainId);
	
	/**
	 * 获取订餐、取餐、未取餐人数  
	 * @param mainId
	 * @return code 1成功 0失败  mealOrderNum订餐人数  gainNum已取餐人数  notNum未取餐人数
	 * 			mealOrderMealNum订餐份数  gainMealNum已取餐份数  notMealNum未取餐份数
	 */
	public Map<String,Object> getMessMealOrderNum(Integer mainId,Integer messType);
	
	/**
	 * 根据取餐号获取订餐信息
	 * @param mealCode
	 * @return
	 */
	public MessMealOrder getMessMealOrderByMealCode(String mealCode);
	
//	/**
//	 * 获取订餐名单
//	 * @param params
//	 * @return
//	 */
//	public Map<String,Object> getMealOrderList(Map<String,Object> params);
	
//	手机端
	
	/**
	 * 根据饭卡ID和活动ID获取订单列表
	 * @param mapId
	 * @return
	 */
	public Page<MessMealOrder> getMessMealOrderPageByCardIdAndMainId(Page<MessMealOrder> page,Map<String, Integer> mapId,Integer nums);

	/**
	 * 获取已预订订餐
	 * @param mapId
	 * @return
	 */
	public List<MessMealOrder> getBookedMessMealOrder(Map<String,Integer> mapId);
	
	/**
	 * 获取未选餐订单
	 * @param mapId
	 * @return
	 */
	public List<MessMealOrder> getNotChooseMessMealOrder(Map<String,Integer> mapId);
	
	/**
	 * 保存订餐
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveMealOrder(Map<String,Object> params) throws Exception;
	
	/**
	 * 保存选餐（选择早晚餐）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveChooseMealOrder(Map<String,Object> params) throws Exception;
	
	/**
	 * 根据部门ID查询已订（早中晚夜）餐人数
	 * @param mapId
	 * @return
	 */
	public List<MessMealOrder> getNumsByDepIdAndMealType(Map<String,Integer> mapId);
	
//	手机端
	
	/**
	 * 取消订单
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public int cancelOrder(Integer orderId) throws Exception;
	
	/**
	 * 
	 * @param map cardId(饭卡ID),mealType(订餐类型),mainId(主表ID)
	 * @return
	 */
	public MessMealOrder getMessMealOrderByMap(Map<String,Object> map);
	
	/**
	 * 获取过期订单
	 * @param mapId
	 * @return
	 */
	public List<MessMealOrder> getPastMessMealOrderListByCardIdAndMainId(Map<String,Integer> mapId);
	
	/**
	 * 更新
	 * @param messMealOrder
	 * @return
	 * @throws Exception
	 */
	public int update(MessMealOrder messMealOrder) throws Exception;
	
	/**
	 * 删除订单
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int delOrder(Integer id) throws Exception;
	
	/**
	 * 根据日期获取已定餐数
	 * @param params
	 * @return
	 */
	public List<MessMealOrder> getBookMessMealOrderByToDay(Map<String,Object> params);
	
	/**
	 * 根据日期删除未选餐
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int delNotCMealOrder(Map<String,Object> map) throws Exception;
	
	/**
	 * 保存或更新追加订单
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveOrUpdateAddOrder(Map<String,Object> params) throws Exception;
}
