package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessOrderManage;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessOrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订餐管理模块
 */
@Controller
@RequestMapping(value = "mealOrdering")
public class MealOrderingController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessOrderManageService messOrderManageService;

    //	订餐管理模块

    /**
     * 订餐管理入口
     *
     * @param request
     * @param response
     * @return
     */
//	@CommAnno(menu_url="mess/messOrderManageIndex.do")
    @RequestMapping(value = "/messOrderManageIndex")
    public ModelAndView messOrderManageIndex(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("merchants/trade/mess/admin/messOrderManage/messOrderManageIndex");
        return mv;
    }

    /**
     * 订餐管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/messOrderManage")
    public ModelAndView messOrderManage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("merchants/trade/mess/admin/messOrderManage/messOrderManage");
        return mv;
    }

    /**
     * 获取不可订餐日子
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getMessOrderList")
    public void getMessOrderList(HttpServletRequest request, HttpServletResponse response
    ) {
        PrintWriter out = null;
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            out = response.getWriter();
            List<MessOrderManage> messOrderManages =
                    messOrderManageService.getMessOrderManageListByMainId(mainId);
            StringBuffer buffer = new StringBuffer();
            for(MessOrderManage  messOrderManage: messOrderManages){
                buffer.append(messOrderManage.getDay().toString()+",");
            }
            out.write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 保存或更新订餐管理
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新订餐管理",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateMessOrderManage")
    public void saveOrUpdateOrderManage(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            params.put("mainId", mainId);
            data = messOrderManageService.saveOrUpdateMessOrderManage(params);
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
            }else if(data == -1){
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
