package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.mess.base.BaseController;
import com.gt.mess.dao.MessCardGroupMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.entity.*;
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
     * 饭卡管理入口
     *
     * @param request
     * @param response
     * @return
     */
//	@CommAnno(menu_url="mess/ticketManageIndex.do")
    @RequestMapping(value = "/ticketManageIndex")
    public ModelAndView indexCar(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("merchants/trade/mess/admin/ticketManage/indexTicketManage");
        return mv;
    }

    /**
     * 饭卡管理
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/ticketManage")
    public ModelAndView ticketManage(HttpServletRequest request, HttpServletResponse response,
                                     Page<MessCard> page) {
        ModelAndView mv = new ModelAndView();
        try {
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
            mv.addObject("mainId", mainId);
            mv.addObject("messCards", messCards);
            mv.addObject("messCardGroups", messCardGroups);
            mv.addObject("messDepartments", messDepartments);
            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 饭卡管理(根据卡号查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectTicketManageByCardCode")
    public ModelAndView selectTicketManageByCardCode(HttpServletRequest request, HttpServletResponse response,
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            mv.addObject("messCardGroups", messCardGroups);
            mv.addObject("messDepartments", messDepartments);
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("search", search);
            mv.addObject("messCards", messCards);
            mv.addObject("selectType", 0);//0卡号查询 1名字查询 2部门查询
            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 饭卡管理(根据名字查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectTicketManageByName")
    public ModelAndView selectTicketManageByName(HttpServletRequest request, HttpServletResponse response,
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            mv.addObject("messCardGroups", messCardGroups);
            mv.addObject("messDepartments", messDepartments);
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("search", search);
            mv.addObject("messCards", messCards);
            mv.addObject("selectType", 1);//0卡号查询 1名字查询 2部门查询
            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 饭卡管理(根据部门查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectTicketManageByDepartment")
    public ModelAndView selectTicketManageByDepartment(HttpServletRequest request, HttpServletResponse response,
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
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            mv.addObject("messCardGroups", messCardGroups);
            mv.addObject("messDepartments", messDepartments);
            mv.addObject("messBasisSet", messBasisSet);
            mv.addObject("search", search);
            mv.addObject("messCards", messCards);
            mv.addObject("selectType", 2);//0卡号查询 1名字查询 2部门查询
            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 导出模板
     * @param request
     * @param response
     * @param params
     */
    @RequestMapping(value = "/exportsTemplate")
    public void exportsTemplate(HttpServletRequest request,
                                HttpServletResponse response) {
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
    public ModelAndView enteringCard(HttpServletRequest request, HttpServletResponse response,
                                     Page<MessCard> page,@PathVariable("depId") Integer depId) {
        ModelAndView mv = new ModelAndView();
        Integer mainId = null;
        try {
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
                mv.addObject("msg", "导入成功！");
                mv.addObject("size", Integer.valueOf(map.get("size").toString()));
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
                mv.addObject("listName", listName);
            }else{
                mv.addObject("msg", "导入失败！");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            mv.addObject("msg", "导入失败！");
        }finally {
            Page<MessCard> messCards =
                    messCardService.getMessCardPageByMainId(page, mainId, 10);
            List<MessDepartment> messDepartments =
                    messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
            List<MessCardGroup> messCardGroups =
                    messCardGroupMapper.getMessCardGroupPageByMainId(mainId);
            mv.addObject("messCardGroups", messCardGroups);
            mv.addObject("messDepartments", messDepartments);
            mv.addObject("mainId", mainId);
            mv.addObject("messCards", messCards);
            mv.setViewName("merchants/trade/mess/admin/ticketManage/ticketManage");
        }
        return mv;
    }

    /**
     * 保存或更新饭卡
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 保存或更新饭卡",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/saveOrUpdateMessCard")
    public void saveOrUpdateMessCard(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam Map <String,Object> params) {
        int data = 0;
        try {
            data = messCardService.saveOrUpdateMessCard(params);
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
     * 删除饭卡
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 删除饭卡",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "{cardId}/delMessCard")
    public void delMessCard(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("cardId") Integer cardId) {
        int data = 0;
        try {
            data = messCardService.delMessCard(cardId);
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
     * 批量删除饭卡
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量删除饭卡",op_function="4")//保存2，修改3，删除4
    @RequestMapping(value = "/delMessCards")
    public void delMessCards(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam String cardIds) {
        int data = 0;
        try {
            String [] tempArr = cardIds.split(",");
            for(String cardId : tempArr){
                data = messCardService.delMessCard(Integer.valueOf(cardId));
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
     * 修改饭卡组
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 修改饭卡组",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeGroup")
    public void changeGroup(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messCardService.changeGroup(params);
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
     * 批量修改饭卡组
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量修改部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeGroups")
    public void changeGroups(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            for(String cardId: params.get("cardIds").toString().split(",")){
                params.put("cardId", cardId);
                data = messCardService.changeGroup(params);
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
     * 修改部门
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 修改部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeDep")
    public void changeDep(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            data = messCardService.changeDep(params);
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
     * 批量修改部门
     *
     * @param request
     * @param response
     * @return
     */
//	@SysLogAnnotation(description="微食堂 批量修改部门",op_function="3")//保存2，修改3，删除4
    @RequestMapping(value = "/changeDeps")
    public void changeDeps(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam Map<String,Object> params) {
        int data = 0;
        try {
            for(String cardId: params.get("cardIds").toString().split(",")){
                params.put("cardId", cardId);
                data = messCardService.changeDep(params);
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
