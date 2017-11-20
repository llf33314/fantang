package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessCancelTicket;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessCancelTicketMapper extends BaseMapper<MessCancelTicket> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessCancelTicket record);

    MessCancelTicket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessCancelTicket record);

    int updateByPrimaryKey(MessCancelTicket record);
    
    List<MessCancelTicket> getMessCancelTicketPageByMainId(Integer id);
    
    List<MessCancelTicket> selectCancelRecord(Map<String,Object> params);

	List<Map<String, Object>> getMessCancelTicketPageByMainIdNew(@Param("mainId") Integer mainId, @Param("nums") int nums);

	int queryCancelTicketMember(Map<String, Object> params);
    
}