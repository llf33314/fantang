package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessRootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础设置模块
 */
@Controller
@RequestMapping(value = "basisSet")
public class BasisSetController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessBasisSetService messBasisSetService;

    @Autowired
    private MessCardService messCardService;

    @Autowired
    private MessRootService messRootService;

    @Autowired
    private WxmpApiProperties wxmpApiProperties;

    //	基础设置模块
    /**
     * 基础设置
     * @param request
     * @return
     */
    @RequestMapping(value = "/basisSet")
    public ResponseDTO basisSet(HttpServletRequest request) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            if(messBasisSet == null){
                jsonData.put("setType", "save");
            }else{
                jsonData.put("setType", "update");
            }
            int num = messCardService.getNotCancelTicketNum(messMain.getId());
            jsonData.put("root", messRootService.getMessRootInfo(request));
            jsonData.put("url", wxmpApiProperties.getAdminUrl()+"messMobile/"+messMain.getId()+"/79B4DE7C/index.do");
            jsonData.put("date", new Date());
            jsonData.put("num", num);
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("mainId", mainId);
//            mv.setViewName("merchants/trade/mess/admin/basisSet/basisSet");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("基础设置数据获取失败");
        }
    }

    /**
     * 保存或更新基础设置
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新基础设置",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateBasisSet")
    public ResponseDTO saveOrUpdateBasisSet(@RequestParam Map<String,Object> params) {
        try {
            int data = messBasisSetService.saveOrUpdateBasisSet(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新基础设置失败");
        }
    }

    /**
     * 更改票种类
     * @param request
     * @return
     */
//	@SysLogAnnotation(description="微食堂  更改票种类",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "{type}/changeTicketType")
    public ResponseDTO changeTicketType(HttpServletRequest request,
                                 @PathVariable("type") Integer type) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            int data = messCardService.changeTicketType(messMain.getId(),type);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("更改票种类失败");
        }
    }

    /**
     * 一键清空饭卡以及未来订单
     * @return
     */
//	@SysLogAnnotation(description="微食堂  一键清空饭卡以及未来订单",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "{mainId}/cleanTicketAndOrder")
    public ResponseDTO cleanTicketAndOrder(@PathVariable("mainId") Integer mainId) {
        try {
            int data = messCardService.cleanTicketAndOrder(mainId);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("更改票种类失败");
        }
    }
}
