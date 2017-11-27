package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 保存或更新饭卡组
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "saveOrUpdateCardGroupVo", description = "保存或更新饭卡组")
@Data
public class SaveOrUpdateCardGroupVo {

    @NotNull(message = "setType不能为空")
    @ApiModelProperty(name = "setType", value = "save:为保存，否则为更新", required = true)
    private String setType;

    @ApiModelProperty(name = "id", value = "主键")
    private Integer id;

    @NotNull(message = "主表ID不能为空")
    @ApiModelProperty(name = "mainId", value = "主表ID:messMain的ID", required = true)
    private Integer mainId;

    @NotNull(message = "名称不能为空")
    @ApiModelProperty(name = "name", value = "名称", required = true)
    private String name;

    @NotNull(message = "bitUse不能为空")
    @ApiModelProperty(name = "bitUse", value = "是否启用: 0启用 1不启用", required = true)
    private Integer bitUse;

    @ApiModelProperty(name = "breakfastPrice", value = "早餐价格")
    private Double breakfastPrice;

    @ApiModelProperty(name = "lunchPrice", value = "午餐价格")
    private Double lunchPrice;

    @ApiModelProperty(name = "dinnerPrice", value = "晚餐价格")
    private Double dinnerPrice;

    @ApiModelProperty(name = "nightPrice", value = "夜宵价格")
    private Double nightPrice;

    @ApiModelProperty(name = "universalPrice", value = "通用票价格")
    private Double universalPrice;

}
