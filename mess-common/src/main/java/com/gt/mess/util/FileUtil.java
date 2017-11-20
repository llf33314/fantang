package com.gt.mess.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 文件操作工具类
 *
 * @author zengwx
 * @create 2017/7/4
 */
public final class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    //工具类一般不需要建立对象.所以直接私有化构造方法
    private FileUtil() {

    }

    /**
     * 读取文件 <p> 读取文件时，加入编码格式 避免中文乱码。 默认编码 UTF-8
     *
     * @param filePathAndName String 如 c:\\1.txt 绝对路径
     *
     * @return 字符串
     */
    public static String readFile(String filePathAndName) {
	return readFile(filePathAndName, "UTF-8");
    }

    /**
     * 读取文件内容
     *
     * @param filePathAndName 文件路径 注：绝对路径
     * @param encode          编码格式
     *
     * @return 字符串
     */
    public static String readFile(String filePathAndName, String encode) {
	if (encode == null) {
	    encode = "UTF-8";
	}
	String fileContent = "";
	InputStreamReader read = null;
	try {
	    File f = new File(filePathAndName);
	    if (f.isFile() && f.exists()) {
		read = new InputStreamReader(new FileInputStream(f), encode);
		BufferedReader bufferedReader = new BufferedReader(read);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
		    fileContent += line;
		}
	    }
	} catch (IOException e) {
	    logger.error("读取文件内容操作出错", e);
	} finally {
	    if (read != null) {
		try {
		    read.close();
		} catch (IOException e) {
		    logger.error("关闭IO连接失败", e);
		}
	    }
	}
	return fileContent;
    }

    /**
     * 写入文件 <p> 读取文件时，加入编码格式 避免中文乱码。
     *
     * @param filePathAndName 文件路径 绝对路径
     * @param fileContent     文件内容
     */
    public static void writeFile(String filePathAndName, String fileContent) {
	writeFile(filePathAndName, fileContent, "UTF-8");
    }

    /**
     * 写入文件 <p> 读取文件时，加入编码格式 避免中文乱码。
     *
     * @param filePathAndName 文件路径 绝对路径
     * @param fileContent     文件内容
     */
    public static void writeFile(String filePathAndName, String fileContent, String encode) {
	OutputStreamWriter write = null;
	try {
	    if (encode == null) {
		encode = "UTF-8";
	    }
	    File f = new File(filePathAndName);
	    if (!f.exists()) {
		f.createNewFile();
	    }
	    write = new OutputStreamWriter(new FileOutputStream(f), encode);
	    BufferedWriter writer = new BufferedWriter(write);
	    writer.write(fileContent);
	    writer.close();
	} catch (IOException ex) {
	    logger.error("读取文件内容操作出错", ex);
	} finally {
	    if (write != null) {
		try {
		    write.close();
		} catch (IOException e) {
		    logger.error("关闭IO连接失败", e);
		}
	    }
	}
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     *
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
	File file = new File(fileName);
	if (!file.exists()) {
	    logger.warn("删除文件失败:{}不存在！", fileName);
	    return false;
	} else {
	    if (file.isFile()) {
		return deleteFile(fileName);
	    } else {
		return deleteDirectory(fileName);
	    }
	}
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     *
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
	File file = new File(fileName);
	// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
	if (file.exists() && file.isFile()) {
	    if (file.delete()) {
		logger.debug("删除{}文件成功！", fileName);
		return true;
	    } else {
		logger.warn("删除{}文件失败！", fileName);
		return false;
	    }
	} else {
	    logger.warn("删除文件失败：{}不存在！", fileName);
	    return false;
	}
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     *
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
	// 如果dir不以文件分隔符结尾，自动添加文件分隔符
	if (!dir.endsWith(File.separator)) {
	    dir = dir + File.separator;
	}
	File dirFile = new File(dir);
	// 如果dir对应的文件不存在，或者不是一个目录，则退出
	if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
	    logger.warn("删除目录失败: {}不存在 ", dirFile);
	    return false;
	}
	boolean flag = true;
	// 删除文件夹中的所有文件包括子目录
	File[] files = dirFile.listFiles();
	for (int i = 0; i < files.length; i++) {
	    // 删除子文件
	    if (files[i].isFile()) {
		flag = FileUtil.deleteFile(files[i].getAbsolutePath());
		if (!flag) {
		    break;
		}
	    }
	    // 删除子目录
	    else if (files[i].isDirectory()) {
		flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
		if (!flag) {
		    break;
		}
	    }
	}
	if (!flag) {
	    logger.warn("删除目录失败！");
	    return false;
	}
	// 删除当前目录
	if (dirFile.delete()) {
	    logger.warn("删除目录{}成功！", dir);
	    return true;
	} else {
	    return false;
	}
    }

    public static void main(String[] args) {
	// // 删除单个文件
	// String file = "c:/test/test.txt";
	// DeleteFileUtil.deleteFile(file);
	// System.out.println();
	// 删除一个目录
	String dir = "\\\\HUAMAO-SERVER\\Server\\tomcat-8080\\webapps\\upload\\voice\\2\\gh_b0a77493e00a\\3\\20160510\\0C7892783D0C4EE2C59490D225C73677.mp3";
	System.out.println(dir);
	//FileUtil.deleteDirectory(dir);
	FileUtil.delete(dir);
	// System.out.println();
	// // 删除文件
	// dir = "c:/test/test0";
	// DeleteFileUtil.delete(dir);

    }
}
