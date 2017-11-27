package com.gt.mess.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessBuyTicketOrderService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessMealOrderService;
import com.gt.mess.service.MessTopUpOrderService;
import com.gt.mess.vo.SelectBuyTicketVo;
import com.gt.mess.vo.SelectMainIdDepIdCardCodeVo;
import com.gt.mess.vo.SelectMealOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食堂后台（订单）
 * @author ZengWenXiang
 * @QQ 307848200
 */
@Api(description = "食堂后台（订单）")
@Controller
@RequestMapping(value = "messOrder")
public class MessOrderController {

	@Autowired
	private MessMainService messMainService;
	
	@Autowired
	private MessMealOrderService messMealOrderService;
	
	@Autowired
	private MessBuyTicketOrderService messBuyTicketOrderService;

	@Autowired
	private MessTopUpOrderService messTopUpOrderService;

	@Autowired
	private MessDepartmentMapper messDepartmentMapper;
//	/**
//	 * 获取订餐名单
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "{mealType}/{status}/getMealOrderList")
//	public void getMealOrderList(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable("mealType") Integer mealType,@PathVariable("status") Integer status) {
//		int data = 0;
//		Map<String,Object> map = null;
//		try {
//			Map<String,Object> params = new HashMap<String, Object>();
//			params.put("mealType", mealType);
//			params.put("status", status);
//			map = messMealOrderService.getMealOrderList(params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();	
//			if(map != null){
//				map.put("status","success");			
//			}else{
//				map = new HashMap<String,Object>();
//				map.put("status","error");
//			}			
//		} catch (IOException e) {
//			e.printStackTrace();
//			map = new HashMap<String,Object>();
//			map.put("status","error");		
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
	
