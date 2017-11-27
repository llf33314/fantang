package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessMenus;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessMenusService;
import com.gt.mess.vo.SaveOrUpdateMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 菜品管理模块
 */
@Api(description = "菜品管理模块")
@Controller
@RequestMapping(value = "basisSet")
public class MenuController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessMenusService messMenusService;


    //	菜品管理模块

    /**
     * 菜品管理
     * @param request
     * @return
     */
    @ApiOperation(value = "菜品管理",notes = "菜品管理数据获取",httpMethod = "GET")
    @RequestMapping(value = "{week}/menuManage", method = RequestMethod.GET)
    public ResponseDTO menuManage(HttpServletRequest request,
                                  Page<MessMenus> page,
                                  @ApiParam(name = "week", value = "星期几，如：星期一对应1", required = true)
                                  @PathVariable("week") Integer week) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("week", week);
            map.put("mainId", mainId);
            Page<MessMenus> messMenus =
                    messMenusService.getMessMenusPageByMainIdAndWeekNum(page, map, 9999);
            jsonData.put("messMenus", messMenus);
            jsonData.put("mainId", mainId);
            jsonData.put("week", week);
//            mv.setViewName("merchants/trade/mess/admin/menuManage/menuManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("菜品管理数据获取失败");
        }
    }

    /**
     * 保存或更新菜品
     * @return
     */
    @ApiOperation(value = "保存或更新菜品",notes = "保存或更新菜品",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateMenu", method = RequestMethod.POST)
    public ResponseDTO saveOrUpdateMenu(@Valid @ModelAttribute SaveOrUpdateMenuVo saveVo) {
        try {
            int data = messMenusService.saveOrUpdateMenu(saveVo);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新菜品失败");
        }
    }

    /**
     * 删除菜品
     * @return
     */
    @ApiOperation(value = "删除菜品",notes = "删除菜品",httpMethod = "GET")
    @RequestMapping(value = "{mId}/delMenu", method = RequestMethod.GET)
    public ResponseDTO saveOrUpdateMenus(@ApiParam(name = "mId", value = "菜品表ID", required = true)
                                         @PathVariable("mId") Integer mId) {
        try {
            int data = messMenusService.delMenu(mId);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除菜品失败");
        }
    }

    /**
     * 批量删除菜品
     * @param mIds
     * @return
     */
    @ApiOperation(value = "批量删除菜品",notes = "批量删除菜品",httpMethod = "POST")
    @RequestMapping(value = "/delMenus", method = RequestMethod.POST)
    public ResponseDTO saveOrUpdateMenus(@ApiParam(name = "mIds", value = "菜品表ID列表，如：1,2,3,4", required = true)
                                         @RequestParam String mIds) {
        try {
            int data = 0;
            String [] tempArr = mIds.split(",");
            for(String mId : tempArr){
                data = messMenusService.delMenu(Integer.valueOf(mId));
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("批量删除菜品失败");
        }
    }
}
