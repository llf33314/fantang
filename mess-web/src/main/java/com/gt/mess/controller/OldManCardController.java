package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessOldManCard;
import com.gt.mess.exception.BaseException;
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
import java.util.Date;
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
     * @return
     */
    @RequestMapping(value = "/oldManCardManage")
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
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectOldManCardManage")
    public ResponseDTO selectOldManCardManage(HttpServletRequest request, HttpServletResponse response,
                                               Page<MessOldManCard> page,@RequestParam Map<String,Object> params) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            params.put("mainId", mainId);
            Page<MessOldManCard> messOldManCards =
                    messOldManCardService.selectOldManCardManage(page, params, 10);
            jsonData.put("search", params.get("cardCode").toString());
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
//	@SysLogAnnotation(description="微食堂 保存或更新老人卡表",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOldManCard")
    public ResponseDTO saveOldManCard(@RequestParam Map <String,Object> params) {
        try {
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
     * @return
     */
//	@SysLogAnnotation(description="微食堂 老人卡补票（扣票）",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/addOrDelTicket")
    public ResponseDTO addOrDelTicket(@RequestParam Map <String,Object> params) {
        try {
            int data = messOldManCardService.addOrDelTicket(params);
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
//	@SysLogAnnotation(description="微食堂 删除老人卡",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "/{id}/delOldManCard")
    public ResponseDTO delOldManCard(@PathVariable("id") Integer id) {
        try {
            int data = messOldManCardService.delOldManCard(id);
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
//	@SysLogAnnotation(description="微食堂  老人卡一键补票（扣票）",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/addOrDelTickets")
    public ResponseDTO addOrDelTickets(@RequestParam Map <String,Object> params) {
        try {
            int data = 0;
            String [] ids = params.get("ids").toString().split(",");
            for(String id : ids){
                params.put("id", id);
                data = messOldManCardService.addOrDelTicket(params);
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
