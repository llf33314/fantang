package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.base.BaseController;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BaseException;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 饭票管理模块
 */
@Controller
@RequestMapping(value = "ticket")
public class TicketController {

    @Autowired
    private MessMainService messMainService;

    @Autowired
    private MessBasisSetService messBasisSetService;

    @Autowired
    private MessCardService messCardService;

    @Autowired
    private MessDepartmentMapper messDepartmentMapper;

    @Autowired
    private MessCardGroupMapper messCardGroupMapper;


    //	饭票管理模块

    /**
     * 饭卡管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/ticketManage")
    public ResponseDTO ticketManage(HttpServletRequest request, HttpServletResponse response,
                                     Page<MessCard> page) {
        try {
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId = messMain.getId();
            Page<MessCard> messCards =
                    messCardService.getMessCardPageByMainId(page, mainId, 10);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            jsonData.put("mainId", mainId);
            jsonData.put("messCards", messCards.getRecords());
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡管理数据获取失败");
        }
    }

    /**
     * 饭卡管理(根据卡号查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectTicketManageByCardCode")
    public ResponseDTO selectTicketManageByCardCode(HttpServletRequest request,
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", search);
            jsonData.put("messCards", messCards.getRecords());
            jsonData.put("selectType", 0);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡管理(根据卡号查询)数据获取失败");
        }
    }

    /**
     * 饭卡管理(根据名字查询)
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectTicketManageByName")
    public ResponseDTO selectTicketManageByName(HttpServletRequest request,
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", search);
            jsonData.put("messCards", messCards.getRecords());
            jsonData.put("selectType", 1);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡管理(根据名字查询)数据获取失败");
        }
    }

    /**
     * 饭卡管理(根据部门查询)
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectTicketManageByDepartment")
    public ResponseDTO selectTicketManageByDepartment(HttpServletRequest request,
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("search", search);
            jsonData.put("messCards", messCards.getRecords());
            jsonData.put("selectType", 2);//0卡号查询 1名字查询 2部门查询
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("饭卡管理(根据部门查询)数据获取失败");
        }
    }

    /**
     * 导出模板
     * @param response
     */
    @RequestMapping(value = "/exportsTemplate")
    public void exportsTemplate(HttpServletResponse response) {
        try {
            Map<String, Object> msg = messCardService.exports();
            if ((boolean) msg.get("result")) {
//				HSSFWorkbook wb = (HSSFWorkbook) msg.get("book");
                Workbook wb = (Workbook) msg.get("book");
                String filename = msg.get("fileName").toString()+".xls";
                response.reset();
                // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
                response.addHeader("Content-Disposition",
                        "attachment;filename="
                                + new String(filename.replaceAll(" ", "")
                                .getBytes("utf-8"), "iso8859-1"));
                OutputStream os = new BufferedOutputStream(
                        response.getOutputStream());
                response.setContentType("application/octet-stream");
                wb.write(os);// 输出文件
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入饭卡
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 导入饭卡",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "{depId}/enteringCard")
    public ResponseDTO enteringCard(HttpServletRequest request, HttpServletResponse response,
                                     Page<MessCard> page,@PathVariable("depId") Integer depId) {
        try {
            Integer mainId = null;
            JSONObject jsonData = new JSONObject();
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            mainId = messMain.getId();
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("filez");
            int data = 0;
            Map<String,Object> map = new HashMap<String, Object>();
            map = messCardService.enteringCard(multipartFile.getInputStream(),mainId,depId);
            data = Integer.valueOf(map.get("dataType").toString());
            if(data == 1){
                jsonData.put("msg", "导入成功！");
                jsonData.put("size", Integer.valueOf(map.get("size").toString()));
                List<String> list = null;
                try {
                    list = (List<String>)map.get("list");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                StringBuffer listName = new StringBuffer();
                if(list.size() != 0){
                    for(int i = 0;list.size() > i;i++){
                        listName.append(list.get(i).trim()+",");
                    }
                    listName.deleteCharAt(listName.length()-1);
                }
                jsonData.put("listName", listName);
            }else{
                return ResponseDTO.createByErrorMessage("导入失败！");
            }
            Page<MessCard> messCards =
                    messCardService.getMessCardPageByMainId(page, mainId, 10);
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("mainId", mainId);
            jsonData.put("messCards", messCards.getRecords());
//            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
            return ResponseDTO.createBySuccess(jsonData);
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("导入饭卡失败");
        }
    }

    /**
     * 保存或更新饭卡
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新饭卡",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateMessCard")
    public ResponseDTO saveOrUpdateMessCard(@RequestParam Map <String,Object> params) {
        try {
            int data = messCardService.saveOrUpdateMessCard(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("保存或更新饭卡失败");
        }
    }

    /**
     * 删除饭卡
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除饭卡",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{cardId}/delMessCard")
    public ResponseDTO delMessCard(@PathVariable("cardId") Integer cardId) {
        try {
            int data = messCardService.delMessCard(cardId);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("删除饭卡失败");
        }
    }

    /**
     * 批量删除饭卡
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量删除饭卡",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "/delMessCards")
    public ResponseDTO delMessCards(@RequestParam String cardIds) {
        try {
            int data = 0;
            String [] tempArr = cardIds.split(",");
            for(String cardId : tempArr){
                data = messCardService.delMessCard(Integer.valueOf(cardId));
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("批量删除饭卡失败");
        }
    }

    /**
     * 修改饭卡组
     * @return
     */
//	@SysLogAnnotation(description="微食堂 修改饭卡组",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeGroup")
    public ResponseDTO changeGroup(@RequestParam Map<String,Object> params) {
        try {
            int data = messCardService.changeGroup(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("修改饭卡组失败");
        }
    }

    /**
     * 批量修改饭卡组
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量修改部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeGroups")
    public ResponseDTO changeGroups(@RequestParam Map<String,Object> params) {
        try {
            int data = 0;
            for(String cardId: params.get("cardIds").toString().split(",")){
                params.put("cardId", cardId);
                data = messCardService.changeGroup(params);
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("批量修改饭卡组失败");
        }
    }

    /**
     * 修改部门
     * @return
     */
//	@SysLogAnnotation(description="微食堂 修改部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeDep")
    public ResponseDTO changeDep(@RequestParam Map<String,Object> params) {
        try {
            int data = messCardService.changeDep(params);
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("修改部门失败");
        }
    }

    /**
     * 批量修改部门
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量修改部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeDeps")
    public ResponseDTO changeDeps(@RequestParam Map<String,Object> params) {
        try {
            int data = 0;
            for(String cardId: params.get("cardIds").toString().split(",")){
                params.put("cardId", cardId);
                data = messCardService.changeDep(params);
            }
            if(data == 1)
                return ResponseDTO.createBySuccess();
            else
                return ResponseDTO.createByError();
        } catch (Exception e) {
            // TODO: handle exception
            throw new BaseException("批量修改部门失败");
        }
    }

}
