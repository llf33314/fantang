package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessAuthorityMemberService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "messAuthority")
public class MessAuthorityController {

	
	@Autowired
	private MessAuthorityMemberService messAuthorityMemberService;
	
	@Autowired
	private MessMainService messMainService;
	
	private Logger logger = Logger.getLogger(MessAuthorityController.class);
	
	
	@RequestMapping("authorityIndex")
	public String authorityIndex(HttpServletRequest request , HttpServletResponse response){
		Object indexurl = request.getParameter("indexurl");
		if(indexurl!=null){
			request.setAttribute("indexurl", indexurl);
		}
		return "merchants/trade/mess/admin/authority/authorityIndex";
	}
	
	/**
	 * 授权人员管理
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/authorityManage")
	public ResponseDTO authorityManage(HttpServletRequest request,
			Page<Map<String,Object>> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			Page<Map<String,Object>> authoritys =
					messAuthorityMemberService.getMessAuthorityMemberPageByMainId(page, mainId, 10);
			jsonData.put("authorityList", authoritys.getRecords());
			jsonData.put("messMain", messMain);
//			mv.setViewName("merchants/trade/mess/admin/authority/authorityManage");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("授权人员管理数据获取失败");
		}
	}

	/**
	 * 删除授权人员
	 * @param response
	 * @param params
	 * @throws IOException
	 */
//	@SysLogAnnotation(description = "饭票删除权限信息", op_function = "4")
	@RequestMapping("/delAuthorityMember")
	public ResponseDTO delAuthorityMember(HttpServletResponse response,
	@RequestParam Map<String, Object> params) throws IOException {
		try {
			return messAuthorityMemberService.delAuthorityMember(params);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("取消授权人员失败");
		}
	}
	
	/**
	 * 微饭票重置授权信息并删除、更新授权人员
	 * @param request
	 * @param response
	 * @param params
	 * @throws IOException
	 */
//	@SysLogAnnotation(description = "微饭票重置授权信息并删除、更新授权人员", op_function = "4")
	@RequestMapping("/delAuthorityMembers")
	public ResponseDTO delAuthorityMembers(HttpServletRequest request,HttpServletResponse response,
	@RequestParam Map<String, Object> params) throws IOException {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			params.put("busId", busUser.getId());
			params.put("path", CommonUtil.getURL(request));//设置二维码访问域
			return messAuthorityMemberService.delAuthorityMembers(params);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("微饭票重置授权信息并删除、更新授权人员失败");
		}
	}
}
