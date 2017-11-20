package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessOldManCard;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessOldManCardService;
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
import java.util.List;
import java.util.Map;

/**
 * 老人卡模块
 */
@Controller
@RequestMapping(value = "oldManCard")
public class OldManCardController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessDepartmentMapper messDepartmentMapper;

    @Autowired
    private MessOldManCardService messOldManCardService;

    //	老人卡模块
    /**
     * 老人卡管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/oldManCardManage")
    public ModelAndView oldManCardManage(HttpServletRequest request, HttpServletResponse response,
                                         Page<MessOldManCard> page) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Page<MessOldManCard> messOldManCards =
                    messOldManCardService.getMessOldManCardPageByMainId(page, mainId, 10);
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            mv.addObject("messDepartments", messDepartments);
            mv.addObject("messOldManCards", messOldManCards);
            mv.addObject("mainId", mainId);
            mv.setViewName("merchants/trade/mess/admin/ticketManage/oldManCardManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 老人卡管理(搜索)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectOldManCardManage")
    public ModelAndView selectOldManCardManage(HttpServletRequest request, HttpServletResponse response,
                                               Page<MessOldManCard> page,@RequestParam Map<String,Object> params) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            params.put("mainId", mainId);
            Page<MessOldManCard> messOldManCards =
                    messOldManCardService.selectOldManCardManage(page, params, 10);
            mv.addObject("search", params.get("cardCode").toString());
            mv.addObject("messOldManCards", messOldManCards);
            mv.addObject("mainId", mainId);
            mv.setViewName("merchants/trade/mess/admin/ticketManage/oldManCardManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 保存或更新老人卡表
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新老人卡表",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOldManCard")
    public void saveOldManCard(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messOldManCardService.saveOldManCard(params);
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
     * 老人卡补票（扣票）
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 老人卡补票（扣票）",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/addOrDelTicket")
    public void addOrDelTicket(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messOldManCardService.addOrDelTicket(params);
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
     * 删除老人卡
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除老人卡",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "/{id}/delOldManCard")
    public void delOldManCard(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("id") Integer id) {
        int data = 0;
        try {
            data = messOldManCardService.delOldManCard(id);
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
     * 老人卡一键补票（扣票）
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂  老人卡一键补票（扣票）",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/addOrDelTickets")
    public void addOrDelTickets(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            String [] ids = params.get("ids").toString().split(",");
            for(String id : ids){
                params.put("id", id);
                data = messOldManCardService.addOrDelTicket(params);
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
