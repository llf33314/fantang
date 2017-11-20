package com.gt.mess.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
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
	 * @param response
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/authorityManage")
	public ModelAndView authorityManage(HttpServletRequest request, HttpServletResponse response,
			Page<Map<String,Object>> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			Page<Map<String,Object>> authoritys =
					messAuthorityMemberService.getMessAuthorityMemberPageByMainId(page, mainId, 10);
			mv.addObject("authorityList", authoritys.getRecords());
			mv.addObject("messMain", messMain);
			mv.setViewName("merchants/trade/mess/admin/authority/authorityManage");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	
	
	/**
	 * 删除授权人员
	 * @param request
	 * @param response
	 * @param params
	 * @throws IOException
	 */
//	@SysLogAnnotation(description = "饭票删除权限信息", op_function = "4")
	@RequestMapping("/delAuthorityMember")
	public void delAuthorityMember(HttpServletRequest request,HttpServletResponse response,
	@RequestParam Map<String, Object> params) throws IOException {
		Map<String, Object> msg = null;
		try {
		    msg = messAuthorityMemberService.delAuthorityMember(params);
		} catch (BaseException e) {
			msg = new HashMap<>();
			msg.put("result", e.getCode());
			msg.put("message", e.getMessage());
			logger.error("取消授权人员错误！！");
			e.printStackTrace();
		} finally {
			CommonUtil.write(response, msg);
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
	public void delAuthorityMembers(HttpServletRequest request,HttpServletResponse response,
	@RequestParam Map<String, Object> params) throws IOException {
		Map<String, Object> msg = null;
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			params.put("busId", busUser.getId());
			String path =CommonUtil.getURL(request);
			params.put("path", path);//设置二维码访问域
		    msg = messAuthorityMemberService.delAuthorityMembers(params);
		} catch (BaseException e) {
			msg = new HashMap<>();
			msg.put("result", e.getCode());
			msg.put("message", e.getMessage());
			logger.error("微饭票重置授权信息并删除、更新授权人员错误！！");
			e.printStackTrace();
		} finally {
			CommonUtil.write(response, msg);
		}
	}
}
