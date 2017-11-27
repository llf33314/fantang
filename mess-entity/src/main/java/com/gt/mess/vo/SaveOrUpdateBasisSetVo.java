package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 基础设置保存或更新
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "saveOrUpdateBasisSetVo", description = "基础设置保存或更新")
@Data
public class SaveOrUpdateBasisSetVo {

    @NotNull(message = "setType不能为空")
    @ApiModelProperty(name = "setType", value = "save:为保存，否则为更新", required = true)
    private String setType;

    @ApiModelProperty(name = "id", value = "主键")
    private Integer id;

    @NotNull(message = "供餐类型不能为空")
    @ApiModelProperty(name = "messType", value = "供餐类型（0早 1中 2晚 3夜宵）", required = true)
    private String messType;

    @NotNull(message = "饭票类型不能为空")
    @ApiModelProperty(name = "bitUniversal", value = "饭票类型（0通用 1多用）", required = true)
    private Integer bitUniversal;

    @ApiModelProperty(name = "capNum", value = "封顶份数")
    private Integer capNum = 0;

    @NotNull(message = "是否允许粉丝购买饭票不能为空")
    @ApiModelProperty(name = "bitBuy", value = "是否允许粉丝购买饭票（0是 1否）", required = true)
    private Integer bitBuy;

    @NotNull(message = "是否允许粉丝充值不能为空")
    @ApiModelProperty(name = "bitTopUp", value = "是否允许粉丝充值（0是 1否）", required = true)
    private Integer bitTopUp;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "breakfastStart", value = "早餐开始时间")
    private Date breakfastStart;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "breakfastEnd", value = "早餐结束时间")
    private Date breakfastEnd;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "breakfastReserve", value = "早餐预定时间")
    private Date breakfastReserve;

    @ApiModelProperty(name = "breakfastPrice", value = "早餐饭票价格")
    private Double breakfastPrice;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "lunchStart", value = "午餐开始时间")
    private Date lunchStart;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "lunchEnd", value = "午餐结束时间")
    private Date lunchEnd;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "lunchReserve", value = "午餐预定时间")
    private Date lunchReserve;

    @ApiModelProperty(name = "lunchPrice", value = "午餐饭票价格")
    private Double lunchPrice;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "dinnerStart", value = "晚餐开始时间")
    private Date dinnerStart;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "dinnerEnd", value = "晚餐结束时间")
    private Date dinnerEnd;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "dinnerReserve", value = "晚餐预定时间")
    private Date dinnerReserve;

    @ApiModelProperty(name = "dinnerPrice", value = "晚餐饭票价格")
    private Double dinnerPrice;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "nightStart", value = "夜宵开始时间")
    private Date nightStart;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "nightEnd", value = "夜宵结束时间")
    private Date nightEnd;

    @DateTimeFormat(pattern = "HH:mm")
    @ApiModelProperty(name = "nightReserve", value = "夜宵预定时间")
    private Date nightReserve;

    @ApiModelProperty(name = "nightPrice", value = "夜宵饭票价格")
    private Double nightPrice;

    @ApiModelProperty(name = "universalPrice", value = "通用饭票价格")
    private Double universalPrice;

    @NotNull(message = "主表ID不能为空")
    @ApiModelProperty(name = "mainId", value = "主表ID:messMain的ID", required = true)
    private Integer mainId;

    @NotNull(message = "名称不能为空")
    @ApiModelProperty(name = "name", value = "名称", required = true)
    private String name;

    @NotNull(message = "饭票有效期不能为空")
    @ApiModelProperty(name = "ticketDay", value = "饭票有效期（天）", required = true)
    private Integer ticketDay;

    @NotNull(message = "过期提前天数不能为空")
    @ApiModelProperty(name = "pastDay", value = "过期提前天数", required = true)
    private Integer pastDay;

    @NotNull(message = "预定提前天数不能为空")
    @ApiModelProperty(name = "bookDay", value = "预定提前天数", required = true)
    private Integer bookDay;

}
