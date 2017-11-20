package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessAddFood;
import com.gt.mess.entity.MessMain;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.MessAddFoodService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.util.QRcodeKit;
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
 * 加菜管理模块
 */
@Controller
@RequestMapping(value = "addFood")
public class AddFoodController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessAddFoodService messAddFoodService;

    @Autowired
    private WxmpApiProperties wxmpApiProperties;

    /**
     * 加菜管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addFoodManage")
    public ModelAndView addFoodManage(HttpServletRequest request, HttpServletResponse response,
                                      Page<MessAddFood> page) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Page<MessAddFood> messAddFoods =
                    messAddFoodService.getMessAddFoodPageByMainId(page, mainId, 10);
            mv.addObject("messAddFoods", messAddFoods);
            mv.addObject("mainId", mainId);
            mv.setViewName("merchants/trade/mess/admin/ticketManage/addFoodManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 保存或更新加菜表
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新加菜表",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateAddFood")
    public void saveOrUpdateAddFood(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messAddFoodService.saveOrUpdateAddFood(params);
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
     * 删除加菜表
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除加菜表",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{afId}/delAddFood")
    public void delAddFood(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable("afId") Integer afId) {
        int data = 0;
        try {
            data = messAddFoodService.delAddFood(afId);
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
     * 批量删除加菜表
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量删除加菜表",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "delAddFoods")
    public void delAddFoods(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam String afIds) {
        int data = 0;
        try {
            String [] tempArr = afIds.split(",");
            for(String afId : tempArr){
                data = messAddFoodService.delAddFood(Integer.valueOf(afId));
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

    /**
     * 加菜码
     * @param request
     * @param response
     */
    @RequestMapping(value = "{mainId}/{fdId}/79B4DE7C/addFoodQRcode")
    public void addFoodQRcode(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable Integer mainId, @PathVariable Integer fdId) {
        try {
            QRcodeKit.buildQRcode(wxmpApiProperties.getAdminUrl()+"messMobile/"+mainId+"/"+fdId+"/79B4DE7C/addFood.do", 300, 300, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
