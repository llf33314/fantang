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
 * <p>饭票表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_card_ticket" )
public class MessCardTicket extends Model<MessCardTicket> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer cardId;

    private String ticketCode;

    private Double price;

    private Integer status;

    private Date time;

    private Integer isfree;

    private Integer ticketType;

    private Integer day;
    
    private Integer mainId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}