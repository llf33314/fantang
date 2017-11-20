package com.gt.mess.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gt.mess.entity.MessMealTempOrder;

public interface MessMealTempOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessMealTempOrder record);

    int insertSelective(MessMealTempOrder record);

    MessMealTempOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessMealTempOrder record);

    int updateByPrimaryKey(MessMealTempOrder record);

	long countMessMealTempByMainId(Integer mainId);

	void deletLastByMainId(Integer mainId);

	List<Map<String, Object>> getMessMealTempOrderByMainId(@Param("mainId") Integer mainId, @Param("nums") int nums);
    
}