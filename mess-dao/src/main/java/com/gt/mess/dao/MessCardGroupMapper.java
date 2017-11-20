package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessCardGroup;

import java.util.List;

public interface MessCardGroupMapper extends BaseMapper<MessCardGroup> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessCardGroup record);

    MessCardGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessCardGroup record);

    int updateByPrimaryKey(MessCardGroup record);
    
    List<MessCardGroup> getMessCardGroupPageByMainId(Integer id);
}