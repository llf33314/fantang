package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 保存或更新老人卡表
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "saveOldManCardVo", description = "保存或更新老人卡表")
@Data
public class SaveOldManCardVo {

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @ApiModelProperty(name = "department", value = "部门名称")
    private String department;

    @NotNull(message = "持卡人名字不能为空")
    @ApiModelProperty(name = "name", value = "持卡人名字", required = true)
    private String name;

    @ApiModelProperty(name = "depId")
    private Integer depId;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(name = "sex", value = "性别（0女 1男）", required = true)
    private Integer sex;

    @NotNull(message = "饭票数不能为空")
    @ApiModelProperty(name = "ticketNum", value = "饭票数", required = true)
    private Integer ticketNum;
}
