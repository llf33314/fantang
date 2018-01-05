package com.gt.mess.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 保存或更新公告
 *
 * @author zengwx
 * @date 2017年9月20日
 * @version 1.0
 * @remark
 */
@ApiModel(value = "saveOrUpdateNoticeVo", description = "保存或更新公告")
@Data
public class SaveOrUpdateNoticeVo {

    @NotNull(message = "setType不能为空")
    @ApiModelProperty(name = "setType", value = "save:为保存，否则为更新", required = true)
    private String setType;

    @ApiModelProperty(name = "id", value = "主键")
    private Integer id;

    @ApiModelProperty(name = "mainId", value = "主表ID", hidden = true)
    private Integer mainId;

    @NotNull(message = "标题不能为空")
    @ApiModelProperty(name = "title", value = "标题", required = true)
    private String title;

    @NotNull(message = "公告内容不能为空")
    @ApiModelProperty(name = "notice", value = "公告内容", required = true)
    private String notice;
}
