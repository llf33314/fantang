package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gt.mess.entity.MessAddFoodOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessAddFoodOrderMapper extends BaseMapper<MessAddFoodOrder> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessAddFoodOrder record);

    MessAddFoodOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessAddFoodOrder record);

    int updateByPrimaryKey(MessAddFoodOrder record);
    
    List<MessAddFoodOrder> getMessAddFoodOrderPageByMainId(@Param( "page" ) Pagination page, Integer id);
    
    List<MessAddFoodOrder> selectAddFoodOrders(@Param( "page" ) Pagination page, Map<String,Object> params);

    List<MessAddFoodOrder> selectAddFoodOrders(Map<String,Object> params);
}