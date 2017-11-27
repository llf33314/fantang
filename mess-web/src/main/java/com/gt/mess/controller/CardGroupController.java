package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessCardGroupService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.vo.SaveOrUpdateCardGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 饭卡组管理模块
 */
@Api(description = "饭卡组管理模块")
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
    @ApiOperation(value = "饭卡组管理",notes = "饭卡组管理数据获取",httpMethod = "GET")
    @RequestMapping(value = "/cardGroupManage", method= RequestMethod.GET)
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
            jsonData.put("messCardGroups", messCardGroups.getRecords());
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
    @ApiOperation(value = "保存或更新饭卡组",notes = "保存或更新饭卡组",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateCardGroup", method= RequestMethod.POST)
    public ResponseDTO saveOrUpdateCardGroup(@Valid @ModelAttribute SaveOrUpdateCardGroupVo saveVo) {
        try {
            int data = messCardGroupServcie.saveOrUpdateCardGroup(saveVo);
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
     * @param request
     * @return
     */
    @ApiOperation(value = "删除饭卡组",notes = "删除饭卡组",httpMethod = "GET")
    @RequestMapping(value = "{groupId}/delCardGroup", method= RequestMethod.GET)
    public ResponseDTO delCardGroup(HttpServletRequest request,
                                    @ApiParam(name = "groupId", value = "饭卡组ID", required = true)
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
