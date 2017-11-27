package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 根据条件搜索
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "selectMainIdDepIdCardCodeVo", description = "根据条件搜索")
@Data
public class SelectMainIdDepIdCardCodeVo {

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @ApiModelProperty(name = "stime", value = "开始时间")
    private Date stime;

    @ApiModelProperty(name = "etime", value = "结束时间")
    private Date etime;

    @ApiModelProperty(name = "depId", value = "部门ID")
    private Integer depId;

    @ApiModelProperty(name = "cardCode", value = "饭票卡号")
    private String cardCode;
}
