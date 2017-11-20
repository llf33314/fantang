package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.MessCancelTicketMapper;
import com.gt.mess.entity.MessCancelTicket;
import com.gt.mess.service.MessCancelTicketService;
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
public class MessCancelTicketServiceImpl implements MessCancelTicketService{

	private Logger logger = Logger.getLogger(MessCancelTicketServiceImpl.class);
	
	@Autowired
	private MessCancelTicketMapper messCancelTicketMapper;
	
	@Override
	public Page<MessCancelTicket> getMessCancelTicketPageByMainId(Page<MessCancelTicket> page, Integer mainId,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCancelTicketMapper.getMessCancelTicketPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessCancelTicket> selectCancelRecord(Page<MessCancelTicket> page, Map<String, Object> params,
			Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCancelTicketMapper.selectCancelRecord(params) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getMessCancelTicketPageByMainIdNew(Integer mainId, int nums) {
		return messCancelTicketMapper.getMessCancelTicketPageByMainIdNew(mainId, nums);
	}

	@Override
	public Map<String, Object> exports(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Map<String, Object> msg = new HashMap<>();
		boolean result = true;
		String message = "生成成功！";
		try {
			List<MessCancelTicket> messCancelTickets = messCancelTicketMapper.selectCancelRecord(params);
			String title = "核销记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
			HSSFWorkbook book = exportExcelForRecoding(messCancelTickets, title);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "核销记录导出excel失败！");
			logger.error("核销记录导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}
	
	private HSSFWorkbook exportExcelForRecoding(List<MessCancelTicket> messCancelTickets, String title) {
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
		createCell(wb, row1, 1, "饭票卡号", font1);
		createCell(wb, row1, 2, "姓名", font1);
		createCell(wb, row1, 3, "性别", font1);
		createCell(wb, row1, 4, "部门", font1);
		createCell(wb, row1, 5, "份数(份)", font1);
		createCell(wb, row1, 6, "金额(元)", font1);
		createCell(wb, row1, 7, "核销时间", font1);
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messCancelTickets.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessCancelTicket messCancelTicket = messCancelTickets.get(i);
			createCell(wb, rowData, 0, String.valueOf(i + 1), font1);
			createCell(wb, rowData, 1, delWithColumn(messCancelTicket.getCardCode()), font1);
			createCell(wb, rowData, 2, delWithColumn(messCancelTicket.getName()), font1);
			if(messCancelTicket.getSex() == 0){
				createCell(wb, rowData, 3, delWithColumn("女"), font1);
			}else{
				createCell(wb, rowData, 3, delWithColumn("男"), font1);
			}
			createCell(wb, rowData, 4, delWithColumn(messCancelTicket.getDepartment()) , font1);
			createCell(wb, rowData, 5, delWithColumn(messCancelTicket.getTicketNum()), font1);
			createCell(wb, rowData, 6, delWithColumn(messCancelTicket.getMoney()), font1);
			createCell(wb, rowData, 7, delWithColumn(time("yyyy-MM-dd hh:mm:ss",messCancelTicket.getTime().toString())), font1);
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

}
