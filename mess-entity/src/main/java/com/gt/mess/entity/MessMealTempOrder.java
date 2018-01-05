package com.gt.mess.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>订餐临时记录表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_meal_temp_order" )
public class MessMealTempOrder extends Model<MessMealTempOrder> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.UUID )
    private String id;

    private Integer mainId;

    private String time;

    private String cardCode;

    private Double money;

    private Integer addNum;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}