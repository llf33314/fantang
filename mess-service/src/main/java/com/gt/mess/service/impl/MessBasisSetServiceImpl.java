package com.gt.mess.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

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
	public int saveOrUpdateBasisSet(Map<String, Object> params) throws Exception{
		// TODO Auto-generated method stub
		int dataType = 0;
		String setType = params.get("setType").toString();
		MessBasisSet messBasisSet = null;
		if("save".equals(setType)){
			messBasisSet = new MessBasisSet();	
		}else{
			messBasisSet = messBasisSetMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm", Locale.UK);
		String messType = params.get("messType").toString();
		Integer bitUniversal = Integer.valueOf(params.get("bitUniversal").toString());
		messBasisSet.setMessType(messType);
		try {
			messBasisSet.setCapNum(Integer.valueOf(params.get("capNum").toString()));
		} catch (Exception e) {
			// TODO: handle exception
			messBasisSet.setCapNum(0);
		}
		
		messBasisSet.setBitBuy(Integer.valueOf(params.get("bitBuy").toString()));
		messBasisSet.setBitUniversal(bitUniversal);
		messBasisSet.setBitTopUp(Integer.valueOf(params.get("bitTopUp").toString()));
		if(messType.contains("0")){
			messBasisSet.setBreakfastEnd(sdf.parse(params.get("breakfastEnd").toString()));
			messBasisSet.setBreakfastStart(sdf.parse(params.get("breakfastStart").toString()));
			messBasisSet.setBreakfastReserve(sdf.parse(params.get("breakfastReserve").toString()));
			if(bitUniversal == 1){
				messBasisSet.setBreakfastPrice(Double.valueOf(params.get("breakfastPrice").toString()));
			}else{
				messBasisSet.setBreakfastPrice(0.0);
			}
		}else{
			messBasisSet.setBreakfastEnd(sdf.parse("00:00"));
			messBasisSet.setBreakfastStart(sdf.parse("00:00"));
			messBasisSet.setBreakfastReserve(sdf.parse("00:00"));
		}
		
		if(messType.contains("1")){
			messBasisSet.setLunchEnd(sdf.parse(params.get("lunchEnd").toString()));
			messBasisSet.setLunchReserve(sdf.parse(params.get("lunchReserve").toString()));
			messBasisSet.setLunchStart(sdf.parse(params.get("lunchStart").toString()));
			if(bitUniversal == 1){
				messBasisSet.setLunchPrice(Double.valueOf(params.get("lunchPrice").toString()));
			}else{
				messBasisSet.setLunchPrice(0.0);
			}
		}else{
			messBasisSet.setLunchEnd(sdf.parse("00:00"));
			messBasisSet.setLunchReserve(sdf.parse("00:00"));
			messBasisSet.setLunchStart(sdf.parse("00:00"));
		}
		
		if(messType.contains("2")){
			messBasisSet.setDinnerEnd(sdf.parse(params.get("dinnerEnd").toString()));
			messBasisSet.setDinnerReserve(sdf.parse(params.get("dinnerReserve").toString()));
			messBasisSet.setDinnerStart(sdf.parse(params.get("dinnerStart").toString()));
			if(bitUniversal == 1){
				messBasisSet.setDinnerPrice(Double.valueOf(params.get("dinnerPrice").toString()));
			}else{
				messBasisSet.setDinnerPrice(0.0);
			}
		}else{
			messBasisSet.setDinnerEnd(sdf.parse("00:00"));
			messBasisSet.setDinnerReserve(sdf.parse("00:00"));
			messBasisSet.setDinnerStart(sdf.parse("00:00"));
		}
		
		if(messType.contains("3")){
			messBasisSet.setNightEnd(sdf.parse(params.get("nightEnd").toString()));
			messBasisSet.setNightReserve(sdf.parse(params.get("nightReserve").toString()));
			messBasisSet.setNightStart(sdf.parse(params.get("nightStart").toString()));
			if(bitUniversal == 1){
				messBasisSet.setNightPrice(Double.valueOf(params.get("nightPrice").toString()));
			}else{
				messBasisSet.setNightPrice(0.0);
			}
		}else{
			messBasisSet.setNightEnd(sdf.parse("00:00"));
			messBasisSet.setNightReserve(sdf.parse("00:00"));
			messBasisSet.setNightStart(sdf.parse("00:00"));
		}
		
		if(bitUniversal == 0){
			messBasisSet.setUniversalPrice(Double.valueOf(params.get("universalPrice").toString()));
		}else{
			messBasisSet.setUniversalPrice(0.0);
		}
		messBasisSet.setMainId(Integer.valueOf(params.get("mainId").toString()));
		messBasisSet.setName(params.get("name").toString());
		messBasisSet.setTicketDay(Integer.valueOf(params.get("ticketDay").toString()));
		messBasisSet.setPastDay(Integer.valueOf(params.get("pastDay").toString()));
		messBasisSet.setBookDay(Integer.valueOf(params.get("bookDay").toString()));
		if("save".equals(setType)){
			dataType = messBasisSetMapper.insertSelective(messBasisSet);
		}else{
			dataType = messBasisSetMapper.updateByPrimaryKeySelective(messBasisSet);
		}
		return dataType;
	}

}
