package com.gt.mess.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gt.mess.exception.BaseException;

/**
 * 
 * <B> Function : </B> MessRootService <br>
 * <B> Description : </B> 微饭堂权限管理 <br>
 * <B> Company : </B> <br>
 *
 * @author Pan_Siran <br>
 * @data 2017年1月11日 上午11:37:43 <br>
 * @version v1.0
 *
 */
public interface MessRootService {

	/**
	 * 获取微饭堂的权限管理
	 * @param request
	 * @return oneClickClear 一键清空功能 0：有 1：无
	 * @throws BaseException
	 */
	public Map<String, Object> getMessRootInfo(HttpServletRequest request) throws BaseException;

}
