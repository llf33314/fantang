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
 * <p>基础设置表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_basis_set" )
public class MessBasisSet extends Model<MessBasisSet> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer mainId;

    private String name;

    private Integer bitUniversal;

    private Integer bitBuy;

    private String messType;

    private Integer ticketDay;

    private Date breakfastStart;

    private Date breakfastEnd;

    private Date breakfastReserve;

    private Double breakfastPrice;

    private Date lunchStart;

    private Date lunchEnd;

    private Date lunchReserve;

    private Double lunchPrice;

    private Date dinnerStart;

    private Date dinnerEnd;

    private Date dinnerReserve;

    private Double dinnerPrice;

    private Date nightStart;

    private Date nightEnd;

    private Date nightReserve;

    private Double nightPrice;

    private Double universalPrice;
    
    private Integer universalNum;
    
    private Integer bitTopUp;
    
    private Integer bookDay;
    
	private Integer pastDay;
	
	private Integer capNum;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}