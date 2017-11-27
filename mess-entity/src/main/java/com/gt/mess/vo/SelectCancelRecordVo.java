package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 核销记录(根据条件查询)
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "selectCancelRecordVo", description = "核销记录(根据条件查询)")
@Data
public class SelectCancelRecordVo {

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @ApiModelProperty(name = "stime", value = "开始时间")
    private Date stime;

    @ApiModelProperty(name = "etime", value = "结束时间")
    private Date etime;

    @ApiModelProperty(name = "ndate", value = "...问司然")
    private Date ndate;

    @ApiModelProperty(name = "depId", value = "部门ID")
    private Integer depId;

    @ApiModelProperty(name = "cardCode", value = "饭票卡号")
    private String cardCode;

    @NotNull(message = "起始页不能为空")
    @Min(value = 1, message = "起始页最小值为1")
    @ApiParam(name = "pageNum", value = "起始页", defaultValue = "1", required = true)
    private int pageNum;

}
