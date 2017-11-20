package com.gt.mess.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * freemark 工具类
 * <pre>
 *     freemarker是一种非常轻量易用的模板引擎，除了用于在web mvc框架中渲染html页面以外，还可以用在其他需要生成其他有复杂格式的文档，并且需要用数据进行格式化的场景下；将生成的字符串写入指定的Java流中。
 * </pre>
 *
 * @author zengwx
 * @version 1.0.0
 * @date 2017/08/28
 */
public class FreemarkerUtil {

    private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtil.class);

    /**
     * 使用传入的Map对象，渲染指定的freemarker模板
     *
     * @param baseDir   根目录
     * @param fileName  文件名
     * @param globalMap 数据绑定
     *
     * @return html字符串
     */
    public static String loadFtlHtml(File baseDir, String fileName, Map globalMap) throws IOException, TemplateException {
	if (baseDir == null || !baseDir.isDirectory() || globalMap == null || fileName == null || "".equals(fileName)) {
	    throw new IllegalArgumentException("Directory file");
	}
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
	cfg.setDirectoryForTemplateLoading(baseDir);
	cfg.setDefaultEncoding("UTF-8");
	cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);//.RETHROW
	cfg.setClassicCompatible(true);
	Template temp = cfg.getTemplate(fileName);
	StringWriter stringWriter = new StringWriter();
	temp.process(globalMap, stringWriter);
	return stringWriter.toString();
    }

    /**
     * 使用传入的Map对象，渲染指定的freemarker模板
     *
     * @param realPath  ftl文件内容
     * @param fileName  文件名称
     * @param globalMap 数据绑定
     *
     * @return html字符串
     */
    public static String loadFtlHtml(String realPath, String fileName, Map globalMap) throws IOException, TemplateException {
	if (realPath == null || globalMap == null || fileName == null || "".equals(fileName)) {
	    throw new IllegalArgumentException("非法参数");
	}
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
	cfg.setDefaultEncoding("UTF-8");
	cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);//.RETHROW
	cfg.setClassicCompatible(true);
	StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
	stringTemplateLoader.putTemplate(fileName, realPath);
	cfg.setTemplateLoader(stringTemplateLoader);
	Template temp = cfg.getTemplate(fileName, "UTF-8");
	StringWriter stringWriter = new StringWriter(2048);
	Map< String, Map > root = new HashMap<>();
	root.put("item", globalMap);
	temp.process(root, stringWriter);
	// 替换 \n \r
	return stringWriter.toString().replaceAll("[\\n\\r]", "");
    }
}
