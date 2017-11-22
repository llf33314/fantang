package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCard;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessCardGroupService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessMainService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 饭卡组管理模块
 */
@Controller
@RequestMapping(value = "cardGroup")
public class CardGroupController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessBasisSetService messBasisSetService;

    @Autowired
    private MessCardService messCardService;

    @Autowired
    private MessCardGroupService messCardGroupServcie;

    //	饭卡组管理模块

    /**
     * 饭卡组管理
     * @param request
     * @return
     */
    @RequestMapping(value = "/cardGroupManage")
    public ResponseDTO cardGroupManage(HttpServletRequest request,
                                        Page<MessCardGroup> page) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(mainId);
            Page<MessCardGroup> messCardGroups =
                    messCardGroupServcie.getMessCardGroupPageByMainId(page, mainId, 10);
            jsonData.put("mainId", mainId);
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("messCardGroups", messCardGroups);
//            mv.setViewName("merchants/trade/mess/admin/basisSet/cardGroupManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡组管理数据获取失败");
        }
    }


    /**
     * 保存或更新饭卡组
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新饭卡组",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateCardGroup")
    public ResponseDTO saveOrUpdateCardGroup(@RequestParam Map<String,Object> params) {
        try {
            int data = messCardGroupServcie.saveOrUpdateCardGroup(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新饭卡组失败");
        }
    }

    /**
     * 删除饭卡组
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除饭卡组",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{groupId}/delCardGroup")
    public ResponseDTO delCardGroup(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("groupId") Integer groupId) {
        try {
            int data = 0;
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Map<String,Integer> mapId = new HashMap<String, Integer>();
            mapId.put("mainId", messMain.getId());
            mapId.put("groupId", groupId);
            Integer messCardNums =
                    messCardService.getMessCardNumsByGroupId(mapId);
            if(messCardNums > 0){
                data = -1;
            }else{
                data = messCardGroupServcie.delCardGroup(groupId);
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除饭卡组失败");
        }
    }
}
