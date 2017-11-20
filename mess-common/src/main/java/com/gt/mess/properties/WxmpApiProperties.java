package com.gt.mess.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Ftp 文件上传属性-组件类
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/08/21
 */
@Component
@ConfigurationProperties( prefix = "mess.wxmp_api" )
@Data
public class WxmpApiProperties {

    private String adminUrl;//自己的域名

    private String homeUrl;//wxmp域名

    private String memberHomeUrl;//会员服务域名

    private String memberKey;//会员查询密钥

    private String wxmpKey;//wxmp查询密钥

    private String redisName;//redis命名前缀

}
