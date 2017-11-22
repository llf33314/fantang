package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessMenus;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessMenusService;
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
 * 菜品管理模块
 */
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
    @RequestMapping(value = "{week}/menuManage")
    public ResponseDTO menuManage(HttpServletRequest request,
                                   Page<MessMenus> page, @PathVariable("week") Integer week) {
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
//	@SysLogAnnotation(description="微食堂 保存或更新菜单",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateMenu")
    public ResponseDTO saveOrUpdateMenu(@RequestParam Map <String,Object> params) {
        try {
            int data = messMenusService.saveOrUpdateMenu(params);
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
//	@SysLogAnnotation(description="微食堂 删除菜品",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{mId}/delMenu")
    public ResponseDTO saveOrUpdateMenus(@PathVariable("mId") Integer mId) {
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
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量删除菜品",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "/delMenus")
    public ResponseDTO saveOrUpdateMenus(HttpServletRequest request, HttpServletResponse response,
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
