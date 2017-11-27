package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessAuthorityMemberMapper;
import com.gt.mess.dao.MessCancelTicketMapper;
import com.gt.mess.dao.MessMainMapper;
import com.gt.mess.dto.ResponseDTO;
import com.gt.mess.entity.MessAuthorityMember;
import com.gt.mess.entity.MessMain;
import com.gt.mess.exception.BaseException;
import com.gt.mess.properties.FtpProperties;
import com.gt.mess.service.MessAuthorityMemberService;
import com.gt.mess.util.CommonUtil;
import com.gt.mess.util.EncryptUtil;
import com.gt.mess.util.QRcodeKit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class MessAuthorityMemberServiceImpl implements MessAuthorityMemberService {

	@Autowired
	private MessAuthorityMemberMapper messAuthorityMemberMapper;
	
	private Logger logger = Logger.getLogger(MessAuthorityMemberServiceImpl.class);
	
	@Autowired
	private MessMainMapper messMainMapper;
	
	@Autowired
	private MessCancelTicketMapper messCancelTicketMapper;

	@Autowired
	private FtpProperties ftpProperties;

	/**
	 * 饭票
	 */
	public static final String IMAGE_FOLDER_TYPE_24="24";

	@Override
	public Page<Map<String,Object>> getMessAuthorityMemberPageByMainId(Page<Map<String,Object>> page, Integer mainId,
			int nums) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> param = new HashMap<String,Object>();
			param.put("mainId", mainId);
			param.put("delStatus", 0);
			page.setRecords( messAuthorityMemberMapper.getMessAuthorityMemberPageByMainId(page,param) );
			Page<Map<String, Object>> myPage = page;
			List<Map<String,Object>> list = myPage.getRecords();
			for (Map<String, Object> map : list) {
				map.put("member_name", CommonUtil.isEmpty(map.get("member_name")) ? "未知用户" : CommonUtil.Blob2String(map.get("member_name")));
			}
			return myPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResponseDTO delAuthorityMember(Integer id) throws BaseException {
		String message = "取消失败！";
		try {
			if(!CommonUtil.isEmpty(id)){
				//如果有核销过的 则修改状态
				MessAuthorityMember authority = messAuthorityMemberMapper.selectByPrimaryKey(id);
				Map<String,Object> params = new HashMap<>();
				params.put("memberId", authority.getId());
				int count = messCancelTicketMapper.queryCancelTicketMember(params);
				if(count>0){
					authority.setDeleteStatus(1);
					messAuthorityMemberMapper.updateByPrimaryKeySelective(authority);
				}else{
					//没有核销过  删除
					messAuthorityMemberMapper.deleteByPrimaryKey(id);
				}
				message = "取消成功！";
				return ResponseDTO.createBySuccessMessage(message);
			}
			return ResponseDTO.createByErrorMessage(message);
		}catch (Exception e) {
			// TODO: handle exception
			throw new BaseException("取消授权人员失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResponseDTO delAuthorityMembers(Map<String, Object> params) throws BaseException {
		String message = "重置失败！";
		params.put("delStatus", 0);
		List<MessAuthorityMember> list = messAuthorityMemberMapper.getMessAuthorityMember(params);
		List<MessAuthorityMember> updateList = new ArrayList<MessAuthorityMember>();
		List<MessAuthorityMember> delList = new ArrayList<MessAuthorityMember>();
		for (MessAuthorityMember auth : list) {
			params.put("memberId", auth.getId());
			int count = messCancelTicketMapper.queryCancelTicketMember(params);
			if(count>0){
				updateList.add(auth);
			}else{
				delList.add(auth);
			}
		}
		if(updateList.size()>0){
			int update_count = messAuthorityMemberMapper.updateAuthorityMembers(updateList);
		}
		if(delList.size()>0){
			int del_count = messAuthorityMemberMapper.delAuthorityMembers(delList);
		}
		Integer mainId = CommonUtil.toInteger(params.get("mainId"));
		String path = params.get("path").toString();
		MessMain main = messMainMapper.selectByPrimaryKey(mainId);
		String authoritySign = main.getAuthoritySign();
		String savePath = ftpProperties.getImageAccess() + "/2/" + IMAGE_FOLDER_TYPE_24 + "/" + main.getId() ;
		String num = authoritySign.substring(authoritySign.indexOf("p")+1);
		int new_num = Integer.parseInt(num) + 1;
		main.setAuthoritySign("f"+main.getId()+"p"+new_num);
		String ticket_encrypt = EncryptUtil.encrypt(main.getId()+"CFCCBD66B12B62E5256FAA90A931A01F", main.getId()+"");//加密后参数
		try {
			ticket_encrypt = URLEncoder.encode(URLEncoder.encode(ticket_encrypt,"UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String rcode = QRcodeKit.buildQRcode(path+"/messMobile/"+main.getId()+"/79B4DE7C/authority.do?ticketCode="+ticket_encrypt+"&sign="+main.getAuthoritySign(), savePath, 250, 250,ftpProperties);//授权扫描
		String[] code_arr = new String[] {};
		code_arr = rcode.split("/upload/");
		//删除原二维码图片
		String dataUrl = ftpProperties.getImageAccess() + main.getAuthorityUrl().split("image")[1];
		CommonUtil.delFile(dataUrl);
		if (!CommonUtil.isEmpty(code_arr) && code_arr.length > 1) {
			main.setAuthorityUrl(code_arr[1]);
		}
		int count = messMainMapper.updateByPrimaryKeySelective(main);
		if(count>0){
			message = "重置成功！";
			return ResponseDTO.createBySuccessMessage(message);
		}else {
			return ResponseDTO.createByErrorMessage(message);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> saveAuthority(Map<String, Object> params) throws BaseException {
		Map<String, Object> msg = new HashMap<String, Object>();
		MessMain messMain = (MessMain)params.get("messMain");
		String result = "false";
		String message = "授权失败！";
		try{
			params.put("mainId", messMain.getId());
			params.put("delStatus", 0);//只有一个false状态  可能有多个true状态
			List<MessAuthorityMember> tams = messAuthorityMemberMapper.getMessAuthorityMember(params);
			if(tams!=null&&tams.size()>0){
				result="false";
				message = "已授权！";
			}else{
				MessAuthorityMember authority = new MessAuthorityMember();
				authority.setDeleteStatus(0);
				authority.setCreatetime(new Date());
				authority.setMainId(messMain.getId());
				authority.setMemberId(CommonUtil.toInteger(params.get("memberId")));
				messAuthorityMemberMapper.insertSelective(authority);
				result = "true";
				message = "授权成功！";
			}
		}catch(Exception e){
			logger.error("授权保存失败，原因：" + e.getMessage());
			e.printStackTrace();
			throw new BaseException(message);
		}finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}

	@Override
	public List<MessAuthorityMember> getMessAuthorityByMember(Integer mainId, Integer memberId) {
		Map<String, Object> param = new HashMap<>();
		param.put("mainId", mainId);
		param.put("memberId", memberId);
		param.put("delStatus", 0);
		return messAuthorityMemberMapper.getMessAuthorityMember(param);
	}
}
