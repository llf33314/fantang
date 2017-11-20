package com.gt.mess.base;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.mess.constant.CommonConst;
import com.gt.mess.entity.TCommonStaff;
import com.gt.mess.exception.ResponseEntityException;
import com.gt.mess.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * BaseController
 *
 * @author zengwx
 * @create 2017/7/10
 */
public abstract class BaseController {
    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger( BaseController.class );

    /**
     * 获取Sessionid
     *
     * @param session HttpSession
     *
     * @return
     */
    public String getSessionId( HttpSession session ) {
	return session.getId();
    }

    /**
     * 参数校验是否合法
     *
     * @param result BindingResult
     */
    protected void InvalidParameter( BindingResult result ) {
	if ( result.hasErrors() ) {
	    List< ObjectError > errorList = result.getAllErrors();
	    for ( ObjectError error : errorList ) {
		logger.warn( "{}", error.getDefaultMessage() );
		throw new ResponseEntityException( error.getDefaultMessage() );
	    }
	}
    }

    public BusUser getLoginUser(HttpServletRequest request) {
        try {
//            BusUser busUser = new BusUser();
//            busUser.setId(36);
//            busUser.setName("name");
//            return busUser;
            Object o = request.getSession().getAttribute(CommonConst.CURRENT_BUS_USER);
            if(o!=null){
                return JSONObject.parseObject(o.toString(), BusUser.class);
            }
        } catch (Exception e) {
            logger.error("Session获取BusUser失败。",e);
            return null;
        }
        return null;
    }

    public static TCommonStaff getLoginStaff(HttpServletRequest request) {
        try {
            Object Ob = request.getSession().getAttribute(CommonConst.CURRENT_TCOMMON_STAFF);
            JSONObject Json = JSONObject.parseObject(Ob.toString());
            if(Json != null){
                TCommonStaff obj = (TCommonStaff)  JSONObject.toJavaObject(Json,TCommonStaff.class);
                return  obj;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer  getLoginStyle(HttpServletRequest request){
        try {
            Object Ob = request.getSession().getAttribute(CommonConst.SESSION_LOGIN_STYLE);

            if(Ob != null){
                Integer loginStyle   =  CommonUtil.toInteger(Ob);
                return  loginStyle;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
