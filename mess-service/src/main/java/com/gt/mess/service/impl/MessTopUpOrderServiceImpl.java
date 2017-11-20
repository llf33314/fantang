package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessCardMapper;
import com.gt.mess.dao.MessConsumerDetailMapper;
import com.gt.mess.dao.MessTopUpOrderMapper;
import com.gt.mess.entity.MessCard;
import com.gt.mess.entity.MessConsumerDetail;
import com.gt.mess.entity.MessTopUpOrder;
import com.gt.mess.service.MessTopUpOrderService;
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
public class MessTopUpOrderServiceImpl implements MessTopUpOrderService{

	private Logger logger = Logger.getLogger(MessTopUpOrderServiceImpl.class);
	
	@Autowired
	private MessTopUpOrderMapper messTopUpOrderMapper;
	
	@Autowired
	private MessCardMapper messCardMapper;
	
	@Autowired
	private MessConsumerDetailMapper messConsumerDetailMapper;
	
	@Override
	public Page<MessTopUpOrder> getMessTopUpOrderPageByMainId(Page<MessTopUpOrder> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messTopUpOrderMapper.getMessTopUpOrderPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessTopUpOrder> selectTopUpOrder(Page<MessTopUpOrder> page, Map<String, Object> params, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messTopUpOrderMapper.selectTopUpOrder(params) );
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
			List<MessTopUpOrder> MessTopUpOrders = messTopUpOrderMapper.selectTopUpOrder(params);
			String title = "充值记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
			HSSFWorkbook book = exportExcelForRecoding(MessTopUpOrders, title);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "充值记录导出excel失败！");
			logger.error("充值记录导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}

	private HSSFWorkbook exportExcelForRecoding(List<MessTopUpOrder> messTopUpOrders, String title) {
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
		createCell(wb, row1, 1, "流水号", font1);
		createCell(wb, row1, 2, "姓名", font1);
		createCell(wb, row1, 3, "性别", font1);
		createCell(wb, row1, 4, "部门", font1);
		createCell(wb, row1, 5, "饭票卡号", font1);
		createCell(wb, row1, 6, "充值时间", font1);
		createCell(wb, row1, 7, "充值金额(元)", font1);
		createCell(wb, row1, 8, "充值后余额(元)", font1);
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messTopUpOrders.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessTopUpOrder messTopUpOrder = messTopUpOrders.get(i);
			createCell(wb, rowData, 0, String.valueOf(i + 1), font1);
			createCell(wb, rowData, 1, delWithColumn(messTopUpOrder.getOrderNo()), font1);
			createCell(wb, rowData, 2, delWithColumn(messTopUpOrder.getName()), font1);
			if(messTopUpOrder.getSex() == 0){
				createCell(wb, rowData, 3, delWithColumn("女"), font1);
			}else{
				createCell(wb, rowData, 3, delWithColumn("男"), font1);
			}
			createCell(wb, rowData, 4, delWithColumn(messTopUpOrder.getDepartment()) , font1);
			createCell(wb, rowData, 5, delWithColumn(messTopUpOrder.getCardCode()), font1);
			createCell(wb, rowData, 6, delWithColumn(time("yyyy-MM-dd",messTopUpOrder.getTime().toString())), font1);
			createCell(wb, rowData, 7, delWithColumn(messTopUpOrder.getMoney()), font1);
			createCell(wb, rowData, 8, delWithColumn(messTopUpOrder.getLaterMoney()), font1);
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
	public MessTopUpOrder getMessTopUpOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		try {
			return messTopUpOrderMapper.getMessTopUpOrderByOrderNo(orderNo);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional
	@Override
	public int update(MessTopUpOrder messTopUpOrder) throws Exception {
		// TODO Auto-generated method stub
		return messTopUpOrderMapper.updateByPrimaryKeySelective(messTopUpOrder);
	}

	@Transactional
	@Override
	public Map<String, Object> topUpPay(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		Double money = messCard.getMoney() + Double.valueOf(params.get("money").toString());
//		messCard.setMoney(money);

		//消费流水表
		MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
		messConsumerDetail.setMainId(messCard.getMainId());
		messConsumerDetail.setCardId(messCard.getId());
		messConsumerDetail.setTime(new Date());
		messConsumerDetail.setTableType(3);
		messConsumerDetail.setBitSubsidy(1);
		messConsumerDetail.setOnLine(0);
		messConsumerDetail.setBreakfastNum(0);
		messConsumerDetail.setLunchNum(0);
		messConsumerDetail.setDinnerNum(0);
		messConsumerDetail.setNightNum(0);
		messConsumerDetail.setUniversalNum(0);
		messConsumerDetail.setStatus(1);
		messConsumerDetail.setMoney(Double.valueOf(params.get("money").toString()));
		
		MessTopUpOrder messTopUpOrder = new MessTopUpOrder();
		messTopUpOrder.setCardCode(messCard.getCardCode());
		messTopUpOrder.setCardId(messCard.getId());
		messTopUpOrder.setDepartment(messCard.getDepartment());
		messTopUpOrder.setLaterMoney(money);
		messTopUpOrder.setMainId(messCard.getMainId());
		messTopUpOrder.setMemberId(messCard.getMemberId());
		messTopUpOrder.setMoney(Double.valueOf(params.get("money").toString()));
		messTopUpOrder.setName(messCard.getName());
		messTopUpOrder.setOrderNo("ST"+System.currentTimeMillis());
		messTopUpOrder.setSex(messCard.getSex());
		messTopUpOrder.setStatus(1);
		messTopUpOrder.setTime(new Date());
		messTopUpOrder.setType(1);
		messTopUpOrder.setDepId(messCard.getDepId());
		int data = messTopUpOrderMapper.insertSelective(messTopUpOrder);
		data = messConsumerDetailMapper.insertSelective(messConsumerDetail);
		Map<String,Object> mapObj = new HashMap<String, Object>();
		//messCardMapper.updateByPrimaryKeySelective(messCard)
		mapObj.put("data", data);
		mapObj.put("orderNo", messTopUpOrder.getOrderNo());
		mapObj.put("detailId", messConsumerDetail.getId());
		return mapObj;
	}

}
