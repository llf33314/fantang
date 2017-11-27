package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BaseException;
import com.gt.mess.exception.BusinessException;
import com.gt.mess.exception.ResponseEntityException;
import com.gt.mess.service.*;
import com.gt.mess.util.CommonUtil;
import com.gt.mess.util.DateTimeKit;
import com.gt.mess.util.WxmpUtil;
import com.gt.mess.vo.SelectCancelRecordVo;
import com.gt.mess.vo.SelectMainIdDepIdCardCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 食堂营业前台
 * @author psr
 *
 */
@Api(description = "食堂营业前台")
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
            }
        }
        mapObj.put("type", 1);
        Member member = SessionUtils.getLoginMember(request,busId);
        mapObj.put("member", member);
        return mapObj;
    }


//    /**
//	 * 前台操作入口
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("receptionIndex")
//	public String messStart(HttpServletRequest request){
//		Object indexurl = request.getParameter("indexurl");
//		if(indexurl!=null){
//			request.setAttribute("indexurl", indexurl);
//		}
//		BusUser busUser = SessionUtils.getLoginUser(request);
//		MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
//		if(CommonUtil.isEmpty(messMain)){
//			return "redirect:/mess/messStart.do";
//		}
//		return "merchants/trade/mess/admin/reception/receptionIndex";
//	}

	/**
	 * 跳转到现场核销页面
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "跳转到现场核销页面",notes = "跳转到现场核销页面",httpMethod = "GET")
	@RequestMapping(value = "/toLocaleCancel", method = RequestMethod.GET)
	public ResponseDTO toLocaleCancel(HttpServletRequest request) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			if(CommonUtil.isEmpty(messMain)){
				return ResponseDTO.createByError();
			}
			jsonData.put("authorityUrl", messMain.getAuthorityUrl());
//			mv.setViewName("merchants/trade/mess/admin/reception/localeCancel");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("基础设置数据获取失败");
		}
	}

	/**
	 * 跳转到现场核销页面(列表)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "跳转到现场核销页面(列表)",notes = "跳转到现场核销页面(列表)",httpMethod = "GET")
	@RequestMapping(value = "/toListCancel", method = RequestMethod.GET)
	public ResponseDTO toListCancel(HttpServletRequest request) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			if(CommonUtil.isEmpty(messMain)){
				return ResponseDTO.createByError();
			}
			int mainId = messMain.getBusId();
			jsonData.put("mainId", mainId);
//			mv.setViewName("merchants/trade/mess/admin/reception/listCancel");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("基础设置数据获取失败");
		}
	}

	/**
	 * 现场核销信息
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "现场核销信息",notes = "现场核销信息",httpMethod = "GET")
	@RequestMapping(value = "/localeCancelInfo", method = RequestMethod.GET)
	public ResponseDTO localeCancelInfo(HttpServletRequest request) {
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
	 * @return
	 */
	@ApiOperation(value = "现场核销信息",notes = "现场核销信息",httpMethod = "POST")
	@RequestMapping(value = "/localeCancelList", method = RequestMethod.POST)
	public ResponseDTO localeCancelList(HttpServletRequest request,
										@ApiParam(name = "nums", value = "条数", required = true)
										@RequestParam Integer nums) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
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
	 * @param saveVo
	 * @return
	 */
	@ApiOperation(value = "核销记录(根据条件查询)",notes = "核销记录(根据条件查询)",httpMethod = "POST")
	@RequestMapping(value = "/selectCancelRecord", method = RequestMethod.POST)
	public ResponseDTO selectCancelRecord(HttpServletRequest request,
										  @Valid @ModelAttribute SelectCancelRecordVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
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
	 * @param saveVo
	 */
	@ApiOperation(value = "根据条件导出核销记录",notes = "根据条件导出核销记录",httpMethod = "POST")
	@RequestMapping(value = "/exports", method = RequestMethod.POST)
	public void exports(HttpServletRequest request,
			HttpServletResponse response,@Valid @ModelAttribute SelectCancelRecordVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain = messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
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
	 * @param busId
	 * @param cardId
	 * @param mealCode
	 * @return
	 */
	@ApiOperation(value = "扫码核销 (扫码员) - GET",notes = "扫码核销 (扫码员) - GET",httpMethod = "GET")
	@RequestMapping(value = "/{busId}/{cardId}/{mealCode}/79B4DE7C/wirteOff", method = RequestMethod.GET)
	public ModelAndView wirteOffGET(HttpServletRequest request, HttpServletResponse response,
									@ApiParam(name = "busId", value = "商家ID", required = true)
									@PathVariable Integer busId,
									@ApiParam(name = "cardId", value = "饭卡ID", required = true)
									@PathVariable Integer cardId,
									@ApiParam(name = "mealCode", value = "取餐号", required = true)
									@PathVariable String mealCode) {
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
	 * @param cardId
	 * @param mealCode
	 * @return
	 */
	@ApiOperation(value = "扫码核销 - POST",notes = "扫码核销 - POST",httpMethod = "POST")
	@RequestMapping(value = "/{cardId}/{mealCode}/79B4DE7C/wirteOff", method = RequestMethod.POST)
	public ResponseDTO wirteOffPOST(@ApiParam(name = "cardId", value = "饭卡ID", required = true)
									@PathVariable Integer cardId,
									@ApiParam(name = "mealCode", value = "取餐号", required = true)
									@PathVariable String mealCode) {
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
	 * @param mealCode
	 * @return
	 */
	@ApiOperation(value = "取餐码核销",notes = "取餐码核销",httpMethod = "POST")
	@RequestMapping(value = "/wirteOff", method = RequestMethod.POST)
	public ResponseDTO wirteOffByMealCode(@ApiParam(name = "mealCode", value = "取餐号", required = true)
										  @RequestParam String mealCode) {
		try {
			// 开始核销
			if(CommonUtil.isEmpty(mealCode)){
                return ResponseDTO.createByErrorMessage("核销失败，取餐号不能为空");
			}
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
	 * @return
	 */
	@ApiOperation(value = "加餐核销订单",notes = "加餐核销订单数据获取",httpMethod = "GET")
	@RequestMapping(value = "/addFoodOrder", method = RequestMethod.GET)
	public ResponseDTO addFoodOrder(HttpServletRequest request,
			Page<MessAddFoodOrder> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			Page<MessAddFoodOrder> messAddFoodOrders =
					messAddFoodOrderService.getMessAddFoodOrderPageByMainId(page, mainId, 10);
			jsonData.put("messAddFoodOrders", messAddFoodOrders);
			jsonData.put("url", "messReception/addFoodOrder.do");
			jsonData.put("mainId", mainId);
//			mv.setViewName("merchants/trade/mess/admin/reception/addFoodOrder");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("加餐核销订单数据获取失败");
		}
	}

	/**
	 * 加餐核销订单（搜索）
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "加餐核销订单（搜索）",notes = "搜索加餐核销订单",httpMethod = "POST")
	@RequestMapping(value = "/selectAddFoodOrder", method = RequestMethod.POST)
	public ResponseDTO selectAddFoodOrder(HttpServletRequest request,
			Page<MessAddFoodOrder> page,@Valid @ModelAttribute SelectMainIdDepIdCardCodeVo saveVo) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			saveVo.setMainId(mainId);
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
			Page<MessAddFoodOrder> messAddFoodOrders =
					messAddFoodOrderService.selectAddFoodOrder(page, params, 10);
			jsonData.put("messAddFoodOrders", messAddFoodOrders);
			jsonData.put("url", "messReception/selectAddFoodOrder.do");
			jsonData.put("params", params);
			jsonData.put("mainId", mainId);
//			mv.setViewName("merchants/trade/mess/admin/reception/addFoodOrder");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("加餐核销订单（搜索）订单数据获取失败");
		}
	}

	/**
	 * 导出加餐核销记录
	 * @param request
	 * @param response
	 * @param saveVo
	 */
	@ApiOperation(value = "导出加餐核销记录",notes = "导出加餐核销记录",httpMethod = "GET")
	@RequestMapping(value = "/exportsAddFoodOrder", method = RequestMethod.GET)
	public void exportsAddFoodOrder(HttpServletRequest request,
			HttpServletResponse response,@Valid @ModelAttribute SelectMainIdDepIdCardCodeVo saveVo) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			Map<String,Object> params = JSONObject.parseObject(JSONObject.toJSONString(saveVo),Map.class);
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
	 * @return
	 */
	@ApiOperation(value = "老人卡",notes = "老人卡数据获取",httpMethod = "GET")
	@RequestMapping(value = "/oldManCard", method = RequestMethod.GET)
	public ResponseDTO oldManCard(HttpServletRequest request,
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
//			mv.setViewName("merchants/trade/mess/admin/reception/oldManCard");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("老人卡订单数据获取失败");
		}
	}

	/**
	 * 老人卡(搜索)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "老人卡(搜索)",notes = "老人卡(搜索)",httpMethod = "POST")
	@RequestMapping(value = "/selectOldManCard", method = RequestMethod.POST)
	public ResponseDTO selectOldManCard(HttpServletRequest request,
			Page<MessOldManCard> page,
			@ApiParam(name = "cardCode", value = "饭卡号", required = true)
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
//			mv.setViewName("merchants/trade/mess/admin/reception/oldManCard");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("老人卡(搜索)订单数据获取失败");
		}
	}

	/**
	 * 普通饭卡
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "普通饭卡",notes = "普通饭卡数据获取",httpMethod = "GET")
	@RequestMapping(value = "/commonCard", method = RequestMethod.GET)
	public ResponseDTO commonCard(HttpServletRequest request,
			Page<MessCard> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Page<MessCard> messCards =
					messCardService.commonCard(page, mainId, 10);
			MessBasisSet messBasisSet =
					messBasisSetService.getMessBasisSetByMainId(messMain.getId());
			jsonData.put("messBasisSet", messBasisSet);
			jsonData.put("messCards", messCards);
			jsonData.put("mainId", mainId);
//			mv.setViewName("merchants/trade/mess/admin/reception/commonCard");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("普通饭卡订单数据获取失败");
		}
	}

	/**
	 * 核销饭票（手动核销）
	 * @param id
	 * @param ticketNum
	 * @param ticketType
	 * @return
	 */
	@ApiOperation(value = "核销饭票（手动核销）",notes = "核销饭票（手动核销）",httpMethod = "POST")
	@RequestMapping(value = "/cancelTicket", method = RequestMethod.POST)
	public ResponseDTO cancelTicket(@ApiParam(name = "id", value = "饭卡ID", required = true)
									@RequestParam Integer id,
									@ApiParam(name = "ticketNum", value = "核销张数", required = true)
									@RequestParam Integer ticketNum,
									@ApiParam(name = "ticketType", value = "饭票类型（0早 1中 2晚 3夜宵 4通用）", required = true)
									@RequestParam Integer ticketType) {
		try {
			int data = messCardService.cancelTicket(id,ticketNum,ticketType);
			if(data == 1)
				return ResponseDTO.createBySuccess();
			else
				return ResponseDTO.createByError();
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("核销饭票（手动核销）失败");
		}
	}

	/**
	 * 普通饭卡(根据卡号查询)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "普通饭卡(根据卡号查询)",notes = "普通饭卡(根据卡号查询)",httpMethod = "GET")
	@RequestMapping(value = "/selectCommonCardByCardCode", method = RequestMethod.GET)
	public ResponseDTO selectCommonCardByCardCode(HttpServletRequest request,
			Page<MessCard> page,
			@ApiParam(name = "cardCode", value = "饭卡号", required = true)
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
			jsonData.put("messBasisSet", messBasisSet);
			jsonData.put("cardCode", cardCode);
			jsonData.put("messCards", messCards);
			jsonData.put("selectType", 0);//0卡号查询 1名字查询 2部门查询
//			mv.setViewName("merchants/trade/mess/admin/reception/commonCard");
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("普通饭卡(根据卡号查询)数据获取失败");
		}
	}


//	核销记录

	/**
	 * 老人卡&普通卡（手动核销列表）
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "老人卡&普通卡（手动核销列表）",notes = "老人卡&普通卡（手动核销列表）",httpMethod = "POST")
	@RequestMapping(value = "/{type}/messOldManCardOrder", method = RequestMethod.POST)
	public ResponseDTO messOldManCardOrder(HttpServletRequest request,
			@ApiParam(name = "type", value = "type为 3:获取普通卡（手动核销列表），0:老人卡（手动核销列表） 1:老人卡（商家扣票、补票记录）", required = true)
			@PathVariable("type") Integer type,Page<MessOldManCardOrder> page) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Page<MessOldManCardOrder> messOldManCardOrders = null;
			Integer mainId = messMain.getId();
			if(type == 3){
				messOldManCardOrders =
						messOldManCardOrderService.getCommonMessCardOrderPageByMainId(page, mainId, 10);
				jsonData.put("url", "messReception/3/messOldManCardOrder.do");
//				mv.setViewName("merchants/trade/mess/admin/reception/manualListCancel");
			}else if(type == 0){
				messOldManCardOrders =
						messOldManCardOrderService.getMessOldManCardOrderPageByMainId(page, mainId, 10);
				jsonData.put("url", "messReception/0/messOldManCardOrder.do");
//				mv.setViewName("merchants/trade/mess/admin/reception/oldManListCancel");
			}else if(type == 1){
				messOldManCardOrders =
						messOldManCardOrderService.getMessOldManCardOrderPageByMainId2(page, mainId, 10);
				jsonData.put("url", "messReception/1/messOldManCardOrder.do");
//				mv.setViewName("merchants/trade/mess/admin/mealOrder/oldManCardOrder");
			}

			jsonData.put("messOldManCardOrders", messOldManCardOrders.getRecords());
			jsonData.put("mainId", mainId);
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("老人卡&普通卡（手动核销列表）数据获取失败");
		}
	}

	/**
	 * 老人卡&普通卡(搜索)
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "老人卡&普通卡(搜索)",notes = "老人卡&普通卡(搜索)",httpMethod = "POST")
	@RequestMapping(value = "/{type}/selectMessOldManCardOrder", method = RequestMethod.POST)
	public ResponseDTO selectMessOldManCardOrder(HttpServletRequest request,
												 @ApiParam(name = "type", value = "type为 3:获取普通卡（手动核销列表），0:老人卡（手动核销列表） 1:老人卡（商家扣票、补票记录）", required = true)
												 @PathVariable("type") Integer type,Page<MessOldManCardOrder> page,
												 @ApiParam(name = "cardCode", value = "饭卡号")
												 String cardCode,
												 @ApiParam(name = "stime", value = "开始时间")
												 String stime,
												 @ApiParam(name = "etime", value = "结束时间")
												 String etime) {
		try {
			JSONObject jsonData = new JSONObject();
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId = messMain.getId();
			Map<String,Object> params = new HashMap<>();
			params.put("stime", stime);
			params.put("etime", etime);
			params.put("mainId", mainId);
			params.put("cardCode", cardCode);
			Page<MessOldManCardOrder> messOldManCardOrders = null;
			if(type == 3){
				messOldManCardOrders =
						messOldManCardOrderService.selectMessCommonCardOrder(page, params, 10);
				jsonData.put("url", "messReception/3/selectMessOldManCardOrder.do");
//				mv.setViewName("merchants/trade/mess/admin/reception/manualListCancel");
			}else if(type == 0){
				messOldManCardOrders =
						messOldManCardOrderService.selectMessOldManCardOrder(page, params, 10);
				jsonData.put("url", "messReception/0/selectMessOldManCardOrder.do");
//				mv.setViewName("merchants/trade/mess/admin/reception/oldManListCancel");
			}else if(type == 1){
				messOldManCardOrders =
						messOldManCardOrderService.selectMessOldManCardOrder2(page, params, 10);
				jsonData.put("url", "messReception/1/selectMessOldManCardOrder.do");
//				mv.setViewName("merchants/trade/mess/admin/mealOrder/oldManCardOrder");
			}
			jsonData.put("params", params);
			jsonData.put("messOldManCardOrders", messOldManCardOrders.getRecords());
			jsonData.put("mainId", mainId);
			return ResponseDTO.createBySuccess(jsonData);
		} catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("老人卡&普通卡(搜索)数据获取失败");
		}
	}

	/**
	 * 导出老人卡&普通卡核销记录
	 * @param request
	 * @param response
	 * @param type
	 * @param cardCode
	 * @param stime
	 * @param etime
	 */
	@ApiOperation(value = "导出老人卡&普通卡核销记录",notes = "导出老人卡&普通卡核销记录",httpMethod = "POST")
	@RequestMapping(value = "/exportsCardOrder", method = RequestMethod.POST)
	public void exportsCardOrder(HttpServletRequest request,HttpServletResponse response,
								 @ApiParam(name = "type", value = "type为 3:获取普通卡（手动核销列表），0:老人卡（手动核销列表） 1:老人卡（商家扣票、补票记录）", required = true)
								 @RequestParam Integer type,
								 @ApiParam(name = "cardCode", value = "饭卡号")
											 String cardCode,
								 @ApiParam(name = "stime", value = "开始时间")
											 String stime,
								 @ApiParam(name = "etime", value = "结束时间")
											 String etime) {
		try {
			BusUser busUser = SessionUtils.getLoginUser(request);
			MessMain messMain =
					messMainService.getMessMainByBusId(busUser.getId());
			Integer mainId= messMain.getId();
			Map<String,Object> params = new HashMap<>();
			params.put("type", type);
			params.put("stime", stime);
			params.put("etime", etime);
			params.put("mainId", mainId);
			params.put("cardCode", cardCode);
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
