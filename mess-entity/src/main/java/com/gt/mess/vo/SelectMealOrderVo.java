package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 订餐记录（根据条件搜索）
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "selectMealOrderVo", description = "订餐记录（根据条件搜索）")
@Data
public class SelectMealOrderVo {

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @ApiModelProperty(name = "stime", value = "开始时间")
    private Date stime;

    @ApiModelProperty(name = "etime", value = "结束时间")
    private Date etime;

    @NotNull(message = "订餐类型不能为空")
    @ApiModelProperty(name = "mealType", value = "订餐类型（-1全部 0早 1中 2晚 3夜宵 4未选餐）", required = true)
    private Integer mealType;

    @ApiModelProperty(name = "depId", value = "部门ID")
    private Integer depId;

    @NotNull(message = "订单状态不能为空")
    @ApiModelProperty(name = "status", value = "订单状态（-1全部 0未选餐 1已预订 2取消订单  4已取餐 5订单过期 ）", required = true)
    private Integer status;

    @ApiModelProperty(name = "cardCode", value = "饭票卡号")
    private String cardCode;

}
