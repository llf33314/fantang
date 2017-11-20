package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessMain;

import java.util.List;

public interface MessMainMapper extends BaseMapper<MessMain> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessMain record);

    MessMain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessMain record);

    int updateByPrimaryKey(MessMain record);
    
    List<MessMain> getMessMainByBusId(Integer id);
}