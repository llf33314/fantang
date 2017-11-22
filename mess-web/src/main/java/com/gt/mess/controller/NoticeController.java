package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessNotice;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessNoticeService;
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
 * 公告管理模块
 */
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
    @RequestMapping(value = "/noticeManage")
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
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新公告",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateNotice")
    public ResponseDTO saveOrUpdateNotice(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam Map<String,Object> params) {
        try {
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
//	@SysLogAnnotation(description="微食堂 删除公告",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{nId}/delNotice")
    public ResponseDTO delNotice(@PathVariable("nId") Integer nId) {
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
