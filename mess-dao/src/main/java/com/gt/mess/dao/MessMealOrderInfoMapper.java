package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessMealOrderInfo;

import java.util.List;

public interface MessMealOrderInfoMapper extends BaseMapper<MessMealOrderInfo> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessMealOrderInfo record);

    MessMealOrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessMealOrderInfo record);

    int updateByPrimaryKey(MessMealOrderInfo record);
    
    List<MessMealOrderInfo> getMessMealOrderInfoListByMoId(Integer id);
}