package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessOldManCard;

import java.util.List;
import java.util.Map;

public interface MessOldManCardMapper extends BaseMapper<MessOldManCard> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessOldManCard record);

    MessOldManCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessOldManCard record);

    int updateByPrimaryKey(MessOldManCard record);
    
    List<MessOldManCard> getMessOldManCardPageByMainId(Integer id);
    
    List<MessOldManCard> selectOldManCardManage(Map<String,Object> params);
}