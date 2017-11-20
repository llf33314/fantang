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
 * <p>微饭票老人卡订单</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_old_man_card_order" )
public class MessOldManCardOrder extends Model<MessOldManCardOrder> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer oldId;

    private Integer mainId;

    private String name;

    private String department;

    private String cardCode;

    private Integer num;

    private Integer sex;

    private Date time;
    
    private Integer type;
    
    private Integer mealType = 4;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}