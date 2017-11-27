package com.gt.mess.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCard;
import com.gt.mess.entity.MessCardTicket;
import com.gt.mess.vo.SaveOrUpdateMessCardVo;

/**
 * 饭卡表
 * @author Administrator
 *
 */
public interface MessCardService {

	/**
	 * 根据主表ID获取饭卡用户列表
	 * @param mainId
	 * @return
	 */
	public Page<MessCard> getMessCardPageByMainId(Page<MessCard> page,Integer mainId,Integer nums);

	/**
	 * 根据主表ID获取饭卡用户列表（手动核销列表）
	 * @param mainId
	 * @return
	 */
	public Page<MessCard> commonCard(Page<MessCard> page,Integer mainId,Integer nums);
	
	/**
	 * 保存或更新饭卡
	 * @param saveVo
	 * @return
	 */
	public int saveOrUpdateMessCard(SaveOrUpdateMessCardVo saveVo) throws Exception;

	/**
	 * 余额充值
	 * @param cardId
	 * @param money
	 * @return
	 * @throws Exception
	 */
	public int topUpMoney(Integer cardId,Double money) throws Exception;

	/**
	 * 饭票购买
	 * @param cardId
	 * @param dMoney
	 * @param messType
	 * @param ticketNum
	 * @return
	 * @throws Exception
	 */
	public int buyTicket(Integer cardId,Double dMoney,Integer messType,Integer ticketNum) throws Exception;

	/**
	 * 补助
	 * @param cardId
	 * @param messType
	 * @param ticketNum
	 * @return
	 * @throws Exception
	 */
	public int subsidyTicket(Integer cardId,Integer messType,Integer ticketNum) throws Exception;

	/**
	 * 扣除饭票（只限免费）
	 * @param cardId
	 * @param messType
	 * @param ticketNum
	 * @return
	 * @throws Exception
	 */
	public int deductTicket(Integer cardId,Integer messType,Integer ticketNum) throws Exception;

	/**
	 * 核销饭票（手动核销）
	 * @param id
	 * @param ticketNum
	 * @param ticketType
	 * @return
	 * @throws Exception
	 */
	public int cancelTicket(Integer id,Integer ticketNum,Integer ticketType) throws Exception;
	
	/**
	 * 删除饭卡
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public int delMessCard(Integer cardId) throws Exception;
	
	/**
	 * 根据卡号查询饭卡
	 * @param page
	 * @param map
	 * @param nums
	 * @return
	 */
	public Page<MessCard> selectCardApplyByCardCode(Page<MessCard> page,Map<String, Object> map,Integer nums);
	
	/**
	 * 根据用户名查询饭卡
	 * @param page
	 * @param map
	 * @param nums
	 * @return
	 */
	public Page<MessCard> selectCardApplyByName(Page<MessCard> page,Map<String, Object> map,Integer nums);
	
	/**
	 * 根据部门查询饭卡
	 * @param page
	 * @param map
	 * @param nums
	 * @return
	 */
	public Page<MessCard> selectCardApplyByDepartment(Page<MessCard> page,Map<String, Object> map,Integer nums);
	
	/**
	 * 导入饭卡
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> enteringCard(InputStream inputStream,Integer mainId,Integer depId) throws Exception;
	
	/**
	 * 核销饭票
	 * @param mealCode 取餐号
	 * @return code 1成功 -1失败
	 */
	public Map<String,Object> cancelMealTicket(String mealCode,Integer cardId,Integer type,Integer memberId) throws Exception; 
	
	/**
	 * 导出模板
	 * @param params
	 * @return
	 */
	public Map<String,Object> exports();
	
	/**
	 * 获取饭卡中未使用的饭票
	 * @param cardId
	 * @return
	 */
	public List<MessCardTicket> getMessCardTicketListByCardId(Integer cardId);

	/**
	 * 修改部门
	 * @param cardId
	 * @param depId
	 * @param department
	 * @return
	 * @throws Exception
	 */
	public int changeDep(Integer cardId, Integer depId, String department)throws Exception;
	
	/**
	 * 修改饭卡组
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public int changeGroup(Integer cardId,Integer groupId)throws Exception;
	
	/**
	 * 获取当前部门有多少饭卡
	 * @param mapId
	 * @return
	 */
	public int getMessCardNumsByDepId(Map<String,Integer> mapId);
	
	/**
	 * 获取当前饭卡组有多少饭卡
	 * @param mapId
	 * @return
	 */
	public int getMessCardNumsByGroupId(Map<String,Integer> mapId);
	
	/**
	 * 改变票种
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	public int changeTicketType(Integer mainId,Integer type) throws Exception;
	
	/**
	 * 一键清空饭卡以及未来订单
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	public int cleanTicketAndOrder(Integer mainId) throws Exception;
	
	/**
	 * 一键清票
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	public int emptyTicket(Integer cardId) throws Exception;
	
//	手机端接口
	
	/**
	 * 根据memberId获取本次活动的饭卡
	 * @param mapId
	 * @return
	 */
	public MessCard getMessCardByMainIdAndMemberId(Map<String,Integer> mapId);
	
	/**
	 * 绑定饭卡
	 * @param params
	 * @return
	 */
	public int bindingCard(Map<String,Object> params)throws Exception;
	
	/**
	 * 根据ID查询饭卡
	 * @param cardId
	 * @return
	 */
	public MessCard getMessCardById(Integer cardId);
	
	/**
	 * 更新
	 * @param messCard
	 * @return
	 * @throws Exception
	 */
	public int update(MessCard messCard) throws Exception;
	
	/**
	 * 根据主表ID获取未核销饭票数
	 * @param mainId
	 * @return
	 */
	public int getNotCancelTicketNum(Integer mainId);
	
	/**
	 * 取餐码验证
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> verify(Integer cardId,String mealCode) throws Exception;
	
	/**
	 * 判断是否有过期饭票
	 * @param messCardTickets
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> pastDue(MessCard messCard, MessBasisSet messBasisSet, List<MessCardTicket> messCardTickets,Integer mainId) throws Exception;
	
	/**
	 * 判断是否有饭票即将过期（1天）
	 * @param messCardTickets
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> pastDue2(MessCard messCard, MessBasisSet messBasisSet, List<MessCardTicket> messCardTickets,Integer mainId,Integer day);

//	小程序接口

	/**
	 * 绑定饭卡(小程序)
	 * @param params
	 * @return
	 */
	public int srBindingCard(Map<String,Object> params)throws Exception;
	
	/**
	 * 根据小程序用户openId获取饭卡
	 * @param openId
	 * @return
	 */
	public MessCard getMessCardByOpenId(String openId);

	/**
	 * 平衡
	 * @param messCardTickets
	 * @param mainId
	 * @return
	 * @throws Exception
	 */
	public void balance(MessCard messCard, MessBasisSet messBasisSet, List<MessCardTicket> messCardTickets,Integer mainId) throws Exception;
}
