package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gt.mess.entity.MessAddFood;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessAddFoodMapper extends BaseMapper<MessAddFood> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessAddFood record);

    MessAddFood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessAddFood record);

    int updateByPrimaryKey(MessAddFood record);
    
    List<MessAddFood> getMessAddFoodPageByMainId(@Param( "page" ) Pagination page, Integer id);
}