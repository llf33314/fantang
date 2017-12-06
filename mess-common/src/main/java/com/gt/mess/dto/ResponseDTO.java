package com.gt.mess.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gt.mess.enums.ResponseEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 服务响应类
 * <pre>
 *     统一响应返回数据格式
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/16
 */
//保证序列化json的时候,如果是null的对象,key也会消失
@ApiModel
@Getter
@ToString
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ResponseDTO< T > implements Serializable {

    /*状态码*/
    @ApiModelProperty( value = "状态码", name = "状态码" )
    private Integer code;

    /*返回消息*/
    @ApiModelProperty( value = "状态码描述", name = "状态码描述" )
    private String msg;

    /*泛型数据*/
    @ApiModelProperty( value = "数据对象", name = "数据对象" )
    private T data;

    protected ResponseDTO(int code) {
        this.code = code;
    }

    protected ResponseDTO(int code, T data) {
        this.code = code;
        this.data = data;
    }

    protected ResponseDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected ResponseDTO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 创建响应成功
     *
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createBySuccess() {
        return createBySuccessMessage(ResponseEnums.SUCCESS.getDesc());
    }

    /**
     * 创建响应成功
     *
     * @param data 数据包
     *
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createBySuccess(T data) {
        return createBySuccess(null, data);
    }

    /**
     * 创建响应成功
     *
     * @param msg 返回消息
     *
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createBySuccessMessage(String msg) {
        return createBySuccess(msg, null);
    }

    /**
     * 创建响应成功
     * @param msg  消息
     * @param data 数据包
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createBySuccess(String msg, T data) {
        return createBySuccessCodeMessage(ResponseEnums.SUCCESS.getCode(), msg, data);
    }

    /**
     * 创建响应成功
     * @param code 状态码
     * @param msg  消息
     * @param data 数据包
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createBySuccessCodeMessage(int code, String msg, T data) {
        return new ResponseDTO<>(code, msg, data);
    }

    /**
     * 创建响应失败
     *
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createByError() {
        return createByErrorCodeMessage(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
    }

    /**
     * 创建响应失败
     *
     * @param errorMessage 消息
     *
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createByErrorMessage(String errorMessage) {
        return createByErrorCodeMessage(ResponseEnums.ERROR.getCode(), errorMessage);
    }

    /**
     * 创建响应失败
     *
     * @param errorCode    状态码
     * @param errorMessage 消息
     *
     * @return ResponseDTO
     */
    public static < T > ResponseDTO< T > createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ResponseDTO<>(errorCode, errorMessage);
    }

    //使之不在json序列化结果当中，作用用于判断
    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResponseEnums.SUCCESS.getCode();
    }

   /* public int getCode() {
	return code;
    }

    public T getData() {
	return data;
    }

    public String getMsg() {
	return msg;
    }*/

}
