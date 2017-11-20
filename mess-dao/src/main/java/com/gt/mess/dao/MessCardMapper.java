package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessCard;

import java.util.List;
import java.util.Map;

public interface MessCardMapper extends BaseMapper<MessCard> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessCard record);

    MessCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessCard record);

    int updateByPrimaryKey(MessCard record);
    
    List<MessCard> getMessCardPageByMainId(Integer id);
    
    List<MessCard> commonCard(Integer id);
    
    List<MessCard> selectCardApplyByCardCode(Map<String,Object> map);
    
    List<MessCard> selectCardApplyByName(Map<String,Object> map);
    
    List<MessCard> selectCardApplyByDepartment(Map<String,Object> map);
    
    int cleanTicketNum(Integer id);
    
    int changeCardDepartment(Map<String,Object> map);
    
    int getMessCardNumsByDepId(Map<String, Integer> mapId);
    
    int getMessCardNumsByGroupId(Map<String, Integer> mapId);
    
    List<MessCard> getMessCardListByMap(Map<String,Object> map);
    
//  手机端接口
    
    List<MessCard> getMessCardByMainIdAndMemberId(Map<String, Integer> mapId);
    
    List<MessCard> getMessCardByCardCodeAndMainId(Map<String, Object> map);
    
//  小程序接口
    
    List<MessCard> getMessCardByMainIdAndOpenId(Map<String, Object> mapId);
    
    List<MessCard> getMessCardByOpenId(String openId);
    
    List<MessCard> getMessCardByCardCode(String cardCode);
}