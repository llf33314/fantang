package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessOldManCardOrder;

import java.util.List;
import java.util.Map;

public interface MessOldManCardOrderMapper extends BaseMapper<MessOldManCardOrder> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessOldManCardOrder record);

    MessOldManCardOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessOldManCardOrder record);

    int updateByPrimaryKey(MessOldManCardOrder record);
    
    List<MessOldManCardOrder> getMessOldManCardOrderPageByMainId(Integer id);
    
    List<MessOldManCardOrder> selectMessOldManCardOrder(Map<String,Object> params);
    
    List<MessOldManCardOrder> getCommonMessCardOrderPageByMainId(Integer id);
    
    List<MessOldManCardOrder> selectMessCommonCardOrder(Map<String,Object> params);
    
    List<MessOldManCardOrder> getMessOldManCardOrderPageByMainId2(Integer id);
    
    List<MessOldManCardOrder> selectMessOldManCardOrder2(Map<String,Object> params);
    
}