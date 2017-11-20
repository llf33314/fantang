package com.gt.mess.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.mess.dao.*;
import com.gt.mess.entity.*;
import com.gt.mess.service.MessCardService;
import com.gt.mess.util.CommonUtil;
import com.gt.mess.util.DateTimeKit;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessCardServiceImpl implements MessCardService {
	
	private Logger logger = Logger.getLogger(MessCardServiceImpl.class);

	@Autowired
	private MessCardMapper messCardMapper;

	@Autowired
	private MessCardTicketMapper messCardTicketMapper;

	@Autowired
	private MessBasisSetMapper messBasisSetMapper;

	@Autowired
	private MessBuyTicketOrderMapper messBuyTicketOrderMapper;

	@Autowired
	private MessMealOrderMapper messMealOrderMapper;

	@Autowired
	private MessMealOrderInfoMapper messMealOrderInfoMapper;

	@Autowired
	private MessCancelTicketMapper messCancelTicketMapper;

	@Autowired
	private MessCancelTicketInfoMapper messCancelTicketInfoMapper;
	
	@Autowired
	private MessTopUpOrderMapper messTopUpOrderMapper;
	
	@Autowired
	private MessConsumerDetailMapper messConsumerDetailMapper;
	
	@Autowired
	private MessMealTempOrderMapper messMealTempOrderMapper;
	
	@Autowired
	private MessMainMapper messMainMapper;
	
	@Autowired
	private MessDepartmentMapper messDepartmentMapper;
	
	@Autowired
	private MessOldManCardOrderMapper messOldManCardOrderMapper;
	

	@Override
	public Page<MessCard> getMessCardPageByMainId(Page<MessCard> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCardMapper.getMessCardPageByMainId(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int saveOrUpdateMessCard(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int dataType = 0;
		MessCard messCard = new MessCard();
		messCard.setBreakfastNum(0);
		String cardCode = "" + PJWHash("" + System.currentTimeMillis());
		if(cardCode.length() != 9){
			for(int i = 0;i <100;i++){
				cardCode = "" + PJWHash("" + System.currentTimeMillis());
				if(cardCode.length() == 9){
					break;
				}
			}
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("cardCode", cardCode);
		map.put("mainId", Integer.valueOf(params.get("mainId").toString()));
		if(messCardMapper.getMessCardByCardCodeAndMainId(map).size() >= 1){
			return 0;
		}
		messCard.setCardCode(cardCode);
		
		messCard.setDinnerNum(0);
		messCard.setFreeTicketNum(0);
		messCard.setLunchNum(0);
		messCard.setMainId(Integer.valueOf(params.get("mainId").toString()));
		try {
			messCard.setDepId(Integer.valueOf(params.get("depId").toString()));
		} catch (Exception e) {
			// TODO: handle exception
			messCard.setDepId(null);
		}
		try {
			messCard.setGroupId(Integer.valueOf(params.get("groupId").toString()));
		} catch (Exception e) {
			// TODO: handle exception
			messCard.setGroupId(null);
		}
		if(messCard.getDepId() != null && messCard.getDepId() != -1){
			messCard.setDepartment(params.get("department").toString());
		}else{
			messCard.setDepartment("暂无部门");
		}
		try {
			messCard.setMoney(Double.valueOf(params.get("money").toString()));
		} catch (Exception e) {
			// TODO: handle exception
			messCard.setMoney(0.0);
		}
		messCard.setName(params.get("name").toString());
		messCard.setNightNum(0);
		messCard.setSex(Integer.valueOf(params.get("sex").toString()));
		messCard.setStatus(1);
		messCard.setTicketNum(0);
		messCard.setUniversalNum(0);
		// 这里的绑定时间不能给，因为还没绑定
		dataType = messCardMapper.insertSelective(messCard);
		if(messCard.getMoney() > 0.0){
			
			//消费流水表
			MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
			messConsumerDetail.setMainId(messCard.getMainId());
			messConsumerDetail.setCardId(messCard.getId());
			messConsumerDetail.setTime(new Date());
			messConsumerDetail.setTableType(3);
			messConsumerDetail.setBitSubsidy(1);
			messConsumerDetail.setBreakfastNum(0);
			messConsumerDetail.setLunchNum(0);
			messConsumerDetail.setDinnerNum(0);
			messConsumerDetail.setNightNum(0);
			messConsumerDetail.setUniversalNum(0);
			messConsumerDetail.setStatus(0);
			messConsumerDetail.setMoney(Double.valueOf(params.get("money").toString()));
			messConsumerDetail.setOnLine(1);
			
			MessTopUpOrder messTopUpOrder = new MessTopUpOrder();
			messTopUpOrder.setCardCode(messCard.getCardCode());
			messTopUpOrder.setCardId(messCard.getId());
			messTopUpOrder.setDepartment(messCard.getDepartment());
			messTopUpOrder.setLaterMoney(messCard.getMoney());
			messTopUpOrder.setMainId(messCard.getMainId());
			messTopUpOrder.setMemberId(messCard.getMemberId());
			messTopUpOrder.setMoney(Double.valueOf(params.get("money").toString()));
			messTopUpOrder.setName(messCard.getName());
			messTopUpOrder.setOrderNo("ST"+System.currentTimeMillis());
			messTopUpOrder.setSex(messCard.getSex());
			messTopUpOrder.setStatus(0);
			messTopUpOrder.setTime(new Date());
			messTopUpOrder.setType(0);
			messTopUpOrder.setDepId(messCard.getDepId());
			messTopUpOrderMapper.insertSelective(messTopUpOrder);
			messConsumerDetailMapper.insertSelective(messConsumerDetail);
		}
		return dataType;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int topUpMoney(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		if (messCard.getCardCode().equals(params.get("cardCode").toString())) {
			messCard.setMoney(messCard.getMoney() + Double.valueOf(params.get("money").toString()));
			
			//消费流水表
			MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
			messConsumerDetail.setMainId(messCard.getMainId());
			messConsumerDetail.setCardId(messCard.getId());
			messConsumerDetail.setTime(new Date());
			messConsumerDetail.setTableType(3);
			messConsumerDetail.setBitSubsidy(1);
			messConsumerDetail.setBreakfastNum(0);
			messConsumerDetail.setLunchNum(0);
			messConsumerDetail.setDinnerNum(0);
			messConsumerDetail.setNightNum(0);
			messConsumerDetail.setUniversalNum(0);
			messConsumerDetail.setStatus(0);
			messConsumerDetail.setMoney(Double.valueOf(params.get("money").toString()));
			messConsumerDetail.setOnLine(1);
			
			MessTopUpOrder messTopUpOrder = new MessTopUpOrder();
			messTopUpOrder.setCardCode(messCard.getCardCode());
			messTopUpOrder.setCardId(messCard.getId());
			messTopUpOrder.setDepartment(messCard.getDepartment());
			messTopUpOrder.setLaterMoney(messCard.getMoney());
			messTopUpOrder.setMainId(messCard.getMainId());
			messTopUpOrder.setMemberId(messCard.getMemberId());
			messTopUpOrder.setMoney(Double.valueOf(params.get("money").toString()));
			messTopUpOrder.setName(messCard.getName());
			messTopUpOrder.setOrderNo("ST"+System.currentTimeMillis());
			messTopUpOrder.setSex(messCard.getSex());
			messTopUpOrder.setStatus(0);
			messTopUpOrder.setTime(new Date());
			messTopUpOrder.setType(0);
			messTopUpOrder.setDepId(messCard.getDepId());
			messTopUpOrderMapper.insertSelective(messTopUpOrder);
			messConsumerDetailMapper.insertSelective(messConsumerDetail);
		}
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int buyTicket(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		MessBasisSet messBasisSet = messBasisSetMapper.getMessBasisSetByMainId(messCard.getMainId());
		if (messCard.getCardCode().equals(params.get("cardCode").toString())) {
			Double money = messCard.getMoney() - Double.valueOf(params.get("money").toString());
			if (money >= 0) {
				Integer messType = Integer.valueOf(params.get("messType").toString());
				Integer ticketNum = Integer.valueOf(params.get("ticketNum").toString());
				messCard.setMoney(money);
				messCard.setTicketNum(messCard.getTicketNum() + ticketNum);
				//消费流水表
				MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
				messConsumerDetail.setMainId(messCard.getMainId());
				messConsumerDetail.setCardId(messCard.getId());
				messConsumerDetail.setTime(new Date());
				messConsumerDetail.setTableType(1);
				messConsumerDetail.setBitSubsidy(1);
				messConsumerDetail.setOnLine(1);
				messConsumerDetail.setBreakfastNum(0);
				messConsumerDetail.setLunchNum(0);
				messConsumerDetail.setDinnerNum(0);
				messConsumerDetail.setNightNum(0);
				messConsumerDetail.setUniversalNum(0);
				messConsumerDetail.setTicketNum(ticketNum);
				
				if (messType == 0) {// 早餐
					messCard.setBreakfastNum(messCard.getBreakfastNum() + ticketNum);
					messConsumerDetail.setBreakfastNum(ticketNum);
					messConsumerDetail.setBreakfastPrice(messBasisSet.getBreakfastPrice());
				} else if (messType == 1) {// 午餐
					messCard.setLunchNum(messCard.getLunchNum() + ticketNum);
					messConsumerDetail.setLunchNum(ticketNum);
					messConsumerDetail.setLunchPrice(messBasisSet.getLunchPrice());
				} else if (messType == 2) {// 晚餐
					messCard.setDinnerNum(messCard.getDinnerNum() + ticketNum);
					messConsumerDetail.setDinnerNum(ticketNum);
					messConsumerDetail.setDinnerPrice(messBasisSet.getDinnerPrice());
				} else if (messType == 3) {// 夜宵
					messCard.setNightNum(messCard.getNightNum() + ticketNum);
					messConsumerDetail.setNightNum(ticketNum);
					messConsumerDetail.setNightPrice(messBasisSet.getNightPrice());
				} else if (messType == 4) {// 通用
					messCard.setUniversalNum(messCard.getUniversalNum() + ticketNum);
					messConsumerDetail.setUniversalNum(ticketNum);
					messConsumerDetail.setUniversalPrice(messBasisSet.getUniversalPrice());
				}
				
				
				MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
				messBuyTicketOrder.setBitSubsidy(1);
				messBuyTicketOrder.setBuyLaterNum(messCard.getTicketNum() + messCard.getFreeTicketNum() + ticketNum);
				messBuyTicketOrder.setBuyNum(ticketNum);
				messBuyTicketOrder.setBuyType(1);
				messBuyTicketOrder.setCardCode(messCard.getCardCode());
				messBuyTicketOrder.setCardId(messCard.getId());
				messBuyTicketOrder.setDepartment(messCard.getDepartment());
				messBuyTicketOrder.setMemberId(messCard.getMemberId());
				messBuyTicketOrder.setMainId(messCard.getMainId());
				messBuyTicketOrder.setMoney(Double.valueOf(params.get("money").toString()));
				messBuyTicketOrder.setName(messCard.getName());
				messBuyTicketOrder.setOrderNo("ST" + System.currentTimeMillis());
				messBuyTicketOrder.setSex(messCard.getSex());
				messBuyTicketOrder.setTime(new Date());
				messBuyTicketOrder.setTicketType(messType);
				messBuyTicketOrder.setDepId(messCard.getDepId());
				messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
				messConsumerDetailMapper.insertSelective(messConsumerDetail);
				for (int i = 0; i < ticketNum; i++) {
					MessCardTicket messCardTicket = new MessCardTicket();
					messCardTicket.setCardId(messCard.getId());
					messCardTicket.setMainId(messCard.getMainId());
					messCardTicket.setDay(messBasisSet.getTicketDay());
					messCardTicket.setIsfree(1);
					if (messType == 0) {// 早餐
						messCardTicket.setPrice(messBasisSet.getBreakfastPrice());
					} else if (messType == 1) {// 午餐
						messCardTicket.setPrice(messBasisSet.getLunchPrice());
					} else if (messType == 2) {// 晚餐
						messCardTicket.setPrice(messBasisSet.getDinnerPrice());
					} else if (messType == 3) {// 夜宵
						messCardTicket.setPrice(messBasisSet.getNightPrice());
					} else if (messType == 4) {// 通用
						messCardTicket.setPrice(messBasisSet.getUniversalPrice());
					}
					messCardTicket.setStatus(1);
					messCardTicket.setTicketCode("GT" + System.currentTimeMillis());
					messCardTicket.setTicketType(messType);
					messCardTicket.setTime(new Date());
					messCardTicketMapper.insertSelective(messCardTicket);
				}
				
			} else {
				return 0;
			}
		}
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Override
	public int subsidyTicket(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		MessBasisSet messBasisSet = messBasisSetMapper.getMessBasisSetByMainId(messCard.getMainId());
		if (messCard != null) {
			Integer messType = Integer.valueOf(params.get("messType").toString());
			Integer freeTicketNum = Integer.valueOf(params.get("ticketNum").toString());
			messCard.setFreeTicketNum(messCard.getFreeTicketNum()+freeTicketNum);
			//消费流水表
			MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
			messConsumerDetail.setMainId(messCard.getMainId());
			messConsumerDetail.setCardId(messCard.getId());
			messConsumerDetail.setTime(new Date());
			messConsumerDetail.setTableType(1);
			messConsumerDetail.setBitSubsidy(0);
			messConsumerDetail.setOnLine(1);
			messConsumerDetail.setBreakfastNum(0);
			messConsumerDetail.setLunchNum(0);
			messConsumerDetail.setDinnerNum(0);
			messConsumerDetail.setNightNum(0);
			messConsumerDetail.setUniversalNum(0);
			messConsumerDetail.setTicketNum(freeTicketNum);
			if (messType == 0) {// 早餐
				messCard.setBreakfastNum(messCard.getBreakfastNum() + freeTicketNum);
				messConsumerDetail.setBreakfastNum(freeTicketNum);
				//messConsumerDetail.setBreakfastPrice(messBasisSet.getBreakfastPrice());
			} else if (messType == 1) {// 午餐
				messCard.setLunchNum(messCard.getLunchNum() + freeTicketNum);
				messConsumerDetail.setLunchNum(freeTicketNum);
				//messConsumerDetail.setLunchPrice(messBasisSet.getLunchPrice());
			} else if (messType == 2) {// 晚餐
				messCard.setDinnerNum(messCard.getDinnerNum() + freeTicketNum);
				messConsumerDetail.setDinnerNum(freeTicketNum);
				//messConsumerDetail.setDinnerPrice(messBasisSet.getDinnerPrice());
			} else if (messType == 3) {// 夜宵
				messCard.setNightNum(messCard.getNightNum() + freeTicketNum);
				messConsumerDetail.setNightNum(freeTicketNum);
				//messConsumerDetail.setNightPrice(messBasisSet.getNightPrice());
			} else if (messType == 4) {// 通用
				messCard.setUniversalNum(messCard.getUniversalNum() + freeTicketNum);
				messConsumerDetail.setUniversalNum(freeTicketNum);
				//messConsumerDetail.setUniversalPrice(messBasisSet.getUniversalPrice());
			}
			MessBuyTicketOrder messBuyTicketOrder = new MessBuyTicketOrder();
			messBuyTicketOrder.setBitSubsidy(0);
			messBuyTicketOrder.setBuyLaterNum(messCard.getTicketNum() + messCard.getFreeTicketNum() + freeTicketNum);
			messBuyTicketOrder.setBuyNum(freeTicketNum);
			messBuyTicketOrder.setBuyType(0);
			messBuyTicketOrder.setCardCode(messCard.getCardCode());
			messBuyTicketOrder.setCardId(messCard.getId());
			messBuyTicketOrder.setDepartment(messCard.getDepartment());
			messBuyTicketOrder.setMemberId(messCard.getMemberId());
			messBuyTicketOrder.setMainId(messCard.getMainId());
			messBuyTicketOrder.setMoney(0.0);
			messBuyTicketOrder.setName(messCard.getName());
			messBuyTicketOrder.setOrderNo("ST" + System.currentTimeMillis());
			messBuyTicketOrder.setSex(messCard.getSex());
			messBuyTicketOrder.setTime(new Date());
			messBuyTicketOrder.setDepId(messCard.getDepId());
			messBuyTicketOrderMapper.insertSelective(messBuyTicketOrder);
			messConsumerDetailMapper.insertSelective(messConsumerDetail);
			for (int i = 0; i < freeTicketNum; i++) {
				MessCardTicket messCardTicket = new MessCardTicket();
				messCardTicket.setCardId(messCard.getId());
				messCardTicket.setDay(messBasisSet.getTicketDay());
				messCardTicket.setIsfree(0);
				if (messType == 0) {// 早餐
					messCardTicket.setPrice(messBasisSet.getBreakfastPrice());
				} else if (messType == 1) {// 午餐
					messCardTicket.setPrice(messBasisSet.getLunchPrice());
				} else if (messType == 2) {// 晚餐
					messCardTicket.setPrice(messBasisSet.getDinnerPrice());
				} else if (messType == 3) {// 夜宵
					messCardTicket.setPrice(messBasisSet.getNightPrice());
				} else if (messType == 4) {// 通用
					messCardTicket.setPrice(messBasisSet.getUniversalPrice());
				}
				messCardTicket.setMainId(messCard.getMainId());
				messCardTicket.setStatus(1);
				messCardTicket.setTicketCode("" + PJWHash("" + System.currentTimeMillis()));
				messCardTicket.setTicketType(messType);
				messCardTicket.setTime(new Date());
				messCardTicketMapper.insertSelective(messCardTicket);
			}
		}
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int delMessCard(Integer cardId) throws Exception {
		// TODO Auto-generated method stub
		messCardTicketMapper.delTicketByCardId(cardId);
		return messCardMapper.deleteByPrimaryKey(cardId);
	}

	@Override
	public Page<MessCard> selectCardApplyByCardCode(Page<MessCard> page, Map<String, Object> map, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCardMapper.selectCardApplyByCardCode(map) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessCard> selectCardApplyByName(Page<MessCard> page, Map<String, Object> map, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCardMapper.selectCardApplyByName(map) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Page<MessCard> selectCardApplyByDepartment(Page<MessCard> page, Map<String, Object> map, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCardMapper.selectCardApplyByDepartment(map) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String,Object> enteringCard(InputStream inputStream, Integer mainId,Integer depId) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>(); 
		try {
			map = parseExcel(inputStream, mainId,depId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据路径加载解析Excel
	 * @param inputStream
	 * @param mainId
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> parseExcel(InputStream inputStream, Integer mainId,Integer depId) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>(); 
		Workbook workBook = null;
		Sheet sheet = null;
		workBook = WorkbookFactory.create(inputStream);
		if (workBook != null) {
			int numberSheet = workBook.getNumberOfSheets();
			if (numberSheet > 0) {
				sheet = workBook.getSheetAt(0);// 获取第一个工作簿(Sheet)的内容【注意根据实际需要进行修改】
				map = getExcelContent(sheet, mainId,depId);
			} else {
				System.out.println("目标表格工作簿(Sheet)数目为0！");
			}
		}
		return map;
	}

	/**
	 * 解析(读取)Excel内容
	 * 
	 * @param sheet
	 * @return
	 */
	public Map<String,Object> getExcelContent(Sheet sheet, Integer mainId,Integer depId) throws Exception{
		int dataType = 0;
		int dataType2 = 0;
		int rowCount = sheet.getPhysicalNumberOfRows();// 总行数
		
		Map<String,Object> map = new HashMap<String, Object>();
		MessDepartment messDepartment = 
				messDepartmentMapper.selectByPrimaryKey(depId);
		if(messDepartment == null || depId == -1){
			messDepartment = new MessDepartment();
			messDepartment.setId(-1);
			messDepartment.setName("暂无部门");
		}
		
		List<String> list = new ArrayList<String>();
		if (rowCount > 1) {
			Row titleRow = sheet.getRow(0);// 标题行
			String cardCode2 = "";
			for (int i = 2; i < rowCount; i++) {// 遍历行，略过标题行，从第二行开始
				Row row = sheet.getRow(i);
				String name = null;
				Integer sex = null;
				name = row.getCell(0).toString().trim();
//				if(name.length() == 0)
//					continue;
				String sexstr = row.getCell(1).toString().trim();
				if(sexstr.equals("男")){
					sex = 1;
				}else if(sexstr.equals("女")){
					sex = 0;
				}else{
					sex = 2;
				}
				MessCard messCard = new MessCard();
				String cardCode = "" + PJWHash("" + System.currentTimeMillis()+ i);
				if(cardCode.length() != 9){
					for(int j = 0;j <100;j++){
						cardCode = "" + PJWHash("" + System.currentTimeMillis() + j);
						if(cardCode.length() == 9){
							break;
						}
					}
				}
				if(cardCode.equals(cardCode2)){
					messCard.setCardCode("" + PJWHash("" + System.currentTimeMillis())+i);
				}else{
					messCard.setCardCode(cardCode);
				}
				cardCode2 = cardCode;
				messCard.setBreakfastNum(0);
				messCard.setDepartment(messDepartment.getName());
				messCard.setDinnerNum(0);
				messCard.setFreeTicketNum(0);
				messCard.setLunchNum(0);
				messCard.setMainId(mainId);
				messCard.setMoney(0.0);
				messCard.setName(name);
				messCard.setNightNum(0);
				messCard.setSex(sex);
				messCard.setStatus(1);
				messCard.setTicketNum(0);
				messCard.setUniversalNum(0);
				messCard.setDepId(depId);
				try {
					dataType = messCardMapper.insertSelective(messCard);
				} catch (Exception e) {
					// TODO: handle exception
					messCard.setCardCode("" + PJWHash("" + System.currentTimeMillis())+i);
					Map<String,Object> mapObj = new HashMap<String, Object>();
					map.put("cardCode", cardCode);
					map.put("mainId", mainId);
					if(messCardMapper.getMessCardByCardCodeAndMainId(mapObj).size() >= 1){
						dataType = messCardMapper.insertSelective(messCard);
					}else{
						list.add(name);
					}
				}
				if(dataType == 1){
					dataType2 = dataType;
				}
				if(list.size() > 10){
					throw new Exception("超过十个");
				}
			}
		}
		map.put("dataType", dataType2);
		map.put("list", list);
		if(list.size() == 0){
			map.put("size", 0);
		}else{
			map.put("size", list.size());
		}
		
		return map;
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

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Map<String, Object> cancelMealTicket(String mealCode,Integer cardId,Integer type,Integer memberId) throws Exception {
		// TODO Auto-generated method stub
		List<MessMealOrder> messMealOrders = messMealOrderMapper.getMessMealOrderByMealCode(mealCode);
		MessMealOrder messMealOrder = null;
		if(messMealOrders.size() == 1){
			messMealOrder = messMealOrders.get(0);
		}else{
			for (int i = 0; i < messMealOrders.size(); i++) {
				messMealOrder = messMealOrders.get(i);
				if(cardId != null){
					if(messMealOrder.getCardId() == cardId){
						break;
					}
				}
			}
		}
		if(messMealOrder == null){
			if(messMealOrders == null || messMealOrders.size() == 0){
				Map<String, Object> mapObj = new HashMap<String, Object>();
				mapObj.put("code", -1);
				return mapObj;
			}else{
				messMealOrder = messMealOrders.get(0);
			}
		}
		if(messMealOrder.getStatus() == 4){
			Map<String, Object> mapObj = new HashMap<String, Object>();
			mapObj.put("code", -2);
			return mapObj;
		}
		MessCard messCard = messCardMapper.selectByPrimaryKey(messMealOrder.getCardId());
		List<MessMealOrderInfo> messMealOrderInfos = messMealOrderInfoMapper
				.getMessMealOrderInfoListByMoId(messMealOrder.getId());
		// 记录核销信息
		MessCancelTicket messCancelTicket = new MessCancelTicket();
		messCancelTicket.setCardCode(messMealOrder.getCardCode());
		messCancelTicket.setDepartment(messMealOrder.getDepartment());
		messCancelTicket.setMainId(messMealOrder.getMainId());
		messCancelTicket.setMoney(messMealOrder.getMoney());
		messCancelTicket.setSex(messMealOrder.getSex());
		messCancelTicket.setName(messMealOrder.getName());
		messCancelTicket.setCardId(messMealOrder.getCardId());
		messCancelTicket.setTicketNum(messMealOrder.getMealNum());
		messCancelTicket.setTime(new Date());
		messCancelTicket.setType(type);
		messCancelTicket.setDepId(messCard.getDepId());
		if(type == 2){
			messCancelTicket.setMemberId(memberId);
		}
		messCancelTicketMapper.insertSelective(messCancelTicket);
		
		
		List<Map<String, Object>> messMealTempOrders = messMealTempOrderMapper.getMessMealTempOrderByMainId(messCard.getMainId(), 5);
		if(messMealTempOrders.size() >= 5){
			messMealTempOrderMapper.deleteByPrimaryKey(Integer.valueOf(messMealTempOrders.get(4).get("id").toString()));
		}
		MessMealTempOrder messMealTempOrder = new MessMealTempOrder();
		messMealTempOrder.setAddNum(messMealOrder.getMealNum());
		messMealTempOrder.setCardCode(messCard.getCardCode());
		messMealTempOrder.setMainId(messCard.getMainId());
		messMealTempOrder.setMoney(messMealOrder.getMoney());
		messMealTempOrder.setTime(DateTimeKit.getDateTime(new Date(),"yyyy-MM-dd HH:mm:ss"));
		messMealTempOrderMapper.insertSelective(messMealTempOrder);
		final Integer NUM = 1;
		// 查询订餐下使用的饭票
		for (MessMealOrderInfo messMealOrderInfo : messMealOrderInfos) {
			MessCardTicket messCardTicket = messCardTicketMapper.selectByPrimaryKey(messMealOrderInfo.getTicketId());
			messCardTicket.setStatus(0);
			messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket);
			
			// 记录核销表所使用的饭票
			MessCancelTicketInfo messCancelTicketInfo = new MessCancelTicketInfo();
			messCancelTicketInfo.setCtId(messCancelTicket.getId());
			messCancelTicketInfo.setTicketCode(messCardTicket.getTicketCode());
			messCancelTicketInfo.setTicketId(messCardTicket.getId());
			messCancelTicketInfoMapper.insertSelective(messCancelTicketInfo);
//			if (messCardTicket.getIsfree() == 0) {
//				messCard.setFreeTicketNum(messCard.getFreeTicketNum() - NUM);
//			} else {
//				messCard.setTicketNum(messCard.getTicketNum() - NUM);
//			}
			
//			if (messCardTicket.getTicketType() == 0 && messCard.getBreakfastNum() >= 1) {// 早餐
//				messCard.setBreakfastNum(messCard.getBreakfastNum() - NUM);
//			} else if (messCardTicket.getTicketType() == 1 && messCard.getLunchNum() >= 1) {// 午餐
//				messCard.setLunchNum(messCard.getLunchNum() - NUM);
//			} else if (messCardTicket.getTicketType() == 2 && messCard.getDinnerNum() >= 1) {// 晚餐
//				messCard.setDinnerNum(messCard.getDinnerNum() - NUM);
//			} else if (messCardTicket.getTicketType() == 3 && messCard.getNightNum() >= 1) {// 夜宵
//				messCard.setNightNum(messCard.getNightNum() - NUM);
//			} else if (messCardTicket.getTicketType() == 4 && messCard.getUniversalNum() >= 1) {// 通用
//				messCard.setUniversalNum(messCard.getUniversalNum() - NUM);
//			}else{
//				throw new Exception("余额不足");
//			}
		}
//		messCardMapper.updateByPrimaryKeySelective(messCard);
		messMealOrder.setStatus(4);
		Integer code = messMealOrderMapper.updateByPrimaryKeySelective(messMealOrder);
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("code", code);
		return mapObj;
	}

	// 手机端接口

	@Override
	public MessCard getMessCardByMainIdAndMemberId(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messCardMapper.getMessCardByMainIdAndMemberId(mapId).get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int bindingCard(Map<String, Object> params) throws Exception{
		// TODO Auto-generated method stub
		MessMain messMain = 
				messMainMapper.getMessMainByBusId(Integer.valueOf(params.get("busId").toString())).get(0);
		params.put("mainId", messMain.getId());
		MessCard messCard = messCardMapper.getMessCardByCardCodeAndMainId(params).get(0);
		if (messCard != null && messCard.getStatus() == 1) {
			MessCard messCard2 = null;
			try {
				Map<String,Integer> mapId = new HashMap<String, Integer>();
				mapId.put("mainId", messCard.getMainId());
				mapId.put("memberId", Integer.valueOf(params.get("memberId").toString()));
				messCard2 = messCardMapper.getMessCardByMainIdAndMemberId(mapId).get(0);
			} catch (Exception e) {
				// TODO: handle exception
				messCard2 = null;
			}
			if(messCard2 != null){
				return 0;
			}
			messCard.setMemberId(Integer.valueOf(params.get("memberId").toString()));
			messCard.setStatus(0);
			messCard.setTime(new Date());
			return messCardMapper.updateByPrimaryKeySelective(messCard);
		}
		return 0;
	}

	@Override
	public MessCard getMessCardById(Integer cardId) {
		// TODO Auto-generated method stub
		try {
			return messCardMapper.selectByPrimaryKey(cardId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int update(MessCard messCard) throws Exception {
		// TODO Auto-generated method stub
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int changeTicketType(Integer mainId,Integer type) throws Exception {
		// TODO Auto-generated method stub
		if(type == 0){
			messCardTicketMapper.delNotCancelU(mainId);
		}else{
			messCardTicketMapper.delNotCancelD(mainId);
		}
		return messCardMapper.cleanTicketNum(mainId);
	}

	@Override
	public int getNotCancelTicketNum(Integer mainId) {
		// TODO Auto-generated method stub
		try {
			return messCardTicketMapper.getNotCancelTicketNum(mainId);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Map<String,Object> verify(Integer cardId,String mealCode) throws Exception {
		// TODO Auto-generated method stub
		List<MessMealOrder> messMealOrders = messMealOrderMapper.getMessMealOrderByMealCode(mealCode);
		MessMealOrder messMealOrder = null;
		Map<String,Object> mapObj = new HashMap<String, Object>();
		if(messMealOrders.size() == 1){
			messMealOrder = messMealOrders.get(0);
		}else{
			for (int i = 0; i < messMealOrders.size(); i++) {
				messMealOrder = messMealOrders.get(i);
				if(cardId != null){
					if(messMealOrder.getCardId() == cardId){
						break;
					}
				}
			}
		}
		if(messMealOrder == null){
			if(messMealOrders == null || messMealOrders.size() == 0){
				mapObj.put("data", 0);
				return mapObj;
			}else{
				messMealOrder = messMealOrders.get(0);
			}
		}
		MessCard messCard = messCardMapper.selectByPrimaryKey(messMealOrder.getCardId());
		MessBasisSet messBasisSet = messBasisSetMapper.getMessBasisSetByMainId(messCard.getMainId());
		List<MessMealOrderInfo> messMealOrderInfos = messMealOrderInfoMapper
				.getMessMealOrderInfoListByMoId(messMealOrder.getId());
		int data = 0;
		int nums = 0;
		// 查询订餐下使用的饭票
		for (MessMealOrderInfo messMealOrderInfo : messMealOrderInfos) {
			MessCardTicket messCardTicket = messCardTicketMapper.selectByPrimaryKey(messMealOrderInfo.getTicketId());
			if(messCardTicket.getDay() == 0){
				data = 1;
			}else{
				Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), messCardTicket.getDay());
//				Date ltime = DateTimeKit.addDays(messCardTicket.getTime(), messCardTicket.getDay());
//				long temp = DateTimeKit.diffDays(ltime, new Date());
				if(!DateTimeKit.laterThanNow(lastDay)){
					//饭票过期
					messCardTicket.setStatus(2);
					data = messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket);
					if(data == 1){
						nums++;
						//消费流水表
						MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
						messConsumerDetail.setMainId(messCard.getMainId());
						messConsumerDetail.setCardId(messCard.getId());
						messConsumerDetail.setTime(new Date());
						messConsumerDetail.setTableType(5);
						messConsumerDetail.setBitSubsidy(messCardTicket.getIsfree());
						if(messCardTicket.getTicketType() == 0){
							messConsumerDetail.setBreakfastNum(1);
							messConsumerDetail.setBreakfastPrice(messCardTicket.getPrice());
						}else if(messCardTicket.getTicketType() == 1){
							messConsumerDetail.setLunchNum(1);
							messConsumerDetail.setLunchPrice(messCardTicket.getPrice());
						}else if(messCardTicket.getTicketType() == 2){
							messConsumerDetail.setDinnerNum(1);
							messConsumerDetail.setDinnerPrice(messCardTicket.getPrice());
						}else if(messCardTicket.getTicketType() == 3){
							messConsumerDetail.setNightNum(1);
							messConsumerDetail.setNightPrice(messCardTicket.getPrice());
						}else if(messCardTicket.getTicketType() == 4){
							messConsumerDetail.setUniversalNum(1);
							messConsumerDetail.setUniversalPrice(messCardTicket.getPrice());
						}
						messConsumerDetail.setStatus(0);
						messConsumerDetail.setMoney(0.0);
						messConsumerDetail.setOnLine(1);
						messConsumerDetailMapper.insertSelective(messConsumerDetail);
					}
					
					Map<String,Integer> mapId = new HashMap<String, Integer>();
					mapId.put("cardId", messCard.getId());
					mapId.put("type", messCardTicket.getTicketType());
					List<MessCardTicket> messCardTickets = null;
					try {
						messCardTickets = messCardTicketMapper.getNotCancelTicketByCardIdAndType(mapId);
					} catch (Exception e) {
						// TODO: handle exception
						messCardTickets = null;
					}
					//使用未过期饭票
					if(messCardTickets != null && messCardTickets.size() > 0){
						messCardTickets.get(0).setStatus(3);
						messMealOrderInfo.setTicketCode(messCardTickets.get(0).getTicketCode());
						messMealOrderInfo.setTicketId(messCardTickets.get(0).getId());
						Integer num = 0;
						Integer ticketType = messCardTickets.get(0).getTicketType();//票类型
						if(ticketType == 0){
							num = messCard.getBreakfastNum() - 1;
							messCard.setBreakfastNum(num);
						}else if(ticketType == 1){
							num = messCard.getLunchNum() - 1;
							messCard.setLunchNum(num);
						}else if(ticketType == 2){
							num = messCard.getDinnerNum() - 1;
							messCard.setDinnerNum(num);
						}else if(ticketType == 3){
							num = messCard.getNightNum() - 1;
							messCard.setNightNum(num);
						}else if(ticketType == 4){
							num = messCard.getUniversalNum() - 1;
							messCard.setUniversalNum(num);
						}
						if(num < 0){
							throw new Exception("余额不足");
						}else{
							messCardMapper.updateByPrimaryKeySelective(messCard);
						}
						
						//消费流水表
						MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
						messConsumerDetail.setMainId(messCard.getMainId());
						messConsumerDetail.setCardId(messCard.getId());
						messConsumerDetail.setTime(new Date());
						messConsumerDetail.setTableType(0);
						messConsumerDetail.setBitSubsidy(1);
						messConsumerDetail.setTicketNum(1);
						if(messCardTicket.getTicketType() == 0){
							messConsumerDetail.setBreakfastNum(1);
							messConsumerDetail.setBreakfastPrice(messCardTickets.get(0).getPrice());
						}else if(messCardTicket.getTicketType() == 1){
							messConsumerDetail.setLunchNum(1);
							messConsumerDetail.setLunchPrice(messCardTickets.get(0).getPrice());
						}else if(messCardTicket.getTicketType() == 2){
							messConsumerDetail.setDinnerNum(1);
							messConsumerDetail.setDinnerPrice(messCardTickets.get(0).getPrice());
						}else if(messCardTicket.getTicketType() == 3){
							messConsumerDetail.setNightNum(1);
							messConsumerDetail.setNightPrice(messCardTickets.get(0).getPrice());
						}else if(messCardTicket.getTicketType() == 4){
							messConsumerDetail.setUniversalNum(1);
							messConsumerDetail.setUniversalPrice(messCardTickets.get(0).getPrice());
						}
						messConsumerDetail.setStatus(1);
						messConsumerDetail.setMoney(0.0);
						messConsumerDetail.setOnLine(1);
						messConsumerDetailMapper.insertSelective(messConsumerDetail);
						data = messMealOrderInfoMapper.updateByPrimaryKeySelective(messMealOrderInfo);
						data = messCardTicketMapper.updateByPrimaryKeySelective(messCardTickets.get(0));
					}else{
						throw new Exception("报错");
					}
				}else{
					data = 1;
				}
			}
		}
		mapObj.put("data", data);
		mapObj.put("nums", nums);
		return mapObj;
	}

	@Override
	public Map<String, Object> exports() {
		// TODO Auto-generated method stub
		Map<String, Object> msg = new HashMap<>();
		boolean result = true;
		String message = "生成成功！";
		try {
			String title = "模板实例";
			Workbook book = exportExcelForRecoding(title);
			msg.put("book", book);
			msg.put("fileName", time("yyyyMMddhhmmss",new Date().toString()));
		} catch (Exception e) {
			msg.put("result", false);
			msg.put("message", "模板实例导出excel失败！");
			logger.error("模板实例导出excel失败！" + e.getMessage());
			e.printStackTrace();
		} finally {
			msg.put("result", result);
			msg.put("message", message);
		}
		return msg;
	}
	
	private Workbook exportExcelForRecoding(String title) {
		// 创建excel文件对象
		Workbook wb = new SXSSFWorkbook(1000);
//		HSSFWorkbook wb = new HSSFWorkbook();
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
		createCell(wb, row1, 0, "姓名", font1);
		createCell(wb, row1, 1, "性别", font1);
		
		// 第三行表示
		int l = 2;
		// 这里将的信息存入到表格中
		for (int i = 0; i < 2; i++) {
			// 创建一行
			Row rowData = sheet.createRow(l++);
			if(i == 0){
				createCell(wb, rowData, 0, "张小三", font1);
				createCell(wb, rowData, 1, "男", font1);
			}else{
				createCell(wb, rowData, 0, "李小四", font1);
				createCell(wb, rowData, 1, "女", font1);
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
	public List<MessCardTicket> getMessCardTicketListByCardId(Integer cardId) {
		// TODO Auto-generated method stub
		try {
			return messCardTicketMapper.getMessCardTicketListByCardId(cardId);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Map<String, Object> pastDue(MessCard messCard, MessBasisSet messBasisSet, List<MessCardTicket> messCardTickets, Integer mainId) throws Exception {
		// TODO Auto-generated method stub
		
		int nums = 0;
		if(messBasisSet.getBitUniversal() == 0){
			if(messCardTickets.size() == messCard.getUniversalNum()){
				for(MessCardTicket messCardTicket:messCardTickets){
					if(messCardTicket.getDay() != null && messCardTicket.getDay() != 0){
						Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), messCardTicket.getDay());
						if(!DateTimeKit.laterThanNow(lastDay)){
							if(messCardTicket.getTicketType() == 4){
								messCardTicket.setStatus(2);
								if(messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket) == 1){
									//饭票过期消费流水表
									MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
									messConsumerDetail.setMainId(messCard.getMainId());
									messConsumerDetail.setCardId(messCard.getId());
									messConsumerDetail.setTime(new Date());
									messConsumerDetail.setTableType(5);
									messConsumerDetail.setBitSubsidy(messCardTicket.getIsfree());
									if(messCardTicket.getTicketType() == 0){
										messConsumerDetail.setBreakfastNum(1);
										messConsumerDetail.setBreakfastPrice(messCardTicket.getPrice());
									}else if(messCardTicket.getTicketType() == 1){
										messConsumerDetail.setLunchNum(1);
										messConsumerDetail.setLunchPrice(messCardTicket.getPrice());
									}else if(messCardTicket.getTicketType() == 2){
										messConsumerDetail.setDinnerNum(1);
										messConsumerDetail.setDinnerPrice(messCardTicket.getPrice());
									}else if(messCardTicket.getTicketType() == 3){
										messConsumerDetail.setNightNum(1);
										messConsumerDetail.setNightPrice(messCardTicket.getPrice());
									}else if(messCardTicket.getTicketType() == 4){
										messConsumerDetail.setUniversalNum(1);
										messConsumerDetail.setUniversalPrice(messCardTicket.getPrice());
									}
									messConsumerDetail.setStatus(0);
									messConsumerDetail.setMoney(0.0);
									messConsumerDetail.setOnLine(1);
									messConsumerDetailMapper.insertSelective(messConsumerDetail);
								}
								nums++;
							}
						}
					}
				}
				messCard.setUniversalNum(messCard.getUniversalNum()-nums);
				try {
					messCardMapper.updateByPrimaryKeySelective(messCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				messCard.setUniversalNum(messCardTickets.size());
				try {
					messCardMapper.updateByPrimaryKeySelective(messCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			Integer ticketNums = messCard.getBreakfastNum() + messCard.getLunchNum() 
			+ messCard.getDinnerNum() + messCard.getNightNum();
			if(messCardTickets.size() == ticketNums){
				Integer bNum = 0;
				Integer lNum = 0;
				Integer dNum = 0;
				Integer nNum = 0;
				for(MessCardTicket messCardTicket:messCardTickets){
					if(messCardTicket.getDay() != null && messCardTicket.getDay() != 0){
						Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), messCardTicket.getDay());
						if(!DateTimeKit.laterThanNow(lastDay)){
							messCardTicket.setStatus(2);
							if(messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket) == 1){
								//饭票过期消费流水表
								MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
								messConsumerDetail.setMainId(messCard.getMainId());
								messConsumerDetail.setCardId(messCard.getId());
								messConsumerDetail.setTime(new Date());
								messConsumerDetail.setTableType(5);
								messConsumerDetail.setBitSubsidy(messCardTicket.getIsfree());
								if(messCardTicket.getTicketType() == 0){
									messConsumerDetail.setBreakfastNum(1);
									messConsumerDetail.setBreakfastPrice(messCardTicket.getPrice());
								}else if(messCardTicket.getTicketType() == 1){
									messConsumerDetail.setLunchNum(1);
									messConsumerDetail.setLunchPrice(messCardTicket.getPrice());
								}else if(messCardTicket.getTicketType() == 2){
									messConsumerDetail.setDinnerNum(1);
									messConsumerDetail.setDinnerPrice(messCardTicket.getPrice());
								}else if(messCardTicket.getTicketType() == 3){
									messConsumerDetail.setNightNum(1);
									messConsumerDetail.setNightPrice(messCardTicket.getPrice());
								}else if(messCardTicket.getTicketType() == 4){
									messConsumerDetail.setUniversalNum(1);
									messConsumerDetail.setUniversalPrice(messCardTicket.getPrice());
								}
								messConsumerDetail.setStatus(0);
								messConsumerDetail.setMoney(0.0);
								messConsumerDetail.setOnLine(1);
								messConsumerDetailMapper.insertSelective(messConsumerDetail);
							}
							nums++;
							if(messCardTicket.getTicketType() == 0){
								bNum++;
							}else if(messCardTicket.getTicketType() == 1){
								lNum++;
							}else if(messCardTicket.getTicketType() == 2){
								dNum++;
							}else if(messCardTicket.getTicketType() == 3){
								nNum++;
							}
						}
					}
				}
				
				messCard.setBreakfastNum(messCard.getBreakfastNum()-bNum);
				messCard.setLunchNum(messCard.getLunchNum()-lNum);
				messCard.setDinnerNum(messCard.getDinnerNum()-dNum);
				messCard.setNightNum(messCard.getNightNum()-nNum);
				try {
					messCardMapper.updateByPrimaryKeySelective(messCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Integer bNum = 0;
				Integer lNum = 0;
				Integer dNum = 0;
				Integer nNum = 0;
				for(MessCardTicket messCardTicket:messCardTickets){
					if(messCardTicket.getTicketType() == 0){
						bNum++;
					}else if(messCardTicket.getTicketType() == 1){
						lNum++;
					}else if(messCardTicket.getTicketType() == 2){
						dNum++;
					}else if(messCardTicket.getTicketType() == 3){
						nNum++;
					}
				}
				messCard.setBreakfastNum(bNum);
				messCard.setLunchNum(lNum);
				messCard.setDinnerNum(dNum);
				messCard.setNightNum(nNum);
				try {
					messCardMapper.updateByPrimaryKeySelective(messCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("nums", nums);
		return map;
	}

	@Override
	public Map<String, Object> pastDue2(MessCard messCard, MessBasisSet messBasisSet,
			List<MessCardTicket> messCardTickets, Integer mainId, Integer day) {
		// TODO Auto-generated method stub
		int nums = 0;
		if(messBasisSet.getBitUniversal() == 0){
			if(messCardTickets.size() == messCard.getUniversalNum()){
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
								if(messCardTicket.getTicketType() == 4){
									nums++;
								}
//								int temp = messCardTicket.getDay() - messBasisSet.getPastDay();
//								Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), temp);
//								if(!DateTimeKit.laterThanNow(lastDay)){
//									if(messCardTicket.getTicketType() == 4){
//										nums++;
//									}
//								}
							}else{
								if(messCardTicket.getTicketType() != 4){
									nums++;
								}
							}
						}
					}
				}
			}else{
				messCard.setUniversalNum(messCardTickets.size());
				try {
					messCardMapper.updateByPrimaryKeySelective(messCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			Integer ticketNums = messCard.getBreakfastNum() + messCard.getLunchNum() 
			+ messCard.getDinnerNum() + messCard.getNightNum();
			if(messCardTickets.size() == ticketNums){
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
								if(messCardTicket.getTicketType() != 4){
									nums++;
								}
//								int temp = messCardTicket.getDay() - messBasisSet.getPastDay();
//								Date lastDay = DateTimeKit.addDays(messCardTicket.getTime(), temp);
//								if(!DateTimeKit.laterThanNow(lastDay)){
//									if(messCardTicket.getTicketType() == 4){
//										nums++;
//									}
//								}
							}else{
								if(messCardTicket.getTicketType() == 4){
									nums++;
								}
							}
						}
					}
				}
			}else{
				Integer bNum = 0;
				Integer lNum = 0;
				Integer dNum = 0;
				Integer nNum = 0;
				for(MessCardTicket messCardTicket:messCardTickets){
					if(messCardTicket.getTicketType() == 0){
						bNum++;
					}else if(messCardTicket.getTicketType() == 1){
						lNum++;
					}else if(messCardTicket.getTicketType() == 2){
						dNum++;
					}else if(messCardTicket.getTicketType() == 3){
						nNum++;
					}else if(messCardTicket.getTicketType() == 4){
						messCardTicketMapper.deleteByPrimaryKey(messCardTicket.getId());
					}else {
						messCardTicketMapper.deleteByPrimaryKey(messCardTicket.getId());
					}
				}
				messCard.setBreakfastNum(bNum);
				messCard.setLunchNum(lNum);
				messCard.setDinnerNum(dNum);
				messCard.setNightNum(nNum);
				try {
					messCardMapper.updateByPrimaryKeySelective(messCard);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("nums", nums);
		return map;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int changeDep(Map<String,Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		messCard.setDepId(Integer.valueOf(params.get("depId").toString()));
		if(messCard.getDepId() == -1){
			messCard.setDepartment("暂无部门");
		}else{
			messCard.setDepartment(params.get("department").toString());
		}
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Override
	public int getMessCardNumsByDepId(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messCardMapper.getMessCardNumsByDepId(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int deductTicket(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		Integer nums = Integer.valueOf(params.get("ticketNum").toString());
		Integer tempNums = 0;
		Integer ticketType = Integer.valueOf(params.get("ticketType").toString());
		if(messCard.getFreeTicketNum() >= nums){
			if(ticketType == 0){//早餐
				if(messCard.getBreakfastNum() < nums)
					return 0;
			}else if(ticketType == 1){//午餐
				if(messCard.getLunchNum() < nums)
					return 0;
			}else if(ticketType == 2){//晚餐
				if(messCard.getDinnerNum() < nums)
					return 0;
			}else if(ticketType == 3){//夜宵
				if(messCard.getNightNum() < nums)
					return 0;
			}else if(ticketType == 4){//通用
				if(messCard.getUniversalNum() < nums)
					return 0;
			}
			if(messCard.getFreeTicketNum() >= 0){
				List<MessCardTicket> messCardTickets =
						messCardTicketMapper.getMessCardTicketListByCardId(messCard.getId());
				for(MessCardTicket messCardTicket : messCardTickets){
					if(messCardTicket.getIsfree().equals(0) && ticketType.equals(messCardTicket.getTicketType())){
						data = messCardTicketMapper.deleteByPrimaryKey(messCardTicket.getId());
						tempNums++;
						if(nums.equals(tempNums)){
							break;
						}
					}
				}
				messCard.setFreeTicketNum(messCard.getFreeTicketNum()-nums);
				MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
				if(ticketType == 0){//早餐
					messCard.setBreakfastNum(messCard.getBreakfastNum() -nums);
				}else if(ticketType == 1){//午餐
					messCard.setLunchNum(messCard.getLunchNum() -nums);
				}else if(ticketType == 2){//晚餐
					messCard.setDinnerNum(messCard.getDinnerNum() -nums);
				}else if(ticketType == 3){//夜宵
					messCard.setNightNum(messCard.getNightNum() -nums);
				}else if(ticketType == 4){//通用
					messCard.setUniversalNum(messCard.getUniversalNum() -nums);
				}
				if(data == 1){
					data = messCardMapper.updateByPrimaryKeySelective(messCard);
				}
				
				messConsumerDetail.setMainId(messCard.getMainId());
				messConsumerDetail.setCardId(messCard.getId());
				messConsumerDetail.setTime(new Date());
				messConsumerDetail.setTableType(7);
				messConsumerDetail.setBitSubsidy(0);
				messConsumerDetail.setTicketNum(nums);
				messConsumerDetail.setBreakfastNum(0);
				messConsumerDetail.setLunchNum(0);
				messConsumerDetail.setDinnerNum(0);
				messConsumerDetail.setNightNum(0);
				messConsumerDetail.setUniversalNum(0);
				messConsumerDetail.setStatus(0);
				messConsumerDetail.setMoney(0.0);
				messConsumerDetail.setOnLine(1);
				data = messConsumerDetailMapper.insertSelective(messConsumerDetail);
			}
		}
		return data;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int cancelTicket(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("id").toString()));
		Integer nums = Integer.valueOf(params.get("ticketNum").toString());
		Integer tempNums = 0;
		Integer ticketType = Integer.valueOf(params.get("ticketType").toString());
		Integer cardTicketNum = messCard.getFreeTicketNum() + messCard.getTicketNum();
		if(cardTicketNum >= nums){
			if(ticketType == 0){//早餐
				if(messCard.getBreakfastNum() < nums)
					return 0;
			}else if(ticketType == 1){//午餐
				if(messCard.getLunchNum() < nums)
					return 0;
			}else if(ticketType == 2){//晚餐
				if(messCard.getDinnerNum() < nums)
					return 0;
			}else if(ticketType == 3){//夜宵
				if(messCard.getNightNum() < nums)
					return 0;
			}else if(ticketType == 4){//通用
				if(messCard.getUniversalNum() < nums)
					return 0;
			}
			if(cardTicketNum >= 0){
				List<MessCardTicket> messCardTickets =
						messCardTicketMapper.getMessCardTicketListByCardId(messCard.getId());
				Integer freeTicketNum = 0;
				Integer payTicketNum = 0;
				for(MessCardTicket messCardTicket : messCardTickets){
					if(ticketType.equals(messCardTicket.getTicketType())){
						messCardTicket.setStatus(3);
						data = messCardTicketMapper.updateByPrimaryKeySelective(messCardTicket);
						if(messCardTicket.getIsfree().equals(0)){
							freeTicketNum++;
						}else{
							payTicketNum++;
						}
						tempNums++;
						if(nums.equals(tempNums)){
							break;
						}
					}
				}
				messCard.setFreeTicketNum(messCard.getFreeTicketNum()-freeTicketNum);
				if(messCard.getFreeTicketNum() < 0){
					messCard.setFreeTicketNum(0);
				}
				messCard.setTicketNum(messCard.getTicketNum()-payTicketNum);
				if(messCard.getTicketNum() < 0){
					messCard.setTicketNum(0);
				}
				MessOldManCardOrder messOldManCardOrder = new MessOldManCardOrder();
				messOldManCardOrder.setCardCode(messCard.getCardCode());
				messOldManCardOrder.setDepartment(messCard.getDepartment());
				messOldManCardOrder.setMainId(messCard.getMainId());
				messOldManCardOrder.setName(messCard.getName());
				messOldManCardOrder.setNum(nums);
				messOldManCardOrder.setOldId(messCard.getId());
				messOldManCardOrder.setSex(messCard.getSex());
				messOldManCardOrder.setTime(new Date());
				messOldManCardOrder.setType(3);
				messOldManCardOrder.setMealType(ticketType);
				data = messOldManCardOrderMapper.insertSelective(messOldManCardOrder);
				if(ticketType == 0){//早餐
					messCard.setBreakfastNum(messCard.getBreakfastNum() -nums);
				}else if(ticketType == 1){//午餐
					messCard.setLunchNum(messCard.getLunchNum() -nums);
				}else if(ticketType == 2){//晚餐
					messCard.setDinnerNum(messCard.getDinnerNum() -nums);
				}else if(ticketType == 3){//夜宵
					messCard.setNightNum(messCard.getNightNum() -nums);
				}else if(ticketType == 4){//通用
					messCard.setUniversalNum(messCard.getUniversalNum() -nums);
				}
				if(data == 1){
					data = messCardMapper.updateByPrimaryKeySelective(messCard);
				}
				MessConsumerDetail messConsumerDetail = new MessConsumerDetail();
				messConsumerDetail.setMainId(messCard.getMainId());
				messConsumerDetail.setCardId(messCard.getId());
				messConsumerDetail.setTime(new Date());
				messConsumerDetail.setTableType(8);
				messConsumerDetail.setBitSubsidy(0);
				messConsumerDetail.setTicketNum(nums);
				messConsumerDetail.setBreakfastNum(0);
				messConsumerDetail.setLunchNum(0);
				messConsumerDetail.setDinnerNum(0);
				messConsumerDetail.setNightNum(0);
				messConsumerDetail.setUniversalNum(0);
				messConsumerDetail.setStatus(4);
				messConsumerDetail.setMoney(0.0);
				messConsumerDetail.setOnLine(1);
				data = messConsumerDetailMapper.insertSelective(messConsumerDetail);
			}
		}
		return data;
	}

	@Override
	public Page<MessCard> commonCard(Page<MessCard> page, Integer mainId, Integer nums) {
		// TODO Auto-generated method stub
		try {
			page.setRecords( messCardMapper.commonCard(mainId) );
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int cleanTicketAndOrder(Integer mainId) throws Exception {
		// TODO Auto-generated method stub
		int data = 0;
		data = messCardTicketMapper.delNotCancelU(mainId);
		data = messCardTicketMapper.delNotCancelD(mainId);
		data = messCardMapper.cleanTicketNum(mainId);
		data = messMealOrderMapper.cleanFutureOrder(mainId);
		if(data > 0){
			data = 1;
		}else {
			data = 0/1;
		}
		return data;
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int srBindingCard(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
//		MessMain messMain = 
//				messMainMapper.getMessMainByBusId(Integer.valueOf(params.get("busId").toString())).get(0);
//		params.put("mainId", messMain.getId());
		if("undefined".equals(params.get("openId").toString().trim())){
			return 2;
		}
		MessCard messCard = messCardMapper.getMessCardByCardCode(params.get("cardCode").toString()).get(0);
		if (messCard != null && messCard.getStatus() == 0 && messCard.getMemberId() != null && messCard.getSrOpenId() == null) {
			MessCard messCard2 = null;
			try {
				Map<String,Object> mapId = new HashMap<String, Object>();
				mapId.put("mainId", messCard.getMainId());
				mapId.put("openId", params.get("openId").toString());
				messCard2 = messCardMapper.getMessCardByMainIdAndOpenId(mapId).get(0);
			} catch (Exception e) {
				// TODO: handle exception
				messCard2 = null;
			}
			if(messCard2 != null){
				return 0;
			}
			messCard.setSrOpenId(params.get("openId").toString());
			return messCardMapper.updateByPrimaryKeySelective(messCard);
		}else{
			return 2;
		}
	}

	@Override
	public MessCard getMessCardByOpenId(String openId) {
		// TODO Auto-generated method stub
		try {
			return messCardMapper.getMessCardByOpenId(openId).get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void balance(MessCard messCard, MessBasisSet messBasisSet, List<MessCardTicket> messCardTickets, Integer mainId) throws Exception {
        if(messBasisSet.getBitUniversal() == 0){
            if(messCardTickets.size() == messCard.getUniversalNum()){
                Integer uNum = messCard.getFreeTicketNum() + messCard.getTicketNum();
                if(!messCard.getUniversalNum().equals(uNum)){
                    Integer fNum = messCard.getUniversalNum() - messCard.getTicketNum();
                    Integer tNum = 0;
                    if(fNum < 0){
                        tNum = messCard.getUniversalNum() - messCard.getFreeTicketNum();
                        messCard.setTicketNum(tNum);
                    }else{
                        messCard.setFreeTicketNum(fNum);
                    }
                }
                try {
                    messCardMapper.updateByPrimaryKeySelective(messCard);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                messCard.setUniversalNum(messCardTickets.size());
                Integer uNum = messCard.getFreeTicketNum() + messCard.getTicketNum();
                if(!messCard.getUniversalNum().equals(uNum)){
                    Integer fNum = messCard.getUniversalNum() - messCard.getTicketNum();
                    Integer tNum = 0;
                    if(fNum < 0){
                        tNum = messCard.getUniversalNum() - messCard.getFreeTicketNum();
                        messCard.setTicketNum(tNum);
                    }else{
                        messCard.setFreeTicketNum(fNum);
                    }
                }
                try {
                    messCardMapper.updateByPrimaryKeySelective(messCard);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else{
            Integer ticketNums = messCard.getBreakfastNum() + messCard.getLunchNum()
                    + messCard.getDinnerNum() + messCard.getNightNum();
            if(messCardTickets.size() == ticketNums){
                Integer ftNum = messCard.getFreeTicketNum() + messCard.getTicketNum();
                if(!ticketNums.equals(ftNum)){
                    Integer fNum = messCard.getUniversalNum() - messCard.getTicketNum();
                    Integer tNum = 0;
                    if(fNum < 0){
                        tNum = messCard.getUniversalNum() - messCard.getFreeTicketNum();
                        messCard.setTicketNum(tNum);
                    }else{
                        messCard.setFreeTicketNum(fNum);
                    }
                }
                try {
                    messCardMapper.updateByPrimaryKeySelective(messCard);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                Integer ftNum = messCard.getFreeTicketNum() + messCard.getTicketNum();
                if(!ticketNums.equals(ftNum)){
                    Integer fNum = messCard.getUniversalNum() - messCard.getTicketNum();
                    Integer tNum = 0;
                    if(fNum < 0){
                        tNum = messCard.getUniversalNum() - messCard.getFreeTicketNum();
                        messCard.setTicketNum(tNum);
                    }else{
                        messCard.setFreeTicketNum(fNum);
                    }
                }
                try {
                    messCardMapper.updateByPrimaryKeySelective(messCard);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
	public int emptyTicket(Integer cardId) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(cardId);
		MessBasisSet messBasisSet = 
				messBasisSetMapper.getMessBasisSetByMainId(messCard.getMainId());
		if(messBasisSet.getBitUniversal() == 0){
			messCardTicketMapper.delNotCancelUByCardId(cardId);
			messCard.setUniversalNum(0);
			messCard.setFreeTicketNum(0);
			messCard.setTicketNum(0);
		}else{
			messCardTicketMapper.delNotCancelDByCardId(cardId);
			messCard.setBreakfastNum(0);
			messCard.setLunchNum(0);
			messCard.setDinnerNum(0);
			messCard.setNightNum(0);
			messCard.setFreeTicketNum(0);
			messCard.setTicketNum(0);
		}
		return messCardMapper.updateByPrimaryKeySelective(messCard);
	}

	@Override
	public int getMessCardNumsByGroupId(Map<String, Integer> mapId) {
		// TODO Auto-generated method stub
		try {
			return messCardMapper.getMessCardNumsByGroupId(mapId);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int changeGroup(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		MessCard messCard = 
				messCardMapper.selectByPrimaryKey(Integer.valueOf(params.get("cardId").toString()));
		Integer groupId = Integer.valueOf(params.get("groupId").toString());
		if(groupId.equals(-1)){
			messCard.setGroupId(null);
		}else{
			messCard.setGroupId(groupId);
		}
		return messCardMapper.updateByPrimaryKey(messCard);
	}	
	
}
