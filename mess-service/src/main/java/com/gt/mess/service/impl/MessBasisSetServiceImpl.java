package com.gt.mess.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.gt.mess.vo.SaveOrUpdateBasisSetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gt.mess.dao.MessBasisSetMapper;
import com.gt.mess.entity.MessBasisSet;
import com.gt.mess.service.MessBasisSetService;
@Service
public class MessBasisSetServiceImpl implements MessBasisSetService{

	@Autowired
	private MessBasisSetMapper messBasisSetMapper;
	
	@Override
	public MessBasisSet getMessBasisSetByMainId(Integer mainId) {
		// TODO Auto-generated method stub
		try {
			return messBasisSetMapper.getMessBasisSetByMainId(mainId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	@Transactional
	@Override
	public int saveOrUpdateBasisSet(SaveOrUpdateBasisSetVo saveVo) throws Exception{
		// TODO Auto-generated method stub
		int dataType = 0;
		MessBasisSet messBasisSet = null;
		if("save".equals(saveVo.getSetType())){
			messBasisSet = new MessBasisSet();	
		}else{
			messBasisSet = messBasisSetMapper.selectByPrimaryKey(saveVo.getId());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm", Locale.UK);
		String messType = saveVo.getMessType();
		Integer bitUniversal = saveVo.getBitUniversal();
		messBasisSet.setMessType(messType);
		messBasisSet.setCapNum(saveVo.getCapNum());
		
		messBasisSet.setBitBuy(saveVo.getBitBuy());
		messBasisSet.setBitUniversal(bitUniversal);
		messBasisSet.setBitTopUp(saveVo.getBitTopUp());
		if(messType.contains("0")){
			messBasisSet.setBreakfastEnd(saveVo.getBreakfastEnd());//sdf.parse()
			messBasisSet.setBreakfastStart(saveVo.getBreakfastStart());
			messBasisSet.setBreakfastReserve(saveVo.getBreakfastReserve());
			if(bitUniversal == 1){
				messBasisSet.setBreakfastPrice(saveVo.getBreakfastPrice());
			}else{
				messBasisSet.setBreakfastPrice(0.0);
			}
		}else{
			messBasisSet.setBreakfastEnd(sdf.parse("00:00"));
			messBasisSet.setBreakfastStart(sdf.parse("00:00"));
			messBasisSet.setBreakfastReserve(sdf.parse("00:00"));
		}
		
		if(messType.contains("1")){
			messBasisSet.setLunchStart(saveVo.getLunchStart());
			messBasisSet.setLunchEnd(saveVo.getLunchEnd());
			messBasisSet.setLunchReserve(saveVo.getLunchReserve());
			if(bitUniversal == 1){
				messBasisSet.setLunchPrice(saveVo.getLunchPrice());
			}else{
				messBasisSet.setLunchPrice(0.0);
			}
		}else{
			messBasisSet.setLunchEnd(sdf.parse("00:00"));
			messBasisSet.setLunchReserve(sdf.parse("00:00"));
			messBasisSet.setLunchStart(sdf.parse("00:00"));
		}
		
		if(messType.contains("2")){
			messBasisSet.setDinnerStart(saveVo.getDinnerStart());
			messBasisSet.setDinnerEnd(saveVo.getDinnerEnd());
			messBasisSet.setDinnerReserve(saveVo.getDinnerReserve());
			if(bitUniversal == 1){
				messBasisSet.setDinnerPrice(saveVo.getDinnerPrice());
			}else{
				messBasisSet.setDinnerPrice(0.0);
			}
		}else{
			messBasisSet.setDinnerEnd(sdf.parse("00:00"));
			messBasisSet.setDinnerReserve(sdf.parse("00:00"));
			messBasisSet.setDinnerStart(sdf.parse("00:00"));
		}
		
		if(messType.contains("3")){
			messBasisSet.setNightStart(saveVo.getNightStart());
			messBasisSet.setNightEnd(saveVo.getNightEnd());
			messBasisSet.setNightReserve(saveVo.getNightReserve());
			if(bitUniversal == 1){
				messBasisSet.setNightPrice(saveVo.getNightPrice());
			}else{
				messBasisSet.setNightPrice(0.0);
			}
		}else{
			messBasisSet.setNightEnd(sdf.parse("00:00"));
			messBasisSet.setNightReserve(sdf.parse("00:00"));
			messBasisSet.setNightStart(sdf.parse("00:00"));
		}
		
		if(bitUniversal == 0){
			messBasisSet.setUniversalPrice(saveVo.getUniversalPrice());
		}else{
			messBasisSet.setUniversalPrice(0.0);
		}
		messBasisSet.setMainId(saveVo.getMainId());
		messBasisSet.setName(saveVo.getName());
		messBasisSet.setTicketDay(saveVo.getTicketDay());
		messBasisSet.setPastDay(saveVo.getPastDay());
		messBasisSet.setBookDay(saveVo.getBookDay());
		if("save".equals(saveVo.getSetType())){
			dataType = messBasisSetMapper.insertSelective(messBasisSet);
		}else{
			dataType = messBasisSetMapper.updateByPrimaryKeySelective(messBasisSet);
		}
		return dataType;
	}

}
