package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.vo.SaveOrUpdateMessCardVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 饭票管理模块
 */
@Api(description = "饭票管理模块")
@RestController
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
    @ApiOperation(value = "饭卡管理",notes = "饭卡管理数据获取",httpMethod = "GET")
    @RequestMapping(value = "/ticketManage", method = RequestMethod.GET)
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
     * @return
     */
    @ApiOperation(value = "饭卡管理(根据卡号查询)",notes = "饭卡管理(根据卡号查询)",httpMethod = "POST")
    @RequestMapping(value = "/selectTicketManageByCardCode", method = RequestMethod.POST)
    public ResponseDTO selectTicketManageByCardCode(HttpServletRequest request,
                                                    Page<MessCard> page,
                                                    @ApiParam(name = "cardCode", value = "饭卡号")
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messBasisSet", messBasisSet);
            jsonData.put("cardCode", cardCode);
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
    @ApiOperation(value = "饭卡管理(根据名字查询)",notes = "饭卡管理(根据名字查询)",httpMethod = "POST")
    @RequestMapping(value = "/selectTicketManageByName", method = RequestMethod.POST)
    public ResponseDTO selectTicketManageByName(HttpServletRequest request,
                                                Page<MessCard> page,
                                                @ApiParam(name = "name", value = "名字")
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messBasisSet", messBasisSet);
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
    @ApiOperation(value = "饭卡管理(根据部门查询)",notes = "饭卡管理(根据部门查询)",httpMethod = "POST")
    @RequestMapping(value = "/selectTicketManageByDepartment", method = RequestMethod.POST)
    public ResponseDTO selectTicketManageByDepartment(HttpServletRequest request,
                                                      Page<MessCard> page,
                                                      @ApiParam(name = "department", value = "部门名称")
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            jsonData.put("messCardGroups", messCardGroups);
            jsonData.put("messDepartments", messDepartments);
            jsonData.put("messBasisSet", messBasisSet);
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
    @ApiOperation(value = "导出模板",notes = "导出模板",httpMethod = "GET")
    @RequestMapping(value = "/exportsTemplate", method = RequestMethod.GET)
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
     * @param request
     * @return
     */
    @ApiOperation(value = "导入饭卡",notes = "导入饭卡",httpMethod = "GET")
    @RequestMapping(value = "{depId}/enteringCard", method = RequestMethod.GET)
    public ResponseDTO enteringCard(HttpServletRequest request,
                                    Page<MessCard> page,
                                    @ApiParam(name = "depId", value = "部门ID")
                                    @PathVariable("depId") Integer depId) {
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
    @ApiOperation(value = "保存或更新饭卡",notes = "保存或更新饭卡",httpMethod = "POST")
    @RequestMapping(value = "/saveOrUpdateMessCard", method = RequestMethod.POST)
    public ResponseDTO saveOrUpdateMessCard(HttpServletRequest request, @Valid @ModelAttribute SaveOrUpdateMessCardVo saveVo) {
        try {
            BusUser busUser = SessionUtils.getLoginUser(request);
            MessMain messMain =
                    messMainService.getMessMainByBusId(busUser.getId());
            Integer mainId= messMain.getId();
            saveVo.setMainId(mainId);
            int data = messCardService.saveOrUpdateMessCard(saveVo);
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
    @ApiOperation(value = "删除饭卡",notes = "删除饭卡",httpMethod = "GET")
    @RequestMapping(value = "{cardId}/delMessCard", method = RequestMethod.GET)
    public ResponseDTO delMessCard(@ApiParam(name = "cardId", value = "饭卡Id", required = true)
                                   @PathVariable("cardId") Integer cardId) {
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
    @ApiOperation(value = "批量删除饭卡",notes = "批量删除饭卡",httpMethod = "POST")
    @RequestMapping(value = "/delMessCards", method = RequestMethod.POST)
    public ResponseDTO delMessCards(@ApiParam(name = "cardIds", value = "饭卡Id列表", required = true)
                                    @RequestParam String cardIds) {
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
    @ApiOperation(value = "修改饭卡组",notes = "修改饭卡组",httpMethod = "POST")
    @RequestMapping(value = "/changeGroup", method = RequestMethod.POST)
    public ResponseDTO changeGroup(@ApiParam(name = "cardId", value = "饭卡Id", required = true)
                                   @RequestParam Integer cardId,
                                   @ApiParam(name = "groupId", value = "饭卡组Id", required = true)
                                   @RequestParam Integer groupId) {
        try {
            int data = messCardService.changeGroup(cardId,groupId);
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
    @ApiOperation(value = "批量修改饭卡组",notes = "批量修改饭卡组",httpMethod = "POST")
    @RequestMapping(value = "/changeGroups", method = RequestMethod.POST)
    public ResponseDTO changeGroups(@ApiParam(name = "cardIds", value = "饭卡Id列表：1,2,3,4", required = true)
                                        @RequestParam String cardIds,
                                    @ApiParam(name = "groupId", value = "饭卡组Id", required = true)
                                        @RequestParam Integer groupId) {
        try {
            int data = 0;
            for(String cardId: cardIds.split(",")){
                data = messCardService.changeGroup(Integer.valueOf(cardId),groupId);
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
    @ApiOperation(value = "修改部门",notes = "修改部门",httpMethod = "POST")
    @RequestMapping(value = "/changeDep", method = RequestMethod.POST)
    public ResponseDTO changeDep(@ApiParam(name = "cardId", value = "饭卡Id", required = true)
                                     @RequestParam Integer cardId,
                                 @ApiParam(name = "depId", value = "部门ID(当部门ID为-1时，默认是不选部门)", required = true)
                                     @RequestParam Integer depId,
                                 @ApiParam(name = "department", value = "部门名称")
                                     String department) {
        try {
            int data = messCardService.changeDep(cardId,depId,department);
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
    @ApiOperation(value = "批量修改部门",notes = "批量修改部门",httpMethod = "POST")
    @RequestMapping(value = "/changeDeps", method = RequestMethod.POST)
    public ResponseDTO changeDeps(@ApiParam(name = "cardIds", value = "饭卡Id列表", required = true)
                                      @RequestParam String cardIds,
                                  @ApiParam(name = "depId", value = "部门ID(当部门ID为-1时，默认是不选部门)", required = true)
                                      @RequestParam Integer depId,
                                  @ApiParam(name = "department", value = "部门名称")
                                      String department) {
        try {
            int data = 0;
            for(String cardId: cardIds.split(",")){
                data = messCardService.changeDep(Integer.valueOf(cardId),depId,department);
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
