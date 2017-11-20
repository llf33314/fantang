package com.gt.mess.enums;

/**
 * 响应成功Code 类
 *
 * @author zengwx
 * @create 2017/6/12
 */
public enum ResponseEnums {
    // 两位数(<100)以内，作为通用的代码和描述
    SUCCESS(0, "成功"),
    ERROR(1, "未知错误"),
    PAY_ERROR(-1, "支付回调失败"),
    OPERATION_FAILURE(2, "操作失败"),
    NEED_LOGIN(10, "需要登录"),
    // 4000-4999 作为 数据内容的代码和描述
    DATA_NOT_FOUND(4001, "无此数据"),
    INVALID_ARGUMENT(4002, "非法参数"),
    PARAMETERS_ARE_MISSING(4003, "参数缺失"),
    // 3000-3999 作为 工具描述
    SYSTEM_IO_ERROR(3001, "文件传输出错"),
    // 未知异常||不确定的 统一只用 9999
    SYSTEM_ERROR( 9999, "系统错误" ),
    THRRE_API_ERROR( 5001, "第三方API出错" );

    private final int    code;
    private final String desc;

    ResponseEnums( int code, String desc ) {
	this.code = code;
	this.desc = desc;
    }

    public int getCode() {
	return code;
    }

    public String getDesc() {
	return desc;
    }
}
