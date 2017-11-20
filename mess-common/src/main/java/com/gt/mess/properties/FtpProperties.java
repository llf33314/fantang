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
@ConfigurationProperties( prefix = "mess.ftp" )
@Data
public class FtpProperties {

    private String imagePath; // 图片的存放路径

    private String imageAccess; // 资源访问路径

    private String staticSourceFtpIp; // 图片资源ftp ip

    private String staticSourceFtpPort; // 图片资源ftp 端口

    private String staticSourceFtpUser; // 图片资源ftp 用户

    private String staticSourceFtpPwd; // 图片资源ftp 密码

}
