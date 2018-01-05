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
 * <p>饭票核销表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_cancel_ticket" )
public class MessCancelTicket extends Model<MessCancelTicket> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.UUID )
    private String id;

    private Integer mainId;

    private Integer cardId;

	private String cardCode;

    private String name;

    private String department;

    private Integer ticketNum;

    private Double money;

    private Date time;
    
    private Integer sex;
    
	private Integer type;
    
    private Integer memberId;
    
    private Integer depId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}