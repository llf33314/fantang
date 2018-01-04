package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gt.mess.entity.MessConsumerDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessConsumerDetailMapper extends BaseMapper<MessConsumerDetail> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessConsumerDetail record);

    MessConsumerDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessConsumerDetail record);

    int updateByPrimaryKey(MessConsumerDetail record);
    
    List<MessConsumerDetail> getMessConsumerDetailPageByCardIdAndMainId(@Param( "page" ) Pagination page, Map<String,Integer> mapId);
}