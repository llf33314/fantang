package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessMenus;
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
     * 菜品管理入口
     *
     * @param request
     * @param response
     * @return
     */
//	@CommAnno(menu_url="mess/menuManageIndex.do")
    @RequestMapping(value = "/menuManageIndex")
    public ModelAndView menuManageIndex(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("merchants/trade/mess/admin/menuManage/menuManageIndex");
        return mv;
    }

    /**
     * 菜品管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "{week}/menuManage")
    public ModelAndView menuManage(HttpServletRequest request, HttpServletResponse response,
                                   Page<MessMenus> page, @PathVariable("week") Integer week) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("week", week);
            map.put("mainId", mainId);
            Page<MessMenus> messMenus =
                    messMenusService.getMessMenusPageByMainIdAndWeekNum(page, map, 9999);
            mv.addObject("messMenus", messMenus);
            mv.addObject("mainId", mainId);
            mv.addObject("week", week);
            mv.setViewName("merchants/trade/mess/admin/menuManage/menuManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 保存或更新菜品
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新菜单",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateMenu")
    public void saveOrUpdateMenu(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messMenusService.saveOrUpdateMenu(params);
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
     * 删除菜品
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除菜品",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{mId}/delMenu")
    public void saveOrUpdateMenus(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable("mId") Integer mId) {
        int data = 0;
        try {
            data = messMenusService.delMenu(mId);
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
     * 批量删除菜品
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量删除菜品",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "/delMenus")
    public void saveOrUpdateMenus(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam String mIds) {
        int data = 0;
        try {
            String [] tempArr = mIds.split(",");
            for(String mId : tempArr){
                data = messMenusService.delMenu(Integer.valueOf(mId));
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
