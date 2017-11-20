package com.gt.mess.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>订餐管理表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_order_manage" )
public class MessOrderManage extends Model<MessOrderManage> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer mainId;

    private Integer year;

    private Integer month;
    
    private String day;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}