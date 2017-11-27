package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessOldManCardMapper;
import com.gt.mess.dao.MessOldManCardOrderMapper;
import com.gt.mess.entity.MessOldManCard;
import com.gt.mess.entity.MessOldManCardOrder;
import com.gt.mess.service.MessOldManCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class MessOldManCardServiceImpl implements MessOldManCardService{

	@Autowired
	private MessOldManCardMapper messOldManCardMapper;
	
	@Autowired
	private MessOldManCardOrderMapper messOldManCardOrderMapper;

	@Override
	public Page<MessOldManCard> getMessOldManCardPageByMainId(Page<MessOldManCard> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardMapper.getMessOldManCardPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int saveOldManCard(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int date = 0;
		MessOldManCard messOldManCard = new MessOldManCard();
		String cardCode = "" + PJWHash("" + System.currentTimeMillis());
		if(cardCode.length() != 9){
			for(int i = 0;i <100;i++){
				cardCode = "" + PJWHash("" + System.currentTimeMillis());
				if(cardCode.length() == 9){
					break;
				}
			}
		}
		messOldManCard.setCardCode(cardCode);
		messOldManCard.setDepartment(params.get("department").toString());
		messOldManCard.setDepId(Integer.valueOf(params.get("depId").toString()));
		messOldManCard.setMainId(Integer.valueOf(params.get("mainId").toString()));
		messOldManCard.setName(params.get("name").toString());
		messOldManCard.setSex(Integer.valueOf(params.get("sex").toString()));
		messOldManCard.setTicketNum(Integer.valueOf(params.get("ticketNum").toString()));
		messOldManCard.setTime(new Date());
		date = messOldManCardMapper.insertSelective(messOldManCard);
		return date;
	}

	/* PJWHash */
	public static long PJWHash(String str) {
		Random random=new Random();// 定义随机类
		int result=random.nextInt(1000);
		str = str + System.currentTimeMillis() + result;
		long BitsInUnsignedInt = (long) (4 * 8);
		long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
		long OneEighth = (long) (BitsInUnsignedInt / 8);
		long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
		long hash = 0;
		long test = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash << OneEighth) + str.charAt(i);
			if ((test = hash & HighBits) != 0)
				hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
		}
		return hash;
	}

	@Override
	public Page<MessOldManCard> selectOldManCardManage(Page<MessOldManCard> page, Map<String, Object> params,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardMapper.selectOldManCardManage(params) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int addOrDelTicket(Integer cardId, Integer ticketNum, Integer type) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		MessOldManCardOrder messOldManCardOrder = new MessOldManCardOrder();
		MessOldManCard messOldManCard =
				messOldManCardMapper.selectByPrimaryKey(cardId);
		if(type == 0){
			if(messOldManCard.getTicketNum() >= ticketNum){
				messOldManCard.setTicketNum(messOldManCard.getTicketNum() - ticketNum);
			}else{
				return 0;
			}
			messOldManCardOrder.setType(type);
		}else if(type == 1){
			messOldManCard.setTicketNum(messOldManCard.getTicketNum() + ticketNum);
			messOldManCardOrder.setType(type);
		}else if(type == 2){
			if(messOldManCard.getTicketNum() >= ticketNum){
				messOldManCard.setTicketNum(messOldManCard.getTicketNum() - ticketNum);
			}else{
				return 0;
			}
			messOldManCardOrder.setType(type);
		}else {
			return 0;
		}
		data = messOldManCardMapper.updateByPrimaryKeySelective(messOldManCard);
		messOldManCardOrder.setCardCode(messOldManCard.getCardCode());
		messOldManCardOrder.setDepartment(messOldManCard.getDepartment());
		messOldManCardOrder.setMainId(messOldManCard.getMainId());
		messOldManCardOrder.setName(messOldManCard.getName());
		messOldManCardOrder.setNum(ticketNum);
		messOldManCardOrder.setOldId(messOldManCard.getId());
		messOldManCardOrder.setSex(messOldManCard.getSex());
		messOldManCardOrder.setTime(new Date());
		data = messOldManCardOrderMapper.insertSelective(messOldManCardOrder);
		return data;
	}

	@Override
	public int delOldManCard(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return messOldManCardMapper.deleteByPrimaryKey(id);
	}
}
