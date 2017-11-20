package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessCardTicket;

import java.util.List;
import java.util.Map;

public interface MessCardTicketMapper extends BaseMapper<MessCardTicket> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessCardTicket record);

    MessCardTicket selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessCardTicket record);

    int updateByPrimaryKey(MessCardTicket record);
    
    int delTicketByCardId(Integer id);
 
    int delNotCancelU(Integer id);
    
    int delNotCancelD(Integer id);
    
    int delNotCancelUByCardId(Integer id);
    
    int delNotCancelDByCardId(Integer id);
    
    int getNotCancelTicketNum(Integer mainId);
    
    List<MessCardTicket> getNotCancelTicketByCardIdAndType(Map<String,Integer> mapId);
    
    List<MessCardTicket> getMessCardTicketListByCardId(Integer cardId);
}