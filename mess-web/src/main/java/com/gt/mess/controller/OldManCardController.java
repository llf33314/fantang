package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessOldManCard;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessOldManCardService;
import com.gt.mess.vo.SaveOldManCardVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老人卡模块
 */
@Api(description = "老人卡模块")
@RestController
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
     * @return
     */
    @ApiOperation(value = "老人卡管理",notes = "老人卡管理数据获取",httpMethod = "GET")
    @RequestMapping(value = "/oldManCardManage", method = RequestMethod.GET)
    public ResponseDTO oldManCardManage(HttpServletRequest request, 
                                         Page<MessOldManCard> page) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Page<MessOldManCard> messOldManCards =
                    messOldManCardService.getMessOldManCardPageByMainId(page, mainId, 10);
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messOldManCards", messOldManCards.getRecords());
            jsonData.put("mainId", mainId);
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/oldManCardManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("老人卡管理数据获取失败");
        }
    }

    /**
     * 老人卡管理(搜索)
     * @param request
     * @return
     */
    @ApiOperation(value = "老人卡管理(搜索)",notes = "老人卡管理(搜索)",httpMethod = "POST")
    @RequestMapping(value = "/selectOldManCardManage", method = RequestMethod.POST)
    public ResponseDTO selectOldManCardManage(HttpServletRequest request,
                                              Page<MessOldManCard> page,
                                              @ApiParam(name = "cardCode", value = "饭卡号")
                                              @RequestParam String cardCode) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Map<String,Object> params = new HashMap<>();
            params.put("mainId", mainId);
            params.put("cardCode", cardCode);
            Page<MessOldManCard> messOldManCards =
                    messOldManCardService.selectOldManCardManage(page, params, 10);
            jsonData.put("search", cardCode);
            jsonData.put("messOldManCards", messOldManCards.getRecords());
            jsonData.put("mainId", mainId);
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/oldManCardManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("老人卡管理(搜索)数据获取失败");
        }
    }

    /**
     * 保存或更新老人卡表
     * @return
     */
    @ApiOperation(value = "保存或更新老人卡表",notes = "保存或更新老人卡表",httpMethod = "POST")
    @RequestMapping(value = "/saveOldManCard", method = RequestMethod.POST)
    public ResponseDTO saveOldManCard(HttpServletRequest request, @Valid @ModelAttribute SaveOldManCardVo saveVo) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            saveVo.setMainId(mainId);
            Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
            int data = messOldManCardService.saveOldManCard(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新老人卡表失败");
        }
    }

    /**
     * 老人卡补票（扣票）
     * @param cardId
     * @param ticketNum
     * @param type
     * @return
     */
    @ApiOperation(value = "老人卡补票（扣票）",notes = "老人卡补票（扣票）",httpMethod = "POST")
    @RequestMapping(value = "/addOrDelTicket", method = RequestMethod.POST)
    public ResponseDTO addOrDelTicket(@ApiParam(name = "cardId", value = "老人卡ID")
                                      @RequestParam Integer cardId,
                                      @ApiParam(name = "ticketNum", value = "票数")
                                      @RequestParam Integer ticketNum,
                                      @ApiParam(name = "type", value = "接口类型 0 扣票 1加票")
                                      @RequestParam Integer type) {
        try {
            int data = messOldManCardService.addOrDelTicket(cardId,ticketNum,type);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("老人卡补票（扣票）失败");
        }
    }

    /**
     * 删除老人卡
     * @return
     */
    @ApiOperation(value = "删除老人卡",notes = "删除老人卡",httpMethod = "GET")
    @RequestMapping(value = "/{cardId}/delOldManCard", method = RequestMethod.GET)
    public ResponseDTO delOldManCard(@ApiParam(name = "cardId", value = "老人卡ID")
                                     @PathVariable("cardId") Integer cardId) {
        try {
            int data = messOldManCardService.delOldManCard(cardId);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除老人卡失败");
        }
    }

    /**
     * 老人卡一键补票（扣票）
     * @return
     */
    @ApiOperation(value = "老人卡一键补票（扣票）",notes = "老人卡一键补票（扣票）",httpMethod = "POST")
    @RequestMapping(value = "/addOrDelTickets", method = RequestMethod.POST)
    public ResponseDTO addOrDelTickets(@ApiParam(name = "ids", value = "老人卡ID列表：1,2,3,4")
                                           @RequestParam String ids,
                                       @ApiParam(name = "ticketNum", value = "票数")
                                           @RequestParam Integer ticketNum,
                                       @ApiParam(name = "type", value = "接口类型 0 扣票 1加票")
                                           @RequestParam Integer type) {
        try {
            int data = 0;
            String [] idArr = ids.split(",");
            for(String cardId : idArr){
                data = messOldManCardService.addOrDelTicket(Integer.valueOf(cardId),ticketNum,type);
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("老人卡一键补票（扣票）失败");
        }
    }
}
