package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessConsumerDetail;

import java.util.List;
import java.util.Map;

public interface MessConsumerDetailMapper extends BaseMapper<MessConsumerDetail> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessConsumerDetail record);

    MessConsumerDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessConsumerDetail record);

    int updateByPrimaryKey(MessConsumerDetail record);
    
    List<MessConsumerDetail> getMessConsumerDetailPageByCardIdAndMainId(Map<String,Integer> mapId);
}