package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessOldManCardOrderMapper;
import com.gt.mess.entity.MessOldManCardOrder;
import com.gt.mess.service.MessOldManCardOrderService;
import com.gt.mess.util.CommonUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class MessOldManCardOrderServiceImpl implements MessOldManCardOrderService{

	private Logger logger = Logger.getLogger(MessOldManCardOrderServiceImpl.class);
	
	@Autowired
	private MessOldManCardOrderMapper messOldManCardOrderMapper;
	
	@Override
	public Page<MessOldManCardOrder> getMessOldManCardOrderPageByMainId(Page<MessOldManCardOrder> page, Integer mainId,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardOrderMapper.getMessOldManCardOrderPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessOldManCardOrder> selectMessOldManCardOrder(Page<MessOldManCardOrder> page,
			Map<String, Object> params, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardOrderMapper.selectMessOldManCardOrder(params) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessOldManCardOrder> getCommonMessCardOrderPageByMainId(Page<MessOldManCardOrder> page, Integer mainId,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardOrderMapper.getCommonMessCardOrderPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessOldManCardOrder> selectMessCommonCardOrder(Page<MessOldManCardOrder> page,
			Map<String, Object> params, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardOrderMapper.selectMessCommonCardOrder(params) );
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
			List<MessOldManCardOrder> MessOldManCardOrders = null;
			String title = "";
			Integer type = Integer.valueOf(params.get("type").toString());
			if(type.equals(3)){
				title = "普通卡手动核销记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
				MessOldManCardOrders = 
						messOldManCardOrderMapper.selectMessCommonCardOrder(params);
			}else if(type.equals(0)){
				title = "老人卡手动核销记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
				MessOldManCardOrders = 
						messOldManCardOrderMapper.selectMessOldManCardOrder(params);
			}else if(type.equals(1)){
				title = "老人卡补票、扣票记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
				MessOldManCardOrders = 
						messOldManCardOrderMapper.selectMessOldManCardOrder2(params);
			}
			HSSFWorkbook book = exportExcelForRecoding(MessOldManCardOrders, title,type);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "加餐核销记录导出excel失败！");
			logger.error("加餐核销记录导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}

	private HSSFWorkbook exportExcelForRecoding(List<MessOldManCardOrder> messOldManCardOrders, String title,Integer type) {
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
		createCell(wb, row1, 5, "下单时间", font1);
		createCell(wb, row1, 6, "份数", font1);
		createCell(wb, row1, 7, "订单类型", font1);
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messOldManCardOrders.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessOldManCardOrder messOldManCardOrder = messOldManCardOrders.get(i);
			createCell(wb, rowData, 0, String.valueOf(i + 1), font1);
			createCell(wb, rowData, 1, delWithColumn(messOldManCardOrder.getName()), font1);
			if(messOldManCardOrder.getSex() == 0){
				createCell(wb, rowData, 2, delWithColumn("女"), font1);
			}else{
				createCell(wb, rowData, 2, delWithColumn("男"), font1);
			}
			createCell(wb, rowData, 3, delWithColumn(messOldManCardOrder.getDepartment()) , font1);
			createCell(wb, rowData, 4, delWithColumn(messOldManCardOrder.getCardCode()), font1);
			createCell(wb, rowData, 5, delWithColumn(time("yyyy-MM-dd hh:mm:ss",messOldManCardOrder.getTime().toString())), font1);
			createCell(wb, rowData, 6, delWithColumn(messOldManCardOrder.getNum()), font1);
			if(type == 3){
				if(messOldManCardOrder.getMealType() == 0){
					createCell(wb, rowData, 7, delWithColumn("早餐"), font1);
				}else if(messOldManCardOrder.getMealType() == 1){
					createCell(wb, rowData, 7, delWithColumn("午餐"), font1);
				}else if(messOldManCardOrder.getMealType() == 2){
					createCell(wb, rowData, 7, delWithColumn("晚餐"), font1);
				}else if(messOldManCardOrder.getMealType() == 3){
					createCell(wb, rowData, 7, delWithColumn("夜宵"), font1);
				}else {
					createCell(wb, rowData, 7, delWithColumn("通用"), font1);
				}
			}else{
				if(messOldManCardOrder.getType() == 0){
					createCell(wb, rowData, 7, delWithColumn("扣票"), font1);
				}else if(messOldManCardOrder.getType() == 1){
					createCell(wb, rowData, 7, delWithColumn("补票"), font1);
				}else if(messOldManCardOrder.getType() == 2){
					createCell(wb, rowData, 7, delWithColumn("核销"), font1);
				}
			}
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
	public Page<MessOldManCardOrder> getMessOldManCardOrderPageByMainId2(Page<MessOldManCardOrder> page, Integer mainId,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardOrderMapper.getMessOldManCardOrderPageByMainId2(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessOldManCardOrder> selectMessOldManCardOrder2(Page<MessOldManCardOrder> page,
			Map<String, Object> params, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messOldManCardOrderMapper.selectMessOldManCardOrder2(params) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
