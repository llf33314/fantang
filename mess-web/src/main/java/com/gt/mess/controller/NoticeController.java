package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessNotice;
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
     * @param response
     * @return
     */
    @RequestMapping(value = "/noticeManage")
    public ModelAndView noticeManage(HttpServletRequest request, HttpServletResponse response,
                                     Page<MessNotice> page) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            Page<MessNotice> messNotices =
                    messNoticeService.getMessNoticePageByMainId(page, mainId, 1);
            if(messNotices != null && messNotices.getTotal() == 1){
                mv.addObject("type", 1);
            }else{
                mv.addObject("type", 0);
            }
            mv.addObject("messNotices", messNotices);
            mv.addObject("mainId", mainId);
            mv.setViewName("merchants/trade/mess/admin/basisSet/noticeManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }


    /**
     * 保存或更新公告
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新公告",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateNotice")
    public void saveOrUpdateNotice(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messNoticeService.saveOrUpdateNotice(params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter out = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            out = response.getWriter();
            if(data == 1){
                map.put("status","success");
            }else{
                map.put("status","error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("status","error");
        } finally {
            out.write(JSON.toJSONString(map).toString());
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 删除公告
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除公告",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{nId}/delNotice")
    public void delNotice(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("nId") Integer nId) {
        int data = 0;
        try {
            data = messNoticeService.delNotice(nId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter out = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            out = response.getWriter();
            if(data == 1){
                map.put("status","success");
            }else{
                map.put("status","error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("status","error");
        } finally {
            out.write(JSON.toJSONString(map).toString());
            if (out != null) {
                out.close();
            }
        }
    }
}
