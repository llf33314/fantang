package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCard;
import com.gt.mess.entity.MessMain;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessMainService;
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
import java.util.Map;

/**
 * 饭卡应用模块
 */
@Controller
@RequestMapping(value = "cardApply")
public class CardApplyController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessBasisSetService messBasisSetService;

    @Autowired
    private MessCardService messCardService;

    /**
     * 饭卡应用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/cardApply")
    public ModelAndView cardApply(HttpServletRequest request, HttpServletResponse response,
                                  Page<MessCard> page) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Page<MessCard> messCards =
                    messCardService.getMessCardPageByMainId(page, mainId, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("messCards", messCards);
            mv.addObject("mainId", mainId);
            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 余额充值
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 余额充值",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/topUpMoney")
    public void topUpMoney(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messCardService.topUpMoney(params);
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
     * 饭票购买
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 饭票购买",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/buyTicket")
    public void buyTicket(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messCardService.buyTicket(params);
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
     * 补助
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 补助",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/subsidyTicket")
    public void subsidyTicket(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messCardService.subsidyTicket(params);
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
     * 扣除饭票（只限免费）
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 扣除饭票（只限免费）",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/deductTicket")
    public void deductTicket(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messCardService.deductTicket(params);
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
     * 一键补助
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 一键补助",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/subsidyTickets")
    public void subsidyTickets(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            String [] tempArr = params.get("ids").toString().split(",");
            for(String cardId : tempArr){
                params.put("id", cardId);
                data = messCardService.subsidyTicket(params);
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
     * 一键清票
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 一键清票",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/emptyTickets")
    public void emptyTickets(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            String [] tempArr = params.get("ids").toString().split(",");
            for(String cardId : tempArr){
                data = messCardService.emptyTicket(Integer.valueOf(cardId));
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
     * 饭卡应用(根据卡号查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectCardApplyByCardCode")
    public ModelAndView selectCardApplyByCardCode(HttpServletRequest request, HttpServletResponse response,
                                                  Page<MessCard> page,@RequestParam String search) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mainId", mainId);
            map.put("cardCode", search);
            Page<MessCard> messCards =
                    messCardService.selectCardApplyByCardCode(page, map, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("search", search);
            mv.addObject("messCards", messCards);
            mv.addObject("selectType", 0);//0卡号查询 1名字查询 2部门查询
            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 饭卡应用(根据名字查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectCardApplyByName")
    public ModelAndView selectCardApplyByName(HttpServletRequest request, HttpServletResponse response,
                                              Page<MessCard> page,@RequestParam String search) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mainId", mainId);
            map.put("name", search);
            Page<MessCard> messCards =
                    messCardService.selectCardApplyByName(page, map, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("search", search);
            mv.addObject("messCards", messCards);
            mv.addObject("selectType", 1);//0卡号查询 1名字查询 2部门查询
            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 饭卡应用(根据部门查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectCardApplyByDepartment")
    public ModelAndView selectCardApplyByDepartment(HttpServletRequest request, HttpServletResponse response,
                                                    Page<MessCard> page,@RequestParam String search) {
        ModelAndView mv = new ModelAndView();
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mainId", mainId);
            map.put("department", search);
            Page<MessCard> messCards =
                    messCardService.selectCardApplyByDepartment(page, map, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("messCards", messCards);
            mv.addObject("search", search);
            mv.addObject("selectType", 2);//0卡号查询 1名字查询 2部门查询
            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }
}
