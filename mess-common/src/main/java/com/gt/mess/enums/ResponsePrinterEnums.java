package com.gt.mess.enums;

/**
 * 响应成功Code 类
 *
 * @author zengwx
 * @create 2017/6/12
 */
public enum ResponsePrinterEnums {
    PRINTER_CONFIGURATION_DOES_NOT_EXIST(2001, "打印机配置不存在"),
    PRINTER_CONFIGURATION_ALREADY_EXISTS(2002, "打印机配置已存在"),

    TEMPLATE_DOES_NOT_EXIST(2005, "模板不存在"),
    TEMPLATE_ALREADY_BOUND(2006, "当前模板已绑定打印机"),
    // 2999 作为占位符 不使用
    UN(2999, "111");

    private final int    code;
    private final String desc;

    ResponsePrinterEnums(int code, String desc) {
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
