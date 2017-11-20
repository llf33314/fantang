package com.gt.mess.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created with IntelliJ IDEA User : yangqian Date : 2017/7/18 0018 Time : 16:22
 */
public class CommonUtil {

	/**
	 * 判断浏览器
	 *
	 * @return 1:微信浏览器,99:其他浏览器
	 */
	public static Integer judgeBrowser(HttpServletRequest request) {
		Integer result = null;
		String ua = request.getHeader("user-agent")
				.toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 微信-1
			result = 1;
		} else {//其他 -99
			result = 99;
		}
		return result;
	}

	public static String getCode() {
		Long date = new Date().getTime();
		String cardNo = date.toString().substring(1);
		return cardNo;
	}

	public static String Blob2String(Object obj) {
		String string = null;
		try {
			if (obj == null || obj.equals("")) {
				return "";
			}
			byte[] bytes = (byte[]) obj;
			string = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

    /**
     * 判断对象是否为空
     */
    public static boolean isEmpty(Object obj) {
	boolean b = false;
	try {
	    if (obj == null || "".equals(obj)) {
		b = true;
	    } else {
		b = false;
	    }
	} catch (Exception e) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 判断对象是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
	boolean b = false;
	try {
	    if (obj == null || "".equals(obj)) {
		b = false;
	    } else {
		b = true;
	    }
	} catch (Exception e) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 转Integer
     */
    public static Integer toInteger(Object obj) {
	try {
	    if (!isEmpty(obj)) {
		return Integer.parseInt(obj.toString());
	    } else {
		throw new Exception("对象为空，转换失败！");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转String
     */
    public static String toString(Object obj) {
	try {
	    if (!isEmpty(obj)) {
		return obj.toString();
	    } else {
		throw new Exception("对象为空，转换失败！");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转Double
     */
    public static Double toDouble(Object obj) {
	try {
	    if (!isEmpty(obj)) {
		return Double.parseDouble(obj.toString());
	    } else {
		throw new Exception("对象为空，转换失败！");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 校验是否是double数据
     */
    public static boolean isDouble(Object obj) {
	try {
	    Double.parseDouble(obj.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * 返回json数据到客户端
     */
    public static void write(HttpServletResponse response, Object obj) throws IOException {
	if (obj instanceof List || obj instanceof Object[]) {
	    response.getWriter().print(JSONArray.toJSON(obj));
	} else if (obj instanceof Boolean || obj instanceof Integer || obj instanceof String || obj instanceof Double) {
	    Map< String, Object > result = new HashMap< String, Object >();
	    result.put("status", obj);
	    response.getWriter().print(JSONObject.toJSON(result));
	} else {
	    response.getWriter().print(JSONObject.toJSON(obj));
	}
	response.getWriter().flush();
	response.getWriter().close();
    }

	/**
	 * 生成固定位数随机码
	 * @param num
	 * @return
	 */
	public static String getRandomCode(Integer num) {
		String eqCode = "" + PJWHash("" + System.currentTimeMillis());
		if(eqCode.length() != num){
			while (true){
				eqCode = "" + PJWHash("" + System.currentTimeMillis());
				if(eqCode.length() == num){
					break;
				}
			}
		}
		if(eqCode.length() == num){
			return eqCode;
		}else{
			return "-1";
		}
	}

	/**
	 * 生成算法
	 * @param str
	 * @return
	 */
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

	/**
	 * 删除文件
	 * @param path 路径(绝对路径)
	 * @return 返回 -1: 删除失败出现异常 0:该路径是管理后台的资源，不能删除，可继续下一步操作 1:删除文件(夹)成功 ,2:目录不存在
	 */
	public static int  delFile(String path){
		int imgIndex=(path==null||path.isEmpty())?-2:path.indexOf("upload/1");
		int result=-1;
		if(imgIndex>0){//管理后台的图片，不能删除
			result=0;
		}else if(imgIndex==-2){//path为空
			result=1;
		}else{
			// String tempPth=getPath(path);
			File file=new File(path);
			if(file.exists()&&file.isDirectory()){//当path是一个目录时,先通过递归的方式删除目录下的所有文件
				result=deleteDir(file)?1:-1;
			}else if(file.exists()&&file.isDirectory()==false) {//当好path是一个文件时，直接删除文件
				result=file.delete()?1:-1;
			}else{
				result=2;
			}
		}
		return result;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public static String getURL(HttpServletRequest request) {
		String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();
		String url = "http://" + request.getServerName();
		if (CommonUtil.toInteger(Integer.valueOf(request.getServerPort())) != 80) {
			url = url + ":" + CommonUtil.toInteger(Integer.valueOf(request.getServerPort())) + contextPath;
		} else {
			url = url + contextPath;
		}
		return url;
	}
}
