package com.gt.mess.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gt.mess.enums.ResponseEnums;

import java.io.Serializable;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing.DEFAULT_TYPING;

/**
 * Json 异常处理
 *
 * @author zengwx
 * @create 2017/6/21
 */
@JsonSerialize( typing = DEFAULT_TYPING )
public class ErrorDTO< T > extends ResponseDTO< T > implements Serializable {

    private String url;

    private ErrorDTO(int code, String msg, T data, String url) {
	super(code, msg, data);
	this.url = url;
    }

    public static < T > ErrorDTO< T > createByError() {
	return createByErrorCodeMessage(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
    }

    public static < T > ErrorDTO< T > createByErrorCodeMessage(int errorCode, String errorMessage) {
	return createByErrorCodeMessage(errorCode, errorMessage, null);
    }

    public static < T > ErrorDTO< T > createByErrorCodeMessage(int errorCode, String errorMessage, String url) {
	return createByErrorCodeMessage(errorCode, errorMessage, null, url);
    }

    public static < T > ErrorDTO< T > createByErrorCodeMessage(int errorCode, String errorMessage, T data, String url) {
	return new ErrorDTO<>(errorCode, errorMessage, data, url);
    }

    public String getUrl() {
	return url;
    }

    public static void main(String[] args) {
	ErrorDTO< Object > error = ErrorDTO.createByErrorCodeMessage(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
	System.out.println(" url " + error.getUrl());
	System.out.println(JSONObject.toJSON(error));
    }

}
