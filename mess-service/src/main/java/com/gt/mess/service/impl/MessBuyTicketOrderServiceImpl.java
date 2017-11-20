package com.gt.mess.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.*;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessBuyTicketOrderService;
import com.gt.mess.util.CommonUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessBuyTicketOrderServiceImpl implements MessBuyTicketOrderService{

	private Logger logger = Logger.getLogger(MessBuyTicketOrderServiceImpl.class);
	
	@Autowired
	private MessBuyTicketOrderMapper messBuyTicketOrderMapper;
	
	@Autowired
	private MessBasisSetMapper messBasisSetMapper;
	
	@Autowired
	private MessCardMapper messCardMapper;
	
	@Autowired
	private MessConsumerDetailMapper messConsumerDetailMapper;
	
	@Autowired
	private MessCardTicketMapper messCardTicketMapper;
	
	@Autowired
	private MessCardGroupMapper messCardGroupMapper;
	
	@Override
	public Page<MessBuyTicketOrder> getMessBuyTicketOrderPageMainId(Page<MessBuyTicketOrder> page, Integer mainId,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messBuyTicketOrderMapper.getMessBuyTicketOrderPageMainId(page,mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessBuyTicketOrder> selectBuyTicketStatistics(Page<MessBuyTicketOrder> page, Map<String, Object> params,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messBuyTicketOrderMapper.selectBuyTicketStatistics(page,params) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> exports(Map <String,Object> params) {
		Map<String, Object> msg = new HashMap<>();
		boolean result = true;
		String message = "生成成功！";
		try {
			List<MessBuyTicketOrder> messBuyTicketOrders = messBuyTicketOrderMapper.selectBuyTicketStatistics(params);
			String title = "购票记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
			HSSFWorkbook book = exportExcelForRecoding(messBuyTicketOrders, title);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "购票记录导出excel失败！");
			logger.error("购票记录导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}

	private HSSFWorkbook exportExcelForRecoding(List<MessBuyTicketOrder> messBuyTicketOrders, String title) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		Sheet sheet = wb.createSheet();
		// 创建第一行
		Row row = sheet.createRow(0);
		// 设置行高
		row.setHeight((short) 500);
		// 创建第二行
		Row row1 = sheet.createRow(1);
		// 处理时间
		// 设置没列的宽度
		sheet.setColumnWidth((short) (2), (short) 4000);
		sheet.setColumnWidth((short) (3), (short) 4000);
		sheet.setColumnWidth((short) (4), (short) 8000);
		sheet.setColumnWidth((short) (5), (short) 8000);
		sheet.setColumnWidth((short) (6), (short) 6000);
		sheet.setColumnWidth((short) (7), (short) 6000);
		sheet.setColumnWidth((short) (8), (short) 6000);
		// 文件头字体
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false, (short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false, (short) 200);
		// 合并第一行的单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
		// 设置第一列的文字
		createCell(wb, row, 1, title, font0);
		// 给第二行添加文本
		createCell(wb, row1, 0, "序号", font1);
		createCell(wb, row1, 1, "姓名", font1);
		createCell(wb, row1, 2, "性别", font1);
		createCell(wb, row1, 3, "部门", font1);
		createCell(wb, row1, 4, "饭票卡号", font1);
		createCell(wb, row1, 5, "购票时间", font1);
		createCell(wb, row1, 6, "购票方式", font1);
		createCell(wb, row1, 7, "购买票数（张）", font1);
		createCell(wb, row1, 8, "买后票数（张）", font1);
		createCell(wb, row1, 9, "购买金额（元）", font1);
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messBuyTicketOrders.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessBuyTicketOrder messBuyTicketOrder = messBuyTicketOrders.get(i);
			createCell(wb, rowData, 0, String.valueOf(i + 1), font1);
			createCell(wb, rowData, 1, delWithColumn(messBuyTicketOrder.getName()), font1);
			if(messBuyTicketOrder.getSex() == 0){
				createCell(wb, rowData, 2, delWithColumn("女"), font1);
			}else{
				createCell(wb, rowData, 2, delWithColumn("男"), font1);
			}
			createCell(wb, rowData, 3, delWithColumn(messBuyTicketOrder.getDepartment()) , font1);
			createCell(wb, rowData, 4, delWithColumn(messBuyTicketOrder.getCardCode()), font1);
			createCell(wb, rowData, 5, delWithColumn(time("yyyy-MM-dd",messBuyTicketOrder.getTime().toString())), font1);
			if(messBuyTicketOrder.getBuyType() == 0){
				createCell(wb, rowData, 6, delWithColumn("线上"), font1);
			}else if(messBuyTicketOrder.getBuyType()  == 1){
				createCell(wb, rowData, 6, delWithColumn("线下"), font1);
			}
			createCell(wb, rowData, 7, delWithColumn(messBuyTicketOrder.getBuyNum()), font1);
			createCell(wb, rowData, 8, delWithColumn(messBuyTicketOrder.getBuyLaterNum()), font1);
			createCell(wb, rowData, 9, delWithColumn(messBuyTicketOrder.getMoney()), font1);
		}
		return wb;
	}
	
	private HSSFWorkbook exportExcelForRecoding2(List<MessBuyTicketOrder> messBuyTicketOrders, String title) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		Sheet sheet = wb.createSheet();
		// 创建第一行
		Row row = sheet.createRow(0);
		// 设置行高
		row.setHeight((short) 500);
		// 创建第二行
		Row row1 = sheet.createRow(1);
		// 处理时间
		// 设置没列的宽度
		sheet.setColumnWidth((short) (2), (short) 4000);
		sheet.setColumnWidth((short) (3), (short) 4000);
		sheet.setColumnWidth((short) (4), (short) 8000);
		sheet.setColumnWidth((short) (5), (short) 8000);
		sheet.setColumnWidth((short) (6), (short) 6000);
		sheet.setColumnWidth((short) (7), (short) 6000);
		sheet.setColumnWidth((short) (8), (short) 6000);
		// 文件头字体
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false, (short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false, (short) 200);
		// 合并第一行的单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
		// 设置第一列的文字
		createCell(wb, row, 1, title, font0);
		// 给第二行添加文本
		createCell(wb, row1, 0, "序号", font1);
		createCell(wb, row1, 1, "姓名", font1);
		createCell(wb, row1, 2, "性别", font1);
		createCell(wb, row1, 3, "部门", font1);
		createCell(wb, row1, 4, "饭票卡号", font1);
		createCell(wb, row1, 5, "最后补助时间", font1);
		createCell(wb, row1, 6, "补助票数(张)", font1);
		
		
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messBuyTicketOrders.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessBuyTicketOrder messBuyTicketOrder = messBuyTicketOrders.get(i);
			createCell(wb, rowData, 0, String.valueOf(i + 1), font1);
			createCell(wb, rowData, 1, delWithColumn(messBuyTicketOrder.getName()), font1);
			if(messBuyTicketOrder.getSex() == 0){
				createCell(wb, rowData, 2, delWithColumn("女"), font1);
			}else{
				createCell(wb, rowData, 2, delWithColumn("男"), font1);
			}
			createCell(wb, rowData, 3, delWithColumn(messBuyTicketOrder.getDepartment()) , font1);
			createCell(wb, rowData, 4, delWithColumn(messBuyTicketOrder.getCardCode()), font1);
			createCell(wb, rowData, 5, delWithColumn(time("yyyy-MM-dd",messBuyTicketOrder.getTime().toString())), font1);
			createCell(wb, rowData, 6, delWithColumn(messBuyTicketOrder.getBuyNum()), font1);
			
		}
		return wb;
	}
	
	/**
	 * 设置字体
	 * 
	 * @param wb
	 * @return
	 */
	public static Font createFonts(Workbook wb, short bold, String fontName, boolean isItalic, short hight) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeight(hight);
		return font;
	}

	/**
	 * 创建单元格并设置样式,值
	 * 
	 * @param wb
	 * @param row
	 * @param column
	 * @param
	 * @param
	 * @param value
	 */
	public static void createCell(Workbook wb, Row row, int column, String value, Font font) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	}

	private static String delWithColumn(Object obj) {
		if (CommonUtil.isEmpty(obj)) {
			return "";
		} else {
			return obj.toString();
		}
	}
	public String time(String str,String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
		SimpleDateFormat sdf2 = new SimpleDateFormat (str, Locale.UK);
	    Date date = null;
		try {			
			date = sdf.parse(strDate);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf2.format(date);
	}

	@Override
	public Page<MessBuyTicketOrder> getSubsidyTicketOrderPageMainId(Page<MessBuyTicketOrder> page, Integer mainId,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messBuyTicketOrderMapper.getSubsidyTicketOrderPageMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessBuyTicketOrder> selectSubsidyTicket(Page<MessBuyTicketOrder> page, Map<String, Object> params,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messBuyTicketOrderMapper.selectSubsidyTicket(params) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> exportsSubsidyTicket(Map <String,Object> params) {
		// TODO Auto-generated method stub
		Map<String, Object> msg = new HashMap<>();
		boolean result = true;
		String message = "生成成功！";
		try {
			List<MessBuyTicketOrder> messBuyTicketOrders = messBuyTicketOrderMapper.selectSubsidyTicket(params);
			String title = "补助记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
			HSSFWorkbook book = exportExcelForRecoding2(messBuyTicketOrders, title);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "补助记录导出excel失败！");
			logger.error("补助记录导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int buyTicket(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessBasisSet messBasisSet = 
				messBasisSetMapper.getMessBasisSetByMainId(Integer.valueOf(params.get("mainId").toString()));
		MessCard messCard = messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		if(messCard.getGroupId() != null){
			MessCardGroup messCardGroup = 
					messCardGroupMapper.selectByPrimaryKey(messCard.getGroupId());
			JSONObject jsonObject = (JSONObject) JSON.parse(messCardGroup.getAuthority());
			if(jsonObject.getInteger("bitUse").equals(0)){
				if(messBasisSet.getBitUniversal() == 0){
					messBasisSet.setUniversalPrice(jsonObject.getDouble("universalPrice"));
				}else{
					messBasisSet.setBreakfastPrice(jsonObject.getDouble("breakfastPrice"));
					messBasisSet.setDinnerPrice(jsonObject.getDouble("dinnerPrice"));
					messBasisSet.setLunchPrice(jsonObject.getDouble("lunchPrice"));
					messBasisSet.setNightPrice(jsonObject.getDouble("nightPrice"));
				}
			}
		}
		
		Integer breakfastNum = 0;
		Integer lunchNum = 0;
		Integer dinnerNum = 0;
		Integer nightNum = 0;
		Integer universalNum = 0;
		if(messBasisSet.getBitUniversal() == 1){
			universalNum = 0;
			breakfastNum = Integer.valueOf(params.get("breakfastNum").toString());
			lunchNum = Integer.valueOf(params.get("lunchNum").toString());
			dinnerNum = Integer.valueOf(params.get("dinnerNum").toString());
			nightNum = Integer.valueOf(params.get("nightNum").toString());
		}else{
			universalNum = Integer.valueOf(params.get("universalNum").toString());
			breakfastNum = 0;
			lunchNum = 0;
			dinnerNum = 0;
			nightNum = 0;
		}
		int data = 0;
		//消费流水表
		MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
		messConsumerDetail.setMainId(messCard.getMainId());
		messConsumerDetail.setCardId(messCard.getId());
		messConsumerDetail.setTime(new Date());
		messConsumerDetail.setTableType(1);
		messConsumerDetail.setBitSubsidy(1);
		messConsumerDetail.setOnLine(0);
		Double cdMoney = 0.0;
		Integer ticketNum = breakfastNum + lunchNum + dinnerNum + nightNum + universalNum;
		messConsumerDetail.setTicketNum(ticketNum);
		Double money = messCard.getMoney();
		if(breakfastNum > 0){
			data = 1;
			MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
			messBuyTicketOrder.setBitSubsidy(1);
			messBuyTicketOrder.setBuyLaterNum(messCard.getBreakfastNum() + breakfastNum);
			messBuyTicketOrder.setBuyNum(breakfastNum);
			messBuyTicketOrder.setBuyType(0);
			messBuyTicketOrder.setCardCode(messCard.getCardCode());
			messBuyTicketOrder.setCardId(messCard.getId());
			messBuyTicketOrder.setDepartment(messCard.getDepartment());
			messBuyTicketOrder.setMainId(messCard.getMainId());
			messBuyTicketOrder.setMemberId(messCard.getMemberId());
			messBuyTicketOrder.setMoney(messBasisSet.getBreakfastPrice() * breakfastNum);
			messBuyTicketOrder.setName(messCard.getName());
			messBuyTicketOrder.setOrderNo("ST"+System.currentTimeMillis());
			messBuyTicketOrder.setSex(messCard.getSex());
			messBuyTicketOrder.setTime(new Date());
			messBuyTicketOrder.setTicketType(0);
			messBuyTicketOrder.setDepId(messCard.getDepId());
			messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
			money -= messBasisSet.getBreakfastPrice() * breakfastNum;
			messCard.setBreakfastNum(messCard.getBreakfastNum() + breakfastNum);
			
			cdMoney += messBasisSet.getBreakfastPrice() * breakfastNum;
			messConsumerDetail.setMoney(messBasisSet.getBreakfastPrice() * breakfastNum);
			messConsumerDetail.setBreakfastNum(breakfastNum);
			messConsumerDetail.setBreakfastPrice(messBasisSet.getBreakfastPrice());
			
			for (int i = 0; i < breakfastNum; i++) {
				MessCardTicket messCardTicket = new MessCardTicket();
				messCardTicket.setCardId(messCard.getId());
				messCardTicket.setMainId(messCard.getMainId());
				messCardTicket.setDay(messBasisSet.getTicketDay());
				messCardTicket.setIsfree(1);
				messCardTicket.setPrice(messBasisSet.getBreakfastPrice());
				messCardTicket.setStatus(1);
				messCardTicket.setTicketCode("GT" + System.currentTimeMillis());
				messCardTicket.setTicketType(0);
				messCardTicket.setTime(new Date());
				messCardTicketMapper.insertSelective(messCardTicket);
			}
		}else{
			messConsumerDetail.setBreakfastNum(0);
			messConsumerDetail.setBreakfastPrice(0.0);
		}
		if(lunchNum > 0){
			data = 1;
			MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
			messBuyTicketOrder.setBitSubsidy(1);
			messBuyTicketOrder.setBuyLaterNum(messCard.getLunchNum() + lunchNum);
			messBuyTicketOrder.setBuyNum(lunchNum);
			messBuyTicketOrder.setBuyType(0);
			messBuyTicketOrder.setCardCode(messCard.getCardCode());
			messBuyTicketOrder.setCardId(messCard.getId());
			messBuyTicketOrder.setDepartment(messCard.getDepartment());
			messBuyTicketOrder.setMainId(messCard.getMainId());
			messBuyTicketOrder.setMemberId(messCard.getMemberId());
			messBuyTicketOrder.setMoney(messBasisSet.getLunchPrice() * lunchNum);
			messBuyTicketOrder.setName(messCard.getName());
			messBuyTicketOrder.setOrderNo("ST"+System.currentTimeMillis());
			messBuyTicketOrder.setSex(messCard.getSex());
			messBuyTicketOrder.setTime(new Date());
			messBuyTicketOrder.setTicketType(1);
			messBuyTicketOrder.setDepId(messCard.getDepId());
			messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
			money -= messBasisSet.getLunchPrice() * lunchNum;
			messCard.setLunchNum(messCard.getLunchNum() + lunchNum);
			
			cdMoney += messBasisSet.getLunchPrice() * lunchNum;
			messConsumerDetail.setLunchNum(lunchNum);
			messConsumerDetail.setLunchPrice(messBasisSet.getLunchPrice());
			
			for (int i = 0; i < lunchNum; i++) {
				MessCardTicket messCardTicket = new MessCardTicket();
				messCardTicket.setCardId(messCard.getId());
				messCardTicket.setMainId(messCard.getMainId());
				messCardTicket.setDay(messBasisSet.getTicketDay());
				messCardTicket.setIsfree(1);
				messCardTicket.setPrice(messBasisSet.getLunchPrice());
				messCardTicket.setStatus(1);
				messCardTicket.setTicketCode("GT" + System.currentTimeMillis());
				messCardTicket.setTicketType(1);
				messCardTicket.setTime(new Date());
				messCardTicketMapper.insertSelective(messCardTicket);
			}
		}else{
			messConsumerDetail.setLunchNum(0);
			messConsumerDetail.setLunchPrice(0.0);
		}
		if(dinnerNum > 0){
			data = 1;
			MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
			messBuyTicketOrder.setBitSubsidy(1);
			messBuyTicketOrder.setBuyLaterNum(messCard.getDinnerNum() + dinnerNum);
			messBuyTicketOrder.setBuyNum(dinnerNum);
			messBuyTicketOrder.setBuyType(0);
			messBuyTicketOrder.setCardCode(messCard.getCardCode());
			messBuyTicketOrder.setCardId(messCard.getId());
			messBuyTicketOrder.setDepartment(messCard.getDepartment());
			messBuyTicketOrder.setMainId(messCard.getMainId());
			messBuyTicketOrder.setMemberId(messCard.getMemberId());
			messBuyTicketOrder.setMoney(messBasisSet.getDinnerPrice() * dinnerNum);
			messBuyTicketOrder.setName(messCard.getName());
			messBuyTicketOrder.setOrderNo("ST"+System.currentTimeMillis());
			messBuyTicketOrder.setSex(messCard.getSex());
			messBuyTicketOrder.setTime(new Date());
			messBuyTicketOrder.setTicketType(2);
			messBuyTicketOrder.setDepId(messCard.getDepId());
			messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
			money -= messBasisSet.getDinnerPrice() * dinnerNum;
			messCard.setDinnerNum(messCard.getDinnerNum() + dinnerNum);
			
			cdMoney += messBasisSet.getDinnerPrice() * dinnerNum;
			messConsumerDetail.setDinnerNum(dinnerNum);
			messConsumerDetail.setDinnerPrice(messBasisSet.getDinnerPrice());
			
			for (int i = 0; i < dinnerNum; i++) {
				MessCardTicket messCardTicket = new MessCardTicket();
				messCardTicket.setCardId(messCard.getId());
				messCardTicket.setMainId(messCard.getMainId());
				messCardTicket.setDay(messBasisSet.getTicketDay());
				messCardTicket.setIsfree(1);
				messCardTicket.setPrice(messBasisSet.getDinnerPrice());
				messCardTicket.setStatus(1);
				messCardTicket.setTicketCode("GT" + System.currentTimeMillis());
				messCardTicket.setTicketType(2);
				messCardTicket.setTime(new Date());
				messCardTicketMapper.insertSelective(messCardTicket);
			}
		}else{
			messConsumerDetail.setDinnerNum(0);
			messConsumerDetail.setDinnerPrice(0.0);
		}
		if(nightNum > 0){
			data = 1;
			MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
			messBuyTicketOrder.setBitSubsidy(1);
			messBuyTicketOrder.setBuyLaterNum(messCard.getNightNum() + nightNum);
			messBuyTicketOrder.setBuyNum(nightNum);
			messBuyTicketOrder.setBuyType(0);
			messBuyTicketOrder.setCardCode(messCard.getCardCode());
			messBuyTicketOrder.setCardId(messCard.getId());
			messBuyTicketOrder.setDepartment(messCard.getDepartment());
			messBuyTicketOrder.setMainId(messCard.getMainId());
			messBuyTicketOrder.setMemberId(messCard.getMemberId());
			messBuyTicketOrder.setMoney(messBasisSet.getNightPrice() * nightNum);
			messBuyTicketOrder.setName(messCard.getName());
			messBuyTicketOrder.setOrderNo("ST"+System.currentTimeMillis());
			messBuyTicketOrder.setSex(messCard.getSex());
			messBuyTicketOrder.setTime(new Date());
			messBuyTicketOrder.setTicketType(3);
			messBuyTicketOrder.setDepId(messCard.getDepId());
			messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
			money -= messBasisSet.getNightPrice() * nightNum;
			messCard.setNightNum(messCard.getNightNum() + nightNum);
			
			cdMoney += messBasisSet.getNightPrice() * nightNum;
			messConsumerDetail.setNightNum(nightNum);
			messConsumerDetail.setNightPrice(messBasisSet.getNightPrice());
			
			for (int i = 0; i < nightNum; i++) {
				MessCardTicket messCardTicket = new MessCardTicket();
				messCardTicket.setCardId(messCard.getId());
				messCardTicket.setMainId(messCard.getMainId());
				messCardTicket.setDay(messBasisSet.getTicketDay());
				messCardTicket.setIsfree(1);
				messCardTicket.setPrice(messBasisSet.getNightPrice());
				messCardTicket.setStatus(1);
				messCardTicket.setTicketCode("GT" + System.currentTimeMillis());
				messCardTicket.setTicketType(3);
				messCardTicket.setTime(new Date());
				messCardTicketMapper.insertSelective(messCardTicket);
			}
		}else{
			messConsumerDetail.setNightNum(0);
			messConsumerDetail.setNightPrice(0.0);
		}
		if(universalNum > 0){
			data = 1;
			MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
			messBuyTicketOrder.setBitSubsidy(1);
			messBuyTicketOrder.setBuyLaterNum(messCard.getUniversalNum() + universalNum);
			messBuyTicketOrder.setBuyNum(universalNum);
			messBuyTicketOrder.setBuyType(0);
			messBuyTicketOrder.setCardCode(messCard.getCardCode());
			messBuyTicketOrder.setCardId(messCard.getId());
			messBuyTicketOrder.setDepartment(messCard.getDepartment());
			messBuyTicketOrder.setMainId(messCard.getMainId());
			messBuyTicketOrder.setMemberId(messCard.getMemberId());
			messBuyTicketOrder.setMoney(messBasisSet.getUniversalPrice() * universalNum);
			messBuyTicketOrder.setName(messCard.getName());
			messBuyTicketOrder.setOrderNo("ST"+System.currentTimeMillis());
			messBuyTicketOrder.setSex(messCard.getSex());
			messBuyTicketOrder.setTime(new Date());
			messBuyTicketOrder.setTicketType(4);
			messBuyTicketOrder.setDepId(messCard.getDepId());
			messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
			money -= messBasisSet.getUniversalPrice() * universalNum;
			messCard.setUniversalNum(messCard.getUniversalNum() + universalNum);
			
			cdMoney += messBasisSet.getUniversalPrice() * universalNum;
			messConsumerDetail.setUniversalNum(universalNum);
			messConsumerDetail.setUniversalPrice(messBasisSet.getUniversalPrice());
			
			for (int i = 0; i < universalNum; i++) {
				MessCardTicket messCardTicket = new MessCardTicket();
				messCardTicket.setCardId(messCard.getId());
				messCardTicket.setMainId(messCard.getMainId());
				messCardTicket.setDay(messBasisSet.getTicketDay());
				messCardTicket.setIsfree(1);
				messCardTicket.setPrice(messBasisSet.getUniversalPrice());
				messCardTicket.setStatus(1);
				messCardTicket.setTicketCode("GT" + System.currentTimeMillis());
				messCardTicket.setTicketType(4);
				messCardTicket.setTime(new Date());
				messCardTicketMapper.insertSelective(messCardTicket);
			}
		}else{
			messConsumerDetail.setUniversalNum(0);
			messConsumerDetail.setUniversalPrice(0.0);
		}
		if(cdMoney > 0){
			messConsumerDetail.setMoney(cdMoney);
		}else{
			throw new BaseException("卡上余额不足");
		}
		messConsumerDetailMapper.insertSelective(messConsumerDetail);
		if(data == 0){
			return -1;
		}
		if(money < 0){
			throw new BaseException("卡上余额不足");
		}
		messCard.setMoney(money);
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}	
}
