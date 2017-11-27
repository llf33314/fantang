package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.*;
import com.gt.mess.entity.*;
import com.gt.mess.service.MessAddFoodService;
import com.gt.mess.util.DateTimeKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service
public class MessAddFoodServiceImpl implements MessAddFoodService{

	@Autowired
	private MessAddFoodMapper messAddFoodMapper;
	
	@Autowired
	private MessCardMapper messCardMapper;
	
	@Autowired
	private MessAddFoodOrderMapper messAddFoodOrderMapper;
	
	@Autowired
	private MessConsumerDetailMapper messConsumerDetailMapper;
	
	@Autowired
	private MessMealTempOrderMapper messMealTempOrderMapper;
	
	@Override
	public Page<MessAddFood> getMessAddFoodPageByMainId(Page<MessAddFood> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messAddFoodMapper.getMessAddFoodPageByMainId(page,mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	@Override
	public int saveOrUpdateAddFood(String saveType,Integer mainId,Integer id,String comment,Double price) throws Exception {
		// TODO Auto-generated method stub
		int dataType = 0;
		MessAddFood messAddFood = null;
		if("save".equals(saveType)){
			messAddFood = new MessAddFood();	
		}else{
			messAddFood = messAddFoodMapper.selectByPrimaryKey(id);
		}
		messAddFood.setComment(comment);
		messAddFood.setMainId(mainId);
		messAddFood.setPrice(price);
		messAddFood.setTime(new Date());
		if("save".equals(saveType)){
			dataType = messAddFoodMapper.insertSelective(messAddFood);
		}else{
			dataType = messAddFoodMapper.updateByPrimaryKeySelective(messAddFood);
		}
		return dataType;
	}
	
	@Transactional
	@Override
	public int delAddFood(Integer afId) throws Exception {
		// TODO Auto-generated method stub
		return messAddFoodMapper.deleteByPrimaryKey(afId);
	}

//	手机端接口
	@Transactional
	@Override
	public int addFood(MessCard messCard, Integer fdId) throws Exception {
		// TODO Auto-generated method stub
		MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
		Double money = messCard.getMoney() - messAddFood.getPrice();
		if(money < 0){
			return 0;
		}
		//消费流水表
		MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
		messConsumerDetail.setMainId(messCard.getMainId());
		messConsumerDetail.setCardId(messCard.getId());
		messConsumerDetail.setTime(new Date());
		messConsumerDetail.setTableType(2);
		messConsumerDetail.setBitSubsidy(1);
		messConsumerDetail.setOnLine(0);
		messConsumerDetail.setBreakfastNum(0);
		messConsumerDetail.setLunchNum(0);
		messConsumerDetail.setDinnerNum(0);
		messConsumerDetail.setNightNum(0);
		messConsumerDetail.setUniversalNum(0);
		messConsumerDetail.setStatus(0);
		messConsumerDetail.setMoney(messAddFood.getPrice());
		
		messCard.setMoney(money);
		MessAddFoodOrder messAddFoodOrder = new MessAddFoodOrder();
		messAddFoodOrder.setAddNum(1);
		messAddFoodOrder.setAfId(messAddFood.getId());
		messAddFoodOrder.setCardCode(messCard.getCardCode());
		messAddFoodOrder.setCardId(messCard.getId());
		messAddFoodOrder.setDepartment(messCard.getDepartment());
		messAddFoodOrder.setMainId(messCard.getMainId());
		messAddFoodOrder.setMemberId(messCard.getMemberId());
		messAddFoodOrder.setMoney(messAddFood.getPrice());
		messAddFoodOrder.setName(messCard.getName());
		messAddFoodOrder.setSex(messCard.getSex());
		messAddFoodOrder.setTime(new Date());
		
		MessMealTempOrder messMealTempOrder = new MessMealTempOrder();
		messMealTempOrder.setAddNum(1);
		messMealTempOrder.setCardCode(messCard.getCardCode());
		messMealTempOrder.setMainId(messCard.getMainId());
		messMealTempOrder.setMoney(messAddFood.getPrice());
		messMealTempOrder.setTime(DateTimeKit.getDateTime(new Date(),"yyyy-MM-dd HH:mm:ss"));
		messMealTempOrderMapper.insertSelective(messMealTempOrder);
		messAddFoodOrderMapper.insertSelective(messAddFoodOrder);
		messConsumerDetailMapper.insertSelective(messConsumerDetail);
		return messCardMapper.updateByPrimaryKey(messCard);
	}

	@Override
	public MessAddFood getMessAddFoodById(Integer fId) {
		// TODO Auto-generated method stub
		try {
			return messAddFoodMapper.selectByPrimaryKey(fId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

}