//	数据统计模块
	
	/**
	 * 订餐记录
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "订餐记录",notes = "订餐记录数据获取",httpMethod = "GET")
	@RequestMapping(value = "/mealOrder", method= RequestMethod.GET)
	public ResponseDTO mealOrder(HttpServletRequest request,
			Page<MessMealOrder> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessMealOrder> messMealOrders =
					messMealOrderService.getMessMealOrderPageByMainId(page, mainId, 10);
			List<MessMealOrder> messMealOrderList =
					messMealOrderService.getMessMealOrderListforToday(mainId);
			//人数
			Integer breakfastNum = 0;
			Integer lunchNum = 0;
			Integer dinnerNum = 0;
			Integer nightNum = 0;
			//份数
			Integer breakfastMealNum = 0;
			Integer lunchMealNum = 0;
			Integer dinnerMealNum = 0;
			Integer nightMealNum = 0;
			for(MessMealOrder messMealOrder : messMealOrderList){
				if(messMealOrder.getMealType() == 0){
					breakfastMealNum += messMealOrder.getMealNum();
					breakfastNum++;
				}else if(messMealOrder.getMealType() == 1){
					lunchMealNum += messMealOrder.getMealNum();
					lunchNum++;
				}else if(messMealOrder.getMealType() == 2){
					dinnerMealNum += messMealOrder.getMealNum();
					dinnerNum++;
				}else if(messMealOrder.getMealType() == 3){
					nightMealNum += messMealOrder.getMealNum();
					nightNum++;
				}
			}
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);

			jsonData.put("breakfastNum", breakfastNum);
			jsonData.put("lunchNum", lunchNum);
			jsonData.put("dinnerNum", dinnerNum);
			jsonData.put("nightNum", nightNum);

			jsonData.put("breakfastMealNum", breakfastMealNum);
			jsonData.put("lunchMealNum", lunchMealNum);
			jsonData.put("dinnerMealNum", dinnerMealNum);
			jsonData.put("nightMealNum", nightMealNum);

			jsonData.put("url", "messOrder/mealOrder.do");
			jsonData.put("messMealOrders", messMealOrders.getRecords());
			jsonData.put("mainId", mainId);
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/mealOrder");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("订餐记录数据获取失败");
		}
	}
	
	/**
	 * 订餐记录（根据条件搜索）
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "订餐记录（根据条件搜索）",notes = "订餐记录（根据条件搜索）",httpMethod = "POST")
	@RequestMapping(value = "/selectMealOrder", method= RequestMethod.POST)
	public ResponseDTO selectMealOrder(HttpServletRequest request,
									   Page<MessMealOrder> page,@Valid @ModelAttribute SelectMealOrderVo saveVo) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Page<MessMealOrder> messMealOrders =
					messMealOrderService.selectMealOrder(page, params, 10);

			List<MessMealOrder> messMealOrderList =
					messMealOrderService.getMessMealOrderListforToday(mainId);
			//人数
			Integer breakfastNum = 0;
			Integer lunchNum = 0;
			Integer dinnerNum = 0;
			Integer nightNum = 0;
			//份数
			Integer breakfastMealNum = 0;
			Integer lunchMealNum = 0;
			Integer dinnerMealNum = 0;
			Integer nightMealNum = 0;
			for(MessMealOrder messMealOrder : messMealOrderList){
				if(messMealOrder.getMealType() == 0){
					breakfastMealNum += messMealOrder.getMealNum();
					breakfastNum++;
				}else if(messMealOrder.getMealType() == 1){
					lunchMealNum += messMealOrder.getMealNum();
					lunchNum++;
				}else if(messMealOrder.getMealType() == 2){
					dinnerMealNum += messMealOrder.getMealNum();
					dinnerNum++;
				}else if(messMealOrder.getMealType() == 3){
					nightMealNum += messMealOrder.getMealNum();
					nightNum++;
				}
			}
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);

			jsonData.put("breakfastNum", breakfastNum);
			jsonData.put("lunchNum", lunchNum);
			jsonData.put("dinnerNum", dinnerNum);
			jsonData.put("nightNum", nightNum);

			jsonData.put("breakfastMealNum", breakfastMealNum);
			jsonData.put("lunchMealNum", lunchMealNum);
			jsonData.put("dinnerMealNum", dinnerMealNum);
			jsonData.put("nightMealNum", nightMealNum);

			jsonData.put("params", params);
			jsonData.put("url", "messOrder/selectMealOrder.do");
			jsonData.put("messMealOrders", messMealOrders.getRecords());
			jsonData.put("mainId", mainId);
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/mealOrder");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("订餐记录（根据条件搜索）数据获取失败");
		}
	}
	
	/**
	 * 导出订餐记录
	 * @param response
	 * @param saveVo
	 */
	@ApiOperation(value = "订餐记录（根据条件搜索）",notes = "订餐记录（根据条件搜索）",httpMethod = "POST")
	@RequestMapping(value = "/exportsMealOrder", method= RequestMethod.POST)
	public void exportsMealOrder(HttpServletRequest request,HttpServletResponse response,
								 @Valid @ModelAttribute SelectMealOrderVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Map<String, Object> msg = messMealOrderService.exports(saveVo);
			if ((boolean) msg.get("result")) {
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
	 * 导出订餐记录(月总)
	 * @param response
	 * @param mainId
	 */
	@ApiOperation(value = "导出订餐记录(月总)",notes = "导出订餐记录(月总)",httpMethod = "POST")
	@RequestMapping(value = "/exportsMealOrderForMonth", method= RequestMethod.POST)
	public void exportsMealOrderForMonth(HttpServletResponse response,
										 @ApiParam(name = "mainId", value = "主表ID", required = true)
										 @RequestParam Integer mainId,
										 @ApiParam(name = "depId", value = "部门ID")
										 Integer depId) {
		try {
			Map<String, Object> msg = messMealOrderService.exportsMealOrderForMonth(mainId,depId);
			if ((boolean) msg.get("result")) {
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
	 * 购票统计
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "购票统计",notes = "购票统计",httpMethod = "GET")
	@RequestMapping(value = "/buyTicketStatistics", method= RequestMethod.GET)
	public ResponseDTO buyTicketStatistics(HttpServletRequest request,
			Page<MessBuyTicketOrder> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessBuyTicketOrder> messBuyTicketOrders =
					messBuyTicketOrderService.getMessBuyTicketOrderPageMainId(page, mainId, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);
			jsonData.put("messBuyTicketOrders", messBuyTicketOrders.getRecords());
			jsonData.put("mainId", mainId);
			jsonData.put("url", "messOrder/buyTicketStatistics.do");
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/buyTicketStatistics");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("购票统计数据获取失败");
		}
	}
	
	/**
	 * 购票统计(根据条件查询)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "购票统计(根据条件查询)",notes = "购票统计(根据条件查询)",httpMethod = "POST")
	@RequestMapping(value = "/selectBuyTicketStatistics", method= RequestMethod.POST)
	public ResponseDTO selectBuyTicketStatistics(HttpServletRequest request,
												 Page<MessBuyTicketOrder> page,
												 @Valid @ModelAttribute SelectBuyTicketVo saveVo) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Page<MessBuyTicketOrder> messBuyTicketOrders =
					messBuyTicketOrderService.selectBuyTicketStatistics(page, params, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);
			jsonData.put("messBuyTicketOrders", messBuyTicketOrders.getRecords());
			jsonData.put("mainId", mainId);
			jsonData.put("params", params);
			jsonData.put("url", "messOrder/selectBuyTicketStatistics.do");
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/buyTicketStatistics");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("购票统计(根据条件查询)数据获取失败");
		}
	}
	
	/**
	 * 导出购票记录
	 * @param response
	 * @param saveVo
	 */
	@ApiOperation(value = "导出购票记录",notes = "导出购票记录",httpMethod = "POST")
	@RequestMapping(value = "/exportsBuyTicket", method= RequestMethod.POST)
	public void exportsBuyTicket(HttpServletRequest request,HttpServletResponse response,
								 @Valid @ModelAttribute SelectBuyTicketVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Map<String, Object> msg = messBuyTicketOrderService.exports(params);
			if ((boolean) msg.get("result")) {
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
	 * 商家补助
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "商家补助",notes = "商家补助",httpMethod = "GET")
	@RequestMapping(value = "/subsidyTicket", method= RequestMethod.GET)
	public ResponseDTO subsidyTicket(HttpServletRequest request,
			Page<MessBuyTicketOrder> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessBuyTicketOrder> messBuyTicketOrders =
					messBuyTicketOrderService.getSubsidyTicketOrderPageMainId(page, mainId, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);
			jsonData.put("messBuyTicketOrders", messBuyTicketOrders.getRecords());
			jsonData.put("mainId", mainId);
			jsonData.put("url", "messOrder/subsidyTicket.do");
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/subsidyTicket");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("商家补助数据获取失败");
		}
	}
	
	/**
	 * 商家补助(根据条件查询)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "商家补助(根据条件查询)",notes = "商家补助(根据条件查询)",httpMethod = "POST")
	@RequestMapping(value = "/selectSubsidyTicket", method= RequestMethod.POST)
	public ResponseDTO selectSubsidyTicket(HttpServletRequest request,
			Page<MessBuyTicketOrder> page,@Valid @ModelAttribute SelectMainIdDepIdCardCodeVo saveVo) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Map<String,Object> params = new HashMap<>();
			saveVo.setMainId(mainId);
			params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Page<MessBuyTicketOrder> messBuyTicketOrders =
					messBuyTicketOrderService.selectSubsidyTicket(page, params, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);
			jsonData.put("messBuyTicketOrders", messBuyTicketOrders.getRecords());
			jsonData.put("mainId", mainId);
			jsonData.put("params", params);
			jsonData.put("url", "messOrder/selectSubsidyTicket.do");
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/subsidyTicket");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("商家补助(根据条件查询)数据获取失败");
		}
	}
	
	/**
	 * 导出补助记录
	 * @param response
	 * @param saveVo
	 */
	@ApiOperation(value = "导出补助记录",notes = "导出补助记录",httpMethod = "POST")
	@RequestMapping(value = "/exportsSubsidyTicket", method= RequestMethod.POST)
	public void exportsSubsidyTicket(HttpServletResponse response,HttpServletRequest request,
									 @Valid @ModelAttribute SelectMainIdDepIdCardCodeVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Map<String, Object> msg = messBuyTicketOrderService.exportsSubsidyTicket(params);
			if ((boolean) msg.get("result")) {
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
	 * 充值记录
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "充值记录",notes = "充值记录",httpMethod = "GET")
	@RequestMapping(value = "/topUpOrder", method= RequestMethod.GET)
	public ResponseDTO topUpOrder(HttpServletRequest request,
			Page<MessTopUpOrder> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessTopUpOrder> messTopUpOrders =
					messTopUpOrderService.getMessTopUpOrderPageByMainId(page, mainId, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);
			jsonData.put("messTopUpOrders", messTopUpOrders.getRecords());
			jsonData.put("mainId", mainId);
			jsonData.put("url", "messOrder/topUpOrder.do");
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/topUpOrder");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("充值记录数据获取失败");
		}
	}
	
	/**
	 * 充值记录(根据条件查询)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "充值记录(根据条件查询)",notes = "充值记录(根据条件查询)",httpMethod = "POST")
	@RequestMapping(value = "/selectTopUpOrder", method= RequestMethod.POST)
	public ResponseDTO selectTopUpOrder(HttpServletRequest request,
			Page<MessTopUpOrder> page, @Valid @ModelAttribute SelectMainIdDepIdCardCodeVo saveVo) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Page<MessTopUpOrder> messTopUpOrders =
					messTopUpOrderService.selectTopUpOrder(page, params, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			jsonData.put("messDepartments", messDepartments);
			jsonData.put("messTopUpOrders", messTopUpOrders);
			jsonData.put("mainId", mainId);
			jsonData.put("params", params);
			jsonData.put("url", "messOrder/selectTopUpOrder.do");
//			mv.setViewName("merchants/trade/mess/admin/mealOrder/topUpOrder");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("充值记录(根据条件查询)数据获取失败");
		}
	}
	
	/**
	 * 导出充值记录
	 * @param response
	 * @param saveVo
	 */
	@RequestMapping(value = "/exportsTopUpOrder")
	public void exportsTopUpOrder(HttpServletRequest request,HttpServletResponse response,
								  @Valid @ModelAttribute SelectMainIdDepIdCardCodeVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Map<String, Object> msg = messTopUpOrderService.exports(params);
			if ((boolean) msg.get("result")) {
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
	
}
