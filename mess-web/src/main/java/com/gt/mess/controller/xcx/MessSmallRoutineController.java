package com.gt.mess.controller.xcx;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.jws.WebResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.util.SessionUtils;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.exception.BaseException;
import com.gt.mess.exception.BusinessException;
import com.gt.mess.properties.FtpProperties;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.util.*;
import com.gt.util.entity.result.pay.WxPayOrder;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.mess.dao.MessAddFoodMapper;
import com.gt.mess.dao.MessAuthorityMemberMapper;
import com.gt.mess.dao.MessDepartmentMapper;
import com.gt.mess.entity.MessAddFood;
import com.gt.mess.entity.MessAuthorityMember;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.entity.MessCard;
import com.gt.mess.entity.MessCardGroup;
import com.gt.mess.entity.MessCardTicket;
import com.gt.mess.entity.MessConsumerDetail;
import com.gt.mess.entity.MessDepartment;
import com.gt.mess.entity.MessMain;
import com.gt.mess.entity.MessMealOrder;
import com.gt.mess.entity.MessMenus;
import com.gt.mess.entity.MessNotice;
import com.gt.mess.entity.MessOrderManage;
import com.gt.mess.entity.MessTopUpOrder;
import com.gt.mess.service.MessAddFoodService;
import com.gt.mess.service.MessAuthorityMemberService;
import com.gt.mess.service.MessBasisSetService;
import com.gt.mess.service.MessBuyTicketOrderService;
import com.gt.mess.service.MessCardGroupService;
import com.gt.mess.service.MessCardService;
import com.gt.mess.service.MessConsumerDetailService;
import com.gt.mess.service.MessMainService;
import com.gt.mess.service.MessMealOrderService;
import com.gt.mess.service.MessMenusService;
import com.gt.mess.service.MessNoticeService;
import com.gt.mess.service.MessOrderManageService;
import com.gt.mess.service.MessTopUpOrderService;

/**
 * 食堂小程序
 * @author ZengWenXiang
 * @QQ 307848200
 */
@Api(description = "食堂小程序API", hidden = true)
@Controller
@RequestMapping(value = "messSmallRoutine")
public class MessSmallRoutineController {

	private Logger logger = Logger.getLogger(MessSmallRoutineController.class);

	@Autowired
	private MessAuthorityMemberMapper messAuthorityMemberMapper;

	@Autowired
	private MessMainService messMainService;

	@Autowired
	private MessBasisSetService messBasisSetService;

	@Autowired
	private MessCardService messCardService;

	@Autowired
	private MessAddFoodService messAddFoodService;

	@Autowired
	private MessMenusService messMenusService;

	@Autowired
	private MessOrderManageService messOrderManageService;

	@Autowired
	private MessNoticeService messNoticeService;

	@Autowired
	private MessMealOrderService messMealOrderService;

	@Autowired
	private MessBuyTicketOrderService messBuyTicketOrderService;

	@Autowired
	private MessTopUpOrderService messTopUpOrderService;

	@Autowired
	private MessConsumerDetailService messConsumerDetailService;

	@Autowired
	private MessAuthorityMemberService messAuthorityMemberService;

	@Autowired
	private MessAddFoodMapper messAddFoodMapper;

	@Autowired
	private MessCardGroupService messCardGroupService;

	@Autowired
	private MessDepartmentMapper messDepartmentMapper;

    @Autowired
    private WxmpUtil wxmpUtil;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private WxmpApiProperties wxmpApiProperties;

	@Autowired
	private FtpProperties ftpProperties;



//	/**
//	 * 用户授权  OK
//	 * actId busId
//	 * @param only
//	 * @param response
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/{busId}/79B4DE7C/userGrant")
//	public void userGrant(HttpServletRequest request, @PathVariable Integer busId,
//			HttpServletResponse response, HttpSession session, String sign_type, String sign, String ticketCode,Integer fdId) throws Exception  {
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
//			String redirect_uri= temp.replace("$", "messMobile/" + users.getId()+"/"+busId);
//			Map<String, Object> param = new HashMap<String,Object>();
//			if(!CommonUtil.isEmpty(sign_type) && "autho".equals(sign_type)){//扫码授权
//				ticketCode = URLEncoder.encode(URLEncoder.encode(ticketCode,"UTF-8"), "UTF-8");
//				param.put("ticketCode", ticketCode);
//				param.put("sign", sign);
//			}
//			if(fdId != null){
//				param.put("fdId", fdId);
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
//	 * @param appid
//	 * @param code
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/{userId}/{busId}/79B4DE7C/getUserOpenId")
//	public String getUserOpenId(HttpServletRequest request, String code, @PathVariable Integer userId, @PathVariable Integer busId,
//			String state,Model model, String sign, String ticketCode,Integer fdId) throws Exception{
//		Map<String, Object> result = memberService.saveMember(code, userId, true);
//		Member member=(Member) result.get("member");
//		MessMain messMain = messMainService.getMessMainByBusId(busId);
//		String redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/79B4DE7C/index.do";
//		if(!CommonUtil.isEmpty(ticketCode)){//扫码授权人员权限
//			ticketCode = URLEncoder.encode(ticketCode,"UTF-8");
//			redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/79B4DE7C/authority.do?sign="+sign+"&ticketCode="+ticketCode;
//		}
//		if(fdId != null){
//			redirect_rul = "redirect:/messMobile/" + messMain.getId() + "/"+ fdId +"/79B4DE7C/addFood.do";
//		}
//		if(member != null){
//			CommonUtil.setLoginMember(request, member);
//		}
//		logger.info("进入回调！");
//		return redirect_rul;
//	}

