package com.gt.mess.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.exception.SignException;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.mess.constant.CommonConst;
import com.gt.mess.exception.BusinessException;
import com.gt.mess.properties.WxmpApiProperties;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Web请求工具类
 *
 * @author zengwx
 * @create 2017/6/30
 */
public class WebUtil {

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE      = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static boolean isAjax(HttpServletRequest request) {
	return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    /**
     * 发送socket
     * @param pushName
     * @param message
     * @param pushStyle
     * @param wxmpApiProperties
     * @return
     * @throws SignException
     * @throws UnsupportedEncodingException
     */
    public static String sendSocket(String pushName,String message,String pushStyle,WxmpApiProperties wxmpApiProperties) throws SignException, UnsupportedEncodingException {
        Map<String,Object> params = new HashMap<>();
        if(pushStyle == null){
            params.put("pushStyle","");
        }else {
            params.put("pushStyle",pushStyle);
        }
        params.put("pushMsg", JSON.toJSONString(message).toString());
        params.put("pushName",pushName);
        String result = null;
        result = SignHttpUtils.WxmppostByHttp(wxmpApiProperties.getHomeUrl()+"/8A5DA52E/socket/getSocketApi.do", params, CommonConst.SIGNKEY);
        result = new String(result.getBytes("GBK"), "utf-8");
        return result;
    }

    /**
     * 根据商家ID获取公众号信息
     * @param busId
     * @param wxmpApiProperties
     * @return
     * @throws BusinessException
     */
    public static JSONObject getWxPublic(Integer busId,WxmpApiProperties wxmpApiProperties) throws BusinessException {
        JSONObject json = new JSONObject();
        json.put("reqdata",busId);
        String result = HttpClienUtils.reqPostUTF8(JSONObject.toJSONString(json),
                wxmpApiProperties.getHomeUrl()+"8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/selectByUserId.do",
                String.class, CommonConst.SIGNKEY);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return JSONObject.parseObject(jsonObject.get("data").toString());
    }


    //    public static boolean isAjax(SavedRequest request) {
    //        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    //    }
    //
    //    public static boolean isContentTypeJson(SavedRequest request) {
    //        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    //    }

}
