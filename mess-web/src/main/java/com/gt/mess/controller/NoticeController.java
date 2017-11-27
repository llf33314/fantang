package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessNotice;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessNoticeService;
import com.gt.mess.vo.SaveOrUpdateNoticeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * 公告管理模块
 */
@Api(description = "公告管理模块")
@Controller
@RequestMapping(value = "notice")
public class NoticeController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessNoticeService messNoticeService;


    //	公告管理模块

    /**
     * 公告管理
     * @param request
     * @return
     */
    @ApiOperation(value = "公告管理",notes = "公告管理数据获取",httpMethod = "GET")
    @RequestMapping(value = "/noticeManage", method = RequestMethod.GET)
    public ResponseDTO noticeManage(HttpServletRequest request,
                                     Page<MessNotice> page) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            Page<MessNotice> messNotices =
                    messNoticeService.getMessNoticePageByMainId(page, mainId, 1);
            if(messNotices != null && messNotices.getTotal() == 1){
                jsonData.put("type", 1);
            }else{
                jsonData.put("type", 0);
            }
            jsonData.put("messNotices", messNotices);
            jsonData.put("mainId", mainId);
//            mv.setViewName("merchants/trade/mess/admin/basisSet/noticeManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("公告管理数据获取失败");
        }
    }


    /**
     * 保存或更新公告
     * @return
     */
    @ApiOperation(value = "保存或更新公告",notes = "保存或更新公告",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateNotice", method = RequestMethod.POST)
    public ResponseDTO saveOrUpdateNotice(HttpServletRequest request,@Valid @ModelAttribute SaveOrUpdateNoticeVo saveVo) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            saveVo.setMainId(mainId);
            Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
            int data = messNoticeService.saveOrUpdateNotice(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新公告失败");
        }
    }

    /**
     * 删除公告
     * @return
     */
    @ApiOperation(value = "删除公告",notes = "删除公告",httpMethod = "GET")
    @RequestMapping(value = "{nId}/delNotice", method = RequestMethod.GET)
    public ResponseDTO delNotice(@ApiParam(name = "nId", value = "公告表ID")
                                 @PathVariable("nId") Integer nId) {
        try {
            int data = messNoticeService.delNotice(nId);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除公告失败");
        }
    }
}
