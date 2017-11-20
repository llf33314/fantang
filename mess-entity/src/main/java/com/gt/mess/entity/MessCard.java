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
 * <p>微饭票卡号表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_card" )
public class MessCard extends Model<MessCard> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer memberId;

    private Integer mainId;

    private String name;

    private String department;

    private String cardCode;

    private Double money;

    private Integer ticketNum;

    private Integer freeTicketNum;

    private Integer sex;

    private Integer status;

    private Date time;

    private Integer breakfastNum;

    private Integer lunchNum;

    private Integer dinnerNum;

    private Integer nightNum;

    private Integer universalNum;
    
    private Integer depId;
    
    private String srOpenId;

	private String unionId;
	
	private Integer groupId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}