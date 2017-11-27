package com.gt.mess.controller;

import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessOrderManage;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessOrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订餐管理模块
 */
@Api(description = "订餐管理模块")
@Controller
@RequestMapping(value = "mealOrdering")
public class MealOrderingController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessOrderManageService messOrderManageService;

    //	订餐管理模块

    /**
     * 获取不可订餐日子
     * @param request
     * @return
     */
    @ApiOperation(value = "获取不可订餐日子",notes = "获取不可订餐日子",httpMethod = "GET")
    @RequestMapping(value = "/getMessOrderList", method= RequestMethod.GET)
    public ResponseDTO getMessOrderList(HttpServletRequest request) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            List<MessOrderManage> messOrderManages =
                    messOrderManageService.getMessOrderManageListByMainId(mainId);
            StringBuffer buffer = new StringBuffer();
            for(MessOrderManage  messOrderManage: messOrderManages){
                buffer.append(messOrderManage.getDay().toString()+",");
            }
            return ResponseDTO.createBySuccess(buffer.toString());
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("获取不可订餐日子失败");
        }
    }

    /**
     * 保存或更新订餐管理
     * @param request
     * @return
     */
    @ApiOperation(value = "保存或更新订餐管理",notes = "保存或更新订餐管理",httpMethod = "GET")
    @RequestMapping(value = "/saveOrUpdateMessOrderManage", method = RequestMethod.GET)
    public ResponseDTO saveOrUpdateOrderManage(HttpServletRequest request,
                                               @ApiParam(name = "days", value = "格式如：1,2,3,4,5", required = true)
                                               @RequestParam String days) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> params = new HashMap<>();
            params.put("days", days);
            params.put("mainId", mainId);
            int data = messOrderManageService.saveOrUpdateMessOrderManage(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新订餐管理失败");
        }
    }
}
