package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessOrderManageInfo;

import java.util.List;
import java.util.Map;

public interface MessOrderManageInfoMapper extends BaseMapper<MessOrderManageInfo> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessOrderManageInfo record);

    MessOrderManageInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessOrderManageInfo record);

    int updateByPrimaryKey(MessOrderManageInfo record);
    
    int delMessOrderManageInfoByOmId(Integer id);
    
    MessOrderManageInfo getMessOrderManageInfoByOmIdAndDay(Map<String,Integer> mapId);
    
    List<MessOrderManageInfo> getMessOrderManageInfoListByOmId(Integer id);
}