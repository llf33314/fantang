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
 * <p>总消费流水表</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_consumer_detail" )
public class MessConsumerDetail extends Model<MessConsumerDetail> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;

    private Integer cardId;

    private Integer mainId;

    private Integer tableType;

    private Date time;

    private Integer status;

    private Integer bitSubsidy;

    private Double money = 0.0;

    private Integer ticketNum = 0;

    private Integer breakfastNum = 0;

    private Integer lunchNum = 0;

    private Integer dinnerNum = 0;

    private Integer nightNum = 0;

    private Integer universalNum = 0;

    private Double breakfastPrice = 0.0;

    private Double lunchPrice = 0.0;

    private Double dinnerPrice = 0.0;

    private Double nightPrice = 0.0;

    private Double universalPrice = 0.0;
    
    private Integer onLine;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}