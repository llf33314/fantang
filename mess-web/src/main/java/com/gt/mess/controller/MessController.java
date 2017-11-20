package com.gt.mess.controller;

import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.entity.MessMain;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.*;
import com.gt.mess.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 食堂后台
 * @author ZengWenXiang
 * @QQ 307848200
 */
@Controller
@RequestMapping(value = "mess")
public class MessController {

	@Autowired
	private MessMainService messMainService;

	@Autowired
	private MessBasisSetService messBasisSetService;

	@Autowired
	private MessCardService messCardService;

	@Autowired
	private MessAddFoodService messAddFoodService;

	@Autowired
	private MessMenusService messMenusService;

	@Autowired
	private MessOrderManageService messOrderManageService;

	@Autowired
	private MessNoticeService messNoticeService;

	@Autowired
	private MessDepartmentService messDepartmentService;

	@Autowired
	private MessDepartmentMapper messDepartmentMapper;

//	@Autowired
//	private IVoiceCourseService course;

	@Autowired
	private MessOldManCardService messOldManCardService;

	@Autowired
	private MessRootService messRootService;

	@Autowired
	private MessCardGroupService messCardGroupServcie;

	@Autowired
	private MessCardGroupMapper messCardGroupMapper;

	@Autowired
	private WxmpApiProperties wxmpApiProperties;

	
	/**
	 * 食堂入口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("messStart")
	public String messStart(HttpServletRequest request , HttpServletResponse response){
		Object indexurl = request.getParameter("indexurl");
		if(indexurl!=null){
			request.setAttribute("indexurl", indexurl);
		}
		BusUser busUser = SessionUtils.getLoginUser(request);
		if(messMainService.getMessMainByBusId(busUser.getId()) == null){
			MessMain messMain = new MessMain();
			messMain.setBusId(busUser.getId());
			messMain.setIsSceneAuthority(0);
			int count = messMainService.saveMessMain(messMain);
			//添加授权
			if(count > 0){
				String path = CommonUtil.getURL(request);
				Map<String, Object> param = new HashMap<String,Object>();
				param.put("path", path);//设置二维码访问域
				param.put("messMain", messMain);
				messMainService.saveMessMainAuthority(param);
			}
		}
		return "merchants/trade/mess/admin/messIndex";
	}




	

	
}
