package com.gt.mess.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>加菜记录表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_add_food_order" )
public class MessAddFoodOrder extends Model<MessAddFoodOrder> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.UUID )
    private String id;

    private Integer mainId;

    private Integer afId;

    private Integer cardId;

    private Integer memberId;

    private String name;

    private Integer sex;

    private String department;

    private String cardCode;

    private Date time;

    private Integer addNum;

    private Double money;
    
    private Integer depId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}