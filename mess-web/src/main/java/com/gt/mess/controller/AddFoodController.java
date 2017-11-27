package com.gt.mess.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessAddFood;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.MessAddFoodService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.util.QRcodeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 加菜管理模块
 */
@Api(description = "加菜管理模块")
@Controller
@RequestMapping(value = "addFood")
public class AddFoodController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessAddFoodService messAddFoodService;

    @Autowired
    private WxmpApiProperties wxmpApiProperties;

    /**
     * 加菜列表
     * @param request
     * @return
     */
    @ApiOperation(value = "加菜列表",notes = "加菜列表数据获取",httpMethod = "GET")
    @RequestMapping(value = "/addFoodManage", method= RequestMethod.GET)
    public ResponseDTO addFoodManage(HttpServletRequest request, Page<MessAddFood> page) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Page<MessAddFood> messAddFoods =
                    messAddFoodService.getMessAddFoodPageByMainId(page, messMain.getId(), 10);
            return ResponseDTO.createBySuccess(messAddFoods.getRecords());
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("加菜列表获取失败");
        }
    }

    /**
     * 保存或更新加菜表
     * @return
     */
    @ApiOperation(value = "保存或更新加菜表",notes = "保存或更新加菜表",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateAddFood", method= RequestMethod.POST)
    public ResponseDTO saveOrUpdateAddFood(@ApiParam(name = "saveType", value = "save:为保存，否则为更新", required = true)
                                               @RequestParam String saveType,
                                           @ApiParam(name = "mainId", value = "主表ID", required = true)
                                               @RequestParam Integer mainId,
                                           @ApiParam(name = "id", value = "加菜表ID")
                                                       Integer id,
                                           @ApiParam(name = "comment", value = "备注", required = true)
                                               @RequestParam String comment,
                                           @ApiParam(name = "price", value = "金额", required = true)
                                               @RequestParam Double price) {
        try {
            int data = messAddFoodService.saveOrUpdateAddFood(saveType,mainId,id,comment,price);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新加菜表失败");
        }
    }

    /**
     * 删除加菜表
     * @return
     */
    @ApiOperation(value = "删除加菜表",notes = "删除加菜表",httpMethod = "POST")
    @RequestMapping(value = "/delAddFood", method= RequestMethod.POST)
    public ResponseDTO delAddFood(@ApiParam(name = "afId", value = "加菜表数据ID", required = true)
                                      @RequestParam("afId") Integer afId) {
        try {
            int data = messAddFoodService.delAddFood(afId);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除加菜表失败");
        }
    }

    /**
     * 批量删除加菜表
     * @return
     */
    @ApiOperation(value = "批量删除加菜表",notes = "批量删除加菜表",httpMethod = "POST")
    @RequestMapping(value = "delAddFoods", method= RequestMethod.POST)
    public ResponseDTO delAddFoods(@ApiParam(name = "afIds", value = "加菜表数据ID，格式：1,2,3,4", required = true)@RequestParam String afIds) {
        try {
            int data = 0;
            String [] tempArr = afIds.split(",");
            for(String afId : tempArr){
                data = messAddFoodService.delAddFood(Integer.valueOf(afId));
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("批量删除加菜表失败");
        }
    }

    /**
     * 加菜码
     * @param response
     */
    @ApiOperation(value = "加菜码",notes = "加菜二维码获取",httpMethod = "GET")
    @RequestMapping(value = "{mainId}/{afId}/79B4DE7C/addFoodQRcode", method= RequestMethod.GET)
    public void addFoodQRcode(HttpServletResponse response,
                              @ApiParam(name = "mainId", value = "主表ID", required = true) @PathVariable Integer mainId,
                              @ApiParam(name = "afId", value = "加菜表数据ID", required = true) @PathVariable Integer afId) {
        try {
            QRcodeKit.buildQRcode(wxmpApiProperties.getAdminUrl()+"messMobile/"+mainId+"/"+afId+"/79B4DE7C/addFood.do", 300, 300, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
