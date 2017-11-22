package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessDepartmentService;
import com.gt.mess.service.MessMainService;
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
 * 部门管理模块
 */
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
    @RequestMapping(value = "/departmentManage")
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
//	@SysLogAnnotation(description="微食堂 保存或更新部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateDepartment")
    public ResponseDTO saveOrUpdateDepartment(@RequestParam Map<String,Object> params) {
        try {
            int data = messDepartmentService.saveOrUpdateDepartment(params);
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
//	@SysLogAnnotation(description="微食堂 删除部门",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{depId}/delDepartment")
    public ResponseDTO delDepartment(HttpServletRequest request,
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
