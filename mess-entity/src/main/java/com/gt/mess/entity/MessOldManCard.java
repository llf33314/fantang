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
 * <p>饭堂老人卡</p>
 *
 * @author zengwx
 * @since 2017-11-17
 */
@Data
@Accessors( chain = true )
@TableName( "t_mess_old_man_card" )
public class MessOldManCard extends Model<MessOldManCard> {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.UUID )
    private String id;

    private Integer mainId;

    private String name;

    private String department;

    private String cardCode;

    private Integer ticketNum;

    private Integer sex;

    private Date time;

    private Integer depId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}