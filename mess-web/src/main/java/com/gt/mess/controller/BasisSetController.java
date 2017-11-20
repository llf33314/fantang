package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessMain;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessRootService;
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
 * 基础设置模块
 */
@Controller
@RequestMapping(value = "basisSet")
public class BasisSetController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessBasisSetService messBasisSetService;

    @Autowired
    private MessCardService messCardService;

    @Autowired
    private MessRootService messRootService;

    @Autowired
    private WxmpApiProperties wxmpApiProperties;

    //	基础设置模块
    /**
     * 基础设置
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/basisSet")
    public ModelAndView basisSet(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        try {
//            mv.addObject("videourl", course.urlquery("7"));
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            if(messBasisSet == null){
                mv.addObject("setType", "save");
            }else{
                mv.addObject("setType", "update");
            }
            String filePath =wxmpApiProperties.getAdminUrl();
            int num = messCardService.getNotCancelTicketNum(messMain.getId());
            mv.addObject("root", messRootService.getMessRootInfo(request));
            mv.addObject("url", filePath+"messMobile/"+messMain.getId()+"/79B4DE7C/index.do");
            mv.addObject("date", new Date());
            mv.addObject("num", num);
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("mainId", mainId);
            mv.setViewName("merchants/trade/mess/admin/basisSet/basisSet");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 保存或更新基础设置
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新基础设置",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateBasisSet")
    public void saveOrUpdateBasisSet(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messBasisSetService.saveOrUpdateBasisSet(params);
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
     * 更改票种类
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂  更改票种类",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "{type}/changeTicketType")
    public void changeTicketType(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("type") Integer type) {
        int data = 0;
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            data = messCardService.changeTicketType(messMain.getId(),type);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter out = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            out = response.getWriter();
            if(data != 0){
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
     * 一键清空饭卡以及未来订单
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂  一键清空饭卡以及未来订单",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "{mainId}/cleanTicketAndOrder")
    public void cleanTicketAndOrder(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable("mainId") Integer mainId) {
        int data = 0;
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            if(messMain.getId().equals(mainId)){
                data = messCardService.cleanTicketAndOrder(mainId);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PrintWriter out = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            out = response.getWriter();
            if(data != 0){
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
