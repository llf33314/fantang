package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessNotice;

import java.util.List;
import java.util.Map;

public interface MessNoticeMapper extends BaseMapper<MessNotice> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessNotice record);

    MessNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessNotice record);

    int updateByPrimaryKey(MessNotice record);
    
    List<MessNotice> getMessNoticePageByMainId(Integer mainId);
    
    List<MessNotice> getMessNoticeListByMainId(Map<String,Integer> mapId);
}