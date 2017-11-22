package com.gt.mess.service;


import com.gt.mess.entity.MessAuthorityMember;
import com.gt.mess.exception.BaseException;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;
import java.util.Map;

public interface MessAuthorityMemberService {

	Page<Map<String,Object>> getMessAuthorityMemberPageByMainId(Page<Map<String, Object>> page, Integer mainId, int i);

	Map<String, Object> delAuthorityMember(Map<String, Object> params) throws BaseException;

	Map<String, Object> delAuthorityMembers(Map<String, Object> params) throws BaseException;

	Map<String, Object> saveAuthority(Map<String, Object> params) throws BaseException;

	List<MessAuthorityMember> getMessAuthorityByMember(Integer mainId, Integer memberId);

}