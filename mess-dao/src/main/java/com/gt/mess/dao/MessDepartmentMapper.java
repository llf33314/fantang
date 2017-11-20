package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mess.entity.MessDepartment;

import java.util.List;

public interface MessDepartmentMapper extends BaseMapper<MessDepartment> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessDepartment record);

    MessDepartment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessDepartment record);

    int updateByPrimaryKey(MessDepartment record);
    
    List<MessDepartment> getMessDepartmentPageByMainId(Integer id);
}