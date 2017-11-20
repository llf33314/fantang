package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessCancelTicketInfo;

public interface MessCancelTicketInfoMapper extends BaseMapper<MessCancelTicketInfo> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessCancelTicketInfo record);

    MessCancelTicketInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessCancelTicketInfo record);

    int updateByPrimaryKey(MessCancelTicketInfo record);
}