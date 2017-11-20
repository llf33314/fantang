package com.gt.mess.exception;

import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.enums.ResponsePrinterEnums;

/**
 * Ajax 异常
 * <pre>
 *     ajax 请求的异常处理类
 * </pre>
 *
 * @author zengwx
 * @create 2017/6/21
 */
public class ResponseEntityException extends BaseException {

    public ResponseEntityException(String desc) {
	super(desc);
    }

    public ResponseEntityException(int code, String desc) {
	super(code, desc);
    }

    public ResponseEntityException(ResponseEnums responseEnums) {
	super(responseEnums);
    }

    public ResponseEntityException(ResponsePrinterEnums responseEnums) {
	super(responseEnums);
    }
}
