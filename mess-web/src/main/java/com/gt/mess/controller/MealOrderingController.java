package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessOrderManage;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessOrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订餐管理模块
 */
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
    @RequestMapping(value = "/getMessOrderList")
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
//	@SysLogAnnotation(description="微食堂 保存或更新订餐管理",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateMessOrderManage")
    public ResponseDTO saveOrUpdateOrderManage(HttpServletRequest request, @RequestParam Map<String,Object> params) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
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
