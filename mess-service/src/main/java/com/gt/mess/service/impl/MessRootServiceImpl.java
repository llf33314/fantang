package com.gt.mess.service.impl;

import com.gt.mess.exception.BaseException;
import com.gt.mess.service.MessRootService;
import com.gt.mess.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessRootServiceImpl implements MessRootService{

	private static String dicsUseType = "1011"; // 选择字典信息，权限
	
	@Override
	public Map<String, Object> getMessRootInfo(HttpServletRequest request) throws BaseException {
		try {
			Map<String, Object> res = new HashMap<>();
//			BusUser busUser = SessionUtils.getLoginUser(request);
//			// 到找该账户是子账号还是主账号
//			if(busUser.getPid() == 0){
//				// 不是子账号
//				Map<String, Map<String, Object>> maps = CommonUtil.getBusDict(request);
//				Map<String, Object> mapse = maps.get(dicsUseType); // 得到权限
//				res = getRootMap(mapse);
//			}else {
//				// 是子账号
//				int mainBusId = dictService.pidUserId(busUser.getId());
//				BusUser mainBusUser = busUserMapper.selectByPrimaryKey(mainBusId);
//				List<Map<String, Object>> listse = dictService.SelectMenus(dicsUseType, mainBusId, mainBusUser.getLevel(), mainBusUser.getIndustryid());
//				res = getRootList(listse);
//			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("权限获取失败");
		}
	}

	/**
	 * 得到权限  0：是	1：否
	 * @param mapse
	 * @return
	 */
	private Map<String, Object> getRootMap(Map<String, Object> mapse){
		Map<String, Object> map = new HashMap<>();
		map.put("oneClickClear", CommonUtil.isEmpty(mapse.get("1"))?1:0); // 是否一键清空	
		return map;
	}

	/**
	 * 得到权限  0：是	1：否
	 * @param listse
	 * @return
	 */
	private Map<String, Object> getRootList(List<Map<String, Object>> listse) {
		Map<String, Object> map = new HashMap<>();
		map.put("oneClickClear", 0); // 没有一键清空
		for(Map<String, Object> m : listse){
			if(CommonUtil.isNotEmpty(m.get("type"))){
				String type = m.get("type").toString();
				if("1".equals(type)){
					map.put("oneClickClear", 1); // 有一键清空	
				}
			}
		}
		return map;
	}
	
}

