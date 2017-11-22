package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BusinessException;
import com.gt.mess.exception.ResponseEntityException;
import com.gt.mess.service.*;
import com.gt.mess.util.CommonUtil;
import com.gt.mess.util.DateTimeKit;
import com.gt.mess.util.RedisCacheUtil;
import com.gt.mess.util.WxmpUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食堂营业前台
 * @author psr
 *
 */
@Controller
@RequestMapping(value = "messReception")
public class MessReceptionController {

	private Logger logger = Logger.getLogger(MessReceptionController.class);

	@Autowired
	private MessMainService messMainService;

	@Autowired
	private MessCancelTicketService messCancelTicketService;

	@Autowired
	private MessMealOrderService messMealOrderService;

	@Autowired
	private MessBasisSetService messBasisSetService;

	@Autowired
	private MessMealTempOrderService messMealTempOrderService;

	@Autowired
	private MessAuthorityMemberService messAuthorityMemberService;

	@Autowired
	private MessCardService messCardService;

	@Autowired
	private MessAddFoodOrderService messAddFoodOrderService;

	@Autowired
	private MessDepartmentMapper messDepartmentMapper;

	@Autowired
	private MessOldManCardService messOldManCardService;

//	@Autowired
//	private DictService  dictService;
//
//	@Autowired
//	private BusUserMapper busUserMapper;
//
//	@Autowired
//	private WxPublicUsersMapper wxPublicUsersMapper;
//
//	@Autowired
//	private MemberService memberService;

//	@Value("#{config['public.getgrant.url.prefix']}")
//	private String signPath;

	@Autowired
	private MessOldManCardOrderService messOldManCardOrderService;

	@Autowired
	private WxmpUtil wxmpUtil;


