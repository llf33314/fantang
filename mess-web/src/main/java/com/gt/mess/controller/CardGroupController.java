package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.entity.MessMain;
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
     * @param response
     * @return
     */
    @RequestMapping(value = "/cardGroupManage")
    public ModelAndView cardGroupManage(HttpServletRequest request, HttpServletResponse response,
                                        Page<MessCardGroup> page) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(mainId);
            Page<MessCardGroup> messCardGroups =
                    messCardGroupServcie.getMessCardGroupPageByMainId(page, mainId, 10);
            mv.addObject("mainId", mainId);
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("messCardGroups", messCardGroups);
            mv.setViewName("merchants/trade/mess/admin/basisSet/cardGroupManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }


    /**
     * 保存或更新饭卡组
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新饭卡组",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateCardGroup")
    public void saveOrUpdateCardGroup(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messCardGroupServcie.saveOrUpdateCardGroup(params);
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
     * 删除饭卡组
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除饭卡组",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{groupId}/delCardGroup")
    public void delCardGroup(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("groupId") Integer groupId) {
        int data = 0;
        try {
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
            }else if(data == -1) {
                map.put("status","error1");
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
