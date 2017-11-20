package com.gt.mess.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.mess.exception.BaseException;
import com.gt.mess.properties.WxmpApiProperties;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.wx.QrcodeCreateFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * wxmp项目接口工具类
 * @author zengwx
 * @date 2017年7月31日
 * @version 1.0
 * @remark wxmp多存临时接口
 */
@Component
public class WxmpUtil {

	private static final Logger log = LoggerFactory.getLogger(WxmpUtil.class);
	
	public static String SESSION_MEMBER = "member";

	@Autowired
	private WxmpApiProperties wxmpApiProperties;

	@Autowired
	private RedisCacheUtil redisCacheUtil;

	/**
	 * 要卖商家ID or 门店ID查询门店信息
	 * @param id
	 * @param isShop
	 * @return
	 * @throws Exception
	 */
	public JSONArray getShops(Integer id, boolean isShop) throws Exception {
		log.info("查询门店信息:" + id);
		JSONObject params = new JSONObject();
		params.put("reqdata", id);
		String result = null;
		if (!isShop) {
			result = HttpClienUtils.reqPostUTF8(params.toJSONString(), wxmpApiProperties.getHomeUrl() + "8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/queryWxShopByBusId.do", String.class, wxmpApiProperties.getWxmpKey());
//			result = SignHttpUtils.postByHttp(wxmpApiProperties.getHomeUrl() + "8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/queryWxShopByBusId.do", params, wxmpApiProperties.getWxmpKey());
		} else {
			result = HttpClienUtils.reqPostUTF8(params.toJSONString(), wxmpApiProperties.getHomeUrl() + "8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/getShopPhotoByShopId.do", String.class, wxmpApiProperties.getWxmpKey());
//			result = SignHttpUtils.postByHttp(wxmpApiProperties.getHomeUrl() + "8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/getShopPhotoByShopId.do", params, wxmpApiProperties.getWxmpKey());
		}
		JSONObject json = JSON.parseObject(result);
		if (json.getInteger("code") != 0) {
			throw new BaseException("获取门店信息失败");
		}
		return json.getJSONArray("data");
		
	}

	/**
	 * 微信授权
	 * @param request
	 * @param busId
	 * @param requestUrl
	 * @param uclogin
	 * @return
	 * @throws Exception
	 */
	public String authorizeMember(HttpServletRequest request,Integer busId,String requestUrl,Object uclogin) throws Exception {
     /*   map.put("busId", 33);
        map.put("requestUrl", "http://shuzheng.tunnel.qydev.com/login");*/
		log.debug("进入--授权方法！");
//		Integer busId = Integer.valueOf(map.get("busId").toString());
		log.debug("busId:{}",busId);
		Integer browser = CommonUtil.judgeBrowser(request);
//		Object uclogin = map.get("uclogin");//参数uclogin 如果uclogin不为空值  是指微信端是要通过授权  其他浏览器不需要授权   反之其他浏览器需要登录
		Map<String, Object> getWxPublicMap = new HashMap<>();
		getWxPublicMap.put("busId", busId);
		//判断商家信息 1是否过期 2公众号是否变更过
		String url = wxmpApiProperties.getHomeUrl() + "/8A5DA52E/busUserApi/getWxPulbicMsg.do";
		String wxpublic = SignHttpUtils.WxmppostByHttp(url, getWxPublicMap, wxmpApiProperties.getWxmpKey());//路径和签名待定
		JSONObject json = JSONObject.parseObject(wxpublic);
		Integer code = Integer.parseInt(json.get("code").toString());
		if (code.equals(-1)) {
			return "";//请求错误
		} else if (code.equals(0)) {
			Object guoqi = json.get("guoqi");
			if (!StringUtils.isEmpty(guoqi)) {//商家已过期
				Object guoqiUrl = json.get("guoqiUrl");
				return "redirect:" + guoqiUrl;
			}
			Object remoteUcLogin = json.get("remoteUcLogin");
			if (!StringUtils.isEmpty(uclogin) || !StringUtils.isEmpty(remoteUcLogin)) return "";
		}
//		String requestUrl = map.get("requestUrl").toString();
		String otherRedisKey = CommonUtil.getCode();

		//redis公用要调wxmp接口
		Map< String,Object > redisMap = new HashMap<>();
		redisMap.put( "redisKey", wxmpApiProperties.getRedisName() + otherRedisKey );
		redisMap.put( "redisValue", requestUrl );
		redisMap.put( "setime", 300 );
		SignHttpUtils.WxmppostByHttp( wxmpApiProperties.getHomeUrl()+"/8A5DA52E/redis/SetExApi.do", redisMap, wxmpApiProperties.getWxmpKey() );
//		if(!redisCacheUtil.set(redisNamePrefix + otherRedisKey,requestUrl,5*60l)){
//			log.error("redis存入出错");
//			throw new ShopsException("系统错误");
//		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("otherRedisKey", wxmpApiProperties.getRedisName() + otherRedisKey);
		queryMap.put("browser", browser);
		queryMap.put("busId", busId);
		queryMap.put("uclogin", uclogin);
		log.info("queryMap=" + JSON.toJSONString(queryMap));
		String params = URLEncoder.encode(JSON.toJSONString(queryMap), "utf-8");
		log.info("params=" + params);
		return "redirect:" + wxmpApiProperties.getHomeUrl() + "/remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + params;
	}

