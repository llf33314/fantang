package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 购票统计（根据条件搜索）
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "selectBuyTicketVo", description = "购票统计（根据条件搜索）")
@Data
public class SelectBuyTicketVo {

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @ApiModelProperty(name = "stime", value = "开始时间")
    private Date stime;

    @ApiModelProperty(name = "etime", value = "结束时间")
    private Date etime;

    @NotNull(message = "购票方式不能为空")
    @ApiModelProperty(name = "buyType", value = "购票方式（-1全部 0线上 1线下）", required = true)
    private Integer buyType;

    @ApiModelProperty(name = "depId", value = "部门ID")
    private Integer depId;

    @NotNull(message = "订单状态不能为空")
    @ApiModelProperty(name = "status", value = "订单状态（-1全部 0未选餐 1已预订 2取消订单  4已取餐 5订单过期 ）", required = true)
    private Integer status;

    @ApiModelProperty(name = "cardCode", value = "饭票卡号")
    private String cardCode;

}
