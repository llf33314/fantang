package com.gt.mess.exception;

import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.enums.ResponsePrinterEnums;

/**
 * 业务异常
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/08/03
 * @since 1.0.0
 */
public class BusinessException extends BaseException {

    public BusinessException(String desc) {
	super(desc);
    }

    public BusinessException(int code, String desc) {
	super(code, desc);
    }

    public BusinessException(ResponseEnums responseEnums) {
	super(responseEnums);
    }

    public BusinessException(ResponsePrinterEnums responseEnums) {
	super(responseEnums);
    }
}
