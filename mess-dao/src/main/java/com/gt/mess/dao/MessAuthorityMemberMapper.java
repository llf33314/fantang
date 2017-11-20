package com.gt.mess.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gt.mess.entity.MessAuthorityMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessAuthorityMemberMapper extends BaseMapper<MessAuthorityMember> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MessAuthorityMember record);

    MessAuthorityMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessAuthorityMember record);

    int updateByPrimaryKey(MessAuthorityMember record);

	List<MessAuthorityMember> getMessAuthorityMember(Map<String, Object> param);

	int delAuthorityMembers(List<MessAuthorityMember> delList);

	int updateAuthorityMembers(List<MessAuthorityMember> updateList);

	List<Map<String, Object>> getMessAuthorityMemberPageByMainId(@Param( "page" ) Pagination page, Map<String, Object> param);
}