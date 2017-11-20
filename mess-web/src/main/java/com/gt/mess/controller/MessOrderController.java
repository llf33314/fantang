package com.gt.mess.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.entity.*;
import com.gt.mess.service.MessBuyTicketOrderService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessMealOrderService;
import com.gt.mess.service.MessTopUpOrderService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 食堂后台（订单）
 * @author ZengWenXiang
 * @QQ 307848200
 */
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
	 * 数据统计入口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
//	@CommAnno(menu_url="messOrder/mealOrderIndex.do")
	@RequestMapping(value = "/mealOrderIndex")
	public ModelAndView mealOrderIndex(HttpServletRequest request, HttpServletResponse response) {		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("merchants/trade/mess/admin/mealOrder/mealOrderIndex");
		return mv;
	}
	
	/**
	 * 订餐记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/mealOrder")
	public ModelAndView mealOrder(HttpServletRequest request, HttpServletResponse response,
			Page<MessMealOrder> page) {
		ModelAndView mv = new ModelAndView();
		try {
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
			mv.addObject("messDepartments", messDepartments);
			
			mv.addObject("breakfastNum", breakfastNum);
			mv.addObject("lunchNum", lunchNum);
			mv.addObject("dinnerNum", dinnerNum);
			mv.addObject("nightNum", nightNum);
			
			mv.addObject("breakfastMealNum", breakfastMealNum);
			mv.addObject("lunchMealNum", lunchMealNum);
			mv.addObject("dinnerMealNum", dinnerMealNum);
			mv.addObject("nightMealNum", nightMealNum);
			
			mv.addObject("url", "messOrder/mealOrder.do");
			mv.addObject("messMealOrders", messMealOrders);
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/mealOrder/mealOrder");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 订餐记录（根据条件搜索）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectMealOrder")
	public ModelAndView selectMealOrder(HttpServletRequest request, HttpServletResponse response,
			Page<MessMealOrder> page,@RequestParam Map <String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
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
			mv.addObject("messDepartments", messDepartments);
			
			mv.addObject("breakfastNum", breakfastNum);
			mv.addObject("lunchNum", lunchNum);
			mv.addObject("dinnerNum", dinnerNum);
			mv.addObject("nightNum", nightNum);
			
			mv.addObject("breakfastMealNum", breakfastMealNum);
			mv.addObject("lunchMealNum", lunchMealNum);
			mv.addObject("dinnerMealNum", dinnerMealNum);
			mv.addObject("nightMealNum", nightMealNum);
			
			mv.addObject("params", params);
			mv.addObject("url", "messOrder/selectMealOrder.do");
			mv.addObject("messMealOrders", messMealOrders);
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/mealOrder/mealOrder");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 导出订餐记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsMealOrder")
	public void exportsMealOrder(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
			Map<String, Object> msg = messMealOrderService.exports(params);
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
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsMealOrderForMonth")
	public void exportsMealOrderForMonth(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
			Map<String, Object> msg = messMealOrderService.exportsMealOrderForMonth(params);
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
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/buyTicketStatistics")
	public ModelAndView buyTicketStatistics(HttpServletRequest request, HttpServletResponse response,
			Page<MessBuyTicketOrder> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessBuyTicketOrder> messBuyTicketOrders = 
					messBuyTicketOrderService.getMessBuyTicketOrderPageMainId(page, mainId, 10);
			List<MessDepartment> messDepartments = 
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messBuyTicketOrders", messBuyTicketOrders);
			mv.addObject("mainId", mainId);
			mv.addObject("url", "messOrder/buyTicketStatistics.do");
			mv.setViewName("merchants/trade/mess/admin/mealOrder/buyTicketStatistics");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 购票统计(根据条件查询)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectBuyTicketStatistics")
	public ModelAndView selectBuyTicketStatistics(HttpServletRequest request, HttpServletResponse response,
			Page<MessBuyTicketOrder> page,@RequestParam Map <String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			Page<MessBuyTicketOrder> messBuyTicketOrders = 
					messBuyTicketOrderService.selectBuyTicketStatistics(page, params, 10);
			List<MessDepartment> messDepartments = 
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messBuyTicketOrders", messBuyTicketOrders);
			mv.addObject("mainId", mainId);
			mv.addObject("params", params);
			mv.addObject("url", "messOrder/selectBuyTicketStatistics.do");
			mv.setViewName("merchants/trade/mess/admin/mealOrder/buyTicketStatistics");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 导出购票记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsBuyTicket")
	public void exportsBuyTicket(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
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
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/subsidyTicket")
	public ModelAndView subsidyTicket(HttpServletRequest request, HttpServletResponse response,
			Page<MessBuyTicketOrder> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessBuyTicketOrder> messBuyTicketOrders = 
					messBuyTicketOrderService.getSubsidyTicketOrderPageMainId(page, mainId, 10);
			List<MessDepartment> messDepartments = 
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messBuyTicketOrders", messBuyTicketOrders);
			mv.addObject("mainId", mainId);
			mv.addObject("url", "messOrder/subsidyTicket.do");
			mv.setViewName("merchants/trade/mess/admin/mealOrder/subsidyTicket");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 商家补助(根据条件查询)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectSubsidyTicket")
	public ModelAndView selectSubsidyTicket(HttpServletRequest request, HttpServletResponse response,
			Page<MessBuyTicketOrder> page,@RequestParam Map <String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			Page<MessBuyTicketOrder> messBuyTicketOrders = 
					messBuyTicketOrderService.selectSubsidyTicket(page, params, 10);
			List<MessDepartment> messDepartments = 
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messBuyTicketOrders", messBuyTicketOrders);
			mv.addObject("mainId", mainId);
			mv.addObject("params", params);
			mv.addObject("url", "messOrder/selectSubsidyTicket.do");
			mv.setViewName("merchants/trade/mess/admin/mealOrder/subsidyTicket");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 导出补助记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsSubsidyTicket")
	public void exportsSubsidyTicket(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
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
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/topUpOrder")
	public ModelAndView topUpOrder(HttpServletRequest request, HttpServletResponse response,
			Page<MessTopUpOrder> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessTopUpOrder> messTopUpOrders = 
					messTopUpOrderService.getMessTopUpOrderPageByMainId(page, mainId, 10);
			List<MessDepartment> messDepartments = 
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messTopUpOrders", messTopUpOrders);
			mv.addObject("mainId", mainId);
			mv.addObject("url", "messOrder/topUpOrder.do");
			mv.setViewName("merchants/trade/mess/admin/mealOrder/topUpOrder");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 充值记录(根据条件查询)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectTopUpOrder")
	public ModelAndView selectTopUpOrder(HttpServletRequest request, HttpServletResponse response,
			Page<MessTopUpOrder> page,@RequestParam Map <String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = 
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			Page<MessTopUpOrder> messTopUpOrders = 
					messTopUpOrderService.selectTopUpOrder(page, params, 10);
			List<MessDepartment> messDepartments = 
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messTopUpOrders", messTopUpOrders);
			mv.addObject("mainId", mainId);
			mv.addObject("params", params);
			mv.addObject("url", "messOrder/selectTopUpOrder.do");
			mv.setViewName("merchants/trade/mess/admin/mealOrder/topUpOrder");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 导出充值记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsTopUpOrder")
	public void exportsTopUpOrder(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
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
