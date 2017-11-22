package com.gt.mess;

import com.alibaba.fastjson.JSONObject;
import com.gt.mess.util.WxmpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试基类
 *
 * @author zengwx
 * @create 2017/6/16
 */
@RunWith( SpringRunner.class )
@SpringBootTest
public class BasicTest {

    protected static final Logger logger = LoggerFactory.getLogger(BasicTest.class);
    // 开始时间
    private Long start_time;

    @Autowired
    private WxmpUtil wxmpUtil;

    @Test
    public void test() throws Exception {
        System.out.println(JSONObject.toJSONString(wxmpUtil.findByMemberId(222)));
    }

    @Before
    public void start() {
	start_time = System.currentTimeMillis();
	logger.info("=======================================  单元测试Start =======================================");
    }

    @After
    public void end() {
	logger.info("执行结束，方法执行 {} 毫秒", (System.currentTimeMillis() - start_time));
	logger.info("=======================================    单元测试End  =======================================");
    }

}
