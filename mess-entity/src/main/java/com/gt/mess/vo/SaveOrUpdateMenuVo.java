package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 保存或更新菜品
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "saveOrUpdateMenuVo", description = "保存或更新菜品")
@Data
public class SaveOrUpdateMenuVo {

    @NotNull(message = "setType不能为空")
    @ApiModelProperty(name = "setType", value = "save:为保存，否则为更新", required = true)
    private String setType;

    @ApiModelProperty(name = "id", value = "主键")
    private Integer id;

    @NotNull(message = "主表ID不能为空")
    @ApiModelProperty(name = "mainId", value = "主表ID:messMain的ID", required = true)
    private Integer mainId;

    @NotNull(message = "备注不能为空")
    @ApiModelProperty(name = "comment", value = "备注", required = true)
    private String comment;

    @NotNull(message = "饭菜图片不能为空")
    @ApiModelProperty(name = "images", value = "饭菜图片", required = true)
    private String images;

    @NotNull(message = "菜名不能为空")
    @ApiModelProperty(name = "name", value = "菜名", required = true)
    private String name;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty(name = "sort", value = "排序", required = true)
    private Integer sort;

    @NotNull(message = "菜品类型不能为空")
    @ApiModelProperty(name = "type", value = "菜品类型（0早 1中 2晚 3夜宵）", required = true)
    private Integer type;

    @NotNull(message = "星期数不能为空")
    @ApiModelProperty(name = "week", value = "星期（1,2,3,4,5,6,7）", required = true)
    private Integer week;


}
