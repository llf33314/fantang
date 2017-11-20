package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessMenus;

import java.util.List;
import java.util.Map;

public interface MessMenusMapper extends BaseMapper<MessMenus> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessMenus record);

    MessMenus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessMenus record);

    int updateByPrimaryKey(MessMenus record);
    
    List<MessMenus> getMessMenusPageByMainIdAndWeekNum(Map<String,Object> map);
    
    List<MessMenus> getMessMenusListByTypeAndWeekNumforMainId(Map<String, Object> map);
}