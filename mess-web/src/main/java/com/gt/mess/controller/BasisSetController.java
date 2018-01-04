package com.gt.mess.controller;

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
import com.gt.mess.vo.SaveOrUpdateBasisSetVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * 基础设置模块
 */
@Api(description = "基础设置模块")
@RestController
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
    @ApiOperation(value = "基础设置",notes = "基础设置数据获取",httpMethod = "GET")
    @RequestMapping(value = "/basisSet", method= RequestMethod.GET)
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
    @ApiOperation(value = "保存或更新基础设置",notes = "保存或更新基础设置",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateBasisSet", method= RequestMethod.POST)
    public ResponseDTO saveOrUpdateBasisSet(@Valid @RequestBody SaveOrUpdateBasisSetVo setVo) {
        try {
            int data = messBasisSetService.saveOrUpdateBasisSet(setVo);
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
    @ApiOperation(value = "更改票种类",notes = "更改票种类",httpMethod = "GET")
    @RequestMapping(value = "{type}/changeTicketType", method= RequestMethod.GET)
    public ResponseDTO changeTicketType(HttpServletRequest request,
                                        @ApiParam(name = "type", value = "type为0时删除未使用的通用票,否则删除未使用的非通用票", required = true)
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
    @ApiOperation(value = "更改票种类",notes = "更改票种类",httpMethod = "GET")
    @RequestMapping(value = "{mainId}/cleanTicketAndOrder", method= RequestMethod.GET)
    public ResponseDTO cleanTicketAndOrder(@ApiParam(name = "mainId", value = "主表ID", required = true)
                                               @PathVariable("mainId") Integer mainId) {
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
