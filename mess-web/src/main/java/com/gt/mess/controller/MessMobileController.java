//package com.gt.mess.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.plugins.Page;
//import com.gt.api.bean.session.BusUser;
//import com.gt.api.bean.session.Member;
//import com.gt.api.bean.session.WxPublicUsers;
//import com.gt.api.util.SessionUtils;
//import com.gt.mess.dao.*;
//import com.gt.mess.entity.*;
//import com.gt.mess.exception.BaseException;
//import com.gt.mess.exception.BusinessException;
//import com.gt.mess.service.*;
//import com.gt.mess.util.CommonUtil;
//import com.gt.mess.util.DateTimeKit;
//import com.gt.mess.util.EncryptUtil;
//import com.gt.mess.util.QRcodeKit;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.jws.WebResult;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * 食堂手机端
// * @author ZengWenXiang
// * @QQ 307848200
// */
//@Controller
//@RequestMapping(value = "messMobile")
//public class MessMobileController {
//
//	private Logger logger = Logger.getLogger(MessMobileController.class);
//
////	@Autowired
////	private MemberService  memberService;
////
////	@Autowired
////	private WxPublicUsersMapper publicUsersMapper;
////
////	@Autowired
////	private WxPublicUsersMapper wxPublicUsersMapper;
//
////	@Autowired
////	private DictService  dictService;
////
////	@Autowired
////	private BusUserMapper busUserMapper;
////
////	@Autowired
////	private MemberMapper memberMapper;
//
//	@Autowired
//	private MessAuthorityMemberMapper messAuthorityMemberMapper;
//
//	@Autowired
//	private MessMainService messMainService;
//
//	@Autowired
//	private MessBasisSetService messBasisSetService;
//
//	@Autowired
//	private MessCardService messCardService;
//
//	@Autowired
//	private MessAddFoodService messAddFoodService;
//
//	@Autowired
//	private MessMenusService messMenusService;
//
//	@Autowired
//	private MessOrderManageService messOrderManageService;
//
//	@Autowired
//	private MessNoticeService messNoticeService;
//
//	@Autowired
//	private MessMealOrderService messMealOrderService;
//
//	@Autowired
//	private MessBuyTicketOrderService messBuyTicketOrderService;
//
//	@Autowired
//	private MessTopUpOrderService messTopUpOrderService;
//
//	@Autowired
//	private MessConsumerDetailService messConsumerDetailService;
//
//	@Autowired
//	private MessAuthorityMemberService messAuthorityMemberService;
//
//	@Autowired
//	private MessAddFoodMapper messAddFoodMapper;
//
//	@Autowired
//	private MessCardTicketMapper messCardTicketMapper;
//
//	@Value("#{config['public.getgrant.url.prefix']}")
//	private String signPath;
//
//	@Autowired
//	private MessConsumerDetailMapper messConsumerDetailMapper;
//
//	@Autowired
//	private MessDepartmentMapper messDepartmentMapper;
//
//	@Autowired
//	private MessCardGroupService messCardGroupService;
//
//
//	/**
//	 * 用户授权  OK
//	 * @param request
//	 * @param busId
//	 * @param mainId
//	 * @param response
//	 * @param session
//	 * @param sign_type
//	 * @param sign
//	 * @param ticketCode
//	 * @param fdId
//	 * @param addFoodCancleMMemberId
//	 * @param flag
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/{busId}/{mainId}/79B4DE7C/userGrant")
//	public void userGrant(HttpServletRequest request, @PathVariable Integer busId, @PathVariable Integer mainId,
//			HttpServletResponse response, HttpSession session, String sign_type, String sign, String ticketCode,Integer fdId, Integer addFoodCancleMMemberId, Integer flag) throws Exception  {
////		// 解决ios返回键问题
////		if(!CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){
////			response.sendRedirect("/messMobile/" + busId + "/79B4DE7C/Index.do");
////			return;
////		}
//		logger.info("进入微食堂活动");
//		String temp = signPath;
//		try {
//			BusUser busUser = busUserMapper.selectByPrimaryKey(busId);
//			Integer busIdwx = busId;
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(busId);
//			}
//			WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
//			String redirect_uri= temp.replace("$", "messMobile/" + users.getId()+"/"+mainId);
//			Map<String, Object> param = new HashMap<String,Object>();
//			if(!CommonUtil.isEmpty(sign_type) && "autho".equals(sign_type)){//扫码授权
//				ticketCode = URLEncoder.encode(URLEncoder.encode(ticketCode,"UTF-8"), "UTF-8");
//				param.put("ticketCode", ticketCode);
//				param.put("sign", sign);
//			}
//			if(fdId != null){
//				param.put("fdId", fdId);
//			}
//			if(flag != null && addFoodCancleMMemberId != null){
//				param.put("addFoodCancleMMemberId", addFoodCancleMMemberId);
//				param.put("flag", flag);
//			}
//			CommonUtil.userGrant(response, users, redirect_uri, true, param);
//		} catch (Exception e) {
//			logger.error("微食堂用户授权失败");
//			throw new BusinessException("用户授权失败");
//		}
//	}
//
//	/**
//	 * 用户授权回调方法 OK
//	 * @param request
//	 * @param code
//	 * @param userId
//	 * @param mainId
//	 * @param sign
//	 * @param ticketCode
//	 * @param fdId
//	 * @param flag
//	 * @param addFoodCancleMMemberId
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/{userId}/{mainId}/79B4DE7C/getUserOpenId")
//	public String getUserOpenId(HttpServletRequest request, String code,
//								@PathVariable Integer userId, @PathVariable Integer mainId,
//								String sign, String ticketCode,Integer fdId, Integer flag ,
//								Integer addFoodCancleMMemberId) throws Exception{
//		String redirect_rul = null;
//		try {
//			Map<String, Object> result = memberService.saveMember(code, userId, true);
//			Member member=(Member) result.get("member");
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/79B4DE7C/index.do";
//			if(!CommonUtil.isEmpty(ticketCode)){//扫码授权人员权限
//                ticketCode = URLEncoder.encode(ticketCode,"UTF-8");
//                redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/79B4DE7C/authority.do?sign="+sign+"&ticketCode="+ticketCode;
//            }
//			if(fdId != null){
//                redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/"+ fdId +"/79B4DE7C/addFood.do";
//                if(flag != null){
//                    redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/"+ fdId + "/" + addFoodCancleMMemberId +"/79B4DE7C/addFoodCancel.do";
//                }
//            }
//			if(member != null){
//                CommonUtil.setLoginMember(request, member);
//            }
//			logger.info("进入回调！");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return redirect_rul;
//	}
//
//	/**
//	 * 微食堂支付完成跳转
//	 * @param request
//	 * @param response
//	 * @param params
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 微食堂支付完成跳转",op_function="3")//保存2，修改3，删除4
//	@RequestMapping("/79B4DE7C/wxMessPayOrder")
//	public String wxMessPayOrder(HttpServletRequest request,HttpServletResponse response,
//			@RequestParam Map<String, Object> params){
//		MessTopUpOrder messTopUpOrder =
//				messTopUpOrderService.getMessTopUpOrderByOrderNo(params.get("orderNo").toString());
//		MessMain messMain = messMainService.getMessMainById(messTopUpOrder.getMainId());
//		if(messTopUpOrder.getStatus() == 1){
//			MessConsumerDetail messConsumerDetail =
//					messConsumerDetailService.getMessConsumerDetailById(Integer.valueOf(params.get("detailId").toString()));
//			MessCard messCard = messCardService.getMessCardById(messTopUpOrder.getCardId());
//			messCard.setMoney(messCard.getMoney() + messTopUpOrder.getMoney());
//
//			messTopUpOrder.setStatus(0);
//			messConsumerDetail.setStatus(0);
//			int data = 0;
//			try {
//				data = messTopUpOrderService.update(messTopUpOrder);
//				data = messConsumerDetailService.update(messConsumerDetail);
//				data = messCardService.update(messCard);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId(busIdwx);
//			Member member = SessionUtils.getLoginMember(request,busId);
//			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
//			if(!CommonUtil.isEmpty(member) && member.getId() != null){
//				if(!member.getPublicId().equals(wxPublicUsers.getId())){
//					SessionUtils.setLoginMember(request,null);//清空缓存
//					member = null;
//				}
//			}
//			if(CommonUtil.isEmpty(member) || member.getId() == null){
//				return "redirect:/messMobile/"+messMain.getId()+"/79B4DE7C/index.do";
//			}
//			return "redirect:/messMobile/"+messMain.getId()+"/79B4DE7C/index.do?data="+data;
//		}else{
//			return "redirect:/messMobile/"+messMain.getId()+"/79B4DE7C/index.do?data="+1;
//		}
//	}
//
//	/**
//	 * 检查是否授权
//	 * @param request
//	 * @param mainId
//	 * @return
//	 */
//	public Map<String,Object> authorize(HttpServletRequest request,HttpServletResponse response,Integer busId,Integer mainId,String url){
//		Map<String,Object> mapObj = new HashMap<String, Object>();
//		/**正式*/
//		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
//			mapObj.put("type", "0");
//			if(url == null){
//				mapObj.put("url", "redirect:/messMobile/" + busId + "/" + mainId +"/79B4DE7C/userGrant.do");
//			}else{
//				Map<String,Object> map=new HashMap<String, Object>();
//				String redisKey = CommonUtil.getCode();
//				JedisUtil.set(redisKey, url, 300);
//				map.put("redisKey", redisKey);
//				String returnUrl=userLogin(request, response, busId, map);
//				if(CommonUtil.isNotEmpty(returnUrl)){
//					mapObj.put("url", returnUrl);
//				}
//			}
//			return mapObj;
//		}else{
//			Member member = SessionUtils.getLoginMember(request,busId);
//			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
//			if(CommonUtil.isEmpty(member)){
//				mapObj.put("type", "0");
//				if(url == null){
//					mapObj.put("url", "redirect:/messMobile/" + busId + "/" + mainId + "/79B4DE7C/userGrant.do");
//				}else{
//					Map<String,Object> map=new HashMap<String, Object>();
//					String redisKey = CommonUtil.getCode();
//					JedisUtil.set(redisKey, url, 300);
//					map.put("redisKey", redisKey);
//					String returnUrl=userLogin(request, response, busId, map);
//					if(CommonUtil.isNotEmpty(returnUrl)){
//						mapObj.put("url", returnUrl);
//					}
//				}
//				return mapObj;
//			}
//		}
//		mapObj.put("type", 1);
//		Member member = SessionUtils.getLoginMember(request,busId);
//		mapObj.put("member", member);
//		return mapObj;
//	}
//
//	/**
//	 * 菜单首页
//	 * @param request
//	 * @param response
//	 * @param mainId
//	 * @return
//	 */
//	@RequestMapping("/{mainId}/79B4DE7C/index")
//	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId){
//		ModelAndView mv = new ModelAndView();
//		MessMain messMain =
//				messMainService.getMessMainById(mainId);
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
////		/**测试*/
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){
////			String id = "1469383";//request.getParameter("id");
////			Member member = memberMapper.selectByPrimaryKey(Integer.parseInt(id));
////			CommonUtil.setLoginMember(request, member);
////			request.getSession().setAttribute("memberId", id); // 测试用
////		}
//
//		String url = "/messMobile/"+mainId+"/79B4DE7C/index.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
////		/**正式*/
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
////			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() +"/79B4DE7C/userGrant.do");
////			return mv;
////		}else{
////			Member member = SessionUtils.getLoginMember(request,busId);
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				return mv;
////			}
////		}
//		Member member = SessionUtils.getLoginMember(request,busId);
//		logger.error("有人登陆饭堂，ID："+member.getId());
//		if(CommonUtil.isEmpty(member) || member.getId() == null || member.getId().toString() == ""){
//			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
//			return mv;
//		}
//		Map<String,Integer> mapId = new HashMap<String, Integer>();
//		mapId.put("memberId", member.getId());
//		mapId.put("mainId", mainId);
//		MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
//		mv.addObject("mainId", mainId);
//		mv.addObject("member", member);
//		mv.addObject("busId", messMain.getBusId());
//		MessBasisSet messBasisSet = null;
//		if(messCard == null){
//			mv.setViewName("merchants/trade/mess/mobile/ticketCard");
//			return mv;
//		}else{
//			messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
//			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
//			mv.addObject("messNotices", messNotices);
//			mv.addObject("messBasisSet", messBasisSet);
//			mv.setViewName("merchants/trade/mess/mobile/home");
//		}
//		List<MessCardTicket> messCardTickets =
//				messCardService.getMessCardTicketListByCardId(messCard.getId());
//		int nums = 0;
//		int nums2 = 0;
//		if(messBasisSet.getPastDay() != 0){
//			//判断是否有饭票即将过期（1天）
//			Map<String,Object> mapObj2 = null;
//			try {
//				mapObj2 = messCardService.pastDue2(messCard, messBasisSet, messCardTickets, mainId,messBasisSet.getPastDay());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(mapObj2 != null){
//				nums2 = Integer.valueOf(mapObj2.get("nums").toString());
//			}
//			//判断是否有饭票过期
//			if(nums2 != 0){
//				Map<String,Object> mapObj = null;
//				try {
//					mapObj = messCardService.pastDue(messCard, messBasisSet, messCardTickets, mainId);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if(mapObj != null){
//					nums = Integer.valueOf(mapObj.get("nums").toString());
//				}
//				if(nums != 0){
//					nums2 = 0;
//				}
//			}
//		}
//		try {
//			messCardService.balance(messCard, messBasisSet, messCardTickets, mainId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Date nowDate = new Date();
//		String dateStr = DateTimeKit.format(nowDate,"HH:mm");
//		Integer mealType = -1;
//		if(DateTimeKit.isInTime(
//				DateTimeKit.format(messBasisSet.getBreakfastStart(),"HH:mm")+"-"+
//				DateTimeKit.format(messBasisSet.getBreakfastEnd(),"HH:mm"),
//				dateStr)){
//			mealType = 0;
//		}else if(DateTimeKit.isInTime(
//				DateTimeKit.format(messBasisSet.getLunchStart(),"HH:mm")+"-"+
//				DateTimeKit.format(messBasisSet.getLunchEnd(),"HH:mm"),
//				dateStr)){
//			mealType = 1;
//		}else if(DateTimeKit.isInTime(
//				DateTimeKit.format(messBasisSet.getDinnerStart(),"HH:mm")+"-"+
//				DateTimeKit.format(messBasisSet.getDinnerEnd(),"HH:mm"),
//				dateStr)){
//			mealType = 2;
//		}else if(DateTimeKit.isInTime(
//				DateTimeKit.format(messBasisSet.getNightStart(),"HH:mm")+"-"+
//				DateTimeKit.format(messBasisSet.getNightEnd(),"HH:mm"),
//				dateStr)){
//			mealType = 3;
//		}
//		if(mealType != -1){
//			Map<String,Object> map = new HashMap<String, Object>();
//			map.put("cardId", messCard.getId());
//			map.put("mealType", mealType);
//			map.put("mainId", messCard.getMainId());
//			MessMealOrder messMealOrder = messMealOrderService.getMessMealOrderByMap(map);
//			if(messMealOrder != null){
//				mv.addObject("mealCode", messMealOrder.getMealCode());
//			}else {
//				mv.addObject("mealCode", -1);
//				mv.addObject("msg", "现时间段无订餐！");
//			}
//		}else{
//			mv.addObject("mealCode", -1);
//			mv.addObject("msg", "现时间段无订餐！");
//		}
//		mv.addObject("messCard", messCard);
//		mv.addObject("nums", nums);
//		mv.addObject("nums2", nums2);
//		mv.addObject("cardId", messCard.getId());
//		return mv;
//	}
//
//	/**
//	 * 取餐码
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping(value = "{mainId}/{cardId}/{mealCode}/79B4DE7C/getMessUrltoQRcode")
//	public void getMessUrltoQRcode(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer cardId,@PathVariable String mealCode) {
//		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			String filePath =wxmpApiProperties.getAdminUrl();
//			QRcodeKit.buildQRcode(filePath+"messReception/"+messMain.getBusId()+"/"+cardId+"/"+ mealCode +"/79B4DE7C/wirteOff.do", 300, 300, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 取餐码验证
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 取餐码验证",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "{mainId}/{cardId}/{mealCode}/79B4DE7C/verify")
//	public void verify(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer cardId,@PathVariable String mealCode) {
//		int data = 0;
//		Map<String,Object> mapObj = new HashMap<String, Object>();
//		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			Member member = SessionUtils.getLoginMember(request,busId);
//			Integer busIdwx = messMain.getBusId();
//			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
////
////				if(busUser.getPid() != 0){
////					busIdwx = dictService.pidUserId(messMain.getBusId());
////				}
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				try {
////					response.sendRedirect("/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				return;
////			}
//
//			String url = "/messMobile/"+mainId+"/"+cardId+"/"+mealCode+"/79B4DE7C/verify.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//
//			mapObj = messCardService.verify(cardId,mealCode);
//			data = Integer.valueOf(mapObj.get("data").toString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			data = -1;
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			map.put("nums",mapObj.get("nums"));
//			if(data > 0){
//				map.put("status","success");
//			}else if(data == -1){
//				map.put("status","error1");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 商家订餐统计
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{mainId}/79B4DE7C/statistics")
//	public ModelAndView statistics(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessMain messMain = messMainService.getMessMainById(mainId);
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//
//		String url = "/messMobile/"+mainId+"/79B4DE7C/statistics.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
////			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////			return mv;
////		}else{
////			Member member = SessionUtils.getLoginMember(request,busId);
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				return mv;
////			}
////		}
//		List<MessMealOrder> messMealOrderList =
//				messMealOrderService.getMessMealOrderListforToday2(mainId);
//		List<MessDepartment> messDepartments =
//				messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
//		List<Map<String,Object>> bListMaps = new ArrayList<Map<String,Object>>();
//		List<Map<String,Object>> lListMaps = new ArrayList<Map<String,Object>>();
//		List<Map<String,Object>> dListMaps = new ArrayList<Map<String,Object>>();
//		List<Map<String,Object>> nListMaps = new ArrayList<Map<String,Object>>();
//		for (int i = 0; i < 4; i++) {
//			for(MessDepartment messDepartment:messDepartments){
//				Map<String,Object> map = new HashMap<String, Object>();
//				Map<String,Integer> mapId = new HashMap<String, Integer>();
//				mapId.put("depId", messDepartment.getId());
//				mapId.put("mainId", mainId);
//				mapId.put("mealType", i);
//				List<MessMealOrder> mealOrderNums = messMealOrderService.getNumsByDepIdAndMealType(mapId);
//				map.put("name", messDepartment.getName());
//				map.put("nums", mealOrderNums.size());
//				Integer mealNums = 0;
//				for(MessMealOrder messMealOrder : mealOrderNums){
//					mealNums +=  messMealOrder.getMealNum();
//				}
//				map.put("mealNums", mealNums);
//				if(i == 0){
//					bListMaps.add(map);
//				}else if(i == 1){
//					lListMaps.add(map);
//				}else if(i == 2){
//					dListMaps.add(map);
//				}else if(i == 3){
//					nListMaps.add(map);
//				}
//			}
//			Map<String,Object> map = new HashMap<String, Object>();
//			Map<String,Integer> mapId = new HashMap<String, Integer>();
//			mapId.put("depId", -1);
//			mapId.put("mainId", mainId);
//			mapId.put("mealType", i);
//			List<MessMealOrder> mealOrderNums = messMealOrderService.getNumsByDepIdAndMealType(mapId);
//			if(mealOrderNums.size() != 0){
//				map.put("name", "暂无部门");
//				map.put("nums", mealOrderNums.size());
//				Integer mealNums = 0;
//				for(MessMealOrder messMealOrder : mealOrderNums){
//					mealNums +=  messMealOrder.getMealNum();
//				}
//				map.put("mealNums", mealNums);
//				if(i == 0){
//					bListMaps.add(map);
//				}else if(i == 1){
//					lListMaps.add(map);
//				}else if(i == 2){
//					dListMaps.add(map);
//				}else if(i == 3){
//					nListMaps.add(map);
//				}
//			}
//		}
//		//人数
//		Integer breakfastNum = 0;
//		Integer lunchNum = 0;
//		Integer dinnerNum = 0;
//		Integer nightNum = 0;
//		//份数
//		Integer breakfastMealNum = 0;
//		Integer lunchMealNum = 0;
//		Integer dinnerMealNum = 0;
//		Integer nightMealNum = 0;
//
//		for(MessMealOrder messMealOrder : messMealOrderList){
//			if(messMealOrder.getMealType() == 0){
//				breakfastMealNum += messMealOrder.getMealNum();
//				breakfastNum++;
//			}else if(messMealOrder.getMealType() == 1){
//				lunchMealNum += messMealOrder.getMealNum();
//				lunchNum++;
//			}else if(messMealOrder.getMealType() == 2){
//				dinnerMealNum += messMealOrder.getMealNum();
//				dinnerNum++;
//			}else if(messMealOrder.getMealType() == 3){
//				nightMealNum += messMealOrder.getMealNum();
//				nightNum++;
//			}
//		}
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
//		mv.addObject("messNotices", messNotices);
//		mv.addObject("breakfastNum", breakfastNum);
//		mv.addObject("lunchNum", lunchNum);
//		mv.addObject("dinnerNum", dinnerNum);
//		mv.addObject("nightNum", nightNum);
//
//		mv.addObject("breakfastMealNum", breakfastMealNum);
//		mv.addObject("lunchMealNum", lunchMealNum);
//		mv.addObject("dinnerMealNum", dinnerMealNum);
//		mv.addObject("nightMealNum", nightMealNum);
//
//		mv.addObject("bListMaps", bListMaps);
//		mv.addObject("lListMaps", lListMaps);
//		mv.addObject("dListMaps", dListMaps);
//		mv.addObject("nListMaps", nListMaps);
//
//		mv.addObject("nums", breakfastNum+lunchNum+dinnerNum+nightNum);
//		mv.addObject("mainId", mainId);
//		mv.setViewName("merchants/trade/mess/mobile/meal");
//		return mv;
//	}
//
//
//	/**
//	 * 绑定饭卡
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 绑定饭卡",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "79B4DE7C/bindingCard")
//	public void bindingCard(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		int data = 0;
//		try {
//			Integer busIdwx = Integer.valueOf(params.get("busId").toString());
//			MessMain messMain = messMainService.getMessMainByBusId(busIdwx);
//			String url = "/messMobile/79B4DE7C/bindingCard.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//			params.put("memberId", member.getId());
//			data = messCardService.bindingCard(params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 加菜完成后跳转
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("/{mainId}/{fdId}/79B4DE7C/addFood")
//	public ModelAndView addFoodL(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer fdId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessMain messMain = messMainService.getMessMainById(mainId);
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		Member member = SessionUtils.getLoginMember(request,busId);
//		String url = "/messMobile/"+mainId+"/"+fdId+"/79B4DE7C/addFood.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
////			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////			return mv;
////		}else{
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				return mv;
////			}
////		}
//		Map<String,Integer> mapId = new HashMap<String, Integer>();
//		mapId.put("memberId", member.getId());
//		mapId.put("mainId", mainId);
//		MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
//		int data = 0;
//		if(messCard == null){
//			mv.setViewName("redirect:/messMobile/" + mainId + "/79B4DE7C/index.do");
//		}else{
//			MessAddFood messAddFood = messAddFoodService.getMessAddFoodById(fdId);
//			mv.addObject("money", messAddFood.getPrice());
//			mv.addObject("mainId", mainId);
//			mv.addObject("cardId", messCard.getId());
//			mv.setViewName("merchants/trade/mess/mobile/addFood");
//			if("1".equals(JedisUtil.get("mess:"+member.getId().toString()))){
//				mv.addObject("data", 1);
//				return mv;
//			}else{
//				JedisUtil.set("mess:"+member.getId(), "1",60);
//			}
//			try {
//				data = messAddFoodService.addFood(messCard, fdId);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				data = 0;
//			}
//			mv.addObject("data", data);
//			JedisUtil.del("mess:"+member.getId());
//		}
//		return mv;
//	}
//
////	购买饭票模块
//
//	/**
//	 * 立即购买（购买饭票）
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/buyNow")
//	public ModelAndView buyNow(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//
//		String url = "/messMobile/"+cardId+"/79B4DE7C/buyNow.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
////			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////			return mv;
////		}else{
////			Member member = SessionUtils.getLoginMember(request,busId);
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				return mv;
////			}
////		}
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messCard.getMainId());
//		if(messCard.getGroupId() != null){
//			MessCardGroup messCardGroup =
//					messCardGroupService.getCardGroupById(messCard.getGroupId());
//			JSONObject jsonObject = (JSONObject) JSON.parse(messCardGroup.getAuthority());
//			if(jsonObject.getInteger("bitUse").equals(0)){
//				if(messBasisSet.getBitUniversal() == 0){
//					messBasisSet.setUniversalPrice(jsonObject.getDouble("universalPrice"));
//				}else{
//					messBasisSet.setBreakfastPrice(jsonObject.getDouble("breakfastPrice"));
//					messBasisSet.setDinnerPrice(jsonObject.getDouble("dinnerPrice"));
//					messBasisSet.setLunchPrice(jsonObject.getDouble("lunchPrice"));
//					messBasisSet.setNightPrice(jsonObject.getDouble("nightPrice"));
//				}
//			}
//		}
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.addObject("messCard", messCard);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", messCard.getId());
//		mv.setViewName("merchants/trade/mess/mobile/buyNow");
//		return mv;
//	}
//
//	/**
//	 * 购买饭票
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 购买饭票",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "79B4DE7C/buyTicket")
//	public void buyTicket(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		int data = 0;
//		try {
//			MessMain messMain = messMainService.getMessMainById(Integer.valueOf(params.get("mainId").toString()));
//			Member member = SessionUtils.getLoginMember(request,busId);
//			Integer busIdwx = messMain.getBusId();
//			String url = "/messMobile/79B4DE7C/buyTicket.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
////				if(busUser.getPid() != 0){
////					busIdwx = dictService.pidUserId(messMain.getBusId());
////				}
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				try {
////					response.sendRedirect("/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				return;
////			}
//			params.put("memberId", member.getId());
//			data = messBuyTicketOrderService.buyTicket(params);
//		} catch (BaseException be){
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else if(data == -1){
//				map.put("status","error1");
//			}else if(data == -2){
//				map.put("status","error2");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
////	我的模块
//
//	/**
//	 * 我的订餐明细
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/myMealOrderDetail")
//	public ModelAndView myMealOrderDetail(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		Integer busIdwx = messMain.getBusId();
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(messMain.getBusId());
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/"+cardId+"/79B4DE7C/myMealOrderDetail.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
////			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////			return mv;
////		}else{
////			Member member = SessionUtils.getLoginMember(request,busId);
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				return mv;
////			}
////		}
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(), 10);
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messMain.getId());
//		System.out.println(messBasisSet.getBitUniversal());
//		mv.addObject("messCard", messCard);
//		mv.addObject("messNotices", messNotices);
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", messCard.getId());
//		mv.setViewName("merchants/trade/mess/mobile/myMealOrderDetail");
//		return mv;
//	}
//
//	/**
//	 * 明细列表
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{mainId}/{cardId}/79B4DE7C/detailList")
//	public ModelAndView detailList(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessMealOrder> page){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessMain messMain = messMainService.getMessMainById(mainId);
//		Integer busIdwx = messMain.getBusId();
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(messMain.getBusId());
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//		String url = "/messMobile/"+mainId+"/"+cardId+"/79B4DE7C/detailList.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
////		if(CommonUtil.isEmpty(SessionUtils.getLoginMember(request,busId))){//用户为空表示没有登录，先跳转到授权页面
////			mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////			return mv;
////		}else{
////			Member member = SessionUtils.getLoginMember(request,busId);
////			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = wxPublicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mv.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do");
////				return mv;
////			}
////		}
//		Map<String,Integer> mapId = new HashMap<String, Integer>();
//		mapId.put("cardId", cardId);
//		mapId.put("mainId", mainId);
//		List<MessMealOrder> messMealOrderList =
//				messMealOrderService.getPastMessMealOrderListByCardIdAndMainId(mapId);
//		for(MessMealOrder mealOrder : messMealOrderList){
//			mealOrder.setStatus(5);
//			try {
//				messMealOrderService.update(mealOrder);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		Page<MessMealOrder> messMealOrders =
//				messMealOrderService.getMessMealOrderPageByCardIdAndMainId(page,mapId,10);
//		mv.addObject("messMealOrders", messMealOrders);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", cardId);
//		String datetime = DateTimeKit.format(new Date(), DateTimeKit.DEFAULT_DATE_FORMAT);
//		datetime += " 00:00:00";
//		Date nowDate = DateTimeKit.parse(datetime, DateTimeKit.DEFAULT_DATETIME_FORMAT);
//		mv.addObject("now", datetime);
//		mv.addObject("nowDate", nowDate);
//		mv.setViewName("merchants/trade/mess/mobile/detailList");
//		return mv;
//	}
//
//	/**
//	 * 明细列表（加载）
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "{mainId}/{cardId}/79B4DE7C/loadDetailList")
//	public void loadDetailList(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessMealOrder> page) {
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			Map<String,Integer> mapId = new HashMap<String, Integer>();
//			mapId.put("cardId", cardId);
//			mapId.put("mainId", mainId);
//			Page<MessMealOrder> messMealOrders =
//					messMealOrderService.getMessMealOrderPageByCardIdAndMainId(page,mapId,10);
//			if(messMealOrders.getPageNum() < page.getPageNum()){
//				out.write("-1");
//				return;
//			}
//			if(messMealOrders != null){
//				out.write(JSON.toJSONString(messMealOrders).toString());
//			}else{
//				out.write(JSON.toJSONString("error").toString());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 取消订单
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 取消订单",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "{mainId}/{orderId}/79B4DE7C/cancelOrder")
//	public void cancelOrder(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer orderId) {
//		int data = 0;
//		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			Member member = SessionUtils.getLoginMember(request,busId);
//			Integer busIdwx = messMain.getBusId();
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			String url = "/messMobile/"+mainId+"/"+orderId+"/79B4DE7C/cancelOrder.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//			data = messMealOrderService.cancelOrder(orderId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 订单列表(消费明细)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{mainId}/{cardId}/79B4DE7C/orderList")
//	public ModelAndView orderList(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessConsumerDetail> page){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessMain messMain = messMainService.getMessMainById(mainId);
//		Integer busIdwx = messMain.getBusId();
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(messMain.getBusId());
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/"+mainId+"/"+cardId+"/79B4DE7C/orderList.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		Map<String,Integer> mapId = new HashMap<String, Integer>();
//		mapId.put("cardId", cardId);
//		mapId.put("mainId", mainId);
//		Page<MessConsumerDetail> messConsumerDetails =
//				messConsumerDetailService.getMessConsumerDetailPageByCardIdAndMainId(page, mapId, 10);
//		mv.addObject("messConsumerDetails", messConsumerDetails);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", cardId);
//		mv.setViewName("merchants/trade/mess/mobile/orderList");
//		return mv;
//	}
//
//	/**
//	 * 订单列表加载(消费明细加载)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{mainId}/{cardId}/79B4DE7C/loadOrderList")
//	public void loadOrderList(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessConsumerDetail> page){
//		//@RequestParam Map <String,Object> params
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			Map<String,Integer> mapId = new HashMap<String, Integer>();
//			mapId.put("cardId", cardId);
//			mapId.put("mainId", mainId);
//			Page<MessConsumerDetail> messConsumerDetails =
//					messConsumerDetailService.getMessConsumerDetailPageByCardIdAndMainId(page, mapId, 10);
//			if(messConsumerDetails.getPageNum() < page.getPageNum()){
//				out.write("-1");
//				return;
//			}
//			if(messConsumerDetails != null && messConsumerDetails.size() > 0){
//				out.write(JSON.toJSONString(messConsumerDetails).toString());
//			}else{
//				out.write(JSON.toJSONString("error").toString());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
////	每周菜单模块
//
//	/**
//	 * 订餐主页(每周菜单)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/{week}/{type}/79B4DE7C/mealOrder")
//	public ModelAndView mealOrder(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId,@PathVariable Integer week,
//			@PathVariable Integer type){
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		Integer mainId = messCard.getMainId();
//		MessMain messMain = messMainService.getMessMainById(mainId);
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = wxPublicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/"+cardId+"/"+week+"/"+type+"/79B4DE7C/mealOrder.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		Member member = SessionUtils.getLoginMember(request,busId);
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("week", week);
//		map.put("type", type);
//		map.put("mainId", mainId);
//		List<MessMenus> messMenus =
//				messMenusService.getMessMenusListByTypeAndWeekNumforMainId(map);
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
//		mv.addObject("messNotices", messNotices);
//		mv.addObject("week", week);
//		mv.addObject("member", member);
//		mv.addObject("mainId", mainId);
//		mv.addObject("messCard", messCard);
//		mv.addObject("cardId", messCard.getId());
//		mv.addObject("messMenus", messMenus);
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.setViewName("merchants/trade/mess/mobile/mealOrder");
//		return mv;
//	}
//
////	订餐模块
//
//	/**
//	 * 订餐
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/calendar")
//	public ModelAndView calendar(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = wxPublicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/"+cardId+"/79B4DE7C/calendar.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		Map<String,Integer> mapId = new HashMap<String, Integer>();
//		mapId.put("mainId", messCard.getMainId());
//		mapId.put("cardId", messCard.getId());
//		List<MessMealOrder> messMealOrderList =
//				messMealOrderService.getPastMessMealOrderListByCardIdAndMainId(mapId);
//		for(MessMealOrder mealOrder : messMealOrderList){
//			mealOrder.setStatus(5);
//			try {
//				messMealOrderService.update(mealOrder);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		//不可订餐
//		List<MessOrderManage> messOrderManages = messOrderManageService.getMessOrderManageListByMainId(messCard.getMainId());
//		//已预订
//		List<MessMealOrder> bookMealOrders =
//				messMealOrderService.getBookedMessMealOrder(mapId);
//		//未选餐
//		List<MessMealOrder> notChooseMealOrders =
//				messMealOrderService.getNotChooseMessMealOrder(mapId);
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messMain.getId());
//		StringBuffer manageBuffer = new StringBuffer();
//		for (int i = 0; i < messBasisSet.getBookDay(); i++) {
//			Date date = DateTimeKit.addDate(new Date(), i);;
//			manageBuffer.append(time("yyyy-MM-dd",date.toString())+",");
//		}
//		//不可订餐
//		if(messOrderManages.size() == 1){
//			manageBuffer.append(messOrderManages.get(0).getDay().toString()+",");
//		}else{
//			for(MessOrderManage  messOrderManage: messOrderManages){
//				if(messOrderManage.getDay() != "")
//					manageBuffer.append(messOrderManage.getDay().toString()+",");
//			}
//		}
//		StringBuffer mealOrderBuffer = new StringBuffer();
//		StringBuffer notChooseMealOrderBuffer = new StringBuffer();
//		//已预订
//		if(bookMealOrders.size() == 1){
//			mealOrderBuffer.append(DateTimeKit.getDateTime(bookMealOrders.get(0).getTime(), "yyyy-M-d")+",");
//		}else{
//			for(MessMealOrder  messMealOrder: bookMealOrders){
//				if(!mealOrderBuffer.toString().contains(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")))
//					mealOrderBuffer.append(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")+",");
//			}
//		}
//		//未选餐
//		if(notChooseMealOrders.size() == 1){
//			notChooseMealOrderBuffer.append(DateTimeKit.getDateTime(notChooseMealOrders.get(0).getTime(), "yyyy-M-d")+",");
//		}else{
//			for(MessMealOrder  messMealOrder: notChooseMealOrders){
//				if(!notChooseMealOrderBuffer.toString().contains(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")))
//					notChooseMealOrderBuffer.append(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")+",");
//			}
//		}
//
//
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("messCard", messCard);
//		mv.addObject("cardId", cardId);
//		//不可订餐
//		if(messOrderManages.size() > 0){
//			mv.addObject("manageBuffer", manageBuffer.substring(0, manageBuffer.length() -1));
//		}else{
//			if(manageBuffer.length() > 0){
//				mv.addObject("manageBuffer", manageBuffer.substring(0, manageBuffer.length() -1));
//			}else{
//				mv.addObject("manageBuffer", "");
//			}
//		}
//
//		//已预订
//		if(bookMealOrders.size() > 0){
//			mv.addObject("mealOrderBuffer", mealOrderBuffer.substring(0, mealOrderBuffer.length() -1));
//		}else{
//			mv.addObject("mealOrderBuffer", "");
//		}
//		//未选餐
//		if(notChooseMealOrders.size() > 0){
//			mv.addObject("notChooseMealOrderBuffer", notChooseMealOrderBuffer.substring(0, notChooseMealOrderBuffer.length() -1));
//		}else{
//			mv.addObject("notChooseMealOrderBuffer", "");
//		}
//		List<MessMealOrder> notMessMealOrders =
//				messMealOrderService.getNotChooseMessMealOrder(mapId);
//		if(notMessMealOrders.size() > 0){
//			mv.addObject("type", 0);
//		}else{
//			mv.addObject("type", 1);
//		}
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
//		mv.addObject("messNotices", messNotices);
//		mv.setViewName("merchants/trade/mess/mobile/calendar");
//		return mv;
//	}
//
//	/**
//	 * 追加订单(或取消订单列表)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("/79B4DE7C/addOrder")
//	public ModelAndView addOrder(HttpServletRequest request,HttpServletResponse response,
//			@RequestParam Map <String,Object> params){
//		ModelAndView mv = new ModelAndView();
//		Integer cardId = Integer.valueOf(params.get("cardId").toString());
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/79B4DE7C/addOrder.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		params.put("mainId", messCard.getMainId());
//		params.put("cardId", messCard.getId());
//		List<MessMealOrder> messMealOrders =
//				messMealOrderService.getBookMessMealOrderByToDay(params);
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messMain.getId());
//		Member member = SessionUtils.getLoginMember(request,busId);
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
//		mv.addObject("messNotices", messNotices);
//		mv.addObject("member", member);
//		if(params.get("time").equals(DateTimeKit.getDateTime(new Date(), "yyyy-M-d"))){
//			mv.addObject("bitqx", 0);
//		}else{
//			mv.addObject("bitqx", 1);
//		}
//		mv.addObject("param", params);
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", cardId);
//		mv.addObject("messCard", messCard);
//		mv.addObject("messMealOrders", messMealOrders);
//		mv.setViewName("merchants/trade/mess/mobile/addOrder");
//		return mv;
//	}
//
//	/**
//	 * 保存或更新追加订单
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 保存或更新追加订单",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "79B4DE7C/saveOrUpdateAddOrder")
//	public void saveOrUpdateAddOrder(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		int data = 0;
//		try {
//			MessMain messMain = messMainService.getMessMainById(Integer.valueOf(params.get("mainId").toString()));
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//			String url = "/messMobile/79B4DE7C/saveOrUpdateAddOrder.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//
//			params.put("memberId", member.getId());
//			data = messMealOrderService.saveOrUpdateAddOrder(params);
//		} catch (BaseException be){
//			if("超过预定时间".equals(be.getMessage())){
//				data = -2;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else if(data == -1){
//				map.put("status","error1");
//			}else if(data == -2){
//				map.put("status","error2");
//			}else {
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 删除未选餐
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 删除未选餐",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "79B4DE7C/delNotCMealOrder")
//	public void delNotCMealOrder(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		int data = 0;
//		try {
//			MessMain messMain = messMainService.getMessMainById(Integer.valueOf(params.get("mainId").toString()));
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//			String url = "/messMobile/79B4DE7C/delNotCMealOrder.do?mainId="+messMain.getId();
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//			params.put("memberId", member.getId());
//			data = messMealOrderService.delNotCMealOrder(params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else if(data == -1){
//				map.put("status","error1");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 保存订餐
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 保存订餐",op_function="2")//保存2，修改3，删除4
//	@RequestMapping(value = "79B4DE7C/saveMealOrder")
//	public void saveMealOrder(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		int data = 0;
//		try {
//			MessMain messMain = messMainService.getMessMainById(Integer.valueOf(params.get("mainId").toString()));
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//			String url = "/messMobile/79B4DE7C/saveMealOrder.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//			params.put("memberId", member.getId());
//			data = messMealOrderService.saveMealOrder(params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else if(data == -1){
//				map.put("status","error1");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 选择早晚餐
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/chooseMeal")
//	public ModelAndView chooseMeal(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/"+cardId+"/79B4DE7C/chooseMeal.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		Map<String,Integer> mapId = new HashMap<String, Integer>();
//		mapId.put("mainId", messCard.getMainId());
//		mapId.put("cardId", messCard.getId());
//		List<MessMealOrder> messMealOrders =
//				messMealOrderService.getNotChooseMessMealOrder(mapId);
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messMain.getId());
//		Member member = SessionUtils.getLoginMember(request,busId);
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
//		mv.addObject("messNotices", messNotices);
//		mv.addObject("size", messMealOrders.size());
//		mv.addObject("member", member);
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", cardId);
//		mv.addObject("messCard", messCard);
//		mv.addObject("messMealOrders", messMealOrders);
//		mv.setViewName("merchants/trade/mess/mobile/chooseMeal");
//		return mv;
//	}
//
//	/**
//	 * 保存选餐（选择早晚餐）
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 保存订餐（选择早晚餐）",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "79B4DE7C/saveChooseMealOrder")
//	public void saveChooseMealOrder(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		int data = 0;
//		try {
//			MessMain messMain = messMainService.getMessMainById(Integer.valueOf(params.get("mainId").toString()));
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//
//			String url = "/messMobile/79B4DE7C/saveChooseMealOrder.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//
//			params.put("memberId", member.getId());
//			data = messMealOrderService.saveChooseMealOrder(params);
//		} catch (BaseException be){
//			if("超过预定时间".equals(be.getMessage())){
//				data = -2;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else if(data == -1){
//				map.put("status","error1");
//			}else if(data == -2){
//				map.put("status","error2");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
////	余额充值模块
//
//	/**
//	 * 购买饭票显示(余额充值)
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/buyTicketShow")
//	public ModelAndView buyTicketShow(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard =
//				messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//		String url = "/messMobile/"+cardId+"/79B4DE7C/buyTicketShow.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//		mv.addObject("messCard", messCard);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", cardId);
//		mv.setViewName("merchants/trade/mess/mobile/buyTicketShow");
//		return mv;
//	}
//
//	/**
//	 * 饭票余额显示
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/ticketMoneyShow")
//	public ModelAndView ticketMoneyShow(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//
//		String url = "/messMobile/"+cardId+"/79B4DE7C/buyTicketShow.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		mv.addObject("messCard", messCard);
//		mv.addObject("mainId", messMain.getId());
//		mv.addObject("cardId", cardId);
//		mv.setViewName("merchants/trade/mess/mobile/ticketMoneyShow");
//		return mv;
//	}
//
//	/**
//	 * 饭卡充值支付
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 饭卡充值支付",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "/79B4DE7C/topUpPay")
//	public void topUpPay(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			MessMain messMain = messMainService.getMessMainById(Integer.valueOf(params.get("mainId").toString()));
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//
//			String url = "/messMobile/79B4DE7C/topUpPay.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				try {
//					response.sendRedirect(obj.get("url").toString());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//
//			params.put("memberId", member.getId());
//			map = messTopUpOrderService.topUpPay(params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			if(map.get("data").toString().equals("1")){
//				map.put("status","success");
//				map.put("url", "wxPay/79B4DE7C/wxMessPayOrder.do?orderNo="+map.get("orderNo").toString()+"&detailId="+map.get("detailId").toString());
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 饭票授权手机端
//	 * @param request
//	 * @param response
//	 * @param mainId
//	 * @param params
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("{mainId}/79B4DE7C/authority")
//	public ModelAndView authority(HttpServletRequest request, HttpServletResponse response,@PathVariable Integer mainId,@RequestParam Map<String, Object> params)
//			throws Exception {
//		Map<String, Object> msg = null;
//		ModelAndView mav = new ModelAndView("merchants/trade/mess/mobile/autho_msg");
//		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			if(CommonUtil.isEmpty(params.get("sign"))){
//				msg = new HashMap<String,Object>();
//				msg.put("message", "该授权码有误！");
//				mav.addObject("msg", msg);
//				return mav;
//			}else{
//				String sign = params.get("sign").toString();
//				if(!sign.equals(messMain.getAuthoritySign())){
//					msg = new HashMap<String,Object>();
//					msg.put("message", "该授权码已失效！");
//					mav.addObject("msg", msg);
//					return mav;
//				}
//			}
//			if(CommonUtil.isEmpty(params.get("ticketCode"))){
//				msg = new HashMap<String,Object>();
//				msg.put("message", "该授权码有误！");
//				mav.addObject("msg", msg);
//				return mav;
//			}else{
//				String decode = URLDecoder.decode(params.get("ticketCode").toString(), "UTF-8");
//				String decrypt_ticket_id = EncryptUtil.decrypt(mainId + "CFCCBD66B12B62E5256FAA90A931A01F", decode);
//				if(mainId.intValue()!=Integer.parseInt(decrypt_ticket_id)){
//					msg = new HashMap<String,Object>();
//					msg.put("message", "该授权码有误！");
//					mav.addObject("msg", msg);
//					return mav;
//				}
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
////			if(!CommonUtil.isEmpty(member) && member.getId() != null){
////				WxPublicUsers users = publicUsersMapper.selectByUserId(busIdwx);
////				if(!member.getPublicId().equals(users.getId())){
////					SessionUtils.setLoginMember(request,null);//清空缓存
////					member = null;
////				}
////			}
////			if(CommonUtil.isEmpty(member) || member.getId() == null){
////				mav.setViewName("redirect:/messMobile/" + busIdwx +"/"+ messMain.getId() + "/79B4DE7C/userGrant.do?sign_type=autho&ticketCode="+params.get("ticketCode").toString()+"&sign="+params.get("sign").toString());
////				return mav;
////			}
//
////			redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/79B4DE7C/authority.do?sign="+sign+"&ticketCode="+ticketCode;
//			String ticketCode = params.get("ticketCode").toString();
//			ticketCode = URLEncoder.encode(ticketCode,"UTF-8");
//			String url = "/messMobile/"+mainId+"/79B4DE7C/authority.do?sign="+params.get("sign")+"&ticketCode="+ticketCode;
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//			if("0".equals(obj.get("type").toString())){
//				mav.setViewName(obj.get("url").toString());
//				return mav;
//			}
//			Map<String, Object> autho = new HashMap<String, Object>();
//			autho.put("mainId", messMain.getId());
//			autho.put("memberId", member.getId());
//		    autho.put("delStatus", 0);
//			List<MessAuthorityMember> tams = messAuthorityMemberMapper.getMessAuthorityMember(autho);
//			if(tams!=null&&tams.size()>0){
//				msg = new HashMap<String,Object>();
//				msg.put("message", "不能重复授权！");
//				mav.addObject("msg", msg);
//				return mav;
//			}
//			params.put("memberId", member.getId());
//			params.put("messMain", messMain);
//			msg = messAuthorityMemberService.saveAuthority(params);
//			mav.addObject("msg", msg);
//		} catch (BaseException e) {
//			msg = new HashMap<>();
//			msg.put("result", e.getResult());
//			msg.put("msg", e.getMessage());
//			e.printStackTrace();
//		}
//		return mav;
//	}
//
////	加菜模块
//
//	/**
//	 * 加餐
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("{cardId}/79B4DE7C/addFood")
//	public ModelAndView addFood(HttpServletRequest request,HttpServletResponse response,
//			@PathVariable Integer cardId,Page<MessAddFood> page){
//		ModelAndView mv = new ModelAndView();
//		MessCard messCard = messCardService.getMessCardById(cardId);
//		Integer mainId = messCard.getMainId();
//		MessMain messMain = messMainService.getMessMainById(mainId);
//		BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//		Integer busIdwx = messMain.getBusId();
//		if(busUser.getPid() != 0){
//			busIdwx = dictService.pidUserId(messMain.getBusId());
//		}
//		WxPublicUsers publicUsers = publicUsersMapper.selectByUserId(busIdwx);
//		try {
//			CommonUtil.getWxParams(publicUsers, request);
//		} catch (Exception e) {
//			logger.debug(e);
//		}
//		String url = "/messMobile/"+mainId+"/79B4DE7C/authority.do";
//		Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),url);
//		if("0".equals(obj.get("type").toString())){
//			mv.setViewName(obj.get("url").toString());
//			return mv;
//		}
//
//		Member member = SessionUtils.getLoginMember(request,busId);
//		Page<MessAddFood> messAddFoods =
//				messAddFoodService.getMessAddFoodPageByMainId(page, mainId, 100);
//		MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
//		List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
//		mv.addObject("mainId", mainId);
//		mv.addObject("member", member);
//		mv.addObject("messCard", messCard);
//		mv.addObject("messNotices", messNotices);
//		mv.addObject("messBasisSet", messBasisSet);
//		mv.addObject("messAddFoods", messAddFoods);
//		mv.setViewName("merchants/trade/mess/mobile/addFoods");
//		return mv;
//	}
//
//	/**
//	 * 加餐核销(电脑核销)
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 加餐核销(电脑核销)",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/addFoodCancel", method = RequestMethod.POST)
//	public void addFoodCancel(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer fdId,@PathVariable Integer memberId) {
//			int data = 0;
//		WebResult result = new WebResult();
//		try {
//			Map<String,Integer> mapId = new HashMap<String, Integer>();
//			mapId.put("memberId", memberId);
//			mapId.put("mainId", mainId);
//			MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
//			data = messAddFoodService.addFood(messCard, fdId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				result.setSuccess(true);
//				MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
//				result.setMessage("加餐成功，金额" + messAddFood.getPrice() + "元");
//			}else{
//				result.setSuccess(false);
//				result.setErrors("加餐失败，金额不足");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			result.setSuccess(false);
//			result.setErrors("加餐失败，请尝试刷新取餐二维码");
//		} finally {
//			out.write(JSON.toJSONString(result).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//
//	/**
//	 * 加餐核销(手机核销)
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 加餐核销(手机核销)",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/addFoodCancel", method = RequestMethod.GET)
//	public ModelAndView addFoodCancelM(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer fdId,@PathVariable Integer memberId) {
//			int data = 0;
//			ModelAndView mv = new ModelAndView();
//		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			BusUser busUser = busUserMapper.selectByPrimaryKey(messMain.getBusId());
//			Integer busIdwx = messMain.getBusId();
//			if(busUser.getPid() != 0){
//				busIdwx = dictService.pidUserId(messMain.getBusId());
//			}
//			Member member = SessionUtils.getLoginMember(request,busId);
//			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
//			if(!CommonUtil.isEmpty(member) && member.getId() != null){
//				WxPublicUsers users = publicUsersMapper.selectByUserId(busIdwx);
//				if(!member.getPublicId().equals(users.getId())){
//					SessionUtils.setLoginMember(request,null);//清空缓存
//					member = null;
//				}
//			}
//			if(CommonUtil.isEmpty(member) || member.getId() == null){
//				mv.setViewName("redirect:/messMobile/" + busIdwx+"/"+ messMain.getId() + "/79B4DE7C/userGrant.do?flag=1&fdId="+fdId+"&addFoodCancleMMemberId="+memberId);
//				return mv;
//			}
//			Map<String, Object> autho = new HashMap<String, Object>();
//			autho.put("mainId", mainId);
//			autho.put("memberId", member.getId());
//		    autho.put("delStatus", 0);
//			List<MessAuthorityMember> tams = messAuthorityMemberMapper.getMessAuthorityMember(autho);
//			if(tams!=null&&tams.size()>0){
//				Map<String,Integer> mapId = new HashMap<String, Integer>();
//				mapId.put("memberId", memberId);
//				mapId.put("mainId", mainId);
//				MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
//				data = messAddFoodService.addFood(messCard, fdId);
//			}else{
//				data = -1;
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(data == 1){
//			MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
//			mv.addObject("msg", messAddFood.getPrice()+"元加餐成功！");
//			mv.setViewName("merchants/trade/mess/mobile/error");
//		}else if(data == -1){
//			mv.addObject("msg", "您未被授权！");
//			mv.setViewName("merchants/trade/mess/mobile/error");
//		}else{
//			mv.addObject("msg", "加餐失败！");
//			mv.setViewName("merchants/trade/mess/mobile/error");
//		}
//		return mv;
//	}
//
//	/**
//	 * 加菜码
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping(value = "{mainId}/{fdId}/79B4DE7C/getAddFoodQRcode")
//	public void getAddFoodQRcode(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable Integer mainId,@PathVariable Integer fdId) {
//		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			String filePath =wxmpApiProperties.getAdminUrl();
//			Member member = SessionUtils.getLoginMember(request,busId);
//			QRcodeKit.buildQRcode(filePath+"messMobile/"+ mainId +"/"+ fdId +"/"+ member.getId() +"/79B4DE7C/addFoodCancel.do", 300, 300, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 删除订单
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@SysLogAnnotation(description="微食堂 删除订单",op_function="4")//保存2，修改3，删除4
//	@RequestMapping(value = "{mId}/79B4DE7C/delMealOrder")
//	public void delMessCard(HttpServletRequest request, HttpServletResponse response,
//			@PathVariable("mId") Integer mId) {
//		int data = 0;
//		try {
//			data = messMealOrderService.delOrder(mId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			if(data == 1){
//				map.put("status","success");
//			}else{
//				map.put("status","error");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
//	public String time(String str,String strDate){
//		SimpleDateFormat sdf = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
//		SimpleDateFormat sdf2 = new SimpleDateFormat (str, Locale.UK);
//	    Date date = null;
//		try {
//			date = sdf.parse(strDate);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return sdf2.format(date);
//	}
//
//	@RequestMapping("/79B4DE7C/test")
//	public ModelAndView test(HttpServletRequest request,HttpServletResponse response
//			){
//		//@RequestParam Map <String,Object> params
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("merchants/newEdit/index");
//		return mv;
//	}
//}
