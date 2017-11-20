package com.gt.mess.service.impl;

import com.gt.mess.dao.MessMainMapper;
import com.gt.mess.entity.MessMain;
import com.gt.mess.properties.FtpProperties;
import com.gt.mess.service.MessMainService;
import com.gt.mess.util.CommonUtil;
import com.gt.mess.util.EncryptUtil;
import com.gt.mess.util.QRcodeKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.Map;

@Service
public class MessMainServiceImpl implements MessMainService{

	@Autowired
	private MessMainMapper messMainMapper;

	/**
	 * 饭票
	 */
	public static final String IMAGE_FOLDER_TYPE_24="24";

	@Autowired
	private FtpProperties ftpProperties;
	
	
//	@Value("#{config['res.image.path']}")
//	private String imgPath; // 图片的存放路径
	
	@Override
	public MessMain getMessMainByBusId(Integer busId) {
		// TODO Auto-generated method stub
		try {
			return messMainMapper.getMessMainByBusId(busId).get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int saveMessMain(MessMain messMain) {
		return messMainMapper.insertSelective(messMain);
	}

	@Override
	public MessMain getMessMainById(Integer id) {
		// TODO Auto-generated method stub
		try {
			return messMainMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveMessMainAuthority(Map<String, Object> param) {
		try {
			MessMain messMain = (MessMain)param.get("messMain");
			String path = param.get("path").toString();
			String savePath = ftpProperties.getImageAccess() + "/2/" + IMAGE_FOLDER_TYPE_24 + "/" + messMain.getId() ;
			messMain.setAuthoritySign("f"+messMain.getId()+"p1");
			String ticket_encrypt = EncryptUtil.encrypt(messMain.getId() + "CFCCBD66B12B62E5256FAA90A931A01F", messMain.getId()+"");//加密后参数
			ticket_encrypt = URLEncoder.encode(URLEncoder.encode(ticket_encrypt,"UTF-8"), "UTF-8");
			String rcode = QRcodeKit.buildQRcode(path+"/messMobile/"+messMain.getId()+"/79B4DE7C/authority.do?ticketCode="+ticket_encrypt+"&sign="+messMain.getAuthoritySign(), savePath, 250, 250,ftpProperties);//授权扫描
			String[] code_arr = new String[] {};
			code_arr = rcode.split("/upload/");
			if (!CommonUtil.isEmpty(code_arr) && code_arr.length > 1) {
				messMain.setAuthorityUrl(code_arr[1]);
			}
			messMainMapper.updateByPrimaryKeySelective(messMain);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
