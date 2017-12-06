package com.gt.mess.controller;

import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessMain;
import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.exception.BaseException;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.*;
import com.gt.mess.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 食堂后台
 * @author ZengWenXiang
 * @QQ 307848200
 */
@Api(description = "食堂后台")
@Controller
@RequestMapping(value = "mess")
public class MessController {

	@Autowired
	private MessMainService messMainService;

	/**
	 * 食堂入口
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "食堂入口",notes = "食堂入口",httpMethod = "GET")
	@RequestMapping(value = "messStart",method = RequestMethod.GET)
	public ResponseDTO messStart(HttpServletRequest request){
		try {
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
			return ResponseDTO.createBySuccess();
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException(ResponseEnums.SYSTEM_ERROR);
		}
	}




	

	
}
