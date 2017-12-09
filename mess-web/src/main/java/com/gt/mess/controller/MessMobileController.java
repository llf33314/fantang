package com.gt.mess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.*;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.*;
import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.exception.BaseException;
import com.gt.mess.exception.ResponseEntityException;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.mess.service.*;
import com.gt.mess.util.*;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 食堂手机端
 * @author ZengWenXiang
 * @QQ 307848200
 */
@Api(description = "食堂手机端",hidden = true)
@RestController
@RequestMapping(value = "messMobile")
public class MessMobileController {

	private Logger logger = Logger.getLogger(MessMobileController.class);

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
	private MessDepartmentMapper messDepartmentMapper;

	@Autowired
	private MessCardGroupService messCardGroupService;

	@Autowired
	private WxmpApiProperties wxmpApiProperties;

	@Autowired
	private RedisCacheUtil redisCacheUtil;


	/**
	 * 微食堂支付完成跳转
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 微食堂支付完成跳转",op_function="3")//保存2，修改3，删除4
	@RequestMapping("/79B4DE7C/wxMessPayOrder")
	public ResponseDTO wxMessPayOrder(HttpServletRequest request,HttpServletResponse response,
									  @RequestParam Map<String, Object> params){
		MessTopUpOrder messTopUpOrder =
				messTopUpOrderService.getMessTopUpOrderByOrderNo(params.get("orderNo").toString());
		MessMain messMain = messMainService.getMessMainById(messTopUpOrder.getMainId());
		if(messTopUpOrder.getStatus() == 1){
			MessConsumerDetail messConsumerDetail =
					messConsumerDetailService.getMessConsumerDetailById(Integer.valueOf(params.get("detailId").toString()));
			MessCard messCard = messCardService.getMessCardById(messTopUpOrder.getCardId());
			messCard.setMoney(messCard.getMoney() + messTopUpOrder.getMoney());

			messTopUpOrder.setStatus(0);
			messConsumerDetail.setStatus(0);
			int data = 0;
			try {
				data = messTopUpOrderService.update(messTopUpOrder);
				data = messConsumerDetailService.update(messConsumerDetail);
				data = messCardService.update(messCard);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject jsonData = new JSONObject();
			jsonData.put("mainId",messMain.getId());
			jsonData.put("busId",messMain.getBusId());
			if(0 != data)
				return ResponseDTO.createBySuccess(jsonData);
			else
				return ResponseDTO.createByError();
		}else{
			return ResponseDTO.createByError();
		}
	}

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
//				redisCacheUtil.set(redisKey, url, 300);
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
//					redisCacheUtil.set(redisKey, url, 300);
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

	/**
	 * 根据mainId获取主表信息
	 * @param mainId
	 * @return
	 */
	@RequestMapping("/{mainId}/79B4DE7C/getMessMainById")
	public ResponseDTO getMessMainById(@PathVariable Integer mainId){
		MessMain messMain =
				messMainService.getMessMainById(mainId);
		return ResponseDTO.createBySuccess(messMain);
	}

	/**
	 * 根据busId获取主表信息
	 * @param busId
	 * @return
	 */
	@RequestMapping("/{busId}/79B4DE7C/getMessMainByBusId")
	public ResponseDTO getMessMainByBusId(@PathVariable Integer busId){
		MessMain messMain =
				messMainService.getMessMainByBusId(busId);
		return ResponseDTO.createBySuccess(messMain);
	}

	/**
	 * 根据cardId获取饭卡信息
	 * @param cardId
	 * @return
	 */
	@RequestMapping("/{cardId}/79B4DE7C/getMessCardById")
	public ResponseDTO getMessCardById(@PathVariable Integer cardId){
		MessCard messCard =
				messCardService.getMessCardById(cardId);
		return ResponseDTO.createBySuccess(messCard);
	}