    /**
     * 检查是否授权
     * @param request
     * @param mainId
     * @return
     */
    public Map<String,Object> authorize(HttpServletRequest request,HttpServletResponse response,Integer busId,Integer mainId,String url){
        Map<String,Object> mapObj = new HashMap<String, Object>();
        /**正式*/
        if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
            mapObj.put("type", "0");
			try {
				wxmpUtil.authorizeMember(request,busId,url,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
//            if(url == null){
//                mapObj.put("url", "redirect:/messMobile/" + busId + "/" + mainId +"/79B4DE7C/userGrant.do");
//            }else{
//                Map<String,Object> map=new HashMap<String, Object>();
//                String redisKey = CommonUtil.getCode();
//                redisCacheUtil.set(redisKey, url, 300L);
//                map.put("redisKey", redisKey);
////				String returnUrl=userLogin(request, response, busId, map);
////                if(CommonUtil.isNotEmpty(returnUrl)){
////                    mapObj.put("url", returnUrl);
////                }
//            }
//            return mapObj;
        }else{
            Member member = SessionUtils.getLoginMember(request,busId);
            //如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
            if(CommonUtil.isEmpty(member)){
                mapObj.put("type", "0");
				try {
					wxmpUtil.authorizeMember(request,busId,url,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
//                if(url == null){
//                    mapObj.put("url", "redirect:/messMobile/" + busId + "/" + mainId + "/79B4DE7C/userGrant.do");
//                }else{
//                    Map<String,Object> map=new HashMap<String, Object>();
//                    String redisKey = CommonUtil.getCode();
//                    redisCacheUtil.set(redisKey, url, 300L);
//                    map.put("redisKey", redisKey);
//                    String returnUrl=userLogin(request, response, busId, map);
//                    if(CommonUtil.isNotEmpty(returnUrl)){
//                        mapObj.put("url", returnUrl);
//                    }
//                }
//                return mapObj;
            }
        }
        mapObj.put("type", 1);
        Member member = SessionUtils.getLoginMember(request,busId);
        mapObj.put("member", member);
        return mapObj;
    }


    /**
	 * 前台操作入口
	 * @param session
	 * @param request
	 * @return
	 */
//	@CommAnno(menu_url="messReception/receptionIndex.do")
	@RequestMapping("receptionIndex")
	public String messStart(HttpServletRequest request , HttpServletResponse response){
		Object indexurl = request.getParameter("indexurl");
		if(indexurl!=null){
			request.setAttribute("indexurl", indexurl);
		}
		BusUser busUser = SessionUtils.getLoginUser(request);
		MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
		if(CommonUtil.isEmpty(messMain)){
			return "redirect:/mess/messStart.do";
		}
		return "merchants/trade/mess/admin/reception/receptionIndex";
	}

	/**
	 * 跳转到现场核销页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/toLocaleCancel")
	public ModelAndView toLocaleCancel(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			if(CommonUtil.isEmpty(messMain)){
				return new ModelAndView("redirect:/mess/messStart.do");
			}
			mv.addObject("authorityUrl", messMain.getAuthorityUrl());
			mv.setViewName("merchants/trade/mess/admin/reception/localeCancel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 跳转到现场核销页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/toListCancel")
	public ModelAndView toListCancel(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			if(CommonUtil.isEmpty(messMain)){
				return new ModelAndView("redirect:/mess/messStart.do");
			}
			int mainId = messMain.getBusId();
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/reception/listCancel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 现场核销信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/localeCancelInfo")
	public ResponseDTO localeCancelInfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			String messType = null; // 当前类型
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);

			if(CommonUtil.isNotEmpty(messBasisSet)){
				String[] messTypes = messBasisSet.getMessType().split(",|，");
				for(String s : messTypes){
					if("0".equals(s)){ // 早餐
						if(DateTimeKit.isBetweenByHHmmss(messBasisSet.getBreakfastStart(), messBasisSet.getBreakfastEnd())){
							messType = "0";
							break;
						}
					}else if("1".equals(s)){ // 中餐
						if(DateTimeKit.isBetweenByHHmmss(messBasisSet.getLunchStart(), messBasisSet.getLunchEnd())){
							messType = "1";
							break;
						}
					}else if("2".equals(s)){ // 晚餐
						if(DateTimeKit.isBetweenByHHmmss(messBasisSet.getDinnerStart(), messBasisSet.getDinnerEnd())){
							messType = "2";
							break;
						}
					}else if("3".equals(s)){ // 夜宵
						if(DateTimeKit.isBetweenByHHmmss(messBasisSet.getNightStart(), messBasisSet.getNightEnd())){
							messType = "3";
							break;
						}
					}
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("messType", "-");
			map.put("mealOrderNum", "-");
			map.put("gainNum", "-");
			map.put("notNum", "-");
			map.put("mealOrderMealNum", "0");
			map.put("gainMealNum", "0");
			map.put("notMealNum", "0");
			map.put("time", new Date());
			if(CommonUtil.isNotEmpty(messType)){
				Map<String, Object> res = messMealOrderService.getMessMealOrderNum(mainId, Integer.valueOf(messType));
				Integer code = (Integer) res.get("code");
				if(code == 1){
					map.put("messType", messType);
					map.put("time", new Date());
					map.put("mealOrderNum", res.get("mealOrderNum"));
					map.put("gainNum", res.get("gainNum"));
					map.put("notNum", res.get("notNum"));
					map.put("mealOrderMealNum", res.get("mealOrderMealNum"));
					map.put("gainMealNum", res.get("gainMealNum"));
					map.put("notMealNum", res.get("notMealNum"));
				}
			}
			// 返回参数
			return ResponseDTO.createBySuccess( JSON.toJSONString(map) );
		} catch ( BusinessException e ) {
			throw new ResponseEntityException( e.getCode(), e.getMessage() );
		}
	}

	/**
	 * 最新5条现场核销信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/localeCancelList")
	public ResponseDTO localeCancelList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			int nums = Integer.valueOf((String) params.get("nums"));
			List<Map<String, Object>> cancelList = messMealTempOrderService.getMessMealTempOrderByMainId(mainId, nums);
            // 返回参数
            return ResponseDTO.createBySuccess( JSON.toJSONString(cancelList) );
		} catch ( BusinessException e ) {
            throw new ResponseEntityException( e.getCode(), e.getMessage() );
        }
	}

	/**
	 * 核销记录(根据条件查询)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectCancelRecord")
	public ResponseDTO selectCancelRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			int nums = Integer.valueOf((String) params.get("nums"));
			int pageNum = 1;
			if(CommonUtil.isNotEmpty(params.get("pageNum"))){
				pageNum = Integer.valueOf((String) params.get("pageNum"));
			}
			Page<MessCancelTicket> page = new Page<>();
			page.setCurrent(pageNum);
			page.setSize(nums);
			// 查询参数
			if(CommonUtil.isEmpty(params.get("cardCode"))){
				params.remove("cardCode");
			}else{
				params.put("cardCode", params.get("cardCode") + "%");
			}
			if(CommonUtil.isEmpty(params.get("stime")) || CommonUtil.isEmpty(params.get("etime"))){
				params.remove("stime");
				params.remove("etime");
			}else{
				long stime = Long.parseLong((String) params.get("stime"));
				long etime = Long.parseLong((String) params.get("etime"));
				params.put("stime", new Date(stime));
				params.put("etime", new Date(etime));
			}
			if(!CommonUtil.isEmpty(params.get("ndate"))){
				long ndate = Long.parseLong((String) params.get("ndate"));
				long etime = DateTimeKit.getToDayTimeStamp();
				long stime = etime - (24 * 60 * 60 * 1000 * ndate);
				params.put("stime", new Date(stime));
				params.put("etime", new Date(etime));
			}
			Page<MessCancelTicket> messCancelTickets =
					messCancelTicketService.selectCancelRecord(page, params, nums);
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("list", messCancelTickets.getRecords());
			long totalNum = 1;
			if(messCancelTickets.getTotal() > nums){
				long tn = messCancelTickets.getTotal() / nums;
				totalNum = (messCancelTickets.getTotal() % nums) > 0 ? (tn + 1) : tn;
			}
			resMap.put("totalNum", totalNum);
			// 返回参数
			return ResponseDTO.createBySuccess( JSON.toJSONString(resMap) );
		} catch ( BusinessException e ) {
			throw new ResponseEntityException( e.getCode(), e.getMessage() );
		}
	}

	/**
	 * 根据条件导出核销记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exports")
	public void exports(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			if(CommonUtil.isEmpty(params.get("cardCode"))){
				params.remove("cardCode");
			}else{
				params.put("cardCode", params.get("cardCode") + "%");
			}
			if(CommonUtil.isEmpty(params.get("stime")) || CommonUtil.isEmpty(params.get("etime"))){
				params.remove("stime");
				params.remove("etime");
			}
			Map<String, Object> msg = messCancelTicketService.exports(params);
			if ((boolean) msg.get("result")) {
				HSSFWorkbook wb = (HSSFWorkbook) msg.get("book");
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

//	/**
//	 * 用户授权  OK
//	 * actId busId
//	 * @param only
//	 * @param response
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/{busId}/{cardId}/{mealCode}/79B4DE7C/userGrant")
//	public void userGrant(HttpServletRequest request, HttpServletResponse response, HttpSession session,
//			@PathVariable Integer busId, @PathVariable Integer cardId, @PathVariable String mealCode) throws Exception  {
//		// 解决ios返回键问题
//		if(!CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){
//			response.sendRedirect("/messReception/" + busId + "/" + cardId + "/" + mealCode + "/79B4DE7C/wirteOff.do");
//			return;
//		}
//		logger.info("进入微食堂核销授权");
//		String temp = signPath;
//		try {
//			BusUser busUser = SessionUtils.getLoginUser(request);
//			Integer busIdwx = busId;
//			if(busUser.getPid() != 0){
//				busIdwx = SessionUtils.getPidBusId(request);
//			}
//			WxPublicUsers users = JSONObject.toJavaObject(wxmpUtil.getWxPublic(busIdwx),WxPublicUsers.class);
//			String redirect_uri= temp.replace("$", "messReception/" + users.getId() + "/" + busId + "/" + cardId +"/" + mealCode);
//			Map<String, Object> param = null;
//			CommonUtil.userGrant(response, users, redirect_uri, true, param);
//		} catch (Exception e) {
//			logger.error("微食堂用户授权失败");
//			throw new BusinessException("用户授权失败");
//		}
//	}

//	/**
//	 * 用户授权回调方法 OK
//	 * @param appid
//	 * @param code
//	 * @return
//	 */
//	@RequestMapping(value="/{userId}/{busId}/{cardId}/{mealCode}/79B4DE7C/getUserOpenId")
//	public String getUserOpenId(HttpServletRequest request, String code,
//			@PathVariable Integer userId, @PathVariable Integer busId, @PathVariable Integer cardId, @PathVariable String mealCode){
//		Map<String, Object> result = memberService.saveMember(code, userId, true);
//		Member member=(Member) result.get("member");
//		String redirect_rul = "redirect:/messReception/" + busId + "/" + cardId + "/" + mealCode + "/79B4DE7C/wirteOff.do";
//		if(member != null){
//			CommonUtil.setLoginMember(request, member);
//		}
//		logger.info("进入回调！");
//		return redirect_rul;
//	}

	/**
	 * 扫码核销 (扫码员) - GET
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/{busId}/{cardId}/{mealCode}/79B4DE7C/wirteOff", method = RequestMethod.GET)
	public ModelAndView wirteOffGET(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer busId, @PathVariable Integer cardId, @PathVariable String mealCode) {
		ModelAndView mav = new ModelAndView();
		try {
			MessMain messMain = messMainService.getMessMainByBusId(busId);
			Integer mainId = messMain.getId();
            BusUser busUser = wxmpUtil.getBusUserApi(messMain.getBusId());
            Integer busIdwx = messMain.getBusId();
            if(busUser.getPid() != 0){
                busIdwx = wxmpUtil.getMainBusId(messMain.getBusId()).getId();
            }
			Member member = SessionUtils.getLoginMember(request,busId);
            String url = "/messReception/" + busId + "/" + cardId + "/" + mealCode + "/79B4DE7C/wirteOff.do";
            Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
            if("0".equals(obj.get("type").toString())){
                mav.setViewName(obj.get("url").toString());
                return mav;
            }
			// 开始核销
			mav.setViewName("merchants/trade/mess/mobile/error");
			List<MessAuthorityMember> lists = messAuthorityMemberService.getMessAuthorityByMember(mainId, member.getId());
			if(lists == null || lists.size() <= 0){
				// 返回无权核销页面
				mav.addObject("msg", "核销失败，你没有核销权限");
				return mav;
			}
			Map<String, Object> map = messCardService.cancelMealTicket(mealCode, cardId,2,member.getId());
			if(map == null || map.size() <= 0 || CommonUtil.isEmpty(map.get("code"))){
				// 返回核销失败页面
				mav.addObject("msg", "核销失败，请尝试刷新取餐二维码");
				return mav;
			}
			Integer code = (Integer) map.get("code");
			if(code == -1){
				// 返回核销失败页面
				mav.addObject("msg", "核销失败，请尝试刷新取餐二维码");
				return mav;
			}
			if(code == -2){
				// 返回核销失败页面
				mav.addObject("msg", "核销失败，已经核销过了");
				return mav;
			}
			// 返回核销成功页面
			mav.addObject("msg", "核销成功");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("msg", "核销错误，请尝试刷新取餐二维码");
		}
		return mav;
	}

	/**
	 * 扫码核销 - POST
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/{busId}/{cardId}/{mealCode}/79B4DE7C/wirteOff", method = RequestMethod.POST)
	public ResponseDTO wirteOffPOST(HttpServletRequest request,
			HttpServletResponse response,@PathVariable Integer busId,@PathVariable Integer cardId,@PathVariable String mealCode,
			@RequestParam Map <String,Object> params) {
		try {
			// 开始核销
			if(CommonUtil.isEmpty(mealCode)){
				return ResponseDTO.createByErrorMessage("核销失败，取餐二维码不正确");
			}
			Map<String, Object> map = messCardService.cancelMealTicket(mealCode, cardId,1,0);
			if(map == null || map.size() <= 0 || CommonUtil.isEmpty(map.get("code"))){
				// 返回核销失败页面
                return ResponseDTO.createByErrorMessage("核销失败，请尝试刷新取餐二维码");
			}
			Integer code = (Integer) map.get("code");
			if(code == -1){
				// 返回核销失败页面
				return ResponseDTO.createByErrorMessage("核销失败，请尝试刷新取餐二维码");
			}
			if(code == -2){
				// 返回核销失败页面
                return ResponseDTO.createByErrorMessage("核销失败，已经核销过了");
			}
			// 返回核销成功页面
            return ResponseDTO.createBySuccessMessage("核销成功");
		} catch (Exception e) {
            throw new ResponseEntityException("核销错误，请尝试刷新取餐二维码");
        }
	}

	/**
	 * 取餐码核销
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/wirteOff")
	public ResponseDTO wirteOffByMealCode(HttpServletRequest request,
			HttpServletResponse response, @RequestParam Map <String,Object> params) {
		try {
			// 开始核销
			if(CommonUtil.isEmpty(params.get("mealCode"))){
                return ResponseDTO.createByErrorMessage("核销失败，取餐号不能为空");
			}
			String mealCode = (String) params.get("mealCode");
			Map<String, Object> map = messCardService.cancelMealTicket(mealCode, null,0,0);
			if(map == null || map.size() <= 0 || CommonUtil.isEmpty(map.get("code"))){
				// 返回核销失败页面
                return ResponseDTO.createByErrorMessage("核销失败，请尝试重新输入取餐号");
			}
			Integer code = (Integer) map.get("code");
			if(code == -1){
				// 返回核销失败页面
                return ResponseDTO.createByErrorMessage("核销失败，请尝试重新输入取餐号");
			}
			if(code == -2){
				// 返回核销失败页面
                return ResponseDTO.createByErrorMessage("核销失败，已经核销过了");
			}
			// 返回核销成功页面
            return ResponseDTO.createBySuccessMessage("核销成功");
		}catch (Exception e) {
            throw new ResponseEntityException("核销错误，请尝试重新输入取餐号");
        }
	}

	/**
	 * 加餐核销订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addFoodOrder")
	public ModelAndView addFoodOrder(HttpServletRequest request, HttpServletResponse response,
			Page<MessAddFoodOrder> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			Page<MessAddFoodOrder> messAddFoodOrders =
					messAddFoodOrderService.getMessAddFoodOrderPageByMainId(page, mainId, 10);
			mv.addObject("messAddFoodOrders", messAddFoodOrders);
			mv.addObject("url", "messReception/addFoodOrder.do");
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/reception/addFoodOrder");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 加餐核销订单（搜索）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectAddFoodOrder")
	public ModelAndView selectAddFoodOrder(HttpServletRequest request, HttpServletResponse response,
			Page<MessAddFoodOrder> page,@RequestParam Map<String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			params.put("mainId", mainId);
			Page<MessAddFoodOrder> messAddFoodOrders =
					messAddFoodOrderService.selectAddFoodOrder(page, params, 10);
			mv.addObject("messAddFoodOrders", messAddFoodOrders);
			mv.addObject("url", "messReception/selectAddFoodOrder.do");
			mv.addObject("params", params);
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/reception/addFoodOrder");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 导出加餐核销记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsAddFoodOrder")
	public void exportsAddFoodOrder(HttpServletRequest request,
			HttpServletResponse response,@RequestParam Map <String,Object> params) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			params.put("mainId", mainId);
			Map<String, Object> msg = messAddFoodOrderService.exports(params);
			if ((boolean) msg.get("result")) {
				HSSFWorkbook wb = (HSSFWorkbook) msg.get("book");
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

//	手动核销
	/**
	 * 老人卡
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/oldManCard")
	public ModelAndView oldManCard(HttpServletRequest request, HttpServletResponse response,
			Page<MessOldManCard> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessOldManCard> messOldManCards =
					messOldManCardService.getMessOldManCardPageByMainId(page, mainId, 10);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			mv.addObject("messDepartments", messDepartments);
			mv.addObject("messOldManCards", messOldManCards);
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/reception/oldManCard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 老人卡(搜索)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectOldManCard")
	public ModelAndView selectOldManCard(HttpServletRequest request, HttpServletResponse response,
			Page<MessOldManCard> page,@RequestParam Map <String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			Page<MessOldManCard> messOldManCards =
					messOldManCardService.selectOldManCardManage(page, params, 10);
			mv.addObject("search", params.get("cardCode").toString());
			mv.addObject("messOldManCards", messOldManCards);
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/reception/oldManCard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 普通饭卡
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/commonCard")
	public ModelAndView commonCard(HttpServletRequest request, HttpServletResponse response,
			Page<MessCard> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessCard> messCards =
					messCardService.commonCard(page, mainId, 10);
			MessBasisSet messBasisSet =
					messBasisSetService.getMessBasisSetByMainId(messMain.getId());
			mv.addObject("messBasisSet", messBasisSet);
			mv.addObject("messCards", messCards);
			mv.addObject("mainId", mainId);
			mv.setViewName("merchants/trade/mess/admin/reception/commonCard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 核销饭票（手动核销）
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 核销饭票（手动核销）",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "/cancelTicket")
	public void cancelTicket(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messCardService.cancelTicket(params);
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
	 * 普通饭卡(根据卡号查询)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectCommonCardByCardCode")
	public ModelAndView selectCommonCardByCardCode(HttpServletRequest request, HttpServletResponse response,
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
			mv.addObject("messBasisSet", messBasisSet);
			mv.addObject("search", search);
			mv.addObject("messCards", messCards);
			mv.addObject("selectType", 0);//0卡号查询 1名字查询 2部门查询
			mv.setViewName("merchants/trade/mess/admin/reception/commonCard");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}


//	核销记录

	/**
	 * 老人卡&普通卡（手动核销列表）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{type}/messOldManCardOrder")
	public ModelAndView messOldManCardOrder(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("type") Integer type,Page<MessOldManCardOrder> page) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Page<MessOldManCardOrder> messOldManCardOrders = null;
			Integer mainId = messMain.getId();
			if(type == 3){
				messOldManCardOrders =
						messOldManCardOrderService.getCommonMessCardOrderPageByMainId(page, mainId, 10);
				mv.addObject("url", "messReception/3/messOldManCardOrder.do");
				mv.setViewName("merchants/trade/mess/admin/reception/manualListCancel");
			}else if(type == 0){
				messOldManCardOrders =
						messOldManCardOrderService.getMessOldManCardOrderPageByMainId(page, mainId, 10);
				mv.addObject("url", "messReception/0/messOldManCardOrder.do");
				mv.setViewName("merchants/trade/mess/admin/reception/oldManListCancel");
			}else if(type == 1){
				messOldManCardOrders =
						messOldManCardOrderService.getMessOldManCardOrderPageByMainId2(page, mainId, 10);
				mv.addObject("url", "messReception/1/messOldManCardOrder.do");
				mv.setViewName("merchants/trade/mess/admin/mealOrder/oldManCardOrder");
			}

			mv.addObject("messOldManCardOrders", messOldManCardOrders);
			mv.addObject("mainId", mainId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 老人卡&普通卡(搜索)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{type}/selectMessOldManCardOrder")
	public ModelAndView selectMessOldManCardOrder(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("type") Integer type,Page<MessOldManCardOrder> page,@RequestParam Map <String,Object> params) {
		ModelAndView mv = new ModelAndView();
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			params.put("mainId", mainId);
			Page<MessOldManCardOrder> messOldManCardOrders = null;
			if(type == 3){
				messOldManCardOrders =
						messOldManCardOrderService.selectMessCommonCardOrder(page, params, 10);
				mv.addObject("url", "messReception/3/selectMessOldManCardOrder.do");
				mv.setViewName("merchants/trade/mess/admin/reception/manualListCancel");
			}else if(type == 0){
				messOldManCardOrders =
						messOldManCardOrderService.selectMessOldManCardOrder(page, params, 10);
				mv.addObject("url", "messReception/0/selectMessOldManCardOrder.do");
				mv.setViewName("merchants/trade/mess/admin/reception/oldManListCancel");
			}else if(type == 1){
				messOldManCardOrders =
						messOldManCardOrderService.selectMessOldManCardOrder2(page, params, 10);
				mv.addObject("url", "messReception/1/selectMessOldManCardOrder.do");
				mv.setViewName("merchants/trade/mess/admin/mealOrder/oldManCardOrder");
			}
			mv.addObject("search", params.get("cardCode").toString());
			mv.addObject("params", params);
			mv.addObject("messOldManCardOrders", messOldManCardOrders);
			mv.addObject("mainId", mainId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 导出老人卡&普通卡核销记录
	 * @param request
	 * @param response
	 * @param params
	 */
	@RequestMapping(value = "/exportsCardOrder")
	public void exportsCardOrder(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			params.put("mainId", mainId);
			Map<String, Object> msg = messOldManCardOrderService.exports(params);
			if ((boolean) msg.get("result")) {
				HSSFWorkbook wb = (HSSFWorkbook) msg.get("book");
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
