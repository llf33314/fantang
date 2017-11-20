package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessMealOrder;

import java.util.List;
import java.util.Map;

public interface MessMealOrderMapper extends BaseMapper<MessMealOrder> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessMealOrder record);

    MessMealOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessMealOrder record);

    int updateByPrimaryKey(MessMealOrder record);
    
    List<MessMealOrder> getMessMealOrderPageByMainId(Integer mainId);
    
    List<MessMealOrder> selectMealOrder(Map<String, Object> params);
    
    List<MessMealOrder> getMessMealOrderListforToday(Integer mainId);
    
    List<MessMealOrder> getMessMealOrderNum(Map<String, Object> mapObj);
    
    List<MessMealOrder> getMessMealOrderByMealCode(String mealCode);
    
    List<MessMealOrder> getMessMealOrderPageByCardIdAndMainId(Map<String,Integer> mapId);
    
    List<MessMealOrder> getBookedMessMealOrder(Map<String, Integer> mapId);
    
    List<MessMealOrder> getNotChooseMessMealOrder(Map<String, Integer> mapId);
    
    List<MessMealOrder> getMessMealOrderByMap(Map<String, Object> map);
    
    List<MessMealOrder> getPastMessMealOrderListByCardIdAndMainId(Map<String, Integer> mapId);
    
    List<MessMealOrder> getBookMessMealOrderByToDay(Map<String, Object> params);
    
    List<MessMealOrder> getMessMealOrderListforToday2(Integer mainId);
    
    int delNotCMealOrder(Map<String, Object> map);
    
    List<MessMealOrder> getNumsByDepIdAndMealType(Map<String, Integer> mapId);
    
    List<String> getMessCardListOrder(Map<String, Object> map);
    
    int cleanFutureOrder(Integer id);
    
    List<MessMealOrder> selectMealOrderForMonth(Map<String, Object> params);

    int selectMealOrderDayCount(Map<String,Object> countMap);
    
//    List<MessMealOrder> getMealOrderList(Map<String,Object> map);
}