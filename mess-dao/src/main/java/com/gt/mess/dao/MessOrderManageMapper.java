package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessOrderManage;

import java.util.List;

public interface MessOrderManageMapper extends BaseMapper<MessOrderManage> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessOrderManage record);

    MessOrderManage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessOrderManage record);

    int updateByPrimaryKey(MessOrderManage record);
    
    List<MessOrderManage> getMessOrderManageListByMainId(Integer id);
    
    int delMessOrderManageByMainId(Integer id);
}