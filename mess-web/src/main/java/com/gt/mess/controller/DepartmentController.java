package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessDepartmentService;
import com.gt.mess.service.MessMainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 部门管理模块
 */
@Api(description = "部门管理模块")
@Controller
@RequestMapping(value = "department")
public class DepartmentController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessCardService messCardService;

    @Autowired
    private MessDepartmentService messDepartmentService;


    //	部门管理模块

    /**
     * 部门管理
     * @param request
     * @return
     */
    @ApiOperation(value = "部门管理",notes = "部门管理数据获取",httpMethod = "GET")
    @RequestMapping(value = "/departmentManage", method= RequestMethod.GET)
    public ResponseDTO departmentManage(HttpServletRequest request,
                                         Page<MessDepartment> page) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            Page<MessDepartment> messDepartments =
                    messDepartmentService.getMessDepartmentPageByMainId(page, mainId, 10);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("mainId", mainId);
//            mv.setViewName("merchants/trade/mess/admin/basisSet/departmentManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("部门管理获取失败");
        }
    }


    /**
     * 保存或更新部门
     * @return
     */
    @ApiOperation(value = "保存或更新部门",notes = "保存或更新部门",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateDepartment", method= RequestMethod.POST)
    public ResponseDTO saveOrUpdateDepartment(@ApiParam(name = "saveType", value = "save:为保存，否则为更新", required = true)
                                              @RequestParam String saveType,
                                              @ApiParam(name = "mainId", value = "主表ID", required = true)
                                              @RequestParam Integer mainId,
                                              @ApiParam(name = "id", value = "部门ID")
                                              Integer id,
                                              @ApiParam(name = "name", value = "部门名称", required = true)
                                              @RequestParam String name) {
        try {
            int data = messDepartmentService.saveOrUpdateDepartment(saveType,mainId,id,name);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新部门失败");
        }
    }

    /**
     * 删除部门
     * @param request
     * @return
     */
    @ApiOperation(value = "保存或更新部门",notes = "保存或更新部门",httpMethod = "GET")
    @RequestMapping(value = "{depId}/delDepartment", method= RequestMethod.GET)
    public ResponseDTO delDepartment(HttpServletRequest request,
                                     @ApiParam(name = "depId", value = "部门ID", required = true)
                                     @PathVariable("depId") Integer depId) {
        try {
            int data = 0;
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Map<String,Integer> mapId = new HashMap<String, Integer>();
            mapId.put("mainId", messMain.getId());
            mapId.put("depId", depId);
            Integer messCardNums =
                    messCardService.getMessCardNumsByDepId(mapId);
            if(messCardNums > 0){
                data = -1;
            }else{
                data = messDepartmentService.delDepartment(depId);
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除部门失败");
        }
    }
}
