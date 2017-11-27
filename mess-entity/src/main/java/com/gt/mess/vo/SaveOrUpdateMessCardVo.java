package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 保存或更新饭卡
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "saveOrUpdateMessCardVo", description = "保存或更新饭卡")
@Data
public class SaveOrUpdateMessCardVo {

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @ApiModelProperty(name = "department", value = "部门名称")
    private String department;

    @NotNull(message = "持卡人名字不能为空")
    @ApiModelProperty(name = "name", value = "持卡人名字", required = true)
    private String name;

    @ApiModelProperty(name = "depId", value = "部门ID")
    private Integer depId;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(name = "sex", value = "性别（0女 1男）", required = true)
    private Integer sex;

    @NotNull(message = "饭票数不能为空")
    @ApiModelProperty(name = "ticketNum", value = "饭票数", required = true)
    private Integer ticketNum;

    @ApiModelProperty(name = "groupId", value = "饭卡组")
    private Integer groupId;

    @NotNull(message = "饭卡余额不能为空")
    @ApiModelProperty(name = "money", value = "饭卡余额", required = true)
    private Double money=0.0;
}
