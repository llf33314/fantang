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
 * <p>购票记录表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_buy_ticket_order" )
public class MessBuyTicketOrder extends Model<MessBuyTicketOrder> {

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

    private Integer buyType;

    private Integer buyNum;

    private Integer buyLaterNum;

    private Double money;

    private Integer bitSubsidy;

    private String orderNo;
   
    private Integer ticketType;
    
    private Integer depId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}