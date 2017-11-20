package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessBasisSet;

public interface MessBasisSetMapper extends BaseMapper<MessBasisSet> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessBasisSet record);

    MessBasisSet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessBasisSet record);

    int updateByPrimaryKey(MessBasisSet record);
    
    MessBasisSet getMessBasisSetByMainId(Integer id);
    
}