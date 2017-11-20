package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gt.mess.entity.MessBuyTicketOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessBuyTicketOrderMapper extends BaseMapper<MessBuyTicketOrder> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessBuyTicketOrder record);

    MessBuyTicketOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessBuyTicketOrder record);

    int updateByPrimaryKey(MessBuyTicketOrder record);
    
    List<MessBuyTicketOrder> getMessBuyTicketOrderPageMainId(@Param( "page" ) Pagination page,Integer id);
    
    List<MessBuyTicketOrder> selectBuyTicketStatistics(@Param( "page" ) Pagination page, Map<String,Object> params);

    List<MessBuyTicketOrder> selectBuyTicketStatistics(Map<String,Object> params);
    
    List<MessBuyTicketOrder> getSubsidyTicketOrderPageMainId(Integer id);
    
    List<MessBuyTicketOrder> selectSubsidyTicket(Map<String,Object> params);
}