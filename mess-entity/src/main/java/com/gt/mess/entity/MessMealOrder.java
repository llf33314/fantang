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
 * <p>订餐记录表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_meal_order" )
public class MessMealOrder extends Model<MessMealOrder> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer mainId;

    private Integer cardId;

    private Integer memberId;

    private String name;

    private Integer sex;

    private String department;

    private String cardCode;

    private Date time;

    private Integer mealType;

    private Integer mealNum;

    private Integer status;

    private Double money;
    
    private Date orderTime;
    
    private String mealCode;
    
    private Integer depId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}