	/**
	 * 检查是否授权
	 * @param request
	 * @param mainId
	 * @return
	 */
	public Map<String,Object> authorize(HttpServletRequest request,Integer mainId,
                                        MessCard messCard){
		Map<String,Object> mapObj = new HashMap<String, Object>();
		MessMain messMain =
				messMainService.getMessMainById(mainId);
		Integer busIdwx = null;
        try {
            busIdwx = wxmpUtil.checkBusId(messMain.getBusId());
        } catch (Exception e) {
            throw new BaseException("获取商家信息失败");
        }
		WxPublicUsers publicUsers = null;
		try {
			publicUsers = wxmpUtil.getWxPublic(busIdwx);
		} catch (Exception e) {
			throw new BaseException("获取商家公众号信息失败");
		}
		mapObj.put("type", 1);
        Member member = null;
        try {
            member = wxmpUtil.findByMemberId(messCard.getMemberId());
            redisCacheUtil.set(wxmpApiProperties.getRedisName()+"messCard_"+messCard.getId(),JSONObject.toJSONString(member));
        } catch (Exception e) {
            throw new BaseException("粉丝信息获取失败");
        }
        mapObj.put("member", member);
		mapObj.put("publicUsers", publicUsers);
		mapObj.put("messMain", messMain);
		mapObj.put("busId", busIdwx);
		return mapObj;
	}

