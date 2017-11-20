package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessTopUpOrder;

import java.util.List;
import java.util.Map;

public interface MessTopUpOrderMapper extends BaseMapper<MessTopUpOrder> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessTopUpOrder record);

    MessTopUpOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessTopUpOrder record);

    int updateByPrimaryKey(MessTopUpOrder record);
    
    List<MessTopUpOrder> getMessTopUpOrderPageByMainId(Integer id);
    
    List<MessTopUpOrder> selectTopUpOrder(Map<String, Object> params);
    
    MessTopUpOrder getMessTopUpOrderByOrderNo(String orderNo);
}