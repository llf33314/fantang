package com.gt.mess.util;

import com.gt.api.bean.session.BusUser;
import com.gt.mess.enums.ResponseEnums;
import com.gt.mess.exception.ResponseEntityException;
import com.gt.mess.properties.FtpProperties;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-16.
 */
public class UploadUtil {

    /**
     * 图文(素材)
     */
    public static final String IMAGE_FOLDER_TYPE_3="3";

    /**
     * 上传图片工具类
     * @param request
     * @param ftpProperties
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String,Object> upLoadImage( HttpServletRequest request, FtpProperties ftpProperties, BusUser obj ) throws Exception {
        Map<String,Object> data = new HashMap<>();
        String path="";
        String originalFilename = "";
        String userName =null;
        if(!CommonUtil.isEmpty(obj)){
            userName =obj.getName();
        }else{
            throw new ResponseEntityException(ResponseEnums.NEED_LOGIN);
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        // 文件名
        originalFilename = multipartFile.getOriginalFilename();
        // 后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 文件大小
        Long fileSize = multipartFile.getSize();
        if(fileSize > 3000000){
            throw new ResponseEntityException(ResponseEnums.SYSTEM_IO_ERROR);
        }
        Long time = System.currentTimeMillis();
        // 获取1007字典类型
        path = ftpProperties.getImagePath() + "/2/" + userName + "/" + IMAGE_FOLDER_TYPE_3 + "/"
                + DateTimeKit.getDateTime(new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD) + "/"
                + MD5Util.getMD5(time + originalFilename.substring(0, originalFilename.lastIndexOf(".")))
                + suffix;
        File file = makefile(path);
        multipartFile.transferTo(file);
        ContinueFTP myFtp = new ContinueFTP();
        myFtp.upload(file.getPath(),ftpProperties);
        path = ftpProperties.getImageAccess() + "image" + path.replace( ftpProperties.getImagePath(), "" );
        data.put("url",path);
        return data;
    }

    /**
     * 创建目录和文件 * @param path * @return * @throws IOException
     */
    private static File makefile(String path) throws IOException {
        if (path == null || "".equals(path.trim()))
            return null;
        String dirPath = path.substring(0, path.lastIndexOf("/"));
        int index = path.lastIndexOf(".");
        if (index > 0) { // 全路径，保存文件后缀
            File dir = new File(dirPath);
            if (!dir.exists()) { //先建目录
                dir.mkdirs();
                dir = null;
            }

            File file = new File(path);
            if (!file.exists()) {//再建文件
                file.createNewFile();
            }
            return file;
        } else {
            File dir = new File(dirPath); //直接建目录
            if (!dir.exists()) {
                dir.mkdirs();
                dir = null;
            }
            return dir;
        }
    }


}