	/**
	 * 微食堂支付完成跳转
	 * @param request
	 * @param response
	 * @param params
	 */
//	@SysLogAnnotation(description="微食堂 微食堂支付完成跳转",op_function="3")//保存2，修改3，删除4
	@RequestMapping("/79B4DE7C/wxMessPayOrder")
	public void wxMessPayOrder(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Map<String, Object> params){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessTopUpOrder messTopUpOrder =
					messTopUpOrderService.getMessTopUpOrderByOrderNo(params.get("orderNo").toString());
			if(messTopUpOrder.getStatus() == 1){
				MessConsumerDetail messConsumerDetail =
						messConsumerDetailService.getMessConsumerDetailById(Integer.valueOf(params.get("detailId").toString()));
				MessCard messCard = messCardService.getMessCardById(messTopUpOrder.getCardId());
				messCard.setMoney(messCard.getMoney() + messTopUpOrder.getMoney());

				messTopUpOrder.setStatus(0);
				messConsumerDetail.setStatus(0);
				int data = 0;
				data = messTopUpOrderService.update(messTopUpOrder);
				data = messConsumerDetailService.update(messConsumerDetail);
				data = messCardService.update(messCard);
				if(data == 1){
					json.put("status","success");
				}else{
					json.put("status","error");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * 菜单首页
	 * @param request
	 * @param response
	 * @param openId
	 */
	@RequestMapping("/{openId}/79B4DE7C/index")
	public void index(HttpServletRequest request,HttpServletResponse response,
			@PathVariable String openId){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardByOpenId(openId);
			if(messCard == null){
				json.put("IsSkip", 0);
				return;
			}else{
				json.put("IsSkip", 1);
			}
			Integer mainId = messCard.getMainId();
			Map<String,Object> obj = authorize(request, mainId,messCard);
			MessMain messMain = (MessMain)obj.get("messMain");
			Member member = (Member)obj.get("member");
			MessBasisSet messBasisSet = null;
			messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			if(messCard.getGroupId() != null){
				MessCardGroup messCardGroup =
						messCardGroupService.getCardGroupById(messCard.getGroupId());
				JSONObject jsonObject = (JSONObject) JSON.parse(messCardGroup.getAuthority());
				if(jsonObject.getInteger("bitUse").equals(0)){
					if(messBasisSet.getBitUniversal() == 0){
						messBasisSet.setUniversalPrice(jsonObject.getDouble("universalPrice"));
					}else{
						messBasisSet.setBreakfastPrice(jsonObject.getDouble("breakfastPrice"));
						messBasisSet.setDinnerPrice(jsonObject.getDouble("dinnerPrice"));
						messBasisSet.setLunchPrice(jsonObject.getDouble("lunchPrice"));
						messBasisSet.setNightPrice(jsonObject.getDouble("nightPrice"));
					}
				}
			}
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
			List<MessCardTicket> messCardTickets =
					messCardService.getMessCardTicketListByCardId(messCard.getId());
			int nums = 0;
			int nums2 = 0;
			if(messBasisSet.getPastDay() != 0){
				//判断是否有饭票即将过期（1天）
				Map<String,Object> mapObj2 = null;
				try {
					mapObj2 = messCardService.pastDue2(messCard, messBasisSet, messCardTickets, mainId,messBasisSet.getPastDay());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(mapObj2 != null){
					nums2 = Integer.valueOf(mapObj2.get("nums").toString());
				}
				//判断是否有饭票过期
				if(nums2 != 0){
					Map<String,Object> mapObj = null;
					try {
						mapObj = messCardService.pastDue(messCard, messBasisSet, messCardTickets, mainId);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(mapObj != null){
						nums = Integer.valueOf(mapObj.get("nums").toString());
					}
					if(nums != 0){
						nums2 = 0;
					}
				}
			}
			try {
				messCardService.balance(messCard, messBasisSet, messCardTickets, mainId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Date nowDate = new Date();
			String dateStr = DateTimeKit.format(nowDate,"HH:mm");
			Integer mealType = -1;
			if(DateTimeKit.isInTime(
					DateTimeKit.format(messBasisSet.getBreakfastStart(),"HH:mm")+"-"+
					DateTimeKit.format(messBasisSet.getBreakfastEnd(),"HH:mm"),
					dateStr)){
				mealType = 0;
			}else if(DateTimeKit.isInTime(
					DateTimeKit.format(messBasisSet.getLunchStart(),"HH:mm")+"-"+
					DateTimeKit.format(messBasisSet.getLunchEnd(),"HH:mm"),
					dateStr)){
				mealType = 1;
			}else if(DateTimeKit.isInTime(
					DateTimeKit.format(messBasisSet.getDinnerStart(),"HH:mm")+"-"+
					DateTimeKit.format(messBasisSet.getDinnerEnd(),"HH:mm"),
					dateStr)){
				mealType = 2;
			}else if(DateTimeKit.isInTime(
					DateTimeKit.format(messBasisSet.getNightStart(),"HH:mm")+"-"+
					DateTimeKit.format(messBasisSet.getNightEnd(),"HH:mm"),
					dateStr)){
				mealType = 3;
			}
			if(mealType != -1){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("cardId", messCard.getId());
				map.put("mealType", mealType);
				map.put("mainId", messCard.getMainId());
				MessMealOrder messMealOrder = messMealOrderService.getMessMealOrderByMap(map);
				if(messMealOrder != null){
					json.put("mealCode", messMealOrder.getMealCode());
				}else {
					json.put("mealCode", -1);
					json.put("msg", "现时间段无订餐！");
				}
			}else{
				json.put("mealCode", -1);
				json.put("msg", "现时间段无订餐！");
			}
			json.put("status","success");
			json.put("messNotices", messNotices);
			json.put("messBasisSet", messBasisSet);
			json.put("mainId", mainId);
			json.put("member", member);
			json.put("busId", messMain.getBusId());
			json.put("messCard", messCard);
			json.put("nums", nums);
			json.put("nums2", nums2);
			json.put("cardId", messCard.getId());
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 个人信息
	 * @param request
	 * @param response
	 * @param mainId
	 * @param memberId
	 */
	@RequestMapping("/{mainId}/{memberId}/79B4DE7C/head")
	public void head(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer memberId){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("memberId", memberId);
			mapId.put("mainId", mainId);
			MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
			MessBasisSet messBasisSet = null;
			Member member =  JSONObject.toJavaObject(JSONObject.parseObject(redisCacheUtil.get(wxmpApiProperties.getRedisName()+"messCard_"+messCard.getId()).toString()),Member.class);
			messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			if(messCard.getGroupId() != null){
				MessCardGroup messCardGroup =
						messCardGroupService.getCardGroupById(messCard.getGroupId());
				JSONObject jsonObject = (JSONObject) JSON.parse(messCardGroup.getAuthority());
				if(jsonObject.getInteger("bitUse").equals(0)){
					if(messBasisSet.getBitUniversal() == 0){
						messBasisSet.setUniversalPrice(jsonObject.getDouble("universalPrice"));
					}else{
						messBasisSet.setBreakfastPrice(jsonObject.getDouble("breakfastPrice"));
						messBasisSet.setDinnerPrice(jsonObject.getDouble("dinnerPrice"));
						messBasisSet.setLunchPrice(jsonObject.getDouble("lunchPrice"));
						messBasisSet.setNightPrice(jsonObject.getDouble("nightPrice"));
					}
				}
			}
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
			json.put("status","success");
			json.put("messNotices", messNotices);
			json.put("messBasisSet", messBasisSet);
			json.put("mainId", mainId);
			json.put("member", member);
//			json.put("busId", messMain.getBusId());
			json.put("messCard", messCard);
			json.put("cardId", messCard.getId());
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 取餐码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "{mainId}/{cardId}/{mealCode}/79B4DE7C/getMessUrltoQRcode")
	public void getMessUrltoQRcode(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer cardId,@PathVariable String mealCode) {
		try {
			MessMain messMain = messMainService.getMessMainById(mainId);
			String filePath =wxmpApiProperties.getAdminUrl();
			QRcodeKit.buildQRcode(filePath+"messReception/"+messMain.getBusId()+"/"+cardId+"/"+ mealCode +"/79B4DE7C/wirteOff.do", 300, 300, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取餐码验证
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 取餐码验证",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "{mainId}/{cardId}/{mealCode}/79B4DE7C/verify")
	public void verify(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer cardId,@PathVariable String mealCode) {
		int data = 0;
		Map<String,Object> mapObj = new HashMap<String, Object>();
		try {
//			Map<String,Object> obj = authorize(request, mainId);
			mapObj = messCardService.verify(cardId,mealCode);
			data = Integer.valueOf(mapObj.get("data").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data = -1;
		}
		PrintWriter out = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			out = response.getWriter();
			map.put("nums",mapObj.get("nums"));
			if(data > 0){
				map.put("status","success");
			}else if(data == -1){
				map.put("status","error1");
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
	 * 商家订餐统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{mainId}/79B4DE7C/statistics")
	public void statistics(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
//			Map<String,Object> obj = authorize(request, mainId);
			List<MessMealOrder> messMealOrderList =
					messMealOrderService.getMessMealOrderListforToday2(mainId);
			List<MessDepartment> messDepartments =
					messDepartmentMapper.getMessDepartmentPageByMainId(mainId);
			List<Map<String,Object>> bListMaps = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> lListMaps = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> dListMaps = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> nListMaps = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < 4; i++) {
				for(MessDepartment messDepartment:messDepartments){
					Map<String,Object> map = new HashMap<String, Object>();
					Map<String,Integer> mapId = new HashMap<String, Integer>();
					mapId.put("depId", messDepartment.getId());
					mapId.put("mainId", mainId);
					mapId.put("mealType", i);
					List<MessMealOrder> mealOrderNums = messMealOrderService.getNumsByDepIdAndMealType(mapId);
					map.put("name", messDepartment.getName());
					map.put("nums", mealOrderNums.size());
					Integer mealNums = 0;
					for(MessMealOrder messMealOrder : mealOrderNums){
						mealNums +=  messMealOrder.getMealNum();
					}
					map.put("mealNums", mealNums);
					if(i == 0){
						bListMaps.add(map);
					}else if(i == 1){
						lListMaps.add(map);
					}else if(i == 2){
						dListMaps.add(map);
					}else if(i == 3){
						nListMaps.add(map);
					}
				}
				Map<String,Object> map = new HashMap<String, Object>();
				Map<String,Integer> mapId = new HashMap<String, Integer>();
				mapId.put("depId", -1);
				mapId.put("mainId", mainId);
				mapId.put("mealType", i);
				List<MessMealOrder> mealOrderNums = messMealOrderService.getNumsByDepIdAndMealType(mapId);
				if(mealOrderNums.size() != 0){
					map.put("name", "暂无部门");
					map.put("nums", mealOrderNums.size());
					Integer mealNums = 0;
					for(MessMealOrder messMealOrder : mealOrderNums){
						mealNums +=  messMealOrder.getMealNum();
					}
					map.put("mealNums", mealNums);
					if(i == 0){
						bListMaps.add(map);
					}else if(i == 1){
						lListMaps.add(map);
					}else if(i == 2){
						dListMaps.add(map);
					}else if(i == 3){
						nListMaps.add(map);
					}
				}
			}
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
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
			json.put("messNotices", messNotices);
			json.put("breakfastNum", breakfastNum);
			json.put("lunchNum", lunchNum);
			json.put("dinnerNum", dinnerNum);
			json.put("nightNum", nightNum);

			json.put("breakfastMealNum", breakfastMealNum);
			json.put("lunchMealNum", lunchMealNum);
			json.put("dinnerMealNum", dinnerMealNum);
			json.put("nightMealNum", nightMealNum);

			json.put("bListMaps", bListMaps);
			json.put("lListMaps", lListMaps);
			json.put("dListMaps", dListMaps);
			json.put("nListMaps", nListMaps);

			json.put("nums", breakfastNum+lunchNum+dinnerNum+nightNum);
			json.put("mainId", mainId);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}


	/**
	 * 绑定饭卡
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 绑定饭卡",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/bindingCard")
	public void bindingCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messCardService.srBindingCard(params);
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
			}else if(data == 2){
				map.put("status","error2");
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
	 * 加菜完成后跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/{mainId}/{fdId}/{memberId}/79B4DE7C/addFood")
	public void addFoodL(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer fdId,@PathVariable Integer memberId){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("memberId", memberId);
			mapId.put("mainId", mainId);
			MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
			int data = 0;
			if(messCard == null){
				json.put("IsSkip", 0);
				out.write(JSON.toJSONString(json).toString());
				return;
			}else{
				MessAddFood messAddFood = messAddFoodService.getMessAddFoodById(fdId);
				json.put("money", messAddFood.getPrice());
				json.put("mainId", mainId);
				json.put("cardId", messCard.getId());
				if("1".equals(redisCacheUtil.get("mess:"+memberId))){
					json.put("data", 1);
					out.write(JSON.toJSONString(json).toString());
					return;
				}else{
					redisCacheUtil.set("mess:"+memberId, "1",60L);
				}
				try {
					data = messAddFoodService.addFood(messCard, fdId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					data = 0;
				}
				json.put("data", data);
				redisCacheUtil.remove("mess:"+memberId);
			}
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

//	购买饭票模块

	/**
	 * 立即购买（购买饭票）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/buyNow")
	public void buyNow(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
//			Map<String,Object> obj = authorize(request, mainId);
//			MessMain messMain = (MessMain)obj.get("messMain");
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messCard.getMainId());
			if(messCard.getGroupId() != null){
				MessCardGroup messCardGroup =
						messCardGroupService.getCardGroupById(messCard.getGroupId());
				JSONObject jsonObject = (JSONObject) JSON.parse(messCardGroup.getAuthority());
				if(jsonObject.getInteger("bitUse").equals(0)){
					if(messBasisSet.getBitUniversal() == 0){
						messBasisSet.setUniversalPrice(jsonObject.getDouble("universalPrice"));
					}else{
						messBasisSet.setBreakfastPrice(jsonObject.getDouble("breakfastPrice"));
						messBasisSet.setDinnerPrice(jsonObject.getDouble("dinnerPrice"));
						messBasisSet.setLunchPrice(jsonObject.getDouble("lunchPrice"));
						messBasisSet.setNightPrice(jsonObject.getDouble("nightPrice"));
					}
				}
			}
			json.put("messBasisSet", messBasisSet);
			json.put("messCard", messCard);
			json.put("mainId", messCard.getMainId());
			json.put("cardId", messCard.getId());
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 购买饭票
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 购买饭票",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/buyTicket")
	public void buyTicket(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messBuyTicketOrderService.buyTicket(params);
		} catch (BaseException be){

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
			}else if(data == -1){
				map.put("status","error1");
			}else if(data == -2){
				map.put("status","error2");
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

//	我的模块

	/**
	 * 我的订餐明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/myMealOrderDetail")
	public void myMealOrderDetail(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
//			Map<String,Object> obj = authorize(request, mainId);
//			MessMain messMain = (MessMain)obj.get("messMain");
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(), 10);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			json.put("messCard", messCard);
			json.put("messNotices", messNotices);
			json.put("messBasisSet", messBasisSet);
			json.put("mainId", mainId);
			json.put("cardId", messCard.getId());
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 明细列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{mainId}/{cardId}/79B4DE7C/detailList")
	public void detailList(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessMealOrder> page){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
//			Map<String,Object> obj = authorize(request, mainId);
//			MessMain messMain = (MessMain)obj.get("messMain");
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			List<MessMealOrder> messMealOrderList =
					messMealOrderService.getPastMessMealOrderListByCardIdAndMainId(mapId);
			for(MessMealOrder mealOrder : messMealOrderList){
				mealOrder.setStatus(5);
				try {
					messMealOrderService.update(mealOrder);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Page<MessMealOrder> messMealOrders =
					messMealOrderService.getMessMealOrderPageByCardIdAndMainId(page,mapId,10);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			for(MessMealOrder messMealOrder : messMealOrders.getRecords()){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("messMealOrder", messMealOrder);
				map.put("time", DateTimeKit.format(messMealOrder.getTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				map.put("orderTime", DateTimeKit.format(messMealOrder.getOrderTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				listMap.add(map);
			}
			json.put("messMealOrders", listMap);
//			json.put("mainId", messMain.getId());
			json.put("cardId", cardId);
			String datetime = DateTimeKit.format(new Date(), DateTimeKit.DEFAULT_DATE_FORMAT);
			datetime += " 00:00:00";
			Date nowDate = DateTimeKit.parse(datetime, DateTimeKit.DEFAULT_DATETIME_FORMAT);
			json.put("now", datetime);
			json.put("nowDate", nowDate);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 明细列表（加载）
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{mainId}/{cardId}/79B4DE7C/loadDetailList")
	public void loadDetailList(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessMealOrder> page) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
//			Map<String,Object> obj = authorize(request, mainId);
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			Page<MessMealOrder> messMealOrders =
					messMealOrderService.getMessMealOrderPageByCardIdAndMainId(page,mapId,10);
			if(messMealOrders.getCurrent() < page.getCurrent()){
				out.write("-1");
				return;
			}
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			for(MessMealOrder messMealOrder : messMealOrders.getRecords()){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("messMealOrder", messMealOrder);
				map.put("time", DateTimeKit.format(messMealOrder.getTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				map.put("orderTime", DateTimeKit.format(messMealOrder.getOrderTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				listMap.add(map);
			}
//			json.put("mainId", messMain.getId());
			if(messMealOrders != null){
				out.write(JSON.toJSONString(listMap).toString());
			}else{
				out.write(JSON.toJSONString("error").toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 取消订单
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 取消订单",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "{mainId}/{orderId}/79B4DE7C/cancelOrder")
	public void cancelOrder(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer orderId) {
		int data = 0;
		try {
//			Map<String,Object> obj = authorize(request, mainId);
			data = messMealOrderService.cancelOrder(orderId);
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
	 * 订单列表(消费明细)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{mainId}/{cardId}/79B4DE7C/orderList")
	public void orderList(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessConsumerDetail> page){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
//			Map<String,Object> obj = authorize(request, mainId);
//			MessMain messMain = (MessMain)obj.get("messMain");
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			Page<MessConsumerDetail> messConsumerDetails =
					messConsumerDetailService.getMessConsumerDetailPageByCardIdAndMainId(page, mapId, 20);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			for(MessConsumerDetail messConsumerDetail : messConsumerDetails.getRecords()){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("messConsumerDetail", messConsumerDetail);
				map.put("time", DateTimeKit.format(messConsumerDetail.getTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				listMap.add(map);
			}
			json.put("messConsumerDetails", listMap);
//			json.put("mainId", messMain.getId());
			json.put("cardId", cardId);
//			mv.setViewName("merchants/trade/mess/mobile/orderList");
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 订单列表加载(消费明细加载)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{mainId}/{cardId}/79B4DE7C/loadOrderList")
	public void loadOrderList(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessConsumerDetail> page){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		try {
			out = response.getWriter();
//			Map<String,Object> obj = authorize(request, mainId);
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			Page<MessConsumerDetail> messConsumerDetails =
					messConsumerDetailService.getMessConsumerDetailPageByCardIdAndMainId(page, mapId, 20);
			if(messConsumerDetails.getCurrent() < page.getCurrent()){
				out.write("-1");
				return;
			}
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			for(MessConsumerDetail messConsumerDetail : messConsumerDetails.getRecords()){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("messConsumerDetail", messConsumerDetail);
				map.put("time", DateTimeKit.format(messConsumerDetail.getTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				listMap.add(map);
			}
			if(messConsumerDetails != null && messConsumerDetails.getTotal() > 0){
				out.write(JSON.toJSONString(listMap).toString());
			}else{
				out.write(JSON.toJSONString("error").toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

//	每周菜单模块

	/**
	 * 订餐主页(每周菜单)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/{week}/{type}/79B4DE7C/mealOrder")
	public void mealOrder(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId,@PathVariable Integer week,
			@PathVariable Integer type){
		Map<String,Object> json = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("week", week);
			map.put("type", type);
			map.put("mainId", mainId);
			List<MessMenus> messMenuList =
					messMenusService.getMessMenusListByTypeAndWeekNumforMainId(map);
			List<MessMenus> messMenus = new ArrayList<MessMenus>();
			for(MessMenus menus:messMenuList){
				menus.setImages(ftpProperties.getImageAccess()+menus.getImages());
				messMenus.add(menus);
			}
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			json.put("messNotices", messNotices);
			json.put("week", week);
//			json.put("member", member);
			json.put("mainId", mainId);
			json.put("messCard", messCard);
			json.put("cardId", messCard.getId());
			json.put("messMenus", messMenus);
			json.put("messBasisSet", messBasisSet);
			out.write(JSON.toJSONString(json).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

//	订餐模块

	/**
	 * 订餐
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/{year}/{month}/79B4DE7C/calendar")
	public void calendar(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId,@PathVariable String year,@PathVariable String month){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
//			Map<String,Object> obj = authorize(request, mainId);
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("mainId", messCard.getMainId());
			mapId.put("cardId", messCard.getId());
			List<MessMealOrder> messMealOrderList =
					messMealOrderService.getPastMessMealOrderListByCardIdAndMainId(mapId);
			for(MessMealOrder mealOrder : messMealOrderList){
				mealOrder.setStatus(5);
				try {
					messMealOrderService.update(mealOrder);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//不可订餐
			List<MessOrderManage> messOrderManages = messOrderManageService.getMessOrderManageListByMainId(messCard.getMainId());
			//已预订
			List<MessMealOrder> bookMealOrders =
					messMealOrderService.getBookedMessMealOrder(mapId);
			//未选餐
			List<MessMealOrder> notChooseMealOrders =
					messMealOrderService.getNotChooseMessMealOrder(mapId);
			List<Map<String,Object>> orederDays = new ArrayList<Map<String,Object>>();

//			String month = "5";
//			String year = "2017";
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date _date = new Date();
			try {
				_date = format.parse(year+"-"+month+"-1");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //获取前月的第一天
	        Calendar cal_1=Calendar.getInstance();//获取当前日期
	        cal_1.setTime(_date);
	        cal_1.add(Calendar.MONTH, 0);
	        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天

	        String firstDay = format.format(cal_1.getTime());
	        //获取前月的最后一天
	        Calendar cale = Calendar.getInstance();
	        cale.setTime(_date);
	        cale.add(Calendar.MONTH, 1);
	        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
	        String lastDay = format.format(cale.getTime());
	        int maxDay=cale.getActualMaximum(Calendar.DATE);
	        try {
		        for(int i = 1;i <= maxDay;i++){
		        	Map<String,Object> orederDay = new HashMap<String, Object>();
		        	String dateStr = year+"-"+month+"-"+i;
		        	Date f_date = format.parse(dateStr);
		        	if(DateTimeKit.daysBetween(new Date(),f_date) < 0){
		        		orederDay.put("date", dateStr);
						orederDay.put("type", 0);
						orederDays.add(orederDay);
		        		continue;
		        	}
		        	int numType = 0;
		        	//不可订餐
					for(MessOrderManage messOrderManage :messOrderManages){
						Date m_date= format.parse(messOrderManage.getDay());
						if(DateTimeKit.daysBetween(f_date,m_date) == 0){
							orederDay.put("date", dateStr);
							orederDay.put("type", 0);
							numType = 1;
							orederDays.add(orederDay);
							break;
						}
					}
					if(numType == 1)
						continue;
					//已预订
					for(MessMealOrder messMealOrder :bookMealOrders){
						if(DateTimeKit.daysBetween(f_date,messMealOrder.getTime()) == 0){
							orederDay.put("date", dateStr);
							orederDay.put("type", 1);
							numType = 1;
							orederDays.add(orederDay);
							break;
						}
					}
					if(numType == 1)
						continue;
					//未选餐
					for(MessMealOrder messMealOrder :notChooseMealOrders){
						if(DateTimeKit.daysBetween(f_date,messMealOrder.getTime()) == 0){
							orederDay.put("date", dateStr);
							orederDay.put("type", 2);
							numType = 1;
							orederDays.add(orederDay);
							break;
						}
					}
					if(numType == 0){
						orederDay.put("date", dateStr);
						orederDay.put("type", 4);
						orederDays.add(orederDay);
					}
		        }
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			json.put("orederDays", orederDays);
			out.write(JSON.toJSONString(json).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 追加订单(或取消订单列表)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/79B4DE7C/addOrder")
	public void addOrder(HttpServletRequest request,HttpServletResponse response,
			@RequestParam Map <String,Object> params){
		//@RequestParam Map <String,Object> params
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			Integer cardId = Integer.valueOf(params.get("cardId").toString());
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
//			Map<String,Object> obj = authorize(request, mainId);
//			MessMain messMain = (MessMain)obj.get("messMain");
			params.put("mainId", messCard.getMainId());
			params.put("cardId", messCard.getId());
			List<MessMealOrder> messMealOrders =
					messMealOrderService.getBookMessMealOrderByToDay(params);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messCard.getMainId());
			Member member = JSONObject.toJavaObject(JSONObject.parseObject(redisCacheUtil.get(
					wxmpApiProperties.getRedisName()+"messCard_"+messCard.getId()).toString()),Member.class);

			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			json.put("messNotices", messNotices);
			json.put("member", member);
			if(params.get("time").equals(DateTimeKit.getDateTime(new Date(), "yyyy-M-d"))){
				json.put("bitqx", 0);
			}else{
				json.put("bitqx", 1);
			}
			json.put("param", params);
			json.put("messBasisSet", messBasisSet);
			json.put("mainId", messCard.getMainId());
			json.put("cardId", cardId);
			json.put("messCard", messCard);
			json.put("messMealOrders", messMealOrders);
			out.write(JSON.toJSONString(json).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 保存或更新追加订单
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 保存或更新追加订单",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/saveOrUpdateAddOrder")
	public void saveOrUpdateAddOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messMealOrderService.saveOrUpdateAddOrder(params);
		} catch (BaseException be){
			if("超过预定时间".equals(be.getMessage())){
				data = -2;
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
			}else if(data == -1){
				map.put("status","error1");
			}else if(data == -2){
				map.put("status","error2");
			}else {
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
	 * 删除未选餐
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 删除未选餐",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/delNotCMealOrder")
	public void delNotCMealOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messMealOrderService.delNotCMealOrder(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			out = response.getWriter();
			if(data >= 1){
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
	 * 保存订餐
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 保存订餐",op_function="2")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/saveMealOrder")
	public void saveMealOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messMealOrderService.saveMealOrder(params);
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
			}else if(data == -1){
				map.put("status","error1");
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
	 * 选择早晚餐
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/chooseMeal")
	public void chooseMeal(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("mainId", messCard.getMainId());
			mapId.put("cardId", messCard.getId());
			List<MessMealOrder> messMealOrders =
					messMealOrderService.getNotChooseMessMealOrder(mapId);
			List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
			for(MessMealOrder messMealOrder : messMealOrders){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("messMealOrder", messMealOrder);
				map.put("time", DateTimeKit.format(messMealOrder.getTime(), DateTimeKit.DEFAULT_DATE_FORMAT));
				listMap.add(map);
			}
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			Member member = JSONObject.toJavaObject(JSONObject.parseObject(redisCacheUtil.get(
					wxmpApiProperties.getRedisName()+"messCard_"+messCard.getId()).toString()),Member.class);
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			json.put("messNotices", messNotices);
			json.put("size", messMealOrders.size());
			json.put("member", member);
			json.put("messBasisSet", messBasisSet);
			json.put("mainId", mainId);
			json.put("cardId", cardId);
			json.put("messCard", messCard);
			json.put("messMealOrders", listMap);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 保存选餐（选择早晚餐）
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 保存订餐（选择早晚餐）",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/saveChooseMealOrder")
	public void saveChooseMealOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map <String,Object> params) {
		int data = 0;
		try {
			data = messMealOrderService.saveChooseMealOrder(params);
		} catch (BaseException be){
			if("超过预定时间".equals(be.getMessage())){
				data = -2;
			}else if("余额不足".equals(be.getMessage())){
				data = -1;
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
			}else if(data == -1){
				map.put("status","error1");
			}else if(data == -2){
				map.put("status","error2");
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

//	余额充值模块

	/**
	 * 购买饭票显示(余额充值)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/buyTicketShow")
	public void buyTicketShow(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			json.put("messCard", messCard);
			json.put("mainId", mainId);
			json.put("cardId", cardId);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 饭票余额显示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/ticketMoneyShow")
	public void ticketMoneyShow(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			json.put("messCard", messCard);
			json.put("mainId", mainId);
			json.put("cardId", cardId);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

//	/**
//	 * 饭卡充值支付
//	 *
//	 * @param request
//	 * @param response
//	 * @return
//	 */
////	@SysLogAnnotation(description="微食堂 饭卡充值支付",op_function="3")//保存2，修改3，删除4
//	@RequestMapping(value = "/79B4DE7C/topUpPay")
//	public void topUpPay(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map <String,Object> params) {
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			map = messTopUpOrderService.topUpPay(params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Map<Object, Object> resultMap = new TreeMap<>();
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//			if(map.get("data").toString().equals("1")){
//				String orderNo = CommonUtil.toString(map.get("orderNo"));
//				MessTopUpOrder order = messTopUpOrderService.getMessTopUpOrderByOrderNo(orderNo);
//				Member member =
//						memberMapper.selectByPrimaryKey(Integer.valueOf(params.get("memberId").toString()));
//				WxPublicUsers pbUser = morderService.getWpUser(member.getId());
//				WxPayOrder wxPayOrder=wxPayOrderMapper.selectByOutTradeNo(orderNo);
//				if(order.getStatus() == 1){
//					if(!CommonUtil.isEmpty(wxPayOrder)&&wxPayOrder.getTradeState().equals("SUCCESS")){
//						map.put("status","error");
//						out.write(JSON.toJSONString(map).toString());
//						return;
//					}else if(!CommonUtil.isEmpty(wxPayOrder)){
//						wxPayService.memberCloseOrd(map);
//					}
//					CommonUtil.getWxParams(pbUser, request);
//				}
//				MessCard messCard = messCardService.getMessCardById(order.getCardId());
//				map.put("appid", params.get("appid").toString());
//				map.put("mchid", pbUser.getMchId());
//				map.put("productId", System.currentTimeMillis());
//				map.put("sysOrderNo", orderNo);
//				map.put("totalFee", order.getMoney());
//				map.put("desc", "微食堂支付");
//				map.put("ip","127.0.0.1");
//				map.put("openid",messCard.getSrOpenId());
//				map.put("key", pbUser.getApiKey());
//				map.put("authRefreshToken", pbUser.getAuthRefreshToken());
//				map.put("url", CommonUtil.getpath(request));
//				map.put("paySource", 1);
//				map.put("model", 27);//模块27-微食堂支付
//				MessMain messMain = messMainService.getMessMainById(order.getMainId());
//				map.put("busId", messMain.getBusId());
//				resultMap = wxPayService.memberPayByWxApplet(map);
//				resultMap.put("orderNo",map.get("orderNo").toString());
//				resultMap.put("detailId",map.get("detailId").toString());
//				resultMap.put("status","success");
////				map.put("url", "wxPay/79B4DE7C/wxMessPayOrder.do?orderNo="+map.get("orderNo").toString()+"&detailId="+map.get("detailId").toString());
//			}else{
//				map.put("status","error");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(resultMap).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}

//	/**
//	 * 饭票授权手机端
//	 * @param request
//	 * @param response
//	 * @param ticketId
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
//			Member member = JSONObject.toJavaObject(JSONObject.parseObject(redisCacheUtil.get(
//					wxmpApiProperties.getRedisName()+"messCard_"+messCard.getId()).toString()),Member.class);
//			//如果session里面的数据不为null，判断是否是该公众号下面的粉丝id，是的话，往下走，不是的话，清空缓存
//			if(!CommonUtil.isEmpty(member)){
//				Integer busIdwx = null;
//				try {
//					busIdwx = Integer.valueOf(redisCacheUtil.get(wxmpApiProperties.getRedisName()+"mainId_"+mainId).toString());
//				} catch (NumberFormatException e) {
//					throw new BaseException("获取商家busId失败");
//				}
//				WxPublicUsers wxPublicUsers = null;
//				try {
//					wxPublicUsers = wxmpUtil.getWxPublic(busIdwx);
//				} catch (Exception e) {
//					throw new BaseException("获取商家公众号信息失败");
//				}
//			}
//			if(CommonUtil.isEmpty(member)){
//				mav.setViewName("redirect:/messMobile/" + messMain.getBusId()+ "/79B4DE7C/userGrant.do?sign_type=autho&ticketCode="+params.get("ticketCode").toString()+"&sign="+params.get("sign").toString());
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

//	加菜模块

	/**
	 * 加餐
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/addFood")
	public void addFood(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Integer cardId,Page<MessAddFood> page){
		PrintWriter out = null;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			out = response.getWriter();
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			Page<MessAddFood> messAddFoods =
					messAddFoodService.getMessAddFoodPageByMainId(page, mainId, 100);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
			json.put("mainId", mainId);
			json.put("messCard", messCard);
			json.put("messNotices", messNotices);
			json.put("messBasisSet", messBasisSet);
			json.put("messAddFoods", messAddFoods);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("status","error");
		} finally {
			out.write(JSON.toJSONString(json).toString());
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 加餐核销(电脑核销)
	 * @param mainId
	 * @param fdId
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/addFoodCancel", method = RequestMethod.POST)
	public ResponseDTO addFoodCancel(@PathVariable Integer mainId,
									 @PathVariable Integer fdId,
									 @PathVariable Integer memberId) {
		int data = 0;
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("memberId", memberId);
			mapId.put("mainId", mainId);
			MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
			data = messAddFoodService.addFood(messCard, fdId);
			if(data == 1){
				MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
				return ResponseDTO.createBySuccessMessage("加餐成功，金额" + messAddFood.getPrice() + "元");
			}else{
				return ResponseDTO.createByErrorMessage("加餐失败，金额不足");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("加餐失败，请尝试刷新取餐二维码");
		}
	}

	/**
	 * 加餐核销(手机核销)
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 加餐核销(手机核销)",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/addFoodCancel", method = RequestMethod.GET)
	public ModelAndView addFoodCancelM(HttpServletRequest request, HttpServletResponse response,
									   @PathVariable Integer mainId,@PathVariable Integer fdId,@PathVariable Integer memberId) {
		int data = 0;
		ModelAndView mv = new ModelAndView();
		try {
//			MessMain messMain = messMainService.getMessMainById(mainId);
//			Integer busIdwx = null;
//			try {
//				busIdwx = Integer.valueOf(redisCacheUtil.get(wxmpApiProperties.getRedisName()+"mainId_"+messMain.getId()).toString());
//			} catch (NumberFormatException e) {
//				throw new BaseException("获取商家busId失败");
//			}
			Member member = wxmpUtil.findByMemberId(memberId);
//			String url = wxmpApiProperties.getAdminUrl()+"messMobile/"+mainId+"/"+fdId+"/"+memberId+"/79B4DE7C/addFoodCancel.do";
//			Map<String,Object> obj = authorize(request,response,busIdwx,messMain.getId(),c);
//			if("0".equals(obj.get("type").toString())){
//				mv.setViewName(obj.get("url").toString());
//				return mv;
//			}
			Map<String, Object> autho = new HashMap<String, Object>();
			autho.put("mainId", mainId);
			autho.put("memberId", member.getId());
			autho.put("delStatus", 0);
			List<MessAuthorityMember> tams = messAuthorityMemberMapper.getMessAuthorityMember(autho);
			if(tams!=null&&tams.size()>0){
				Map<String,Integer> mapId = new HashMap<String, Integer>();
				mapId.put("memberId", memberId);
				mapId.put("mainId", mainId);
				MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
				data = messAddFoodService.addFood(messCard, fdId);
			}else{
				data = -1;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(data == 1){
			MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
			mv.addObject("msg", messAddFood.getPrice()+"元加餐成功！");
			mv.setViewName("merchants/trade/mess/mobile/error");
		}else if(data == -1){
			mv.addObject("msg", "您未被授权！");
			mv.setViewName("merchants/trade/mess/mobile/error");
		}else{
			mv.addObject("msg", "加餐失败！");
			mv.setViewName("merchants/trade/mess/mobile/error");
		}
		return mv;
	}

	/**
	 * 加菜码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/getAddFoodQRcode")
	public void getAddFoodQRcode(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer mainId,@PathVariable Integer fdId,@PathVariable Integer memberId) {
		try {
			String filePath =wxmpApiProperties.getAdminUrl();
			QRcodeKit.buildQRcode(filePath+"messMobile/"+ mainId +"/"+ fdId +"/"+ memberId +"/79B4DE7C/addFoodCancel.do", 300, 300, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除订单
	 *
	 * @param request
	 * @param response
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 删除订单",op_function="4")//保存2，修改3，删除4
	@RequestMapping(value = "{mId}/79B4DE7C/delMealOrder")
	public void delMessCard(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("mId") Integer mId) {
		int data = 0;
		try {
			data = messMealOrderService.delOrder(mId);
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
	public String time(String str,String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
		SimpleDateFormat sdf2 = new SimpleDateFormat (str, Locale.UK);
	    Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf2.format(date);
	}


/**
 * 小程序登录用这个URL，参数不变
 * 8A5DA52E/wxphone/6F6D9AD2/79B4DE7C/79B4DE7C/app_login.do
 **/
//	/**
//	 * 小程序登录1.1
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/79B4DE7C/app_login")
//	public void app_login(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam Map<String, Object> params) {
//		PrintWriter out = null;
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			out = response.getWriter();
//			Jscode2session jscode2session=new Jscode2session();
//			jscode2session.setAppid(CommonUtil.toString(params.get("appid")));
//			ComponentAccessToken component_access_token=componentAPI.api_component_token();
//			jscode2session.setComponent_access_token(component_access_token.getComponent_access_token());
//			jscode2session.setComponent_appid(WxConstants.COMPONENT_APPID);
//			jscode2session.setGrant_type("authorization_code");
//			jscode2session.setJs_code(CommonUtil.toString(params.get("js_code")));
//			Jscode2sessionResult jscode2sessionResult =WxAppletAPI.jscode2session(jscode2session);
//			map.put("status","success");
//			map.put("openid",jscode2sessionResult.getOpenid());
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("status","error");
//		} finally {
//			out.write(JSON.toJSONString(map).toString());
//			if (out != null) {
//				out.close();
//			}
//		}
//	}


	/**
	 * 小程序获取openid
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/79B4DE7C/login")
	public void test(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> params) {
		PrintWriter out = null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			out = response.getWriter();
			JSONObject jsonObject = HttpClienUtil.httpGet(params.get("url").toString());
			map.put("status","success");
			map.put("openid",jsonObject.get("openid").toString());
		} catch (Exception e) {
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
