package com.gt.mess.interceptors;

import com.gt.mess.base.BaseInterceptor;

/**
 * 登录拦截器
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/08/11
 */
public class LoginInterceptors extends BaseInterceptor {
    /**
     * 进入Controller方法之前 判断用户是否已登录
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  Object
     *
     * @return boolean
     * @throws Exception
     */
    /*@Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
	// 1. TODO： 目前 管理员和员工账号存在问题：如果每次
	Object loginStyle = request.getSession().getAttribute( CommonConst.SESSION_LOGIN_STYLE );
//	if(loginStyle){
//
//	}
	// 2. 从共享的 session 中获取商家信息(JSON 数据包)
	Object o = request.getSession().getAttribute( CommonConst.CURRENT_BUS_USER );
	if ( o == null ) {
	    logger.error( "无法获取Session中的用户信息：{}", o );
	    throw new BaseException( ResponseEnums.NEED_LOGIN );
	}

	return super.preHandle( request, response, handler );
    }*/
}