	/**
	 * 菜单首页
	 * @param mainId
	 * @return
	 */
	@RequestMapping("/{mainId}/{memberId}/79B4DE7C/index")
	public ResponseDTO index(@PathVariable Integer mainId, @PathVariable Integer memberId){
		JSONObject data = new JSONObject();
		MessMain messMain =
				messMainService.getMessMainById(mainId);
		Map<String,Integer> mapId = new HashMap<String, Integer>();
		mapId.put("memberId", memberId);
		mapId.put("mainId", mainId);
		MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
		data.put("mainId", mainId);
//		data.put("member", member);
		data.put("busId", messMain.getBusId());
		MessBasisSet messBasisSet = null;
		if(messCard == null){
			return ResponseDTO.createByErrorMessage("饭卡信息为空");
		}else{
			messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
			data.put("messNotices", messNotices);
			data.put("messBasisSet", messBasisSet);
		}
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
				data.put("mealCode", messMealOrder.getMealCode());
			}else {
				data.put("mealCode", -1);
				data.put("msg", "现时间段无订餐！");
			}
		}else{
			data.put("mealCode", -1);
			data.put("msg", "现时间段无订餐！");
		}
		data.put("messCard", messCard);
		data.put("nums", nums);
		data.put("nums2", nums2);
		data.put("cardId", messCard.getId());
		return ResponseDTO.createBySuccess(data);
	}

	/**
	 * 取餐码
	 * @param response
	 */
	@RequestMapping(value = "{mainId}/{cardId}/{mealCode}/79B4DE7C/getMessUrltoQRcode")
	public ResponseDTO getMessUrltoQRcode(HttpServletResponse response,
								   @PathVariable Integer mainId,@PathVariable Integer cardId,@PathVariable String mealCode) {
		try {
			MessMain messMain = messMainService.getMessMainById(mainId);
			String filePath =wxmpApiProperties.getAdminUrl();
			return ResponseDTO.createBySuccess(filePath+"messReception/"+messMain.getBusId()+"/"+cardId+"/"+ mealCode +"/79B4DE7C/wirteOff.do");
//			QRcodeKit.buildQRcode(filePath+"messReception/"+messMain.getBusId()+"/"+cardId+"/"+ mealCode +"/79B4DE7C/wirteOff.do", 300, 300, response);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 取餐码验证
	 * @param cardId
	 * @param mealCode
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 取餐码验证",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "/{cardId}/{mealCode}/79B4DE7C/verify")
	public ResponseDTO verify(@PathVariable Integer cardId,@PathVariable String mealCode) {
		try {
			return ResponseDTO.createBySuccess(messCardService.verify(cardId,mealCode));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 商家订餐统计
	 * @param mainId
	 * @return
	 */
	@RequestMapping("{mainId}/79B4DE7C/statistics")
	public ResponseDTO statistics(@PathVariable Integer mainId){
		JSONObject data = new JSONObject();
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
		data.put("messNotices", messNotices);
		data.put("breakfastNum", breakfastNum);
		data.put("lunchNum", lunchNum);
		data.put("dinnerNum", dinnerNum);
		data.put("nightNum", nightNum);

		data.put("breakfastMealNum", breakfastMealNum);
		data.put("lunchMealNum", lunchMealNum);
		data.put("dinnerMealNum", dinnerMealNum);
		data.put("nightMealNum", nightMealNum);

		data.put("bListMaps", bListMaps);
		data.put("lListMaps", lListMaps);
		data.put("dListMaps", dListMaps);
		data.put("nListMaps", nListMaps);

		data.put("nums", breakfastNum+lunchNum+dinnerNum+nightNum);
		data.put("mainId", mainId);
		return ResponseDTO.createBySuccess(data);
	}


	/**
	 * 绑定饭卡
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 绑定饭卡",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "/79B4DE7C/bindingCard")
	public ResponseDTO bindingCard(@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messCardService.bindingCard(params));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 加菜完成后跳转
	 * @param mainId
	 * @param fdId
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/79B4DE7C/addFood")
	public ResponseDTO addFoodL(@RequestParam Integer mainId,
								@RequestParam Integer fdId,
								@RequestParam Integer memberId){
		JSONObject data = new JSONObject();
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("memberId", memberId);
			mapId.put("mainId", mainId);
			MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
			if(messCard == null){
				ResponseDTO.createByErrorMessage("饭卡信息为空");
			}else{
				MessAddFood messAddFood = messAddFoodService.getMessAddFoodById(fdId);
				data.put("money", messAddFood.getPrice());
				data.put("mainId", mainId);
				data.put("cardId", messCard.getId());
				if("1".equals(redisCacheUtil.get("mess:"+memberId))){
					data.put("data", 1);
					return ResponseDTO.createBySuccess(data);
				}else{
					redisCacheUtil.set("mess:"+memberId, "1",60L);
				}
				data.put("data", messAddFoodService.addFood(messCard, fdId));
//                redisCacheUtil.remove("mess:"+memberId);
			}
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}

	}

//	购买饭票模块

	/**
	 * 立即购买（购买饭票）
	 * @param cardId
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/buyNow")
	public ResponseDTO buyNow(@PathVariable Integer cardId){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
			MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
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
			data.put("messBasisSet", messBasisSet);
			data.put("messCard", messCard);
			data.put("mainId", messMain.getId());
			data.put("cardId", messCard.getId());
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 购买饭票
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 购买饭票",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/buyTicket")
	public ResponseDTO buyTicket(@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messBuyTicketOrderService.buyTicket(params));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

//	我的模块

	/**
	 * 我的订餐明细
	 * @param cardId
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/myMealOrderDetail")
	public ResponseDTO myMealOrderDetail(@PathVariable Integer cardId){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
			MessMain messMain = messMainService.getMessMainById(messCard.getMainId());
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(), 10);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messMain.getId());
			System.out.println(messBasisSet.getBitUniversal());
			data.put("messCard", messCard);
			data.put("messNotices", messNotices);
			data.put("messBasisSet", messBasisSet);
			data.put("mainId", messMain.getId());
			data.put("cardId", messCard.getId());
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 明细列表
	 * @param mainId
	 * @param cardId
	 * @param page
	 * @return
	 */
	@RequestMapping("{mainId}/{cardId}/79B4DE7C/detailList")
	public ResponseDTO detailList(@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessMealOrder> page){
		JSONObject data = new JSONObject();
		try {
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
			data.put("messMealOrders", messMealOrders);
			data.put("mainId", mainId);
			data.put("cardId", cardId);
			String datetime = DateTimeKit.format(new Date(), DateTimeKit.DEFAULT_DATE_FORMAT);
			datetime += " 00:00:00";
			Date nowDate = DateTimeKit.parse(datetime, DateTimeKit.DEFAULT_DATETIME_FORMAT);
			data.put("now", datetime);
			data.put("nowDate", nowDate);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 明细列表（加载）
	 * @param mainId
	 * @param cardId
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "{mainId}/{cardId}/79B4DE7C/loadDetailList")
	public ResponseDTO loadDetailList(@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessMealOrder> page) {
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			Page<MessMealOrder> messMealOrders =
					messMealOrderService.getMessMealOrderPageByCardIdAndMainId(page,mapId,10);
			if(messMealOrders.getCurrent() < page.getCurrent()){
				return ResponseDTO.createBySuccess("-1");
			}
			if(messMealOrders != null){
				return ResponseDTO.createBySuccess(JSON.toJSONString(messMealOrders).toString());
			}else{
				return ResponseDTO.createByError();
			}
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 取消订单
	 * @param orderId
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 取消订单",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "/79B4DE7C/cancelOrder")
	public ResponseDTO cancelOrder(@RequestParam Integer orderId) {
		try {
			return ResponseDTO.createBySuccess(messMealOrderService.cancelOrder(orderId));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 订单列表(消费明细)
	 * @param mainId
	 * @param cardId
	 * @param page
	 * @return
	 */
	@RequestMapping("{mainId}/{cardId}/79B4DE7C/orderList")
	public ResponseDTO orderList(@PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessConsumerDetail> page){
		JSONObject data = new JSONObject();
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			Page<MessConsumerDetail> messConsumerDetails =
					messConsumerDetailService.getMessConsumerDetailPageByCardIdAndMainId(page, mapId, 10);
			data.put("messConsumerDetails", messConsumerDetails);
			data.put("mainId",mainId);
			data.put("cardId", cardId);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 订单列表加载(消费明细加载)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{mainId}/{cardId}/79B4DE7C/loadOrderList")
	public ResponseDTO loadOrderList(HttpServletRequest request,HttpServletResponse response,
									 @PathVariable Integer mainId,@PathVariable Integer cardId,Page<MessConsumerDetail> page){
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("cardId", cardId);
			mapId.put("mainId", mainId);
			Page<MessConsumerDetail> messConsumerDetails =
					messConsumerDetailService.getMessConsumerDetailPageByCardIdAndMainId(page, mapId, 10);
			if(messConsumerDetails.getCurrent() < page.getCurrent()){
				return ResponseDTO.createBySuccess("-1");
			}
			if(messConsumerDetails != null && messConsumerDetails.getCurrent() > 0){
				return ResponseDTO.createBySuccess(messConsumerDetails.getRecords());
			}else{
				return ResponseDTO.createByError();
			}
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

//	每周菜单模块

	/**
	 * 订餐主页(每周菜单)
	 * @param cardId
	 * @param week
	 * @param type
	 * @return
	 */
	@RequestMapping("{cardId}/{week}/{type}/79B4DE7C/mealOrder")
	public ResponseDTO mealOrder(@PathVariable Integer cardId,@PathVariable Integer week,
								 @PathVariable Integer type){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("week", week);
			map.put("type", type);
			map.put("mainId", mainId);
			List<MessMenus> messMenus =
					messMenusService.getMessMenusListByTypeAndWeekNumforMainId(map);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			data.put("messNotices", messNotices);
			data.put("week", week);
			data.put("mainId", mainId);
			data.put("messCard", messCard);
			data.put("cardId", messCard.getId());
			data.put("messMenus", messMenus);
			data.put("messBasisSet", messBasisSet);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

//	订餐模块

	/**
	 * 订餐
	 * @param cardId
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/calendar")
	public ResponseDTO calendar(@PathVariable Integer cardId){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
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
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messCard.getMainId());
			StringBuffer manageBuffer = new StringBuffer();
			for (int i = 0; i < messBasisSet.getBookDay(); i++) {
				Date date = DateTimeKit.addDate(new Date(), i);;
				manageBuffer.append(time("yyyy-MM-dd",date.toString())+",");
			}
			//不可订餐
			if(messOrderManages.size() == 1){
				manageBuffer.append(messOrderManages.get(0).getDay().toString()+",");
			}else{
				for(MessOrderManage  messOrderManage: messOrderManages){
					if(messOrderManage.getDay() != "")
						manageBuffer.append(messOrderManage.getDay().toString()+",");
				}
			}
			StringBuffer mealOrderBuffer = new StringBuffer();
			StringBuffer notChooseMealOrderBuffer = new StringBuffer();
			//已预订
			if(bookMealOrders.size() == 1){
				mealOrderBuffer.append(DateTimeKit.getDateTime(bookMealOrders.get(0).getTime(), "yyyy-M-d")+",");
			}else{
				for(MessMealOrder  messMealOrder: bookMealOrders){
					if(!mealOrderBuffer.toString().contains(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")))
						mealOrderBuffer.append(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")+",");
				}
			}
			//未选餐
			if(notChooseMealOrders.size() == 1){
				notChooseMealOrderBuffer.append(DateTimeKit.getDateTime(notChooseMealOrders.get(0).getTime(), "yyyy-M-d")+",");
			}else{
				for(MessMealOrder  messMealOrder: notChooseMealOrders){
					if(!notChooseMealOrderBuffer.toString().contains(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")))
						notChooseMealOrderBuffer.append(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d")+",");
				}
			}


			data.put("messBasisSet", messBasisSet);
			data.put("mainId", messCard.getMainId());
			data.put("messCard", messCard);
			data.put("cardId", cardId);
			//不可订餐
			if(messOrderManages.size() > 0){
				data.put("manageBuffer", manageBuffer.substring(0, manageBuffer.length() -1));
			}else{
				if(manageBuffer.length() > 0){
					data.put("manageBuffer", manageBuffer.substring(0, manageBuffer.length() -1));
				}else{
					data.put("manageBuffer", "");
				}
			}

			//已预订
			if(bookMealOrders.size() > 0){
				data.put("mealOrderBuffer", mealOrderBuffer.substring(0, mealOrderBuffer.length() -1));
			}else{
				data.put("mealOrderBuffer", "");
			}
			//未选餐
			if(notChooseMealOrders.size() > 0){
				data.put("notChooseMealOrderBuffer", notChooseMealOrderBuffer.substring(0, notChooseMealOrderBuffer.length() -1));
			}else{
				data.put("notChooseMealOrderBuffer", "");
			}
			List<MessMealOrder> notMessMealOrders =
					messMealOrderService.getNotChooseMessMealOrder(mapId);
			if(notMessMealOrders.size() > 0){
				data.put("type", 0);
			}else{
				data.put("type", 1);
			}
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			data.put("messNotices", messNotices);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 追加订单(或取消订单列表)
	 * @param params
	 * @return
	 */
	@RequestMapping("/79B4DE7C/addOrder")
	public ResponseDTO addOrder(@RequestParam Map <String,Object> params){
		JSONObject data = new JSONObject();
		try {
			Integer cardId = Integer.valueOf(params.get("cardId").toString());
			MessCard messCard = messCardService.getMessCardById(cardId);
			params.put("mainId", messCard.getMainId());
			params.put("cardId", messCard.getId());
			List<MessMealOrder> messMealOrders =
					messMealOrderService.getBookMessMealOrderByToDay(params);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messCard.getId());
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			data.put("messNotices", messNotices);
			if(params.get("time").equals(DateTimeKit.getDateTime(new Date(), "yyyy-M-d"))){
				data.put("bitqx", 0);
			}else{
				data.put("bitqx", 1);
			}
			data.put("param", params);
			data.put("messBasisSet", messBasisSet);
			data.put("mainId", messCard.getId());
			data.put("cardId", cardId);
			data.put("messCard", messCard);
			data.put("messMealOrders", messMealOrders);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
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
	public ResponseDTO saveOrUpdateAddOrder(HttpServletRequest request, HttpServletResponse response,
											@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messMealOrderService.saveOrUpdateAddOrder(params));
		} catch (BaseException be){
			if("超过预定时间".equals(be.getMessage())){
				return ResponseDTO.createBySuccess(-2);
			}else {
				throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
			}
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 删除未选餐
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 删除未选餐",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/delNotCMealOrder")
	public ResponseDTO delNotCMealOrder(@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messMealOrderService.delNotCMealOrder(params));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 保存订餐
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 保存订餐",op_function="2")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/saveMealOrder")
	public ResponseDTO saveMealOrder(@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messMealOrderService.saveMealOrder(params));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 选择早晚餐
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/chooseMeal")
	public ResponseDTO chooseMeal(HttpServletRequest request,HttpServletResponse response,
								  @PathVariable Integer cardId){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("mainId", messCard.getMainId());
			mapId.put("cardId", messCard.getId());
			List<MessMealOrder> messMealOrders =
					messMealOrderService.getNotChooseMessMealOrder(mapId);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(messCard.getMainId());
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(messCard.getMainId(),1);
			data.put("messNotices", messNotices);
			data.put("size", messMealOrders.size());
			data.put("messBasisSet", messBasisSet);
			data.put("mainId", messCard.getMainId());
			data.put("cardId", cardId);
			data.put("messCard", messCard);
			data.put("messMealOrders", messMealOrders);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 保存选餐（选择早晚餐）
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 保存订餐（选择早晚餐）",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "79B4DE7C/saveChooseMealOrder")
	public ResponseDTO saveChooseMealOrder(@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messMealOrderService.saveChooseMealOrder(params));
		} catch (BaseException be){
			if("超过预定时间".equals(be.getMessage())){
				return ResponseDTO.createBySuccess(-2);
			}else {
				throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
			}
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

//	余额充值模块

	/**
	 * 购买饭票显示(余额充值)
	 * @param cardId
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/buyTicketShow")
	public ResponseDTO buyTicketShow(@PathVariable Integer cardId){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard =
					messCardService.getMessCardById(cardId);
			data.put("messCard", messCard);
			data.put("mainId", messCard.getMainId());
			data.put("cardId", cardId);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 饭票余额显示
	 * @param cardId
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/ticketMoneyShow")
	public ResponseDTO ticketMoneyShow(@PathVariable Integer cardId){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
			data.put("messCard", messCard);
			data.put("mainId", messCard.getMainId());
			data.put("cardId", cardId);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 饭卡充值支付
	 * @param params
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 饭卡充值支付",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "/79B4DE7C/topUpPay")
	public ResponseDTO topUpPay(@RequestParam Map <String,Object> params) {
		try {
			return ResponseDTO.createBySuccess(messTopUpOrderService.topUpPay(params));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 饭票授权手机端
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/79B4DE7C/authority")
	public ResponseDTO authority(@RequestParam Map<String, Object> params)
			throws Exception {
		try {
			Integer mainId = Integer.valueOf(params.get("mainId").toString());
			MessMain messMain = messMainService.getMessMainById(mainId);
			if(CommonUtil.isEmpty(params.get("sign"))){
				return ResponseDTO.createByErrorMessage("该授权码有误！");
			}else{
				String sign = params.get("sign").toString();
				if(!sign.equals(messMain.getAuthoritySign())){
					return ResponseDTO.createByErrorMessage("该授权码已失效！");
				}
			}
			if(CommonUtil.isEmpty(params.get("ticketCode"))){
				return ResponseDTO.createByErrorMessage("该授权码有误！");
			}else{
				String decode = URLDecoder.decode(params.get("ticketCode").toString(), "UTF-8");
				String decrypt_ticket_id = EncryptUtil.decrypt(mainId + "CFCCBD66B12B62E5256FAA90A931A01F", decode);
				if(mainId.intValue()!=Integer.parseInt(decrypt_ticket_id)){
					return ResponseDTO.createByErrorMessage("该授权码有误！");
				}
			}
			Map<String, Object> autho = new HashMap<String, Object>();
			autho.put("mainId", messMain.getId());
			autho.put("memberId", params.get("memberId"));
			autho.put("delStatus", 0);
			List<MessAuthorityMember> tams = messAuthorityMemberMapper.getMessAuthorityMember(autho);
			if(tams!=null&&tams.size()>0){
				return ResponseDTO.createByErrorMessage("不能重复授权！");
			}
			params.put("memberId", params.get("memberId"));
			params.put("messMain", messMain);
			return ResponseDTO.createBySuccess(messAuthorityMemberService.saveAuthority(params));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

//	加菜模块

	/**
	 * 加餐
	 * @param cardId
	 * @param page
	 * @return
	 */
	@RequestMapping("{cardId}/79B4DE7C/addFood")
	public ResponseDTO addFood(@PathVariable Integer cardId,Page<MessAddFood> page){
		JSONObject data = new JSONObject();
		try {
			MessCard messCard = messCardService.getMessCardById(cardId);
			Integer mainId = messCard.getMainId();
			Page<MessAddFood> messAddFoods =
					messAddFoodService.getMessAddFoodPageByMainId(page, mainId, 100);
			MessBasisSet messBasisSet = messBasisSetService.getMessBasisSetByMainId(mainId);
			List<MessNotice> messNotices = messNoticeService.getMessNoticeListByMainId(mainId,1);
			data.put("mainId", mainId);
			data.put("messCard", messCard);
			data.put("messNotices", messNotices);
			data.put("messBasisSet", messBasisSet);
			data.put("messAddFoods", messAddFoods);
			return ResponseDTO.createBySuccess(data);
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 加餐核销(电脑核销)
	 * @param mainId
	 * @param fdId
	 * @param memberId
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 加餐核销(电脑核销)",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/addFoodCancel", method = RequestMethod.GET)
	public ResponseDTO addFoodCancel(@PathVariable Integer mainId,
									 @PathVariable Integer fdId,
									 @PathVariable Integer memberId) {
		try {
			Map<String,Integer> mapId = new HashMap<String, Integer>();
			mapId.put("memberId", memberId);
			mapId.put("mainId", mainId);
			MessCard messCard = messCardService.getMessCardByMainIdAndMemberId(mapId);
			if(messAddFoodService.addFood(messCard, fdId) == 1){
				MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
				return ResponseDTO.createBySuccess("加餐成功，金额" + messAddFood.getPrice() + "元");
			}else{
				return ResponseDTO.createByErrorMessage("加餐失败，金额不足");
			}
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 加餐核销(手机核销)
	 * @param mainId
	 * @param fdId
	 * @param memberId
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 加餐核销(手机核销)",op_function="3")//保存2，修改3，删除4
	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/addFoodCancel", method = RequestMethod.GET)
	public ResponseDTO addFoodCancelM(@PathVariable Integer mainId,@PathVariable Integer fdId,@PathVariable Integer memberId) {
		int data = 0;
		try {
			Map<String, Object> autho = new HashMap<String, Object>();
			autho.put("mainId", mainId);
			autho.put("memberId", memberId);
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
			if(data == 1){
				MessAddFood messAddFood = messAddFoodMapper.selectByPrimaryKey(fdId);
				return ResponseDTO.createBySuccess(messAddFood.getPrice()+"元加餐成功！");
			}else if(data == -1){
				return ResponseDTO.createByErrorMessage("您未被授权！");
			}else{
				return ResponseDTO.createByErrorMessage("加餐失败！");
			}
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
		}
	}

	/**
	 * 加菜码
	 * @param response
	 * @param mainId
	 * @param fdId
	 * @param memberId
	 */
	@RequestMapping(value = "{mainId}/{fdId}/{memberId}/79B4DE7C/getAddFoodQRcode")
	public ResponseDTO getAddFoodQRcode(HttpServletResponse response,
								 @PathVariable Integer mainId,
								 @PathVariable Integer fdId,
								 @PathVariable Integer memberId) {
		try {
			String filePath =wxmpApiProperties.getAdminUrl();
			return ResponseDTO.createBySuccess(filePath+"messMobile/"+ mainId +"/"+ fdId +"/"+ memberId +"/79B4DE7C/addFoodCancel.do");
			//QRcodeKit.buildQRcode(filePath+"messMobile/"+ mainId +"/"+ fdId +"/"+ memberId +"/79B4DE7C/addFoodCancel.do", 300, 300, response);
		} catch (Exception e) {
			throw new BaseException("获取失败");
		}
	}

	/**
	 * 删除订单
	 * @param mId
	 * @return
	 */
//	@SysLogAnnotation(description="微食堂 删除订单",op_function="4")//保存2，修改3，删除4
	@RequestMapping(value = "{mId}/79B4DE7C/delMealOrder")
	public ResponseDTO delMessCard(@PathVariable("mId") Integer mId) {
		try {
			return ResponseDTO.createBySuccess(messMealOrderService.delOrder(mId));
		} catch (Exception e) {
			throw new ResponseEntityException(ResponseEnums.SYSTEM_ERROR);
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
}
