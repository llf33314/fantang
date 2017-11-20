package com.gt.mess.exception;

import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.enums.ResponsePrinterEnums;
import lombok.Getter;

/**
 * 系统统一异常类
 * <pre>
 *     所有自定义的异常，都继承此类。
 * </pre>
 *
 * @author zengwx
 * @create 2017/6/16
 */
@Getter
public class BaseException extends RuntimeException {

    //状态码
    private int    code;
    //错误消息
    private String desc;

    public BaseException(String desc) {
	super(desc);
	this.code = ResponseEnums.ERROR.getCode();
	this.desc = desc;
    }

    public BaseException(int code, String desc) {
	super(desc);
	this.code = code;
	this.desc = desc;
    }

    public BaseException(ResponseEnums responseEnums) {
	super(responseEnums.getDesc());
	this.code = responseEnums.getCode();
	this.desc = responseEnums.getDesc();
    }

    public BaseException(ResponsePrinterEnums responseEnums) {
	super(responseEnums.getDesc());
	this.code = responseEnums.getCode();
	this.desc = responseEnums.getDesc();
    }

}
