package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.*;
import com.gt.mess.entity.*;
import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessMealOrderService;
import com.gt.mess.util.CommonUtil;
import com.gt.mess.util.DateTimeKit;
import com.gt.mess.util.RedisCacheUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessMealOrderServiceImpl implements MessMealOrderService{

	private Logger logger = Logger.getLogger(MessMealOrderServiceImpl.class);
	
	@Autowired
	private MessMealOrderMapper messMealOrderMapper;
	
	@Autowired
	private MessBasisSetMapper messBasisSetMapper;
	
	@Autowired
	private MessCardMapper messCardMapper;
	
	@Autowired
	private MessCardTicketMapper messCardTicketMapper;
	
	@Autowired
	private MessConsumerDetailMapper messConsumerDetailMapper;
	
	@Autowired
	private MessMealOrderInfoMapper messMealOrderInfoMapper;
	
	@Autowired
	private RedisCacheUtil redisCacheUtil;
	
	@Override
	public Page<MessMealOrder> getMessMealOrderPageByMainId(Page<MessMealOrder> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messMealOrderMapper.getMessMealOrderPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessMealOrder> selectMealOrder(Page<MessMealOrder> page, Map<String, Object> params, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords(  messMealOrderMapper.selectMealOrder(params) );
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
			List<MessMealOrder> messMealOrders = messMealOrderMapper.selectMealOrder(params);
			String title = "订餐记录  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
			Workbook book = exportExcelForRecoding(messMealOrders, title);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "订餐记录导出excel失败！");
			logger.error("订餐记录导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}
	
	private Workbook exportExcelForRecoding(List<MessMealOrder> messMealOrders, String title) {
		// 创建excel文件对象
		Workbook wb = new SXSSFWorkbook(1000);
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
		sheet.setColumnWidth((short) (9), (short) 6000);
		sheet.setColumnWidth((short) (10), (short) 6000);
		sheet.setColumnWidth((short) (11), (short) 6000);
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
		createCell(wb, row1, 5, "订餐时间", font1);
		createCell(wb, row1, 6, "订餐类型", font1);
		createCell(wb, row1, 7, "订单状态", font1);
		createCell(wb, row1, 8, "份数", font1);
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messMealOrders.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessMealOrder messMealOrder = messMealOrders.get(i);
			createCell(wb, rowData, 0, String.valueOf(i + 1), font1);
			createCell(wb, rowData, 1, delWithColumn(messMealOrder.getName()), font1);
			if(messMealOrder.getSex() == 0){
				createCell(wb, rowData, 2, delWithColumn("女"), font1);
			}else{
				createCell(wb, rowData, 2, delWithColumn("男"), font1);
			}
			createCell(wb, rowData, 3, delWithColumn(messMealOrder.getDepartment()) , font1);
			createCell(wb, rowData, 4, delWithColumn(messMealOrder.getCardCode()), font1);
			createCell(wb, rowData, 5, delWithColumn(time("yyyy-MM-dd",messMealOrder.getTime().toString())), font1);
			if(messMealOrder.getMealType() == 0){
				createCell(wb, rowData, 6, delWithColumn("早餐"), font1);
			}else if(messMealOrder.getMealType() == 1){
				createCell(wb, rowData, 6, delWithColumn("午餐"), font1);
			}else if(messMealOrder.getMealType() == 2){
				createCell(wb, rowData, 6, delWithColumn("晚餐"), font1);
			}else if(messMealOrder.getMealType() == 3){
				createCell(wb, rowData, 6, delWithColumn("宵夜"), font1);
			}
			if(messMealOrder.getStatus() == 0){
				createCell(wb, rowData, 7, delWithColumn("未选餐"), font1);
			}else if(messMealOrder.getStatus() == 1){
				createCell(wb, rowData, 7, delWithColumn("已预订"), font1);
			}else if(messMealOrder.getStatus() == 2){
				createCell(wb, rowData, 7, delWithColumn("已取消"), font1);
			}else if(messMealOrder.getStatus() == 4){
				createCell(wb, rowData, 7, delWithColumn("已取餐"), font1);
			}else if(messMealOrder.getStatus() == 5){
				createCell(wb, rowData, 7, delWithColumn("已过期"), font1);
			}
			createCell(wb, rowData, 8, delWithColumn(messMealOrder.getMealNum()), font1);
		}
		return wb;
	}

	/**
	 * 月总导出
	 * @param messMealOrders
	 * @param title
	 * @return
	 */
	private Workbook exportExcelForRecodingMonth(List<MessMealOrder> messMealOrders, String title,Map<String, Object> params) {
		// 创建excel文件对象
		Workbook wb = new SXSSFWorkbook(1000);
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
		sheet.setColumnWidth((short) (3), (short) 2000);
		sheet.setColumnWidth((short) (4), (short) 2000);
		sheet.setColumnWidth((short) (5), (short) 2000);
		sheet.setColumnWidth((short) (6), (short) 2000);
		sheet.setColumnWidth((short) (7), (short) 2000);
		sheet.setColumnWidth((short) (8), (short) 2000);
		sheet.setColumnWidth((short) (9), (short) 2000);
		sheet.setColumnWidth((short) (10), (short) 2000);
		sheet.setColumnWidth((short) (11), (short) 2000);
		sheet.setColumnWidth((short) (12), (short) 2000);
		sheet.setColumnWidth((short) (13), (short) 2000);
		sheet.setColumnWidth((short) (14), (short) 2000);
		sheet.setColumnWidth((short) (15), (short) 2000);
		sheet.setColumnWidth((short) (16), (short) 2000);
		sheet.setColumnWidth((short) (17), (short) 2000);
		sheet.setColumnWidth((short) (18), (short) 2000);
		sheet.setColumnWidth((short) (19), (short) 2000);
		sheet.setColumnWidth((short) (20), (short) 2000);
		sheet.setColumnWidth((short) (21), (short) 2000);
		sheet.setColumnWidth((short) (22), (short) 2000);
		sheet.setColumnWidth((short) (23), (short) 2000);
		sheet.setColumnWidth((short) (24), (short) 2000);
		sheet.setColumnWidth((short) (25), (short) 2000);
		sheet.setColumnWidth((short) (26), (short) 2000);
		sheet.setColumnWidth((short) (27), (short) 2000);
		sheet.setColumnWidth((short) (28), (short) 2000);
		sheet.setColumnWidth((short) (29), (short) 2000);
		sheet.setColumnWidth((short) (30), (short) 2000);
		sheet.setColumnWidth((short) (31), (short) 2000);
		sheet.setColumnWidth((short) (32), (short) 2000);
		sheet.setColumnWidth((short) (33), (short) 2000);
		sheet.setColumnWidth((short) (34), (short) 2000);
		sheet.setColumnWidth((short) (35), (short) 2000);
		sheet.setColumnWidth((short) (36), (short) 2000);
		sheet.setColumnWidth((short) (37), (short) 2000);
		
		// 文件头字体
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false, (short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false, (short) 200);
		// 合并第一行的单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
		
		//获取当月天数
		Calendar a = Calendar.getInstance();  
	    a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
	    a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	    int maxDate = a.get(Calendar.DATE);  
		
		// 设置第一列的文字
		createCell(wb, row, 1, title, font0);
		// 给第二行添加文本
		createCell(wb, row1, 0, "姓名", font1);
		createCell(wb, row1, 1, "部门", font1);
		createCell(wb, row1, 2, "饭票卡号", font1);
		for(int i = 1;i <= maxDate;i++){
			createCell(wb, row1, i+2, i+"日", font1);
		}
		createCell(wb, row1, 2+maxDate, "总份数", font1);
		List<MessCard> messCards = 
				messCardMapper.getMessCardListByMap(params);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		cellStyle.setFont(font1);
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < messCards.size(); i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			MessCard messCard = messCards.get(i);
			createCell2(rowData, 0, delWithColumn(messCard.getName()), cellStyle);
			createCell2(rowData, 1, delWithColumn(messCard.getDepartment()), cellStyle);
			createCell2(rowData, 2, delWithColumn(messCard.getCardCode()), cellStyle);
			Integer mealNums = 0;
			for(int d = 1;d <= maxDate;d++){
				Integer mealNum = 0;
				for(MessMealOrder mealOrder : messMealOrders){
					int day = Integer.valueOf(time("d",mealOrder.getTime().toString()));
					if(mealOrder.getCardCode().equals(messCard.getCardCode())){
						if(mealOrder.getMealType() != 4){
							if(mealOrder.getStatus() == 1 || mealOrder.getStatus() == 4 || mealOrder.getStatus() == 5){
								if(day == d){
									mealNum += mealOrder.getMealNum();
								}
							}
						}
					}
				}
				createCell2(rowData, 2+d, delWithColumn(mealNum), cellStyle);
				mealNums += mealNum;
			}
			createCell2(rowData, 2+maxDate, delWithColumn(mealNums), cellStyle);
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
	 * @param row
	 * @param column
	 * @param
	 * @param
	 * @param value
	 */
	public static void createCell2(Row row, int column, String value, CellStyle cellStyle) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
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
	public List<MessMealOrder> getMessMealOrderListforToday(Integer mainId) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getMessMealOrderListforToday(mainId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public Map<String, Object> getMessMealOrderNum(Integer mainId,Integer messType) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> mapObj = new HashMap<String, Object>();
			mapObj.put("mainId", mainId);
			mapObj.put("messType", messType);
			List<MessMealOrder> messMealOrders =  
					messMealOrderMapper.getMessMealOrderNum(mapObj);
			//人数
			Integer mealOrderNum = 0;
			Integer gainNum = 0;
			Integer notNum = 0;
			
			//份数
			Integer mealOrderMealNum = 0;
			Integer gainMealNum = 0;
			Integer notMealNum = 0;
			
			for(MessMealOrder messMealOrder : messMealOrders){
				if(messMealOrder.getStatus() != 2 && messMealOrder.getStatus() != 0 && messMealOrder.getStatus() != 5){
					mealOrderMealNum += messMealOrder.getMealNum();
					mealOrderNum++;
				}
				if(messMealOrder.getStatus() == 4){
					gainMealNum += messMealOrder.getMealNum();
					gainNum++;
				}else if(messMealOrder.getStatus() == 1){
					notMealNum += messMealOrder.getMealNum();
					notNum++;
				}
			}
			map.put("mealOrderNum", mealOrderNum);
			map.put("notNum", notNum);
			map.put("gainNum", gainNum);
			
			map.put("mealOrderMealNum", mealOrderMealNum);
			map.put("notMealNum", notMealNum);
			map.put("gainMealNum", gainMealNum);
			
			map.put("code", 1);
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", -1);
			return map;
		}
	}

	@Override
	public MessMealOrder getMessMealOrderByMealCode(String mealCode) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getMessMealOrderByMealCode(mealCode).get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public Page<MessMealOrder> getMessMealOrderPageByCardIdAndMainId(Page<MessMealOrder> page,Map<String, Integer> mapId,Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messMealOrderMapper.getMessMealOrderPageByCardIdAndMainId(mapId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<MessMealOrder> getBookedMessMealOrder(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getBookedMessMealOrder(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int saveMealOrder(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		String jedisDays = redisCacheUtil.get("messCarId:"+messCard.getId()).toString();
		if(jedisDays != null){
			if(jedisDays.equals(params.get("days").toString())){
				return 1;
			}
		}
		String [] days = params.get("days").toString().split(",");
		Map<String,Integer> mapId = new HashMap<String, Integer>();
		mapId.put("mainId", messCard.getMainId());
		mapId.put("cardId", messCard.getId());
		List<MessMealOrder> messMealOrders = 
				messMealOrderMapper.getBookedMessMealOrder(mapId);
		int data = 0;
		if(days.length > 0){

			Map<String,Object> countMap = new HashMap<String, Object>();
			countMap.put("mainId", messCard.getMainId());
			countMap.put("cardId", messCard.getId());
			for(String day : days){
				countMap.put("day",null);
				int MealCount = 0;
				if(day != null){
					try {
						countMap.put("day",day.trim());
						MealCount = messMealOrderMapper.selectMealOrderDayCount(countMap);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						MealCount = 0;
					}
				}else{
					continue;
				}
				if(MealCount > 0){
					continue;
				}
				Integer temp = 0;
				for(MessMealOrder messMealOrder : messMealOrders){
					if(day.trim().equals(DateTimeKit.getDateTime(messMealOrder.getTime(), "yyyy-M-d"))){
						temp = 1;
					}
				}
				if(temp == 1){
					data = 1;
					continue;
				}
				if(day.length() < 3){
					return -1;
				}
				MessMealOrder messMealOrder = new MessMealOrder();
				messMealOrder.setCardCode(messCard.getCardCode());
				messMealOrder.setCardId(messCard.getId());
				messMealOrder.setDepartment(messCard.getDepartment());
				messMealOrder.setTime(DateTimeKit.parse(day,"yyyy-MM-dd"));
				messMealOrder.setMainId(messCard.getMainId());
				messMealOrder.setMealType(4);
				messMealOrder.setMemberId(messCard.getMemberId());
				messMealOrder.setName(messCard.getName());
				messMealOrder.setOrderTime(new Date());
				messMealOrder.setSex(messCard.getSex());
				messMealOrder.setStatus(0);
				messMealOrder.setDepId(messCard.getDepId());
				data = messMealOrderMapper.insertSelective(messMealOrder);
			}
		}else{
			return -1;
		}
		redisCacheUtil.set("messCarId:"+messCard.getId(), params.get("days").toString(),60L);
		return data;
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
	public List<MessMealOrder> getNotChooseMessMealOrder(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getNotChooseMessMealOrder(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int saveChooseMealOrder(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		MessBasisSet messBasisSet = 
				messBasisSetMapper.getMessBasisSetByMainId(messCard.getMainId());
		Integer size = Integer.valueOf(params.get("size").toString());	
		Integer mealNum = Integer.valueOf(params.get("mealNum").toString());
		JSONObject json= JSONObject.fromObject(params.get("json").toString());
		for(int i = 0;i < size;i++){
			String mealTypeArr = json.get("mealTypes"+i).toString();
			if("".equals(mealTypeArr)){
				continue;
			}
			MessMealOrder oldMessMealOrder = messMealOrderMapper.selectByPrimaryKey(Integer.valueOf(json.get("id"+i).toString()));
			String [] mealTypes = mealTypeArr.split(",");
			//消费流水表
			MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
			messConsumerDetail.setMainId(messCard.getMainId());
			messConsumerDetail.setCardId(messCard.getId());
			messConsumerDetail.setTime(new Date());
			messConsumerDetail.setTableType(0);
			messConsumerDetail.setBitSubsidy(1);
			messConsumerDetail.setOnLine(0);
			messConsumerDetail.setBreakfastNum(0);
			messConsumerDetail.setLunchNum(0);
			messConsumerDetail.setDinnerNum(0);
			messConsumerDetail.setNightNum(0);
			messConsumerDetail.setUniversalNum(0);
			messConsumerDetail.setStatus(1);
			Integer ticketNum = 0;
//			System.out.println("第"+i+"次"+mealTypes);
			for(String mealTypeStr:mealTypes){
				Integer mealType = Integer.valueOf(mealTypeStr);
				Date messOrderDate = DateTimeKit.addDate(oldMessMealOrder.getTime(), -messBasisSet.getBookDay()+1);
				if(!DateTimeKit.laterThanNow(messOrderDate)){
					throw new BaseException("超过预定时间");
				}
				Date messOrderDate2 = DateTimeKit.addDate(oldMessMealOrder.getTime(), -messBasisSet.getBookDay());
				if(DateTimeKit.isSameDay(new Date(),messOrderDate2)){
					Date sourceTime = null;
//					String nowTime = new SimpleDateFormat("HH:MM").format(new Date());
					if(mealType == 0){//早餐
						sourceTime = messBasisSet.getBreakfastReserve();
					}else if(mealType == 1){
						sourceTime = messBasisSet.getLunchReserve();
					}else if(mealType == 2){
						sourceTime = messBasisSet.getDinnerReserve();
					}else if(mealType == 3){
						sourceTime = messBasisSet.getNightReserve();
					}
					
					if(sourceTime == null || compareDate(new Date(),sourceTime) == 1){
						throw new BaseException("超过预定时间");
					}
				}
				
				MessMealOrder messMealOrder = new MessMealOrder();
				messMealOrder.setCardCode(oldMessMealOrder.getCardCode());
				messMealOrder.setCardId(oldMessMealOrder.getCardId());
				messMealOrder.setDepartment(oldMessMealOrder.getDepartment());
				messMealOrder.setMainId(oldMessMealOrder.getMainId());
				String mealCode = "" + PJWHash("" + System.currentTimeMillis());
				if(mealCode.length() != 9){
					for(int x = 0;x < 100;x++){
						mealCode = "" + PJWHash("" + System.currentTimeMillis());
						if(mealCode.length() == 9){
							break;
						}
					}
				}
				messMealOrder.setMealCode(mealCode);
				messMealOrder.setMealNum(mealNum);
				messMealOrder.setMealType(mealType);
				messMealOrder.setMemberId(oldMessMealOrder.getMemberId());
				messMealOrder.setDepId(messCard.getDepId());
				Double money = 0.0;
				int type = -1;
				if(messBasisSet.getBitUniversal() == 0){
					money = messBasisSet.getUniversalPrice() * mealNum;
					messCard.setUniversalNum(messCard.getUniversalNum() - mealNum);
					if(messCard.getUniversalNum() < 0){
						throw new BaseException("余额不足");
					}
					ticketNum += mealNum;
					messConsumerDetail.setUniversalNum(ticketNum);
					messConsumerDetail.setUniversalPrice(messBasisSet.getUniversalPrice());
					type = 4;
				}else{
					if(mealType == 0){//早餐
						money = messBasisSet.getBreakfastPrice() * mealNum;
						messCard.setBreakfastNum(messCard.getBreakfastNum() - mealNum);
						if(messCard.getBreakfastNum() < 0){
							throw new BaseException("余额不足");
						}
						ticketNum += mealNum;
						messConsumerDetail.setBreakfastNum(mealNum);
						messConsumerDetail.setBreakfastPrice(messBasisSet.getBreakfastPrice());
						type = mealType;
					}else if(mealType == 1){//午餐
						money = messBasisSet.getLunchPrice() * mealNum;
						messCard.setLunchNum(messCard.getLunchNum() - mealNum);
						if(messCard.getLunchNum() < 0){
							throw new BaseException("余额不足");
						}
						ticketNum += mealNum;
						messConsumerDetail.setLunchNum(mealNum);
						messConsumerDetail.setLunchPrice(messBasisSet.getLunchPrice());
						type = mealType;
					}else if(mealType == 2){//晚餐
						money = messBasisSet.getDinnerPrice() * mealNum;
						messCard.setDinnerNum(messCard.getDinnerNum() - mealNum);
						if(messCard.getDinnerNum() < 0){
							throw new BaseException("余额不足");
						}
						ticketNum += mealNum;
						messConsumerDetail.setDinnerNum(mealNum);
						messConsumerDetail.setDinnerPrice(messBasisSet.getDinnerPrice());
						type = mealType;
					}else if(mealType == 3){//夜宵
						money = messBasisSet.getNightPrice() * mealNum;
						messCard.setNightNum(messCard.getNightNum() - mealNum);
						if(messCard.getNightNum() < 0){
							throw new BaseException("余额不足");
						}
						ticketNum += mealNum;
						messConsumerDetail.setNightNum(mealNum);
						messConsumerDetail.setNightPrice(messBasisSet.getNightPrice());
						type = mealType;
					}
				}
				messConsumerDetail.setTicketNum(ticketNum);
				messMealOrder.setMoney(money);
				messMealOrder.setName(oldMessMealOrder.getName());
				messMealOrder.setOrderTime(new Date());
				messMealOrder.setSex(oldMessMealOrder.getSex());
				messMealOrder.setStatus(1);
				messMealOrder.setTime(oldMessMealOrder.getTime());
				messMealOrderMapper.insertSelective(messMealOrder);
				Map<String,Integer> mapId = new HashMap<String, Integer>();
				mapId.put("cardId", messCard.getId());
				mapId.put("type", type);
				List<MessCardTicket> messCardTickets = messCardTicketMapper.getNotCancelTicketByCardIdAndType(mapId);
				for(int j = 0;j < messMealOrder.getMealNum() ;j++){
					MessCardTicket messCardTicket2 = null;
					for(MessCardTicket messCardTicket:messCardTickets){
						if(messCardTicket.getDay() != null && messCardTicket.getDay() != 0){
							int ticketDay = 0;
							Date ticketDate = DateTimeKit.addDays(messCardTicket.getTime(), messCardTicket.getDay());
							try {
								ticketDay = DateTimeKit.daysBetween(new Date(),ticketDate);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(ticketDay != 0 && ticketDay > 0){
								if(ticketDay <= messBasisSet.getPastDay()){
									messCardTicket2 = messCardTicket;
									break;
//									int temp = messCardTicket.getDay() - messBasisSet.getPastDay();
//									Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), temp);
//									if(!DateTimeKit.laterThanNow(lastDay)){
//										messCardTicket2 = messCardTicket;
//										break;	
//									}
								}else{
									continue;
								}
							}
						}
					}
					if(messCardTicket2 == null){
						messCardTicket2 = messCardTickets.get(0);
					}
					MessMealOrderInfo messMealOrderInfo = new MessMealOrderInfo();
					messMealOrderInfo.setOmealId(messMealOrder.getId());
					messMealOrderInfo.setTicketCode(messCardTicket2.getTicketCode());
					messMealOrderInfo.setTicketId(messCardTicket2.getId());
					messMealOrderInfoMapper.insertSelective(messMealOrderInfo);
					messCardTicket2.setStatus(3);
					messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket2);
				}
			}
			messConsumerDetailMapper.insertSelective(messConsumerDetail);
			messMealOrderMapper.deleteByPrimaryKey(oldMessMealOrder.getId());
		}
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int cancelOrder(Integer orderId) throws Exception {
		// TODO Auto-generated method stub
		MessMealOrder messMealOrder = messMealOrderMapper.selectByPrimaryKey(orderId);
		if(DateTimeKit.diffDays(messMealOrder.getTime(),new Date()) < 0){
			return 1;
		}
		if(messMealOrder.getStatus() == 1){
			MessBasisSet messBasisSet = messBasisSetMapper.getMessBasisSetByMainId(messMealOrder.getMainId());
			MessCard messCard = messCardMapper.selectByPrimaryKey(messMealOrder.getCardId());
			messMealOrder.setStatus(2);
		
			//消费流水表(退票)
			MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
			messConsumerDetail.setMainId(messCard.getMainId());
			messConsumerDetail.setCardId(messCard.getId());
			messConsumerDetail.setTime(new Date());
			messConsumerDetail.setTableType(4);
			messConsumerDetail.setBitSubsidy(1);
			messConsumerDetail.setOnLine(0);
			messConsumerDetail.setBreakfastNum(0);
			messConsumerDetail.setLunchNum(0);
			messConsumerDetail.setDinnerNum(0);
			messConsumerDetail.setNightNum(0);
			messConsumerDetail.setUniversalNum(0);
			messConsumerDetail.setStatus(1);
			messConsumerDetail.setTicketNum(messMealOrder.getMealNum());
			if(messBasisSet.getBitUniversal() == 0){
				messCard.setUniversalNum(messCard.getUniversalNum() + messMealOrder.getMealNum());
				messConsumerDetail.setUniversalNum(messMealOrder.getMealNum());
			}else{
				if(messMealOrder.getMealType() == 0){
					messCard.setBreakfastNum(messCard.getBreakfastNum() + messMealOrder.getMealNum());
					messConsumerDetail.setBreakfastNum(messMealOrder.getMealNum());
				}else if(messMealOrder.getMealType() == 1){
					messCard.setLunchNum(messCard.getLunchNum() + messMealOrder.getMealNum());
					messConsumerDetail.setLunchNum(messMealOrder.getMealNum());
				}else if(messMealOrder.getMealType() == 2){
					messCard.setDinnerNum(messCard.getDinnerNum() + messMealOrder.getMealNum());
					messConsumerDetail.setDinnerNum(messMealOrder.getMealNum());
				}else if(messMealOrder.getMealType() == 3){
					messCard.setNightNum(messCard.getNightNum() + messMealOrder.getMealNum());
					messConsumerDetail.setNightNum(messMealOrder.getMealNum());
				}else{
					return 0;
				}
			}
			List<MessMealOrderInfo> messMealOrderInfos = 
					messMealOrderInfoMapper.getMessMealOrderInfoListByMoId(messMealOrder.getId());
			for(MessMealOrderInfo messMealOrderInfo :messMealOrderInfos){
				MessCardTicket messCardTicket = messCardTicketMapper.selectByPrimaryKey(messMealOrderInfo.getTicketId());
				messCardTicket.setStatus(1);
				messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket);
			}
			messCardMapper.updateByPrimaryKey(messCard);
			messConsumerDetailMapper.insertSelective(messConsumerDetail);
			return messMealOrderMapper.updateByPrimaryKeySelective(messMealOrder);
		}else{
			return 0;
		}
	}

	@Override
	public MessMealOrder getMessMealOrderByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getMessMealOrderByMap(map).get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<MessMealOrder> getPastMessMealOrderListByCardIdAndMainId(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getPastMessMealOrderListByCardIdAndMainId(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int update(MessMealOrder messMealOrder) throws Exception {
		// TODO Auto-generated method stub
		return messMealOrderMapper.updateByPrimaryKeySelective(messMealOrder);
	}

	public int compareDate(Date dt1,Date dt2){
		
		Calendar cal=Calendar.getInstance();//使用日历类 
		cal.setTime(dt1);
		Calendar cal2=Calendar.getInstance();//使用日历类 
		cal2.setTime(dt2);
		if(cal.get(cal.HOUR_OF_DAY) < cal2.get(cal2.HOUR_OF_DAY)){
			return 0;
		}else if(cal.get(cal.HOUR_OF_DAY) > cal2.get(cal2.HOUR_OF_DAY)){
			return 1;
		}
		if(cal.get(cal.MINUTE) < cal2.get(cal2.MINUTE)){
			return 0;
		}else if(cal.get(cal.MINUTE) > cal2.get(cal2.MINUTE)){
			return 1;
		}
		return 0;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int delOrder(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return messMealOrderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<MessMealOrder> getBookMessMealOrderByToDay(Map<String, Object> params) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getBookMessMealOrderByToDay(params);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public List<MessMealOrder> getMessMealOrderListforToday2(Integer mainId) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getMessMealOrderListforToday2(mainId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public int delNotCMealOrder(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return messMealOrderMapper.delNotCMealOrder(map);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int saveOrUpdateAddOrder(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		Integer ticketNum = 0;
		Integer mealNum = 1;
		MessMealOrder messMealOrder = null;
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		MessBasisSet messBasisSet = 
				messBasisSetMapper.getMessBasisSetByMainId(messCard.getMainId());
		Integer mealType = Integer.valueOf(params.get("mealType").toString());
		Date time = DateTimeKit.parse(params.get("time").toString(),"yyyy-MM-dd");
		Date messOrderDate = DateTimeKit.addDate(time, -messBasisSet.getBookDay()+1);
		if(!DateTimeKit.laterThanNow(messOrderDate)){
			throw new BaseException("超过预定时间");
		}
		Date messOrderDate2 = DateTimeKit.addDate(time, -messBasisSet.getBookDay());
		if(DateTimeKit.isSameDay(new Date(),messOrderDate2)){
			Date sourceTime = null;
//			String nowTime = new SimpleDateFormat("HH:MM").format(new Date());
			if(mealType == 0){//早餐
				sourceTime = messBasisSet.getBreakfastReserve();
			}else if(mealType == 1){
				sourceTime = messBasisSet.getLunchReserve();
			}else if(mealType == 2){
				sourceTime = messBasisSet.getDinnerReserve();
			}else if(mealType == 3){
				sourceTime = messBasisSet.getNightReserve();
			}
			
			if(sourceTime == null || compareDate(new Date(),sourceTime) == 1){
				throw new BaseException("超过预定时间");
			}
		}
		
		//消费流水表
		MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
		messConsumerDetail.setMainId(messCard.getMainId());
		messConsumerDetail.setCardId(messCard.getId());
		messConsumerDetail.setTime(new Date());
		messConsumerDetail.setTableType(6);
		messConsumerDetail.setBitSubsidy(1);
		messConsumerDetail.setOnLine(0);
		messConsumerDetail.setBreakfastNum(0);
		messConsumerDetail.setLunchNum(0);
		messConsumerDetail.setDinnerNum(0);
		messConsumerDetail.setNightNum(0);
		messConsumerDetail.setUniversalNum(0);
		messConsumerDetail.setStatus(1);
		int type = -1;
		if("save".equals(params.get("saveType").toString())){
			messMealOrder = new MessMealOrder();
			messMealOrder.setMealNum(1);
		}else{
			messMealOrder = 
					messMealOrderMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString().trim()));
			messMealOrder.setMealNum(messMealOrder.getMealNum()+mealNum);
		}
		messMealOrder.setTime(time);
		messMealOrder.setCardCode(messCard.getCardCode());
		messMealOrder.setCardId(messCard.getId());
		messMealOrder.setDepartment(messCard.getDepartment());
		messMealOrder.setMainId(messCard.getMainId());
		String mealCode = "" + PJWHash("" + System.currentTimeMillis());
		if(mealCode.length() != 9){
			for(int x = 0;x < 100;x++){
				mealCode = "" + PJWHash("" + System.currentTimeMillis());
				if(mealCode.length() == 9){
					break;
				}
			}
		}
		messMealOrder.setMealCode(mealCode);
		messMealOrder.setMealType(mealType);
		messMealOrder.setMemberId(messCard.getMemberId());
		messMealOrder.setDepId(messCard.getDepId());
		Double money = 0.0;
		
		if(messBasisSet.getBitUniversal() == 0){
			money = messBasisSet.getUniversalPrice() * mealNum;
			messCard.setUniversalNum(messCard.getUniversalNum() - mealNum);
			if(messCard.getUniversalNum() < 0){
				throw new BaseException("余额不足");
			}
			ticketNum += mealNum;
			messConsumerDetail.setUniversalNum(ticketNum);
			messConsumerDetail.setUniversalPrice(messBasisSet.getUniversalPrice());
			type = 4;
		}else{
			if(mealType == 0){//早餐
				money = messBasisSet.getBreakfastPrice() * mealNum;
				messCard.setBreakfastNum(messCard.getBreakfastNum() - mealNum);
				if(messCard.getBreakfastNum() < 0){
					throw new BaseException("余额不足");
				}
				ticketNum += mealNum;
				messConsumerDetail.setBreakfastNum(mealNum);
				messConsumerDetail.setBreakfastPrice(messBasisSet.getBreakfastPrice());
				type = mealType;
			}else if(mealType == 1){//午餐
				money = messBasisSet.getLunchPrice() * mealNum;
				messCard.setLunchNum(messCard.getLunchNum() - mealNum);
				if(messCard.getLunchNum() < 0){
					throw new BaseException("余额不足");
				}
				ticketNum += mealNum;
				messConsumerDetail.setLunchNum(mealNum);
				messConsumerDetail.setLunchPrice(messBasisSet.getLunchPrice());
				type = mealType;
			}else if(mealType == 2){//晚餐
				money = messBasisSet.getDinnerPrice() * mealNum;
				messCard.setDinnerNum(messCard.getDinnerNum() - mealNum);
				if(messCard.getDinnerNum() < 0){
					throw new BaseException("余额不足");
				}
				ticketNum += mealNum;
				messConsumerDetail.setDinnerNum(mealNum);
				messConsumerDetail.setDinnerPrice(messBasisSet.getDinnerPrice());
				type = mealType;
			}else if(mealType == 3){//夜宵
				money = messBasisSet.getNightPrice() * mealNum;
				messCard.setNightNum(messCard.getNightNum() - mealNum);
				if(messCard.getNightNum() < 0){
					throw new BaseException("余额不足");
				}
				ticketNum += mealNum;
				messConsumerDetail.setNightNum(mealNum);
				messConsumerDetail.setNightPrice(messBasisSet.getNightPrice());
				type = mealType;
			}
		}
		messConsumerDetail.setTicketNum(ticketNum);
		messMealOrder.setMoney(money);
		messMealOrder.setName(messCard.getName());
		messMealOrder.setOrderTime(new Date());
		messMealOrder.setSex(messCard.getSex());
		messMealOrder.setStatus(1);
		if("save".equals(params.get("saveType").toString())){
			data = messMealOrderMapper.insertSelective(messMealOrder);
		}else{
			data = messMealOrderMapper.updateByPrimaryKeySelective(messMealOrder);
		}
		Map<String,Integer> mapId = new HashMap<String, Integer>();
		mapId.put("cardId", messCard.getId());
		mapId.put("type", type);
		List<MessCardTicket> messCardTickets = 
				messCardTicketMapper.getNotCancelTicketByCardIdAndType(mapId);
		if(messCardTickets != null && messCardTickets.size() > 0){
			MessCardTicket messCardTicket2 = null;
			for(MessCardTicket messCardTicket:messCardTickets){
				if(messCardTicket.getDay() != null && messCardTicket.getDay() != 0){
					int ticketDay = -1;
					Date ticketDate = DateTimeKit.addDays(messCardTicket.getTime(), messCardTicket.getDay());
					try {
						ticketDay = DateTimeKit.daysBetween(new Date(),ticketDate);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(ticketDay >= 0){
						if(ticketDay <= messBasisSet.getPastDay()){
							messCardTicket2 = messCardTicket;
							break;
//							int temp = messCardTicket.getDay() - messBasisSet.getPastDay();
//							Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), temp);
//							if(!DateTimeKit.laterThanNow(lastDay)){
//								messCardTicket2 = messCardTicket;
//								break;	
//							}
						}else{
							continue;
						}
					}
				}
			}
			if(messCardTicket2 == null){
				messCardTicket2 = messCardTickets.get(0);
			}
			MessMealOrderInfo messMealOrderInfo = new MessMealOrderInfo();
			messMealOrderInfo.setOmealId(messMealOrder.getId());
			messMealOrderInfo.setTicketCode(messCardTicket2.getTicketCode());
			messMealOrderInfo.setTicketId(messCardTicket2.getId());
			messMealOrderInfoMapper.insertSelective(messMealOrderInfo);
			messCardTicket2.setStatus(3);
			messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket2);
		}
		data = messConsumerDetailMapper.insertSelective(messConsumerDetail);
		data = messCardMapper.updateByPrimaryKeySelective(messCard);
		return data;
	}

	@Override
	public List<MessMealOrder> getNumsByDepIdAndMealType(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messMealOrderMapper.getNumsByDepIdAndMealType(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public Map<String, Object> exportsMealOrderForMonth(Map<String, Object> params) {
		// TODO Auto-generated method stub
		Map<String, Object> msg = new HashMap<>();
		boolean result = true;
		String message = "生成成功！";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
	        //获取前月的第一天
	        Calendar cal_1=Calendar.getInstance();//获取当前日期 
	        cal_1.add(Calendar.MONTH, 0);
	        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        String firstDay = format.format(cal_1.getTime());
	        System.out.println("-----1------firstDay:"+firstDay);
	        //获取前月的最后一天
	        Calendar cale = Calendar.getInstance(); 
	        cale.add(Calendar.MONTH, 1);
	        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
	        String lastDay = format.format(cale.getTime());
	        System.out.println("-----2------lastDay:"+lastDay);
	        params.put("stime", firstDay);
	        params.put("etime", lastDay);
			List<MessMealOrder> messMealOrders = messMealOrderMapper.selectMealOrderForMonth(params);
			String title = "订餐记录("+time("M",new Date().toString())+"月份总订单)  生成日期："+ time("yyyy-MM-dd hh:mm:ss",new Date().toString());
			Workbook book = exportExcelForRecodingMonth(messMealOrders, title,params);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "订餐记录(月总)导出excel失败！");
			logger.error("订餐记录(月总)导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}
		

//	@Override
//	public Map<String, Object> getMealOrderList(Map<String, Object> params) {
//		// TODO Auto-generated method stub
//		List<MessMealOrder> messMealOrders = 
//				messMealOrderMapper.getMealOrderList(params);
//		params.put("messMealOrders", messMealOrders);
//		return params;
//	}A JSONObject text must begin with '{' at character 1 of [{"id":557,"meal":"2"}]
	public static void main(String[] args) {
		System.out.println(DateTimeKit.diffDays(DateTimeKit.parse("2017-06-30","yyyy-MM-dd"),new Date()));


	}
}
