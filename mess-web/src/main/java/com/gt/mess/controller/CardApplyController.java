package com.gt.mess.controller;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 饭卡应用模块
 */
@Api(description = "饭卡应用模块")
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
    @ApiOperation(value = "饭卡应用",notes = "饭卡应用数据获取",httpMethod = "GET")
    @RequestMapping(value = "/cardApply", method= RequestMethod.GET)
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
    @ApiOperation(value = "余额充值",notes = "饭卡余额充值",httpMethod = "POST")
    @RequestMapping(value = "/topUpMoney", method= RequestMethod.POST)
    public ResponseDTO topUpMoney(@ApiParam(name = "cardId", value = "饭卡Id", required = true) @RequestParam Integer cardId,
                                  @ApiParam(name = "money", value = "金额", required = true) @RequestParam Double money) {
        try {
            int data = messCardService.topUpMoney(cardId,money);
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
    @ApiOperation(value = "饭票购买",notes = "饭票购买",httpMethod = "POST")
    @RequestMapping(value = "/buyTicket", method= RequestMethod.POST)
    public ResponseDTO buyTicket(@ApiParam(name = "cardId", value = "饭卡Id", required = true) @RequestParam Integer cardId,
                                 @ApiParam(name = "dMoney", value = "购买金额", required = true) @RequestParam Double dMoney,
                                 @ApiParam(name = "messType", value = "饭票类型（0早 1中 2晚 3夜宵 4通用）", required = true) @RequestParam Integer messType,
                                 @ApiParam(name = "ticketNum", value = "购买数量", required = true) @RequestParam Integer ticketNum) {
        try {
            int data = messCardService.buyTicket(cardId,dMoney,messType,ticketNum);
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
    @ApiOperation(value = "补助",notes = "饭票补助",httpMethod = "POST")
    @RequestMapping(value = "/subsidyTicket", method= RequestMethod.POST)
    public ResponseDTO subsidyTicket(@ApiParam(name = "cardId", value = "饭卡Id", required = true) @RequestParam Integer cardId,
                                     @ApiParam(name = "messType", value = "饭票类型（0早 1中 2晚 3夜宵 4通用）", required = true) @RequestParam Integer messType,
                                     @ApiParam(name = "ticketNum", value = "购买数量", required = true) @RequestParam Integer ticketNum) {
        try {
            int data = messCardService.subsidyTicket(cardId,messType,ticketNum);
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
     * 扣除饭票（只限免费、补助票）
     * @return
     */
    @ApiOperation(value = "扣除饭票",notes = "扣除饭票（只限免费、补助票）",httpMethod = "POST")
    @RequestMapping(value = "/deductTicket", method= RequestMethod.POST)
    public ResponseDTO deductTicket(@ApiParam(name = "cardId", value = "饭卡Id", required = true) @RequestParam Integer cardId,
                                    @ApiParam(name = "messType", value = "饭票类型（0早 1中 2晚 3夜宵 4通用）", required = true) @RequestParam Integer messType,
                                    @ApiParam(name = "ticketNum", value = "购买数量", required = true) @RequestParam Integer ticketNum) {
        try {
            int data = messCardService.deductTicket(cardId,messType,ticketNum);
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
    @ApiOperation(value = "一键补助",notes = "一键补助",httpMethod = "POST")
    @RequestMapping(value = "/subsidyTickets", method= RequestMethod.POST)
    public ResponseDTO subsidyTickets(@ApiParam(name = "cardIds", value = "多个饭卡Id,格式：1,2,3,4", required = true) @RequestParam String cardIds,
                                      @ApiParam(name = "messType", value = "饭票类型（0早 1中 2晚 3夜宵 4通用）", required = true) @RequestParam Integer messType,
                                      @ApiParam(name = "ticketNum", value = "购买数量", required = true) @RequestParam Integer ticketNum) {
        try {
            int data = 0;
            String [] tempArr = cardIds.split(",");
            for(String cardId : tempArr){
                data = messCardService.subsidyTicket(Integer.valueOf(cardId),messType,ticketNum);
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
    @ApiOperation(value = "一键清票",notes = "一键清票",httpMethod = "POST")
    @RequestMapping(value = "/emptyTickets", method= RequestMethod.POST)
    public ResponseDTO emptyTickets(
            @ApiParam(name = "cardIds", value = "多个饭卡Id,格式：1,2,3,4", required = true)
            @RequestParam String cardIds) {
        try {
            int data = 0;
            String [] tempArr = cardIds.split(",");
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
     * @return
     */
    @ApiOperation(value = "饭卡应用(根据卡号查询)",notes = "饭卡应用(根据卡号查询)",httpMethod = "POST")
    @RequestMapping(value = "/selectCardApplyByCardCode", method= RequestMethod.POST)
    public ResponseDTO selectCardApplyByCardCode(HttpServletRequest request,
                                                 Page<MessCard> page,
                                                 @ApiParam(name = "cardCode", value = "饭卡号", required = true)
                                                 @RequestParam String cardCode) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mainId", mainId);
            map.put("cardCode", cardCode);
            Page<MessCard> messCards =
                    messCardService.selectCardApplyByCardCode(page, map, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", cardCode);
            jsonData.put("messCards", messCards.getRecords());
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
     * @return
     */
    @ApiOperation(value = "饭卡应用(根据名字查询)",notes = "饭卡应用(根据名字查询)",httpMethod = "POST")
    @RequestMapping(value = "/selectCardApplyByName", method= RequestMethod.POST)
    public ResponseDTO selectCardApplyByName(HttpServletRequest request,
                                             Page<MessCard> page,
                                             @ApiParam(name = "name", value = "名字", required = true)
                                             @RequestParam String name) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mainId", mainId);
            map.put("name", name);
            Page<MessCard> messCards =
                    messCardService.selectCardApplyByName(page, map, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", name);
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
     * @return
     */
    @ApiOperation(value = "饭卡应用(根据部门查询)",notes = "饭卡应用(根据部门查询)",httpMethod = "POST")
    @RequestMapping(value = "/selectCardApplyByDepartment", method= RequestMethod.POST)
    public ResponseDTO selectCardApplyByDepartment(HttpServletRequest request,
                                                   Page<MessCard> page,
                                                   @ApiParam(name = "name", value = "部门名称", required = true)
                                                   @RequestParam String department) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("mainId", mainId);
            map.put("department", department);
            Page<MessCard> messCards =
                    messCardService.selectCardApplyByDepartment(page, map, 10);
            MessBasisSet messBasisSet =
                    messBasisSetService.getMessBasisSetByMainId(messMain.getId());
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("messCards", messCards);
            jsonData.put("search", department);
            jsonData.put("selectType", 2);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/cardApply");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡应用(根据部门查询)数据获取失败");
        }
    }
}
