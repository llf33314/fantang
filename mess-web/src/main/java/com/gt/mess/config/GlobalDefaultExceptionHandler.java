package com.gt.mess.config;

import com.gt.mess.dto.ErrorDTO;
import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.exception.BaseException;
import com.gt.mess.exception.BusinessException;
import com.gt.mess.exception.ResponseEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常统一处理
 * <pre>
 *
 * </pre>
 *
 * @author zengwx
 * @create 2017/6/21
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    // 自定义异常处理
    @ResponseBody
    @ExceptionHandler( value = BaseException.class )
    public ErrorDTO< String > defaultErrorHandler(HttpServletRequest request, BaseException e) {
	logger.error("异常原因：{} , 异常信息：{} , 请求地址：{}", e.getCause(), e.getMessage(), request.getRequestURL(), e);
	if (e instanceof ResponseEntityException || e instanceof BusinessException) {
	    return ErrorDTO.createByErrorCodeMessage(e.getCode(), e.getMessage(), null);
	} else {
	    return ErrorDTO.createByErrorCodeMessage(ResponseEnums.ERROR.getCode(), e.getMessage(), null);
	}
    }

    // 统一异常处理
    @ResponseBody
    @ExceptionHandler( value = Exception.class )
    public ErrorDTO< String > defaultErrorHandler(Exception e) {
	logger.error("异常详细：", e);
	return ErrorDTO.createByErrorCodeMessage(ResponseEnums.SYSTEM_ERROR.getCode(), ResponseEnums.SYSTEM_ERROR.getDesc());
    }
}