	/**
	 * 微信退款
	 * @param wxmemberPayRefund
	 * @return
	 * @throws Exception
	 */
	public JSONObject wxmemberPayRefund(WxmemberPayRefund wxmemberPayRefund) throws Exception {
		JSONObject json = new JSONObject();
		json.put("reqdata",wxmemberPayRefund);
		String result = HttpClienUtils.reqPostUTF8(JSONObject.toJSONString(json),
				wxmpApiProperties.getHomeUrl()+"8A5DA52E/payApi/6F6D9AD2/79B4DE7C/wxmemberPayRefund.do",
				String.class,wxmpApiProperties.getWxmpKey());
		log.info("wxmpUtil.wxmemberPayRefund.result:{}",result);
		JSONObject jsonObject = JSONObject.parseObject(result);
		return jsonObject;
	}

	/**
	 * 根据商家ID获取公众号关注二维码
	 * @param busId
	 * @return
	 * @throws Exception
	 */
	public JSONObject newqrcodeCreateFinal(Integer busId) throws Exception {
		JSONObject appidJson = this.getWxPublic(busId);
		QrcodeCreateFinal qrcodeCreateFinal = new QrcodeCreateFinal();
		qrcodeCreateFinal.setPublicId(Integer.valueOf(appidJson.get("id").toString()));
		qrcodeCreateFinal.setExternalId(0);
		qrcodeCreateFinal.setModel(12);//源于字典J001
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("reqdata", qrcodeCreateFinal);
		String result = HttpClienUtils.reqPostUTF8(JSONObject.toJSONString(jsonMap),wxmpApiProperties.getHomeUrl()+"8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/newqrcodeCreateFinal.do",
				String.class,wxmpApiProperties.getWxmpKey());
		return JSONObject.parseObject(result);
	}

	/**
	 * 查询公众号信息
	 * @param busId
	 * @return
	 * @throws Exception
	 */
	public JSONObject getWxPublic(Integer busId) throws Exception{
		JSONObject json = new JSONObject();
		json.put("reqdata",busId);
		String result = HttpClienUtils.reqPostUTF8(JSONObject.toJSONString(json),
				wxmpApiProperties.getHomeUrl()+"8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/selectByUserId.do",
				String.class,wxmpApiProperties.getWxmpKey());
		JSONObject jsonObject = JSONObject.parseObject(result);
		return JSONObject.parseObject(jsonObject.get("data").toString());
	}

	/**
	 * 获取字典数据
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public JSONObject getDictApi(JSONObject jsonObject) throws Exception{
		String result = SignHttpUtils.WxmppostByHttp(
				wxmpApiProperties.getHomeUrl()+"/8A5DA52E/dictApi/getDictApi.do",jsonObject,
				wxmpApiProperties.getWxmpKey());
		return JSONObject.parseObject(result);
	}


	/**
	 * 根据子账号ID获取主账号商家信息
	 * @param busId
	 * @return
	 * @throws Exception
	 */
	public BusUser getMainBusId(Integer busId) throws Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId",busId);
		String result = SignHttpUtils.WxmppostByHttp(
				wxmpApiProperties.getHomeUrl()+"/8A5DA52E/childBusUserApi/getMainBusId.do",jsonObject,
				wxmpApiProperties.getWxmpKey());
		return  JSONObject.toJavaObject(JSONObject.parseObject(result),BusUser.class);
	}

	/**
	 * 根据商家ID获取商家信息
	 * @param busId
	 * @return
	 * @throws Exception
	 */
	public BusUser getBusUserApi(Integer busId) throws Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId",busId);
		String result = SignHttpUtils.WxmppostByHttp(
				wxmpApiProperties.getHomeUrl()+"/8A5DA52E/busUserApi/getBusUserApi.do",jsonObject,
				wxmpApiProperties.getWxmpKey());
		return  JSONObject.toJavaObject(JSONObject.parseObject(result),BusUser.class);
	}

}
