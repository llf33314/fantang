package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCard;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
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
     * @return
     */
    @RequestMapping(value = "/cardApply")
    public ResponseDTO cardApply(HttpServletRequest request, Page<MessCard> page) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Page<MessCard> messCards =
                    messCardService.getMessCardPageByMainId(page, mainId, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("messCards", messCards);
            jsonData.put("mainId", mainId);
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡应用数据获取失败");
        }
    }

    /**
     * 余额充值
     * @return
     */
//	@SysLogAnnotation(description="微食堂 余额充值",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/topUpMoney")
    public ResponseDTO topUpMoney(@RequestParam Map<String,Object> params) {
        try {
            int data = messCardService.topUpMoney(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("余额充值失败");
        }
    }

    /**
     * 饭票购买
     * @return
     */
//	@SysLogAnnotation(description="微食堂 饭票购买",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/buyTicket")
    public ResponseDTO buyTicket(@RequestParam Map <String,Object> params) {
        try {
            int data = messCardService.buyTicket(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭票购买失败");
        }
    }

    /**
     * 补助
     * @return
     */
//	@SysLogAnnotation(description="微食堂 补助",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/subsidyTicket")
    public ResponseDTO subsidyTicket(@RequestParam Map <String,Object> params) {
        try {
            int data = messCardService.subsidyTicket(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭票购买失败");
        }
    }

    /**
     * 扣除饭票（只限免费）
     * @return
     */
//	@SysLogAnnotation(description="微食堂 扣除饭票（只限免费）",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/deductTicket")
    public ResponseDTO deductTicket(@RequestParam Map <String,Object> params) {
        try {
            int data = messCardService.deductTicket(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("扣除饭票（只限免费）失败");
        }
    }

    /**
     * 一键补助
     * @return
     */
//	@SysLogAnnotation(description="微食堂 一键补助",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/subsidyTickets")
    public ResponseDTO subsidyTickets(@RequestParam Map <String,Object> params) {
        try {
            int data = 0;
            String [] tempArr = params.get("ids").toString().split(",");
            for(String cardId : tempArr){
                params.put("id", cardId);
                data = messCardService.subsidyTicket(params);
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("一键补助失败");
        }
    }


    /**
     * 一键清票
     * @return
     */
//	@SysLogAnnotation(description="微食堂 一键清票",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/emptyTickets")
    public ResponseDTO emptyTickets(@RequestParam Map <String,Object> params) {
        try {
            int data = 0;
            String [] tempArr = params.get("ids").toString().split(",");
            for(String cardId : tempArr){
                data = messCardService.emptyTicket(Integer.valueOf(cardId));
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("一键清票失败");
        }
    }

    /**
     * 饭卡应用(根据卡号查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectCardApplyByCardCode")
    public ResponseDTO selectCardApplyByCardCode(HttpServletRequest request, HttpServletResponse response,
                                                  Page<MessCard> page,@RequestParam String search) {
        try {
            JSONObject jsonData = new JSONObject();
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
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", search);
            jsonData.put("messCards", messCards);
            jsonData.put("selectType", 0);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡应用(根据卡号查询)数据获取失败");
        }
    }

    /**
     * 饭卡应用(根据名字查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectCardApplyByName")
    public ResponseDTO selectCardApplyByName(HttpServletRequest request, HttpServletResponse response,
                                              Page<MessCard> page,@RequestParam String search) {
        try {
            JSONObject jsonData = new JSONObject();
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
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", search);
            jsonData.put("messCards", messCards);
            jsonData.put("selectType", 1);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡应用(根据名字查询)数据获取失败");
        }
    }

    /**
     * 饭卡应用(根据部门查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectCardApplyByDepartment")
    public ResponseDTO selectCardApplyByDepartment(HttpServletRequest request, HttpServletResponse response,
                                                    Page<MessCard> page,@RequestParam String search) {
        try {
            JSONObject jsonData = new JSONObject();
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
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("messCards", messCards);
            jsonData.put("search", search);
            jsonData.put("selectType", 2);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡应用(根据部门查询)数据获取失败");
        }
    }
}
